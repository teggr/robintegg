---
layout: post
title: "Building a Drawing App with Java, TeaVM and HTML Canvas"
date: "2026-10-19"
description: "Build an interactive drawing editor with Java and TeaVM, using HTML Canvas, browser pointer events and JavaScript interop."
image: /images/teavm-drawing-app.png
tags:
  - java
  - teavm
  - javascript
  - web development
---

Java in the browser with TeaVM: three examples exploring how far you can take Java on the web, from a small game to a full-stack application and an interactive drawing editor.

[Previous: From Java to Web App: Building a Currency Explorer with TeaVM](/2026-10-18-from-java-to-web-app-currency-explorer-with-teavm.html)

In the first article in this series, we took a small Java guessing game and got it running in the browser with TeaVM. The second article went further, building a currency explorer with a Java client, a Spring Boot API, asynchronous JSON requests and a chart rendered with Chart.js.

For the final article, I wanted to try something a little more interactive.

What about building a drawing application?

Not a replacement for Excalidraw, and not a full graphics editor. Just a small application where we can draw shapes, select them, move them around and change their colours.

It brings together a few useful problems. We need to represent the drawing in Java, respond to pointer events in the browser, work out which shape the user has selected, and render the result on screen.

## What we're building

The finished application has a canvas and a small toolbar.

We can choose a rectangle, ellipse or arrow tool, drag on the canvas to create a shape, and switch to selection mode to move existing shapes. A colour palette lets us change the appearance of the selected shape.

That's enough functionality to feel like a small application rather than a technical demonstration, while keeping the implementation understandable.

![TeaVM drawing app running in Chrome](/images/teavm-drawing-app.png)

## Start with a model of the drawing

Before drawing anything, we need to decide how the application will represent its contents.

One option would be to draw directly onto the canvas whenever the user interacts with it. The problem is that the canvas only contains pixels. It doesn't know that a particular collection of pixels represents a rectangle, or that the rectangle should move when the user drags it.

Instead, the application keeps a model of the drawing in Java. Each shape stores its type, start and end points, and colour. Rectangles and ellipses derive normalized bounds from those points, while arrows retain their direction. The bounds come from the same pair of points regardless of which way the user dragged:

```java
public double getLeft() {
    return Math.min(startX, endX);
}

public double getTop() {
    return Math.min(startY, endY);
}

public double getWidth() {
    return Math.abs(endX - startX);
}

public double getHeight() {
    return Math.abs(endY - startY);
}
```

The model stores the shapes in drawing order and tracks the selected shape. This gives us a useful separation of responsibilities:

- Java knows which shapes exist and where they are.
- Java decides which shape is selected and how it changes.
- The browser draws the visual representation.

That separation will make the rest of the application much easier to understand.

## Giving Java a drawing surface

Our model describes the drawing, but we still need somewhere to display it.

HTML Canvas provides exactly that. A canvas element gives the browser a drawing surface, and its 2D rendering context provides operations for drawing paths, rectangles and other shapes.

The page contains the canvas as ordinary HTML:

```html
<canvas id="drawing-canvas" width="900" height="600"></canvas>
```

In `DrawingApp`, TeaVM's JSO APIs retrieve the element and its typed 2D context:

```java
canvas = requireCanvas(document, "drawing-canvas");
CanvasRenderingContext2D context =
        (CanvasRenderingContext2D) canvas.getContext("2d");
if (context == null) {
    throw new IllegalStateException("The browser could not create a 2D canvas context.");
}
renderer = new DrawingRenderer(canvas, context);
```

TeaVM's JSO library already provides Java bindings for the DOM and Canvas 2D methods. This version of JSO doesn't provide a typed `PointerEvent` wrapper, though. The app uses `@JSBody` for the small pieces of browser-specific pointer data and capture, while keeping the drawing model and rendering calls in Java.

For example, the Java method declares the arguments and return type, and TeaVM supplies the JavaScript body at that boundary:

```java
@JSBody(params = { "canvas", "event" }, script = """
        const bounds = canvas.getBoundingClientRect();
        return (event.clientX - bounds.left) * canvas.width / bounds.width;
        """)
static native double canvasX(HTMLCanvasElement canvas, Event event);

@JSBody(params = { "canvas", "event" }, script = """
        const bounds = canvas.getBoundingClientRect();
        return (event.clientY - bounds.top) * canvas.height / bounds.height;
        """)
static native double canvasY(HTMLCanvasElement canvas, Event event);

@JSBody(params = { "canvas", "event" }, script = """
        canvas.setPointerCapture(event.pointerId);
        """)
static native void capturePointer(HTMLCanvasElement canvas, Event event);
```

The interop methods translate the pointer position from the canvas's displayed size to its internal drawing coordinates. Without that scaling, pointer positions would be wrong whenever CSS changes the canvas size.

The app also checks that required elements exist and that the browser returns a 2D context. If either is missing, startup fails with an `IllegalStateException` rather than leaving a blank canvas with no explanation.

## Drawing a shape

The Java model knows a shape's position, dimensions and colour. `DrawingRenderer` takes that information and calls methods on the JSO `CanvasRenderingContext2D` to display it.

For a rectangle, the renderer sets the stroke and fill styles, fills the interior with reduced opacity, then draws the outline:

```java
context.setStrokeStyle(shape.getColor());
context.setFillStyle(shape.getColor());
context.setLineWidth(3);
if (shape.getType() == ShapeType.RECTANGLE) {
context.setGlobalAlpha(0.2);
context.fillRect(shape.getLeft(), shape.getTop(), shape.getWidth(), shape.getHeight());
context.setGlobalAlpha(1);
context.strokeRect(shape.getLeft(), shape.getTop(), shape.getWidth(), shape.getHeight());
}
```

Ellipses use `beginPath`, `ellipse`, `fill` and `stroke`. Arrows are drawn as a line segment plus two lines for the arrowhead.

## Responding to pointer events

A drawing application needs to react to the user's mouse or pointer.

The browser provides pointer events for pressing, moving and releasing a pointer. `DrawingApp` registers TeaVM `EventListener<Event>` handlers on the canvas. Each handler converts the event coordinates, updates the controller, and redraws:

```java
EventListener<Event> down = event -> {
    event.preventDefault();
    CanvasInterop.capturePointer(canvas, event);
    controller.pointerDown(CanvasInterop.canvasX(canvas, event), CanvasInterop.canvasY(canvas, event));
    renderer.render(controller);
};
EventListener<Event> move = event -> {
    controller.pointerMove(CanvasInterop.canvasX(canvas, event), CanvasInterop.canvasY(canvas, event));
    renderer.render(controller);
};
EventListener<Event> up = event -> {
    controller.pointerUp(CanvasInterop.canvasX(canvas, event), CanvasInterop.canvasY(canvas, event));
    renderer.render(controller);
};
EventListener<Event> cancel = event -> {
    controller.pointerCancel();
    renderer.render(controller);
};
```

A drawing gesture has a clear lifecycle: press starts a shape, movement updates its preview, and release commits it to the model.

The controller only commits a preview if it is large enough to see.

Pointer capture keeps delivering movement and release events to the canvas even when the pointer leaves its bounds during a drag. If the browser cancels the gesture, the `pointercancel` handler discards the preview.

## Rendering the whole drawing

Whenever the interaction changes the drawing, the app redraws the scene from the model:

```java
void render(DrawingController controller) {
    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    DrawingModel model = controller.getModel();
    for (DrawingShape shape : model.getShapes()) {
        drawShape(shape);
    }
    DrawingShape preview = controller.getPreview();
    if (preview != null) {
        context.setGlobalAlpha(0.7);
        drawShape(preview);
        context.setGlobalAlpha(1);
    }
    DrawingShape selected = model.getSelectedShape();
    if (selected != null) {
        drawSelection(selected);
    }
}
```

Each interaction updates the model, then redraws the whole scene. The app does this synchronously in response to pointer and toolbar events, without a separate animation loop.

## Selecting and moving shapes

Drawing shapes is only half the problem. We also need to interact with shapes that already exist.

Switching to selection mode changes what a pointer press means. Instead of starting a new shape, the application checks whether the pointer landed on an existing one. The model scans in reverse drawing order, so overlapping shapes select the topmost one:

```java
public boolean selectAt(double x, double y) {
    selectedIndex = -1;
    for (int i = shapes.size() - 1; i >= 0; i--) {
        if (shapes.get(i).contains(x, y)) {
            selectedIndex = i;
            return true;
        }
    }
    return false;
}
```

Hit testing is Java model logic. Rectangles test their normalized bounds, ellipses test whether the point falls inside the ellipse, and arrows use distance to their line segment with an 8-pixel tolerance. The arrowhead itself isn't hit-tested as a separate polygon.

Once a shape is selected, the renderer draws a visible selection indicator around it. Moving it follows the pointer delta and then redraws the scene:

```java
public void pointerMove(double x, double y) {
    if (preview != null) {
        preview.setEnd(x, y);
    } else if (moving) {
        model.moveSelectedBy(x - lastX, y - lastY);
    }
    lastX = x;
    lastY = y;
}
```

## Changing a shape's colour

Recolouring changes the appearance of an existing shape without changing its geometry.

The toolbar palette is registered as ordinary DOM click handlers. This helper updates the selected model shape, then redraws. If nothing is selected, the application displays a status message instead:

```java
private void registerColor(HTMLDocument document, String id, String color) {
    requireElement(document, id).addEventListener("click", event -> {
        if (controller.recolorSelected(color)) {
            status.setTextContent("Selected shape recoloured.");
            renderer.render(controller);
        } else {
            status.setTextContent("Select a shape before choosing a colour.");
        }
    });
}
```

## Try it yourself

The [TeaVM showcase drawing app](https://github.com/teggr/teavm-showcase/tree/main/teavm-showcase-drawing-app) includes the source, tests and browser assets. Use Java 17 or newer and Maven 3.9 or newer. From the showcase repository root, build and test the app, then start its Java static server:

```bash
mvn -pl teavm-showcase-drawing-app -am clean verify
mvn -f teavm-showcase-drawing-app/pom.xml process-classes exec:java
```

The second command compiles the TeaVM app and starts its Java static server. Open <http://localhost:8000/> and stop the server with Ctrl+C. Try drawing a few shapes, selecting one, moving it and changing its colour.

The editor has no persistence, undo/redo, resizing or export. Arrow hit testing checks the shaft, not a separate arrowhead polygon.

Because shapes remain explicit objects in the model, features such as undo and redo could build on recorded changes rather than trying to infer them from canvas pixels. This small editor doesn't implement that history, but the model gives it a clear place to start.

Across the series, we've tried browser interaction, asynchronous communication with a backend, and a stateful drawing editor. If you're a Java developer who's been curious about the browser, clone the showcase and try one of the examples. If you know other Java developers who might find this interesting, share the series with them. I'd be interested to see what they build with it.

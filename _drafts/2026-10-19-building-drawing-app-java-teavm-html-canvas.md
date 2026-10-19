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

# Building a Drawing App with Java, TeaVM and HTML Canvas

In the first article in this series, we took a small Java guessing game and got it running in the browser with TeaVM. The second article went further, building a currency explorer with a Java client, a Spring Boot API, asynchronous JSON requests and a chart rendered with Chart.js.

For the final article, I wanted to try something a little more interactive.

What about building a drawing application?

Not a replacement for Excalidraw, and not a full graphics editor. Just a small application where we can draw shapes, select them, move them around and change their colours.

It brings together a few useful problems. We need to represent the drawing in Java, respond to pointer events in the browser, work out which shape the user has selected, and render the result on screen.

It's also a good opportunity to explore a central idea behind TeaVM: we can write much of our application in Java without giving up the capabilities of the browser.

## What we're building

The finished application has a canvas and a small toolbar.

We can choose a rectangle, ellipse or arrow tool, drag on the canvas to create a shape, and switch to selection mode to move existing shapes. A colour palette lets us change the appearance of the selected shape.

That's enough functionality to feel like a small application rather than a technical demonstration, while keeping the implementation understandable.

![TeaVM drawing app running in Chrome](/images/teavm-drawing-app.png)

The interesting part isn't any individual shape. It's how we connect Java application logic to the browser's drawing and input APIs.

## Start with a model of the drawing

Before drawing anything, we need to decide how the application will represent its contents.

One option would be to draw directly onto the canvas whenever the user interacts with it. The problem is that the canvas only contains pixels. It doesn't know that a particular collection of pixels represents a rectangle, or that the rectangle should move when the user drags it.

Instead, the application keeps a model of the drawing in Java. Each shape stores its type, start and end points, and colour. Rectangles and ellipses derive normalized bounds from those points, while arrows retain their direction.

```java
public final class DrawingShape {
    private final ShapeType type;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private String color;

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
        canvas.setPointerCapture(event.pointerId);
        """)
static native void capturePointer(HTMLCanvasElement canvas, Event event);
```

The horizontal coordinate is offset by the canvas's displayed left edge, then scaled from its displayed width to its internal width. `canvasY` applies the same conversion vertically. Without that scaling, pointer positions would be wrong whenever the canvas is displayed at a different size from its drawing surface.

The app also checks that required elements exist and that the browser returns a 2D context. If either is missing, startup fails with an `IllegalStateException` rather than leaving a blank canvas with no explanation.

## Drawing a shape

The Java model knows a shape's position, dimensions and colour. `DrawingRenderer` takes that information and calls methods on the JSO `CanvasRenderingContext2D` to display it.

For a rectangle, the renderer sets the stroke and fill styles, fills the interior with reduced opacity, then draws the outline:

```java
context.setStrokeStyle(shape.getColor());
context.setFillStyle(shape.getColor());
context.setLineWidth(3);
context.setGlobalAlpha(0.2);
context.fillRect(shape.getLeft(), shape.getTop(), shape.getWidth(), shape.getHeight());
context.setGlobalAlpha(1);
context.strokeRect(shape.getLeft(), shape.getTop(), shape.getWidth(), shape.getHeight());
```

Ellipses use `beginPath`, `ellipse`, `fill` and `stroke`. Arrows are drawn as a line segment plus two lines for the arrowhead. The model doesn't need to know those Canvas details. It describes the shape; the renderer knows how to display it.

This keeps the interop focused. We don't need to move the whole application into JavaScript just because the final drawing operation uses a browser API.

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
```

When the user chooses a drawing tool and drags across the canvas, the controller records the starting point, updates a provisional shape as the pointer moves, then adds it to the model on release if it is large enough to see.

Pointer capture matters here. `setPointerCapture` keeps delivering movement and release events to the canvas even when the pointer leaves its bounds during a drag. If the browser cancels the gesture, the `pointercancel` handler discards the preview.

Coordinates aren't automatically canvas coordinates. The `@JSBody` code above converts page-relative pointer positions to the canvas's intrinsic drawing coordinates, including when CSS changes the displayed size.

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

The canvas is a visual representation of the current state, not the place where that state lives. Moving a shape becomes a model update followed by a redraw. Recolouring works the same way, as does adding a new shape.

The app redraws synchronously in response to pointer and toolbar interactions. It doesn't run a separate animation loop. For a small editor, this gives us a predictable rendering model without a more complicated graphics engine.

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

Notice how little the Canvas API needs to know about selection. The decision about which shape was clicked belongs to our Java application. The browser reports the pointer event, and the renderer displays the result.

That separation is one of the useful outcomes of keeping a model in Java.

## Changing a shape's colour

Recolouring changes the appearance of an existing shape without changing its geometry.

The toolbar palette is registered as ordinary DOM click handlers. Java updates the selected model shape, then redraws. If nothing is selected, the application displays a status message instead:

```java
requireElement(document, "color-red").addEventListener("click", event -> {
    if (controller.recolorSelected("#ef4444")) {
        status.setTextContent("Selected shape recoloured.");
        renderer.render(controller);
    } else {
        status.setTextContent("Select a shape before choosing a colour.");
    }
});
```

We don't need to erase or recolour pixels individually. The next render simply uses the updated model.

## Where TeaVM fits

By this point, we've used several capabilities that are native to the browser:

- HTML for the page structure.
- Canvas for drawing.
- Pointer events for input.
- JavaScript interop to connect the parts TeaVM's JSO bindings don't expose directly.

TeaVM allows our Java application to run in that environment, but it doesn't remove the need to understand the environment itself.

The drawing model, shape selection, hit testing and position updates remain ordinary Java logic. TeaVM's JSO bindings expose the DOM and Canvas 2D methods to Java. A small `@JSBody` bridge reads pointer properties and requests pointer capture.

That is the distinction I wanted this example to demonstrate. We're using Java to build an application that participates in the web platform, calling into browser APIs where they fit.

## Building and running the application

The drawing editor is part of the [TeaVM showcase repository](https://github.com/teggr/teavm-showcase), alongside the examples from the earlier articles.

Use Java 17 or newer and Maven 3.9 or newer. From the repository root, build and test the drawing editor, then serve the generated static app:

```bash
mvn -pl teavm-showcase-drawing-app -am clean verify
python -m http.server 8000 --directory teavm-showcase-drawing-app/target/javascript
```

Open <http://localhost:8000/>. Serve the generated output over HTTP rather than opening the HTML file directly.

Try drawing a few shapes, selecting one, moving it and changing its colour. The point isn't to produce a feature-rich graphics editor. It's to see how the model, event handling and rendering work together.

The application has no persistence, undo/redo, resizing or export. Its hit testing is intentionally small too: arrow selection checks the shaft, not a separate arrowhead polygon.

## What we've learned

Across these three articles, we've moved from a small guessing game to a browser application that communicates with a server, uses a JavaScript charting library and responds to continuous user interaction.

Each example explored a different part of the same idea.

TeaVM makes it possible to use Java for much of the logic in a browser application. The browser still supplies the platform: its document model, network APIs, drawing capabilities and JavaScript ecosystem.

Interop is the connection between those worlds. In this example, most drawing and DOM work uses TeaVM's JSO bindings, while `@JSBody` provides a small bridge for pointer properties that the typed API doesn't expose. That lets the Java code handle the drawing model and interactions while Canvas renders the result.

That feels like a useful place to finish this series: showing how far we can take a Java application when we work with the browser and its APIs.

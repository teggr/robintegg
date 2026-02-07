---
layout: post
title: "Java UI in 2026: The Complete Framework Guide"
---

When developers think about building user interfaces today, React, Angular, and Vue often dominate the conversation. But here's the truth that many miss: **React is not the only player in the room.** Java has a thriving ecosystem of UI frameworks across every platform—desktop, web, mobile, and even terminal environments. These aren't legacy projects limping along; they're production-ready, actively maintained, and powering applications at major enterprises and serving millions of users worldwide.

This guide covers 25+ Java UI frameworks as of 2026, organized by platform. Whether you're building enterprise desktop applications, server-driven web apps, cross-platform mobile experiences, or sophisticated terminal interfaces, there's a modern Java solution ready for you. Each framework includes a summary, code example, and links to get started. Think of this as your reference point and jumping-off place into Java's diverse UI ecosystem.

---

## Desktop UI Frameworks

The Java desktop landscape in 2026 offers everything from mature, battle-tested frameworks like JavaFX and Swing to modern declarative approaches like Compose Desktop. Whether you need native OS widgets, custom-rendered interfaces, or even desktop applications that can also run in a browser, Java has you covered. Rich client platforms like NetBeans and Eclipse RCP provide complete application frameworks with plugin systems, while specialized tools like JCEF and JxBrowser let you embed modern web content seamlessly.

### JavaFX
**Status:** Production-ready | **Java:** 11+ (17+ recommended) | **Learning Curve:** Moderate | **Last Release:** Ongoing (OpenJFX)

JavaFX is the modern standard for Java desktop applications. It provides a rich set of UI controls including tables, trees, charts, media players, and even 3D graphics with hardware-accelerated rendering. The framework supports CSS styling for theming, FXML for separating UI markup from logic, and includes Scene Builder for visual UI design. JavaFX's WebView component (WebKit-based) enables hybrid desktop apps that combine native controls with web content. It's cross-platform by default and has been the go-to choice for enterprise desktop applications requiring modern, professional interfaces. Note that JavaFX is maintained separately under the OpenJFX project and is no longer part of the standard JDK distribution.

**Code Example:**
```java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloJavaFX extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("Hello, JavaFX!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 300, 200);
        stage.setTitle("JavaFX Example");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

**Learn More:** [https://openjfx.io/](https://openjfx.io/)

---

### Compose Desktop
**Status:** Production-ready | **Java:** 11+ (JDK 17+ for packaging) | **Learning Curve:** Moderate (requires Kotlin) | **Last Release:** v1.10.0 (Jan 2026)

Compose Desktop brings JetBrains' modern declarative UI framework to the desktop. Based on Jetpack Compose from Android, it uses Kotlin and a declarative programming model similar to React or SwiftUI. The framework features hardware-accelerated rendering via Skia, hot reload for rapid development, and seamless integration with Kotlin Multiplatform for sharing code across desktop, mobile, and web. Real-world apps like Wrike and Physics Wallah (17M users) demonstrate its production readiness. If you're comfortable with Kotlin and want a modern, declarative approach to desktop UI, Compose Desktop is an excellent choice.

**Code Example:**
```kotlin
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    MaterialTheme {
        Text("Hello, Compose Desktop!")
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Compose Example") {
        App()
    }
}
```

**Learn More:** [https://www.jetbrains.com/lp/compose-desktop/](https://www.jetbrains.com/lp/compose-desktop/)

---

### Swing + FlatLaf
**Status:** Legacy-but-actively-maintained | **Java:** 8+ (11+ recommended for HiDPI) | **Learning Curve:** Easy | **Last Release:** FlatLaf 3.7+ (Dec 2025)

Swing may be a legacy framework, but it's far from dead. The IntelliJ plugin ecosystem relies heavily on Swing, and modern look-and-feel libraries like FlatLaf have completely revitalized its appearance. FlatLaf brings flat, modern design inspired by IntelliJ IDEA and Darcula themes, with built-in light and dark modes, HiDPI/Retina support, and cross-platform consistency. It's actively developed by FormDev Software and used in production by commercial applications like Burp Suite, JOSM (OpenStreetMap editor), and countless IntelliJ plugins. If you're building IntelliJ plugins or maintaining Swing applications, FlatLaf makes them look contemporary with minimal effort.

**Code Example:**
```java
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class HelloSwingFlatLaf {
    public static void main(String[] args) {
        FlatDarkLaf.setup(); // Modern dark theme
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FlatLaf Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JLabel("Hello, Swing with FlatLaf!", SwingConstants.CENTER));
            frame.setSize(300, 200);
            frame.setVisible(true);
        });
    }
}
```

![FlatLaf Dark Theme Example](https://www.formdev.com/flatlaf/images/themes/dark.png)

**Learn More:** [https://www.formdev.com/flatlaf/](https://www.formdev.com/flatlaf/)

---

### Swing-Tree
**Status:** Production-ready | **Java:** 8+ | **Learning Curve:** Easy | **Last Release:** v0.13.0

Swing-Tree brings declarative, fluent APIs to Swing development—think Jetpack Compose or SwiftUI, but for Swing. Developed by Global TCAD Solutions for their own desktop applications, it eliminates Swing's verbose boilerplate with a clean, lambda-friendly API. The entire framework centers around a single `UI` class that can be statically imported, making Swing development feel modern and expressive. It integrates seamlessly with existing Swing components and works beautifully alongside FlatLaf for modern aesthetics. This is a community-driven library that's well-suited for developers seeking a more contemporary Swing development experience.

**Code Example:**
```java
import static swingtree.UI.*;

public class HelloSwingTree {
    public static void main(String[] args) {
        of(new JFrame("Swing-Tree Example"))
            .withDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
            .withSize(300, 200)
            .withLayout("fill, insets 20")
            .add("grow",
                panel("fill, wrap 1")
                .add("grow", label("Hello, Swing-Tree!"))
                .add("grow", button("Click Me")
                    .onClick(it -> System.out.println("Clicked!"))
                )
            )
            .show();
    }
}
```

**Learn More:** [https://github.com/globaltcad/swing-tree](https://github.com/globaltcad/swing-tree)

---

### NetBeans Platform
**Status:** Production-ready, Mature | **Java:** 8+ | **Learning Curve:** Steep | **Maintained By:** Apache Software Foundation

The NetBeans Platform is a full Rich Client Platform (RCP) for building modular desktop applications. NetBeans IDE itself—with over 7 million lines of code and 50,000+ files—is built on this platform, demonstrating its scalability. The platform provides 100+ modules covering window management, auto-update systems, file system abstractions, wizards, property sheets, and more. It's Swing-based and includes the Matisse visual GUI builder for drag-and-drop interface design. If you're building complex, modular desktop applications that need plugin architectures and extensive built-in services, NetBeans Platform provides a comprehensive foundation.

**Code Example:**
```java
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;
import javax.swing.JLabel;

public class HelloNetBeans extends ModuleInstall {
    @Override
    public void restored() {
        WindowManager.getDefault().invokeWhenUIReady(() -> {
            TopComponent tc = new TopComponent();
            tc.setLayout(new BorderLayout());
            tc.add(new JLabel("Hello, NetBeans Platform!"), BorderLayout.CENTER);
            tc.setDisplayName("Hello Example");
            tc.open();
            tc.requestActive();
        });
    }
}
```

**Learn More:** [https://netbeans.apache.org/platform/](https://netbeans.apache.org/platform/)

---

### Eclipse Rich Client Platform (RCP)
**Status:** Production-ready, Mature | **Java:** 8+ | **Learning Curve:** Steep | **Maintained By:** Eclipse Foundation

Eclipse RCP is an OSGi-based Rich Client Platform that powers the Eclipse IDE and countless enterprise applications from companies like IBM and SAP. It uses SWT for native widget rendering, giving applications a true native look and feel on each platform. The platform provides complete application infrastructure including window management, perspectives, views, plugin architectures, and update mechanisms. Eclipse RCP applications can support multiple versions of the same library simultaneously thanks to OSGi's sophisticated class loading. While the learning curve is steep due to OSGi and SWT concepts, the result is highly modular, enterprise-grade desktop applications with massive ecosystem support.

**Code Example:**
```java
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class HelloEclipseRCP {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Eclipse RCP Example");
        shell.setSize(300, 200);
        
        Label label = new Label(shell, SWT.CENTER);
        label.setText("Hello, Eclipse RCP!");
        label.setBounds(50, 80, 200, 30);
        
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
```

**Learn More:** [https://wiki.eclipse.org/Rich_Client_Platform](https://wiki.eclipse.org/Rich_Client_Platform)

---

### SnapKit
**Status:** Production-ready | **Java:** 8+ | **Learning Curve:** Easy-Moderate | **Last Release:** v2025.12 (Dec 2025)

SnapKit is "Swing 2.0"—a UI toolkit developed by ReportMill that aims to run on both desktop and in the browser. Used to power SnapCode (a Java IDE that runs in a browser), SnapKit can deploy to desktop via Swing or JavaFX, and to browsers via CheerpJ JVM. The framework features XML-based UI files with a visual GUI builder (SnapBuilder), a ViewOwner controller pattern, and support for geometric primitives, effects, and even 3D via WebGL/JOGL. While SnapKit presents an interesting approach for dual-platform development, it remains a niche framework with limited adoption compared to mainstream Java UI toolkits.

**Code Example:**
```java
import snap.view.*;
import snap.viewx.WebPage;

public class HelloSnapKit extends WebPage {
    protected View createUI() {
        Label label = new Label("Hello, SnapKit!");
        label.setFont(Font.Arial14.deriveFont(24));
        
        RowView row = new RowView();
        row.setPadding(50, 50, 50, 50);
        row.addChild(label);
        return row;
    }
    
    public static void main(String[] args) {
        new HelloSnapKit().run(args);
    }
}
```

**Learn More:** [https://github.com/reportmill/SnapKit](https://github.com/reportmill/SnapKit)

---

### JCEF (Java Chromium Embedded Framework)
**Status:** Production-ready | **Java:** Requires JetBrains Runtime | **Learning Curve:** Moderate-Complex | **Maintained By:** JetBrains + CEF project

JCEF is a Java wrapper around the Chromium Embedded Framework (CEF), providing a full Chromium/Blink rendering engine inside Swing applications. It's used extensively in IntelliJ IDEA for markdown previews, browser tools, and throughout the IntelliJ Platform plugin ecosystem. JCEF gives you JavaScript interop, Chrome DevTools access, and modern web rendering capabilities. However, it requires native library distribution and understanding of its multi-process architecture (browser vs renderer processes). JetBrains maintains an active fork that's kept in sync with Chromium updates, making it the de facto standard for embedding modern web content in Java desktop applications.

**Code Example:**
```java
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import javax.swing.*;
import java.awt.*;

public class HelloJCEF {
    public static void main(String[] args) {
        CefApp cefApp = CefApp.getInstance(args);
        CefClient client = cefApp.createClient();
        CefBrowser browser = client.createBrowser("https://www.example.com", false, false);
        
        JFrame frame = new JFrame("JCEF Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(browser.getUIComponent(), BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
```

**Learn More:** [https://github.com/jcefmaven/jcefmaven](https://github.com/jcefmaven/jcefmaven)

---

### JxBrowser
**Status:** Production-ready, Commercial | **Java:** 17+ | **Learning Curve:** Moderate | **Last Release:** v8.11.0 (2025)

JxBrowser is a commercial Chromium integration solution from TeamDev that provides superior architecture compared to JCEF. It supports Swing, JavaFX, and SWT with separate process isolation, regular Chromium upgrades (every 3-4 weeks after stable releases), and comprehensive features including hardware acceleration, HiDPI/Retina support, Chrome extensions, PDF rendering, network interception, and DOM manipulation. The Chromium sandbox is fully enabled on Windows and macOS, providing better security than alternatives. With a 30-day free trial and perpetual licensing starting at $1,799 for indie developers, JxBrowser is the professional choice when you need enterprise-grade Chromium embedding with excellent technical support.

**Code Example:**
```java
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.*;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import javax.swing.*;

public class HelloJxBrowser {
    public static void main(String[] args) {
        Engine engine = Engine.newInstance(EngineOptions.newBuilder()
            .build());
        Browser browser = engine.newBrowser();
        browser.navigation().loadUrl("https://www.example.com");
        
        SwingUtilities.invokeLater(() -> {
            BrowserView view = BrowserView.newInstance(browser);
            JFrame frame = new JFrame("JxBrowser Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(view, BorderLayout.CENTER);
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}
```

**Learn More:** [https://www.teamdev.com/jxbrowser](https://www.teamdev.com/jxbrowser)

---

### SWT (Standard Widget Toolkit)
**Status:** Production-ready, Mature | **Java:** 8+ | **Learning Curve:** Moderate | **Maintained By:** Eclipse Foundation

SWT takes a different approach from Swing and JavaFX by using native OS widgets rather than custom-drawn components. This means SWT applications automatically have the exact native look and feel of the underlying operating system. It's the core GUI library for the Eclipse IDE and provides high performance through direct native widget access. While this native approach limits cross-platform UI consistency, it excels when you need applications that feel completely native to each platform. SWT is stable, mature, and backed by the Eclipse Foundation with a large ecosystem of tools and plugins.

**Code Example:**
```java
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class HelloSWT {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT Example");
        shell.setSize(300, 200);
        
        Label label = new Label(shell, SWT.CENTER);
        label.setText("Hello, SWT!");
        label.setBounds(50, 80, 200, 30);
        
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
```

**Learn More:** [https://www.eclipse.org/swt/](https://www.eclipse.org/swt/)

---

## Web UI Frameworks

The Java web UI landscape offers diverse approaches to building web applications. Full-stack Java frameworks like Vaadin and Wicket let you write UIs entirely in Java without touching JavaScript. Component libraries like PrimeFaces provide rich UI widgets for Jakarta EE applications. Modern approaches like HTMX with Spring Boot embrace hypermedia-driven development with minimal JavaScript. Type-safe builders like j2html give you compile-time safety for HTML generation. And compilers like GWT and TeaVM transpile Java bytecode to JavaScript or WebAssembly, letting you target the browser while staying in the Java ecosystem.

### Vaadin
**Status:** Production-ready, Enterprise-focused | **Java:** 17+ (21+ recommended) | **Learning Curve:** Easy | **Last Release:** Vaadin 25

Vaadin is a server-driven UI framework where you write your entire web interface in Java—no JavaScript required. Components live on the JVM and automatically synchronize with the browser, handling all the client-server communication transparently. Vaadin 25 brings deep Spring Boot 3.x integration, support for Java 25 LTS, and the modern Aura design system for professional UIs out of the box. The framework includes AI-assisted development tools and emphasizes security through server-side rendering, which reduces XSS attack surfaces. Vaadin is used by many large enterprises and excels at enterprise dashboards, internal tools, and line-of-business applications where Java teams want to avoid JavaScript context-switching.

**Code Example:**
```java
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;

@Route("")
public class HelloVaadin extends VerticalLayout {
    public HelloVaadin() {
        add(new H1("Hello, Vaadin!"));
        add(new Button("Click me", 
            e -> add(new H1("Button clicked!"))));
    }
}
```

**Learn More:** [https://vaadin.com/](https://vaadin.com/)

---

### Apache Wicket
**Status:** Production-ready, Mature (since 2004) | **Java:** 17+ (Wicket 10) | **Learning Curve:** Moderate | **Last Release:** Wicket 10.7.0 (Sep 2025)

Apache Wicket is a component-oriented web framework that brings Swing-like development to the web. It uses pure Java and HTML markup (no XML), with clean separation between markup and logic. Wicket manages stateful web applications automatically, treating models as opaque POJOs that are serialized between requests. The framework's component hierarchy—Pages, Components, Models—feels familiar to desktop developers and enables significant code reuse through Panels. Wicket 10, built on Java 17, includes Ajax support without writing JavaScript, WebSocket integration, and compatibility with Spring, CDI, and Guice. With 10% of enterprise Java developers using it and thousands of applications deployed worldwide at governments, banks, and universities, Wicket has proven its staying power since surviving the mid-2000s Java web framework wars.

**Code Example:**
```java
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class HelloWicket extends WebPage {
    public HelloWicket() {
        add(new Label("message", "Hello, Wicket!"));
    }
}

// Corresponding HTML (HelloWicket.html):
// <html>
//   <body>
//     <span wicket:id="message"></span>
//   </body>
// </html>
```

**Learn More:** [https://wicket.apache.org/](https://wicket.apache.org/)

---

### PrimeFaces
**Status:** Production-ready | **Java:** 8+ | **Learning Curve:** Easy | **Use Case:** JavaServer Faces (JSF) applications

PrimeFaces is the premier UI component library for Jakarta EE's JavaServer Faces framework. It provides over 100 rich, modern components including data tables, charts, dialogs, file uploads, and more—all with responsive design built in. PrimeFaces emphasizes ease of use with minimal configuration and extensive documentation, making it straightforward to build professional enterprise web applications. Backed by PrimeTek, a commercial company that ensures consistent development and support, PrimeFaces has become the de facto standard for teams building JSF applications in the Jakarta EE ecosystem.

**Code Example:**
```java
// Backing bean
@Named
@ViewScoped
public class HelloBean implements Serializable {
    private String message = "Hello, PrimeFaces!";
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

// XHTML view:
// <p:outputLabel value="#{helloBean.message}" />
// <p:commandButton value="Update" update="@form" />
```

**Learn More:** [https://www.primefaces.org/](https://www.primefaces.org/)

---

### Jakarta Faces (formerly JSF)
**Status:** Production-ready, Enterprise standard | **Java:** 8+ | **Learning Curve:** Moderate | **Maintained By:** Eclipse Foundation

Jakarta Faces (the evolution of JavaServer Faces under the Jakarta EE umbrella) is a component-focused web framework deeply integrated with enterprise Java. As part of the Jakarta EE specification, it provides standardized UI component development with server-side state management, event handling, and validation. The framework includes drag-and-drop features in supporting IDEs and works seamlessly with other Jakarta EE technologies like CDI, JPA, and Bean Validation. For teams already invested in the Jakarta EE ecosystem, Faces provides a stable, standardized approach to web UI development with broad vendor support and extensive enterprise tooling.

**Code Example:**
```java
// Backing bean
@Named
@RequestScoped
public class HelloFaces {
    private String message = "Hello, Jakarta Faces!";
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

// XHTML view:
// <h:outputText value="#{helloFaces.message}" />
```

**Learn More:** [https://jakarta.ee/specifications/faces/](https://jakarta.ee/specifications/faces/)

---

### Google Web Toolkit (GWT)
**Status:** Mature, Maintained | **Java:** 8+ | **Learning Curve:** Moderate | **First Release:** 2006

Google Web Toolkit compiles Java source code to optimized JavaScript, HTML, and CSS, enabling you to write entire web applications in Java without writing JavaScript directly. GWT was revolutionary when released in 2006, powering large-scale applications with its sophisticated compiler optimizations and cross-browser compatibility guarantees. While newer frameworks have emerged, GWT remains actively maintained by Google and continues to power many enterprise applications built during its peak from 2006-2015. It's a mature, stable choice for teams comfortable with Java who want to generate optimized JavaScript without learning the modern JavaScript ecosystem.

**Code Example:**
```java
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;

public class HelloGWT implements EntryPoint {
    public void onModuleLoad() {
        Label label = new Label("Hello, GWT!");
        RootPanel.get().add(label);
    }
}
```

**Learn More:** [http://www.gwtproject.org/](http://www.gwtproject.org/)

---

### TeaVM
**Status:** Production-ready | **Java:** Any (bytecode-based) | **Learning Curve:** Easy | **Last Release:** Active (2025-2026)

TeaVM is an ahead-of-time compiler that transpiles Java bytecode to JavaScript or WebAssembly, making it work with any JVM language—Java, Kotlin, Scala, or others. Unlike GWT which works on source code, TeaVM operates on bytecode, giving it broader language support. The framework supports the new WebAssembly GC standard for garbage-collected languages and produces readable, optimized JavaScript without requiring npm, Webpack, or other frontend build tools. TeaVM includes JSO (JavaScript Objects) API for JS interop, DOM module for browser APIs, and sophisticated optimizations like dead code elimination. It's used by WebFX (JavaFX apps in browsers) and various projects where backend Java developers want to target the web without learning the JavaScript ecosystem.

**Code Example:**
```java
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

public class HelloTeaVM {
    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement body = document.getBody();
        HTMLElement div = document.createElement("div");
        div.setInnerHTML("Hello, TeaVM!");
        body.appendChild(div);
    }
}
```

**Learn More:** [https://teavm.org/](https://teavm.org/)

---

### HTMX + Spring Boot
**Status:** Modern, Very Active | **Java:** 11+ (Spring Boot 2.x+), 17+ (Spring Boot 3.x+) | **Learning Curve:** Easy | **Last Release:** htmx-spring-boot v3.x+

HTMX brings hypermedia-driven development to modern web apps by adding AJAX, WebSockets, and Server-Sent Events via HTML attributes—no JavaScript needed. The htmx-spring-boot library from Wim Deblauwe (with contributions from Spring team members like Oliver Drotbohm) provides annotations like `@HxRequest`, `@HxTrigger`, and `@HxRefresh`, along with specialized views for redirects and out-of-band swaps. The Spring ViewComponent library by Thomas Schühly takes this further with component-based architecture similar to React/Vue but entirely server-side, where ViewComponents are Spring-managed beans with dependency injection. This approach combines the simplicity of server-side rendering with the interactivity of modern SPAs, using template engines like Thymeleaf or JTE. It's gaining strong traction in the Spring community for teams who want dynamic interfaces without heavy JavaScript frameworks.

**Code Example:**
```java
@Controller
public class HelloController {
    
    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "Hello, HTMX + Spring!");
        return "hello";
    }
    
    @GetMapping("/update")
    @HxRequest
    public String update(Model model) {
        model.addAttribute("message", "Updated via HTMX!");
        return "hello :: message";
    }
}

// Thymeleaf template (hello.html):
// <div id="content">
//   <p th:fragment="message" th:text="${message}"></p>
//   <button hx-get="/update" hx-target="#content" hx-swap="innerHTML">
//     Update
//   </button>
// </div>
```

**Learn More:** [https://github.com/wimdeblauwe/htmx-spring-boot](https://github.com/wimdeblauwe/htmx-spring-boot)

---

### j2html
**Status:** Production-ready | **Java:** 8+ | **Learning Curve:** Easy | **Last Release:** v1.6.0

j2html is not a template engine—it's a type-safe HTML builder for pure Java code. Using a fluent API, you construct HTML with compile-time safety, eliminating risks of unclosed tags, mistyped attributes, or XSS vulnerabilities through automatic escaping. It's extremely fast (100k renders in under 1 second, roughly 1000x faster than Velocity) and enables dynamic reuse of view code that's difficult with templates. j2html works particularly well with lightweight frameworks like Javalin for building APIs that return HTML fragments or generating dynamic emails and forms. While not suitable for traditional websites with large amounts of static HTML or heavy copy-pasted CSS framework markup, it excels when type safety and Java-centric development are priorities.

**Code Example:**
```java
import static j2html.TagCreator.*;

public class HelloJ2Html {
    public static void main(String[] args) {
        String html = html(
            head(
                title("j2html Example")
            ),
            body(
                h1("Hello, j2html!"),
                p("Type-safe HTML in Java"),
                button("Click me").withClass("btn")
            )
        ).render();
        
        System.out.println(html);
    }
}
```

**Learn More:** [https://j2html.com/](https://j2html.com/)

---

### Thymeleaf
**Status:** Production-ready | **Java:** 8+ | **Learning Curve:** Easy | **Use Case:** Server-side template engine for Spring Boot

Thymeleaf is a modern server-side template engine that emphasizes natural templating—templates are valid HTML that can be viewed directly in browsers without running the application. This makes collaboration with designers easier since templates aren't filled with non-HTML syntax. Thymeleaf integrates deeply with the Spring ecosystem, particularly Spring Boot, where it's often the default templating choice. The framework supports expression language for accessing model data, fragment inclusion for reusable components, and natural formatting for dates, numbers, and text. For Spring Boot applications needing traditional server-side rendered views with good designer-developer collaboration, Thymeleaf is the standard choice.

**Code Example:**
```java
@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "Hello, Thymeleaf!");
        return "hello";
    }
}

// Thymeleaf template (hello.html):
// <!DOCTYPE html>
// <html xmlns:th="http://www.thymeleaf.org">
// <body>
//   <h1 th:text="${message}">Placeholder</h1>
// </body>
// </html>
```

**Learn More:** [https://www.thymeleaf.org/](https://www.thymeleaf.org/)

---

## Mobile UI Frameworks

Java's mobile UI landscape in 2026 centers on cross-platform solutions that maximize code sharing between iOS and Android. Compose Multiplatform brings Kotlin's modern declarative UI to mobile with up to 96% code reuse. Gluon Mobile extends JavaFX to mobile platforms, recently achieving a major milestone with OpenJDK running natively on iOS. Codename One offers true write-once-run-anywhere capabilities with cloud-based builds that eliminate the need for Mac hardware when targeting iOS. All three frameworks are production-ready and powering real applications with millions of users.

### Compose Multiplatform
**Status:** Production-ready (iOS stable v1.8.0+) | **Java:** 11+ (Kotlin-based) | **Learning Curve:** Moderate | **Last Release:** v1.10.0 (Jan 2026)

Compose Multiplatform from JetBrains extends their modern declarative UI framework across mobile (iOS, Android), desktop, and web platforms. Based on Jetpack Compose from Android, it enables sharing up to 90-96% of code including UI and business logic across platforms. The framework features hardware-accelerated rendering via Skia, stable hot reload as of v1.10.0, and seamless Kotlin Multiplatform integration. Real-world applications demonstrate its production readiness: Feres taxi app has 1M+ downloads with 90% shared UI, Markaz e-commerce handles 100+ screens for 5M+ users, and Physics Wallah serves 17M users. With SwiftUI/UIKit interoperability for iOS and active development from JetBrains, Compose Multiplatform represents the modern approach to cross-platform mobile development from the Kotlin ecosystem.

**Code Example:**
```kotlin
@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Hello, Compose Multiplatform!")
            Button(onClick = { println("Clicked!") }) {
                Text("Click Me")
            }
        }
    }
}
```

**Learn More:** [https://www.jetbrains.com/lp/compose-multiplatform/](https://www.jetbrains.com/lp/compose-multiplatform/)

---

### Gluon Mobile
**Status:** Production-ready, Actively Maintained | **Java:** 17+ | **Learning Curve:** Moderate | **Last Release:** JavaFX 25.0.2 (Jan 2026)

Gluon Mobile brings JavaFX to iOS and Android platforms, enabling developers to write desktop and mobile applications from a single codebase. Developed by Gluon HQ, who co-lead the OpenJFX project itself, the framework provides commercial JavaFX ports with native integrations for mobile platforms. Gluon provides Scene Builder for drag-and-drop UI design, GluonFX Maven and Gradle plugins for building, and optional cloud build services. The framework supports GraalVM native image compilation for mobile deployment (as iOS has historically required ahead-of-time compilation rather than standard JVM execution) and includes commercial long-term support options. For teams already invested in JavaFX for desktop who want to extend to mobile, Gluon Mobile provides a natural migration path.

**Code Example:**
```java
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloGluon extends Application {
    @Override
    public void start(Stage stage) {
        View view = new View("Hello") {
            {
                setCenter(new Label("Hello, Gluon Mobile!"));
            }
            
            @Override
            protected void updateAppBar(AppBar appBar) {
                appBar.setTitleText("Gluon Example");
            }
        };
        
        AppManager.initialize();
        AppManager.getInstance().addViewFactory("hello", () -> view);
        AppManager.getInstance().start(stage);
    }
}
```

**Learn More:** [https://gluonhq.com/products/mobile/](https://gluonhq.com/products/mobile/)

---

### Codename One
**Status:** Production-ready, Mature (since 2012) | **Java:** 8+ (also supports Kotlin) | **Learning Curve:** Easy | **Last Release:** Active (Feb 2026)

Codename One offers true write-once-run-anywhere capabilities for mobile, desktop, and web from a single Java or Kotlin codebase. What makes it unique is the cloud-based build service that eliminates the need for Mac hardware when building iOS applications—it handles Xcode compilation in the cloud. The framework produces truly native apps (not hybrid WebView apps) by statically compiling bytecode to native code: ParparVM for iOS translates bytecode to C, while TeaVM handles web/PWA targets. With 100% code reuse across platforms, drag-and-drop GUI builder, and fast simulator with hot reload, Codename One has powered applications at banks, governments, and telecom companies including BeRider (Prague scooters), HBZ Mobile (banking), and yHomework (1M+ installs). Founded by ex-Sun Microsystems developers from the LWUIT project, it combines open-source core with commercial build services under GPL + Commercial Exception licensing (free for commercial use).

**Code Example:**
```java
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;

public class HelloCodenameOne {
    public void start() {
        Form form = new Form("Codename One", BoxLayout.y());
        form.add(new Label("Hello, Codename One!"));
        form.add(new Button("Click Me", e -> 
            Dialog.show("Info", "Button clicked!", "OK", null)
        ));
        form.show();
    }
}
```

**Learn More:** [https://www.codenameone.com/](https://www.codenameone.com/)

---

## Terminal UI Frameworks

Terminal-based UIs remain essential for server administration, development tools, and environments where graphical displays aren't available. Java's terminal frameworks provide sophisticated text-based interfaces comparable to C's curses library. Lanterna offers a complete GUI toolkit for terminals with windows, buttons, and layouts, while JLine provides advanced console input handling with features like command history, tab completion, and syntax highlighting. Both are pure Java, cross-platform, and used in production tools from Maven to Groovy to Kotlin's REPL.

### Lanterna
**Status:** Production-ready, Mature | **Java:** 8+ | **Learning Curve:** Easy | **Last Release:** v3.1.2

Lanterna is Java's answer to the C curses library for building text-based GUIs in terminal environments—with even more functionality. It's 100% pure Java with no native dependencies, working on any xterm-compatible terminal (konsole, gnome-terminal, putty, xterm, etc.) across Windows, macOS, Linux, and Unix-like systems. The framework provides three layers of abstraction: a low-level terminal layer for direct cursor control, a screen layer using a full-screen buffer/bitmap approach, and a complete GUI layer with windows, buttons, labels, and layouts. Notably, Lanterna bundles a Swing terminal emulator, so you can develop in your IDE (even though it doesn't support ANSI) and deploy unchanged to headless servers. It's perfect for temperature monitoring systems, server administration tools, text-based dashboards, or any headless environment requiring interactive UI.

**Code Example:**
```java
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class HelloLanterna {
    public static void main(String[] args) throws Exception {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        Screen screen = factory.createScreen();
        screen.startScreen();
        
        WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
        Window window = new BasicWindow("Lanterna Example");
        
        Panel panel = new Panel(new LinearLayout());
        panel.addComponent(new Label("Hello, Lanterna!"));
        panel.addComponent(new Button("Close", window::close));
        
        window.setComponent(panel);
        gui.addWindow(window);
        gui.waitForWindowToClose(window);
    }
}
```

**Learn More:** [https://github.com/mabe02/lanterna](https://github.com/mabe02/lanterna)

---

### JLine
**Status:** Production-ready, Very Active | **Java:** 11+ (JLine 4.x), 8+ (JLine 3.x) | **Learning Curve:** Easy-Moderate | **Last Release:** v4.0.0

JLine is Java's alternative to GNU Readline, providing advanced console input handling with portability, flexibility, and deep Java integration. The library offers line editing with Emacs and Vi modes, persistent command history with search, customizable tab completion for commands and file paths, syntax highlighting with custom rules, and password masking. JLine 4.0 brings full JPMS support (proper module-info.java), FFM terminal provider for JDK 22+ with Foreign Function & Memory API, and requires Java 11+ and Maven 4.0+. The modular structure includes terminal abstraction, line reading with completion, a command framework (jline-console), styling support, and built-in commands. Used by major tools like Maven, Gradle, Groovy, and Kotlin REPL, JLine is the standard choice for building interactive shells, REPLs, and sophisticated CLI applications in Java.

**Code Example:**
```java
import org.jline.reader.*;
import org.jline.terminal.*;

public class HelloJLine {
    public static void main(String[] args) throws Exception {
        Terminal terminal = TerminalBuilder.builder().build();
        LineReader reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .build();
        
        String line = reader.readLine("Enter your name: ");
        terminal.writer().println("Hello, " + line + "!");
        terminal.flush();
    }
}
```

**Learn More:** [https://github.com/jline/jline3](https://github.com/jline/jline3)

---

## Choosing the Right Framework

With 25+ frameworks across four platforms, selecting the right one depends on your specific needs. Here's a quick reference guide organized by common scenarios.

### By Platform

**Building a desktop application?**
- **Modern standard:** JavaFX (cross-platform, rich controls, mature ecosystem)
- **Kotlin-first declarative UI:** Compose Desktop (modern, hot reload, Kotlin Multiplatform)
- **IntelliJ plugins or maintaining Swing apps:** Swing + FlatLaf (modern look, active community)
- **Large modular applications with plugin systems:** NetBeans Platform or Eclipse RCP

**Building a web application?**
- **Full-stack Java, no JavaScript:** Vaadin (server-driven, enterprise-focused) or Apache Wicket (component-based, Swing-like)
- **Jakarta EE ecosystem:** PrimeFaces or Jakarta Faces
- **Modern hypermedia-driven apps:** HTMX + Spring Boot (minimal JS, Spring ecosystem)
- **Type-safe HTML generation:** j2html (compile-time safety, fast)
- **Traditional server-side templates:** Thymeleaf (natural templates, Spring integration)
- **Compile Java to browser code:** TeaVM (bytecode → JS/Wasm, supports any JVM language) or GWT (mature, Google-backed)

**Need mobile + desktop from one codebase?**
- **Kotlin-first, modern declarative:** Compose Multiplatform (up to 96% code sharing, 17M+ users)
- **JavaFX-based:** Gluon Mobile (GraalVM native compilation for mobile platforms)
- **True write-once-run-anywhere with cloud iOS builds:** Codename One (no Mac needed)

**Building terminal/CLI tools?**
- **Text-based GUIs:** Lanterna (complete GUI toolkit for terminals)
- **Advanced console input:** JLine (readline alternative, used by Maven/Gradle)

### By Development Style

**Want declarative UI?**
→ Compose Desktop (Kotlin), Swing-Tree (Java, Swing-based)

**Want to avoid JavaScript entirely?**
→ Vaadin, Apache Wicket, j2html, TeaVM

**Want minimal JavaScript with HTML attributes?**
→ HTMX + Spring Boot (hypermedia-driven)

**Want component-based architecture?**
→ Apache Wicket, Vaadin, PrimeFaces, Jakarta Faces (web), NetBeans/Eclipse RCP (desktop)

**Want type safety and compile-time checking?**
→ j2html (HTML in Java), Compose Desktop/Multiplatform (Kotlin DSL)

**Need native OS look-and-feel?**
→ SWT (true native widgets), Eclipse RCP

**Want modern flat design easily?**
→ FlatLaf (Swing), Vaadin (Aura design system)

---

## Conclusion

Java UI development in 2026 is alive, modern, and production-ready across every platform. From declarative frameworks like Compose Desktop to server-driven solutions like Vaadin, from cross-platform mobile frameworks like Codename One to sophisticated terminal interfaces with Lanterna, the Java ecosystem offers mature, actively maintained options for every use case.

React and the JavaScript ecosystem may dominate mindshare, but they're far from the only players in the room. Java frameworks power critical applications at major enterprises, banks, governments, and serve hundreds of millions of users worldwide. Whether you're building enterprise desktop applications, server-rendered web apps, cross-platform mobile experiences, or terminal-based tools, there's a modern Java solution ready for production.

The frameworks covered here represent decades of collective development, battle-tested in real-world applications, and backed by active communities and commercial support. Pick the one that fits your platform and development style, explore the documentation, and start building. The Java UI ecosystem is waiting for you.

**Want to contribute?** Most of these frameworks are open-source. Check their GitHub repositories, try them out, report issues, submit pull requests, or share your experiences with the community. The ecosystem thrives when developers actively participate and share knowledge.

---

*Article researched and compiled February 2026. Framework information current as of publication date. Always check official documentation for the latest releases and features.*
---
layout: post
title: "First look at Java support in Visual Studio Code"
date: "2018-01-07"
---
Microsoft recently released a [Visual Studio Java extension pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) and a [Java Test Runner](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test) to the Visual Studio Code market place. The [Visual Studio Java extension pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) adds debugging support to the [Red Hat language support for Java](https://marketplace.visualstudio.com/items?itemName=redhat.java). The [Java Test Runner](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test) adds support for executing JUnit tests.

I've recently started to use Visual Studio in my work environment for JavaScript development and have now had a chance to take a look at the extension pack and the supported features.  
The hope is that the Visual Studio code could be ready to be a full replacement for Eclipse/Spring Tool Suite in my development toolset for Java and JavaScript applications.

## **tl;dr**

- Quick and easy installation (2-3 extensions)
- Good support for autocompletion, code actions and compilation errors in maven projects
- Out of the box support for maven projects without modules
- Supports Spring Boot application launching and debugging
- Older legacy or multi-module projects don't seem to be completely supported and documentation could be more thorough
- Lacking some workflow elements from Spring Tool Suite / Eclipse

Would I use Visual Studio code for a small new project or demo completely: **YES**, would I use Visual Studio code for every day development at work: **NOT YET**, perhaps on some smaller components as more time needed to determine support for those individual applications.

## **Assessing Java application development in Visual Studio code**

1. See what features are available
2. See if features cover my common use cases
3. Summary

There is a comprehensive tutorial available with azure integrations on the Visual Studio code web site https://code.visualstudio.com/docs/java/java-tutorial.

### **Installation Pre-requisites**

- JDK
- VSCode

### **Installation**

The **[Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)** should be available through the Visual Studio Code" extension tab in the editor.

![]({{site.baseurl}}/assets/images/extension-pack-list-item.png)

The **Java Extension Pack** wraps two extensions togther.

- Language Support for Java by RedHat
- Debugger for Java by Microsoft

Note: After installation make sure you read the individual extension installation instructions. You will need to set the \`java.home\` user setting as per language extension instructions. Don't forget double backslashes in the path to the **root** of your JDK.  
Install the **[Java Test Runner](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test)** extension

![]({{site.baseurl}}/assets/images/test-runner-extension.png)

## **Creating a maven project**

Start by opening a new Visual Studio Code window (Ctrl + Shift + N) and opening a new console (Ctrl + ').  
Navigate to directory where you want to create your new maven project.

```
cd {to-you-project-dir}
```

Then create a new empty maven project using the maven command line. Alternatively the created project can be cloned from the github project [teggr/vs-code-first-look](https://github.com/teggr/vs-code-first-look/)

```
mvn -B archetype:generate \
      -DarchetypeGroupId=org.apache.maven.archetypes \
      -DgroupId=com.robintegg.blog \
      -DartifactId=vs-code-first-look
```

Maven will then create a new folder called **vs-code-first-look** with a prepopulated \`pom.xml\`, \`App\` class and \`AppTest\` unit test.

```
log[INFO] ----------------------------------------------------------------------------[INFO] Using following parameters for creating project from Old (1.x) Archetype: maven-archetype-quickstart:1.0[INFO] ----------------------------------------------------------------------------[INFO] Parameter: basedir, Value: C:\projects[INFO] Parameter: package, Value: com.robintegg.blog[INFO] Parameter: groupId, Value: com.robintegg.blog[INFO] Parameter: artifactId, Value: vs-code-first-look[INFO] Parameter: packageName, Value: com.robintegg.blog[INFO] Parameter: version, Value: 1.0-SNAPSHOT[INFO] project created from Old (1.x) Archetype in dir: C:\projects\vs-code-first-look[INFO] ------------------------------------------------------------------------[INFO] BUILD SUCCESS[INFO] ------------------------------------------------------------------------[INFO] Total time: 30.087 s[INFO] Finished at: 2018-01-05T12:35:11+00:00[INFO] Final Memory: 14M/190M[INFO] ------------------------------------------------------------------------
```

After the maven build has completed, then in the Visual Studio Code file explorer, open the newly created folder. You should then see the new maven project.

![]({{site.baseurl}}/assets/images/initial-maven-project-1024x573.png)

### **Java Extension Pack features**

The language extension should recognise both maven and gradle projects. I'm not sure what maven version is being picked up by the extension. An embedded version or the one availble from my path.  
Open up the Java and XML files and you can try out the extension features.

#### **Autocomplete**

![]({{site.baseurl}}/assets/images/autocomplete.png)

#### **Code actions**

![]({{site.baseurl}}/assets/images/code-actions.png)

#### **Compilation errors**

![]({{site.baseurl}}/assets/images/compilation-errors.png)

### **Debugger for Java**

Visual Studio Code requires a launch configuration to be created that will support running and debugging a Java application.

Adding configuration can be done through the menu

![]({{site.baseurl}}/assets/images/add-configuration.png)

If you have a maven project without any modules, then the tool appears to be able to find the 'main' method and prepopulate for you.

![]({{site.baseurl}}/assets/images/initial-launch-configuration.png)

To run the application, use **Ctrl + F5**

To use debugging, add breakpoints in your editor and run the application with debugging enabled, using **F5**

![]({{site.baseurl}}/assets/images/debugging-1024x573.png)

## **Unit Testing**

Using the maven project above, if you open a Junit test you are then able to interact with the **Java Test Runner** features.  
Each test and class declaration gets a "run test | debug test" option which will run the test(s) in the background.

There's a test explorer window and a test report tool. Supports junit 4.8 upwards so some classes extending TestCase won't work out the box with the extension, but these can call always be run with maven.

![]({{site.baseurl}}/assets/images/spring-boot-application-launch-1024x720.png)

## **Multi Module support**

Many of the maven projects that I deal with on a daily basis will be [multi-module maven projects](https://maven.apache.org/guides/mini/guide-multiple-modules.html).

I've create a project on github called [teggr/vs-code-multimodule](https://github.com/teggr/vs-code-multimodule/)\] that contains an example multi-module maven project. This projects contains a class in the \`vs-code-core\` module which will be added as a dependency to the \`vs-code-web\` module.

Loading this project is fine, if you change the root pom.xml, you might see a message like below:

![]({{site.baseurl}}/assets/images/multi-module-build-change-1024x720.png)

I'm assuming this is Visual Studio Code making sure that it's m2eclipse or internal build files are keeping in sync with the poms.  
One error I have not been able to solve yet, is managing the modules and the classpath. If you simply open the root project folder then you will be presented with a warning below complaining that the classes in the modules are not on the classpath.

![]({{site.baseurl}}/assets/images/multi-module-classpath-warn-1024x720.png)

Eclipse shows the same type of behaviour if you only import the root project, so I tried add the child modules folders to a workspace. I've not spent much time with workspaces in Visual Studio code.

![]({{site.baseurl}}/assets/images/multi-module-add-folder-1024x721.png)

This seems to resolve the classpath resolution. It does mean that you do need to open the workspace file through explorer (does this need to be checked into github?), not just the folder as you can with a single maven project. Not sure if there's a way to recursively import each module into a workspace, like "Import..." in Eclipse.

![]({{site.baseurl}}/assets/images/multi-module-classpath-resolution-1024x721.png)

Once the classpath resolution was working ok, I tried unsuccessfully to run the application and kept getting a host/port error come. More investigation required here I think.

## **Spring Boot support**

Quite often I like to try out and demo applications using [Spring Boot](https://projects.spring.io/spring-boot/).

I have a simple web application in development for testing Spring Boot support. If I clone [teggr/all-in-java](https://github.com/teggr/all-in-java) and open this folder in Visual Studio code then I should be able to start the web application.

![]({{site.baseurl}}/assets/images/spring-boot-application-1024x720.png)

Visual Studio code does create the launch configuration nicely for you

![]({{site.baseurl}}/assets/images/spring-boot-application-launch-1024x720.png)

The application can then be run and debugged as documented above.

## **Summary**

Having already familiarised myself with Visual Studio Code with JavaScript, the process of installing the extensions was quick and easy, though I would say the documenation could do with a few tweaks. I also had to stumble across the test runner in the tutorial, perhaps more cross referencing of the tools would help.  
For the small projects that I imported, the Java compiler (based on m2eclipse) was quick and accurate so I can't fault it there. Compared to Eclipse there are a number of refactoring code actions missing which I make much use of on a daily basis.

I will need to spend more time researching the multi-module support and how to launch an application from a child module. This is key for Visual Studio Code to be my main development tool.  
As the extensions worked well with Spring Boot, this is good for demos and some of my side projects. At work I still have some projects that require WAR deployments into containers and I'm not sure how to manage this development cycle outside of an Eclipse environment without lots of manual steps.

Given Spring Tool Suite is my main development tool, it will always be difficult to transition to a new tool easily and maintain a productive workflow. I've tried IntelliJ a number of times but always go back to Eclipse. Visual Studio Code already seems to have slipped into my development workflow and will become an even more useful tool once I've got to grips with a few more features.

---
layout: post
title: Pretty command line apps with added JBang.
date: "2025-04-01"
image: /images/pretty-command-line.png
tags:
  - java
  - cli
  - tools
  - jbang
---

Following on from a previous post about Spring CLI and also Scripting I ventured further into the built-in [https://www.jbang.dev/documentation/guide/latest/alias_catalogs.html](https://www.jbang.dev/documentation/guide/latest/alias_catalogs.html) in order to take a look into how others are putting together collections of JBang scripts.

What I discovered were a couple of references to some super helpful console UI libraries that work well with picocli and help you build more interactive and professional command line apps.

- [https://github.com/awegmann/consoleui](https://github.com/awegmann/consoleui) ( #ConsoleUI )
- [https://github.com/vdmeer/asciitable](https://github.com/vdmeer/asciitable) ( #AsciiTable )

## How to find other JBang scripts

The first catalog I looked at was from the JBang Github account - [https://github.com/jbangdev/jbang-catalog](https://github.com/jbangdev/jbang-catalog).

JBang has commands to manage catalogs and to view the aliases that are imported from the named catalogs. In this case we can take a look at the aliases registered by the `jbangdev` catalog.

```bash
> jbang alias list jbangdev

bootstrap@jbangdev
   Bootstrap a jbang script to make it self-contained.
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/bootstrap.java
bouncinglogo@jbangdev
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/bouncinglogo.java
catalog2readme@jbangdev
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/catalog2readme.java
dalle@jbangdev
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/dalle.java
ec@jbangdev
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/ec.jsh
env@jbangdev
   Dump table of Environment Variables
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/env.java
faker@jbangdev
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/faker.jsh
gavsearch@jbangdev
   `gavsearch` lets you use search.maven.org from command line.
Example: `gavsearch hibernate` will search for artifacts with hibernate in its name.
You can use any of the search modifiers search.maven.org supports, i.e.:
`gavsearch c:QuarkusTest` will search for artifacts with class `QuarkusTest`
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/gavsearch.java
getjava@jbangdev
   Experimental utility to download Java distributions using api.foojay.io.
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/getjava.java
git@jbangdev
   Git command line tool implemented with jgit. Lets you do basic git features without installing git!
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/jgit.java
h2@jbangdev
   com.h2database:h2:1.4.200
         Arguments: -webAllowOthers -tcpAllowOthers
hello@jbangdev
   Script that says hello back for each argument
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/hello.java
httpd@jbangdev
   `httpd` runs a webserver serving out the content of a directory.
Example: `jbang httpd@jbangdev -d _site` will serve out the `_site` folder on localhost:8000.
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/httpd.java
jmc@jbangdev
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/jmc.jsh
properties@jbangdev
   Dump table of System properties
   https://github.com/jbangdev/jbang-catalog/blob/HEAD/properties.java
```

Running the remote scripts via their alias can be done with the `jbang` command.

```bash
> jbang hello@jbangdev Robin
[jbang] Building jar for hello.java...
Hello Robin
```

## ConsoleUI

The `gavsearch@jbangdev` script ([https://github.com/jbangdev/jbang-catalog/blob/main/gavsearch.java](https://github.com/jbangdev/jbang-catalog/blob/main/gavsearch.java)) caught my eye as it uses some interaction in it’s prompt. When you type in the search terms, you get a list for selection. As a user you can use the up and down keys to move the cursor and enter to choose.

```bash
> jbang gavsearch@jbangdev hibernate
Searching for `hibernate` on search.maven.org...
[maven, gradle, jbang]
? coordinates
  org.graceframework.plugins:hibernate:2023.2.0
  com.github.houbb:hibernate:0.1.0
❯ io.github.qsy7.java.dependencies:hibernate:0.3.3
  com.ksc.mission.base:hibernate:1.2.6.7
  io.basc.framework:hibernate:1.8.3
  com.github.davidmoten:hibernate:0.6
  io.jstate:hibernate:0.0.2
  org.lucee:hibernate:5.4.29.Final
  com.gitlab.bessemer:hibernate:1.0.5
  com.weicoder:hibernate:3.4.4.1
  
  ...
  
  Searching for `hibernate` on search.maven.org...
[maven, gradle, jbang]
? coordinates hibernate:hibernate:3.1rc2
maven:
<dependency>
  <groupId>hibernate</groupId>
  <artifactId>hibernate</artifactId>
  <version>3.1rc2</version>
</dependency>
gradle:
hibernate:hibernate:3.1rc2
jbang:
//DEPS hibernate:hibernate:3.1rc2
? Action
❯ Copy maven to Clipboard
  Copy gradle to Clipboard
  Copy jbang to Clipboard
  Search older versions
  Quit
```

This feature is pretty similar to the [https://docs.spring.io/spring-shell/reference/components/ui/index.html](https://docs.spring.io/spring-shell/reference/components/ui/index.html) I wrote about in the previous article. Looking at the script, we can see the dependency `de.codeshelf.consoleui:consoleui:0.0.13` which is produced by the [https://github.com/awegmann/consoleui](https://github.com/awegmann/consoleui) project.

What does this look like when implementing?

```bash
> mkdir consoleuidemo && cd consoleuidemo
> jbang init --template=cli consoleuidemo.java
> jbang edit . consoleuidemo.java
```

Now we have a script ready to go, change the file to the following so that we have a script that demonstrates how to use the available prompt UI elements:

```bash
/// usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
//DEPS de.codeshelf.consoleui:consoleui:0.0.13

import de.codeshelf.consoleui.elements.ConfirmChoice;
import de.codeshelf.consoleui.elements.PromptableElementIF;
import de.codeshelf.consoleui.prompt.CheckboxResult;
import de.codeshelf.consoleui.prompt.ConfirmResult;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.ExpandableChoiceResult;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import jline.console.completer.FileNameCompleter;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.fusesource.jansi.AnsiConsole;

@Command(name = "consoleuidemo",
		mixinStandardHelpOptions = true,
		version = "consoleuidemo 0.1",
		description = "consoleuidemo made with jbang")
class consoleuidemo implements Callable<Integer> {

	public static void main( String... args ) {
		int exitCode = new CommandLine( new consoleuidemo() ).execute( args );
		System.exit( exitCode );
	}

	@Override
	public Integer call() throws Exception { // your business logic goes here...

		AnsiConsole.systemInstall();

		ConsolePrompt prompt = new ConsolePrompt();

		final List<PromptableElementIF> promptList = prompt.getPromptBuilder()
				// check box prompt
				.createCheckboxPrompt()
					.name( "checkbox" )
					.message( "Checkbox Prompt - Choose any number" )
						.newItem()
							.text( "Check 1" )
							.name( "check-1-value" )
						.add()
						.newItem()
							.text( "Check 2" )
							.name( "check-2-value" )
							.checked( true )
						.add()
					.addPrompt()
				// choice prompt
				.createChoicePrompt()
					.name( "choice" )
				    .message( "Choice Prompt - Choose only one" )
						.newItem()
							.message( "Choice 1" )
							.name( "choice-1-value" )
							.key( '1' )
							.asDefault()
						.add()
						.newItem()
							.message( "Choice 2" )
							.name( "choice-2-value" )
							.key( '2' )
						.add()
					.addPrompt()
				// confirm prompt
				.createConfirmPromp()
					.name( "confirm" )
					.message( "Please confirm yes or no" )
					.defaultValue( ConfirmChoice.ConfirmationValue.YES )
					.addPrompt()
				// input prompt
				.createInputPrompt()
					.name( "input" )
					.message( "Enter a plain input" )
					.defaultValue( "A default answer" )
					.addPrompt()
				// input prompt
				.createInputPrompt()
					.name( "passwd" )
					.message( "Enter a masked password" )
					.mask( '*' )
					.addPrompt()
				// input prompt
				.createInputPrompt()
					.name( "completed" )
					.message( "Enter a completed value" )
					.defaultValue( "A default answer" )
					.addCompleter( new FileNameCompleter() )
					.addPrompt()
				// input prompt
				.createListPrompt()
					.name( "list" )
					.message( "Choose one from the list" )
					.newItem()
						.text( "List Item 1" )
						.name( "list-1-value" )
						.add()
					.newItem()
						.text( "List Item 2" )
						.name( "list-2-value" )
						.add()
					.addPrompt()
				.build();

		final HashMap<String, ? extends PromtResultItemIF> results = prompt.prompt( promptList );

		final CheckboxResult checkbox = (CheckboxResult)results.get( "checkbox" );
		System.out.printf( "checkbox %s%n", checkbox.getSelectedIds() );

		final ExpandableChoiceResult choice = (ExpandableChoiceResult) results.get( "choice" );
		System.out.printf( "choice %s%n", choice.getSelectedId() );

		final ConfirmResult confirm = (ConfirmResult) results.get( "confirm" );
		System.out.printf( "confirm %s%n", confirm.getConfirmed() );

		final InputResult input = (InputResult) results.get( "input" );
		System.out.printf( "input %s%n", input.getInput() );

		final InputResult passwd = (InputResult) results.get( "passwd" );
		System.out.printf( "passwd %s%n", passwd.getInput() );

		final InputResult completed = (InputResult) results.get( "completed" );
		System.out.printf( "completed %s%n", completed.getInput() );

		final ListResult list = (ListResult) results.get( "list" );
		System.out.printf( "list %s%n", list.getSelectedId() );

		return 0;
	}
}

```

Run the scripts and you be able to see each of the prompts in action.

```bash
> jbang consoleuidemo.java

[jbang] Building jar for consoleuidemo.java...

? Checkbox Prompt - Choose any number [check-2-value]
? Choice Prompt - Choose only one Choice 1
? Please confirm yes or no yes
? Enter a plain input A default answer
? Enter a masked password 
? Enter a completed value A default answer
? Choose one from the list List Item 1

CheckboxResult{selectedIds=[check-2-value]}
ExpandableChoiceResult{selectedId='choice-1-value'}
ConfirmResult{confirmed=YES}
InputResult{input='A default answer'}
InputResult{input='null'}
InputResult{input='A default answer'}
ListResult{selectedId='list-1-value'}

```

And so thanks to this great little library we can add some great interactive prompts to our picocli command line.

## AsciiTable

The `env@jbangdev` script was also interesting as the output from the command is a nicely formatted table.

```bash
> jbang env@jbangdev
┌────────────────────────┬─────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│Key                     │Value                                                                                                        │
├────────────────────────┼─────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│COMMAND_MODE            │unix2003                                                                                                     │
│GRADLE_HOME             │/Users/robintegg/.sdkman/candidates/gradle/current                                                           │
│HOME                    │/Users/robintegg                                                                                             │
│HOMEBREW_CELLAR         │/opt/homebrew/Cellar                                                                                         │
│HOMEBREW_PREFIX         │/opt/homebrew                                                                                                │
│JAVA_HOME               │/Users/robintegg/.sdkman/candidates/java/current                                                             │
│JBANG_HOME              │/Users/robintegg/.sdkman/candidates/jbang/current                                                            │
│LANG                    │en_GB.UTF-8                                                                                                  │
│MAVEN_HOME              │/Users/robintegg/.sdkman/candidates/maven/current                                                            │
│PWD                     │/Users/robintegg/spring-boot-cli                                                                             │
│SHELL                   │/bin/zsh                                                                                                     │
│USER                    │robintegg                                                          │
└────────────────────────┴─────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

Taking a look at the source for this script we see a dependency on `de.vandermeer:asciitable:0.3.2` which is a library from the [https://github.com/vdmeer/asciitable](https://github.com/vdmeer/asciitable) project.

What does this look like when implementing?

```bash
> mkdir asciitabledemo && cd asciitable
> jbang init --template=cli asciitable.java
> jbang edit . asciitable.java
```

Now we have a script ready to go, change the file to the following so that we have a script that demonstrates how to use the available prompt table elements:

```bash
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3
//DEPS de.vandermeer:asciitable:0.3.2

import de.vandermeer.asciitable.AsciiTable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "asciitable", mixinStandardHelpOptions = true, version = "asciitable 0.1",
        description = "asciitable made with jbang")
class asciitable implements Callable<Integer> {

    public static void main(String... args) {
        int exitCode = new CommandLine(new asciitable()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...

        AsciiTable at = new AsciiTable();

        at.addRule();
        at.addRow("row 1 col 1", "row 1 col 2");
        at.addRule();
        at.addRow("row 2 col 1", "row 2 col 2");
        at.addRule();

        String rend = at.render();

        System.out.println(rend);

        return 0;
    }
}

```

Run the scripts and you be able to see each of the table in action.

```bash
> jbang asciitable.java 

[jbang] Building jar for asciitable.java...

┌───────────────────────────────────────┬──────────────────────────────────────┐
│row 1 col 1                            │row 1 col 2                           │
├───────────────────────────────────────┼──────────────────────────────────────┤
│row 2 col 1                            │row 2 col 2                           │
└───────────────────────────────────────┴──────────────────────────────────────┘
```

How sweet is that!
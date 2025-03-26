---
layout: post
title: B(ai)tch from Scratch: Building a Spring AI Project Tutorial
date: "2025-03-26"
image: /images/batch-from-scratch.png
tags:
  - java
  - spring
  - ai
  - tutorial
---
# B(ai)tch from Scratch: Building a Spring AI Project Tutorial

In this tutorial we are development team who are going to be integrating an AI model into a chat bot using the [Spring AI framework](https://docs.spring.io/spring-ai/reference/). The [chat bot code](https://github.com/teggr/baitch-from-scratch) has already been provided to us with a defined state machine, we just need to integrate the features requested by the product team.

The chat bot is for the Channel 4's TV show "[Batch from Scratch](https://www.channel4.com/programmes/batch-from-scratch-cooking-for-less)", where Joe Swash and 'The Batch Lady' Suzanne Mulholland dish out a proper helping of time-saving tips and easy-to-follow recipes, to show how batch cooking can turn mealtime mayhem into a dinnertime dream 🍽️. The Channel 4 product team have some great ideas to help viewers put into practice the great ideas from the show.

![batch-from-scratch.png]({{site.baseurl}}/images/batch-from-scratch.png)

And while I can't promise the same level of entertainment as Joe and Suzanne, I can promise we'll create something useful using the power of AI 🧙.

Using the Spring AI framework, we will explore and build something amazing as we work through these awesome features of the framework: 

- 💬 Chat builders
- 👥 Prompt Roles
- 📝 Prompt Templates
- 🎯 Structured Output
- 🔧 Tool / Function calling
- 📜 Chat history
- 🔄 MCP (Model Context Protocol) integration

Let’s get started. 🚀

## Getting Started with the Project

First, let's clone the project repository from GitHub:

```bash
git clone https://github.com/teggr/baitch-from-scratch
cd baitch-from-scratch
```

The project contains two modules:

- **batch-from-scratch-chat-bot**: The main web application containing a simple chat bot implementation and UI. We will implement the core chat logic as we progress through this tutorial.
- **grocery-order-service**: A very small stubbed service providing a MCP endpoint that supports the chat bot.

The cloned project is complete and you can skip ahead an run it straight away, but let’s hold off for now and roll the project back to point where we can start adding the Spring AI features.

### Project walkthrough

There’s a few call outs to be made on the pre-existing code 📣 . 

- `batch-from-scratch-chat-bot/pom.xml`
    - dependency on `spring-ai-openai-spring-boot-starter`
- `batch-from-scratch-chat-bot/../bfs`
    - `bot` - endpoint for the web chat
    - `chat` - lightweight web chat model classes
    - `model` - supplied domain model classes
    - `profiles` - user profiles
    - `BatchMealPlanning` - the chat bot logic ( 💥 where the magic happens )

### Project Reset

To ensure we're all starting from the same point, we'll apply the reset patch:

```bash
git apply reset_project.patch
```

### Running the project

The AI Model of choice is **Open AI**, so before we get started you’ll need an Open AI API key, which you’ll need apply to the following Spring Boot property:

```bash
SPRING_AI_OPENAI_API_KEY=
```

Run the `BatchMealPlanApplication` in the `batch-from-scratch-chat-bot` module through the IDE or command line tool of your choice.

It will be available on [http://localhost:8080](http://localhost:8080).

![bfs-tutorial-image-1.png]({{site.baseurl}}/images/bfs-tutorial-image-1.png)

You can try to interact with Joe, but he’s not going to give you much right now 😬.

## Chat Builder - Talking to an AI model

Firstly, we are going to introduce the [ChatClient](https://docs.spring.io/spring-ai/reference/api/chatclient.html).

The chat client exposes an API for us to communicate with an AI model.

Given the current bot doesn't seem to know what to do, we’ll use a simple AI [prompt](https://docs.spring.io/spring-ai/reference/api/prompt.html) to generate a friendly message in the starting state.

```java
private final ChatClient chatClient;

public BatchMealPlanning( ChatClient.Builder chatClientBuilder ) {
    chatClient = chatClientBuilder.build();
}
	
/* onMessage() */
	
if ( state == State.starting ) {

    final String welcome = chatClient.prompt( """
            In a couple of sentences, welcome our user to this new Meal Planner.
            Ask the user when they will have some free time to do some batch cooking.
            Add a suggestion how they might respond to the question of free time.
            """ ).call().content();

    List<ChatMessage<?>> chatMessages = List.of(
            text( welcome ) );

    state = State.planning;

    return List.of( ChatStream.of( Profile.joe(), chatMessages ) );

}
```

Brill, we've got our first AI model integration. Not much code needed for that! Well done Spring team.

Now, given that we can ask chat mode's about anything and there's no specific way for the chat model to respond, let's work on adding some context to the chat to get a more "Joe Swash"-like response.

## Prompt Roles - Influencing the model’s responses

[Prompt roles](https://docs.spring.io/spring-ai/reference/api/prompt.html#_roles) provide us a way to categorise the different types of messages that we send to the AI model in order to change the output.

Two common roles are the user (default above) and system roles.

Lets update the chat client to have a common system message that will be applied to all chat interactions.

```java
chatClient = chatClientBuilder
    .defaultSystem( """
            You are an AI assistant that knows all about batch cooking.
            Your tone embodies the fun side traits of Joe Swash and 'The Batch Lady' Suzanne Mulholland from the popular channel 4 program, ‘Batch from Scratch’.
            You will help with time-saving tips and easy-to-follow recipes, to show how batch cooking can turn mealtime mayhem into a dinnertime dream.
            """ )
    .build();
```

That's more like it, a nice welcoming message for our user. However, at this point, the AI model does not know who is asking, so let's add some more personlised information into the context. 

## Prompt Templates - Adding context

The Spring Boot AI prompts provide some [templating functionality](https://docs.spring.io/spring-ai/reference/api/prompt.html#_prompttemplate) out of the box so that you can add variables to the messages being sent to the AI model.

Let’s use this feature to add some personalization to the welcome message.

```java
final String welcome = chatClient.prompt()
    .user( m -> m.text( """
        In a couple of sentences, welcome our user called {name} to this new Meal Planner.
        Ask the user when they will have some free time to do some batch cooking.
        Add a suggestion how they might respond to the question of free time.
        """).param( "name", user.getName() ) )
    .call()
    .content();
```

Now we're talking, the AI model is now using our enhanced context to change it's output so that it can reference our user.

![bfs-tutorial-image-2.png]({{site.baseurl}}/images/bfs-tutorial-image-2.png)

## Chat Logging - What’s happening behind the scenes

This is all great, but how do we know that this code is even talking to a model as there doesn't seem to be much code yet?. We can do this by adding an Advisor to the application that will output the contents of the client request/responses.

```java
chatClient = chatClientBuilder
    .defaultAdvisors( new SimpleLoggerAdvisor() )
    .defaultSystem( """
            You are an AI assistant that knows all about batch cooking.
            Your tone embodies the fun side traits of Joe Swash and 'The Batch Lady' Suzanne Mulholland from the popular channel 4 program, ‘Batch from Scratch’.
            You will help with time-saving tips and easy-to-follow recipes, to show how batch cooking can turn mealtime mayhem into a dinnertime dream.
            """ )
    .build();
```

Now when we restart the application, we will see some extra logging for the client.

```bash
[Batch from Scratch Chat Bot] : request: AdvisedRequest[chatModel=OpenAiChatModel [defaultOptions=OpenAiChatOptions: {"streamUsage":false,"model":"gpt-4o-mini","temperature":0.7}], userText=In a couple of sentences, welcome our user called {name} to this new Meal Planner.
Ask the user when they will have some free time to do some batch cooking.
Add a suggestion how they might respond to the question of free time.
, systemText=You are an AI assistant that knows all about batch cooking.
Your tone embodies the fun side traits of Joe Swash and 'The Batch Lady' Suzanne Mulholland from the popular channel 4 program, ‘Batch from Scratch’.
You will help with time-saving tips and easy-to-follow recipes, to show how batch cooking can turn mealtime mayhem into a dinnertime dream.
, chatOptions=OpenAiChatOptions: {"streamUsage":false,"model":"gpt-4o-mini","temperature":0.7}, media=[], functionNames=[], functionCallbacks=[], messages=[], userParams={name=Laura}, systemParams={}, advisors=[org.springframework.ai.chat.client.DefaultChatClient$DefaultChatClientRequestSpec$1@ccb6eab, org.springframework.ai.chat.client.DefaultChatClient$DefaultChatClientRequestSpec$2@6d736572, SimpleLoggerAdvisor, org.springframework.ai.chat.client.DefaultChatClient$DefaultChatClientRequestSpec$1@669073ce, org.springframework.ai.chat.client.DefaultChatClient$DefaultChatClientRequestSpec$2@2f94313a], advisorParams={}, adviseContext={}, toolContext={}]

[Batch from Scratch Chat Bot] : response: {
  "result" : {
    "output" : {
      "messageType" : "ASSISTANT",
      "metadata" : {
        "finishReason" : "STOP",
```

You can see the messages, their classifications and the response with key fields like token usage. [Token](https://docs.spring.io/spring-ai/reference/api/prompt.html#_tokens) usage is important as generally that's what's used for billing by the AI model providers.

Further more the output will be useful once we start looking at structured output, so let’s move on with the chat.

## Structured Output - Mapping responses

Now that we've welcomed our user and prompted them to tell us when they have availability, we can pull all this together and ask the AI model to generate us a batch cooking plan 🧑🏻‍🍳.

Here we are adding a [number of constraints to guide the AI model](https://docs.spring.io/spring-ai/reference/api/prompt.html#_creating_effective_prompts) to generate the content we are interested in.

```java
/* onMessage() */

} else if ( state == State.planning ) {

    final String content = chatClient.prompt()
            .user( u -> u.text( """
                    Given the time constraints provided by the user.
                    Provide a list of recipes that the user can cook within their suggested time window.
                    You should suggest as many different types of meal as possible to cook with their suggested time window.
                    Include in the response all the ingredients required, the number of portions that the recipe will make and freezing instructions.
                    Plan out the cooking session for the specified date using the user's current date and time.
                    Include a friendly summary message that the user will read after browsing the recipes.
                    ###
                    {userMessage}
                    """ ).param( "userMessage", message ) )
            .call()
            .content();

    List<ChatMessage<?>> chatMessages = List.of(
            text( content ) );

    state = State.shopping;

    return List.of( ChatStream.of( Profile.joe(), chatMessages ) );

}
```

This blurb of text is right but poorly formatted for our chat bot. Ideally we don't want to have to write a parser for this free form text, so we can use a popular trick of requesting that the AI model return it's response in JSON format.

```bash
The response should be in json format.
```

This is great, but we don’t want to output just json markdown, that’s not useful for the user, so wouldn’t it be nice if Spring would convert that json content into an object for us. Looks like with the `entity()` call they've [already thought of it](https://docs.spring.io/spring-ai/reference/api/chatclient.html#_call_return_values).

```java
final BatchMealPlan content = chatClient.prompt()
    .user( u -> u.text( """
            Given the time constraints provided by the user.
            Provide a list of recipes that the user can cook within their suggested time window.
            You should suggest as many different types of meal as possible to cook with their suggested time window.
            Include in the response all the ingredients required, the number of portions that the recipe will make and freezing instructions.
            Plan out the cooking session for the specified date using the user's current date and time.
            Include a friendly summary message that the user will read after browsing the recipes.
            The response should be in json format.
            ###
            {userMessage}
            """ ).param( "userMessage", message ) )
    .call()
    .entity(BatchMealPlan.class);

List<ChatMessage<?>> chatMessages = Utils.messagesForBatchMealPlan( content );
```

Now we’ve got the AI model returning structured output, which Spring neatly converts into Java objects for us.

![bfs-tutorial-image-3.png]({{site.baseurl}}/images/bfs-tutorial-image-3.png)

## Tools - Advanced interactions

### Local tools for local people

Let’s add a new feature now. It would be great if we could help the user set a reminder for the batch meal cooking.

```java

/* onMessage() - state = planning */

chatMessages.add( reminder( content.plannedDate() ) );
```

_29th October 2023?_ That doesn’t look right!

You are correct, this is not the right date, this is likely the day that the model is trained to, it doesn’t have the context for what the local date is for the user to work that out.

Let’s use [tools](https://docs.spring.io/spring-ai/reference/api/tools.html) to inject an ability into the chat client to resolve that on behalf of the AI model.

```java
chatClient = chatClientBuilder
    .defaultAdvisors( new SimpleLoggerAdvisor() )
    .defaultTools( new LocalTools() )
    .defaultSystem( """
            You are an AI assistant that knows all about batch cooking.
            Your tone embodies the fun side traits of Joe Swash and 'The Batch Lady' Suzanne Mulholland from the popular channel 4 program, ‘Batch from Scratch’.
            You will help with time-saving tips and easy-to-follow recipes, to show how batch cooking can turn mealtime mayhem into a dinnertime dream.
            """ )
    .build();

static class LocalTools {

    @Tool(description = "Get the current date and time in the user's timezone")
    String getCurrentDateTime() {
        System.out.println( "getCurrentDateTime" );
        return LocalDateTime.now()
                .atZone( LocaleContextHolder.getTimeZone().toZoneId() )
                .toString();
    }

}
```

You should now see in the log output an entry of `getCurrentDateTime` which shows that the chat client is getting information on behalf of the AI model. You will also see the tool context being sent in the logs.

### Fetching data from a service

Hot off the press, other team from Channel 4 have added a new food preferences feature for users and the Product manager is keen for us to integrate those preferences into the batch meal planning. No one wants a freezer full of food they don’t want to eat.

So let’s make us of the tools feature again to stuff to provide more context to the AI model around what the user would like to eat.

```java
private final FoodPreferences foodPreferences;

public BatchMealPlanning( ChatClient.Builder chatClientBuilder, FoodPreferences foodPreferences ) {
    chatClient = chatClientBuilder
        .defaultAdvisors( new SimpleLoggerAdvisor() )
        .defaultTools( new LocalTools() )
        .defaultSystem( """
                You are an AI assistant that knows all about batch cooking.
                Your tone embodies the fun side traits of Joe Swash and 'The Batch Lady' Suzanne Mulholland from the popular channel 4 program, ‘Batch from Scratch’.
                You will help with time-saving tips and easy-to-follow recipes, to show how batch cooking can turn mealtime mayhem into a dinnertime dream.
                """ )
        .build();
    this.foodPreferences = foodPreferences;
}

/* onMessage() - state planning */

final BatchMealPlan content = chatClient.prompt()
    .user( u -> u.text( """
            Given the time constraints provided by the user.
            Provide a list of recipes that the user can cook within their suggested time window.
            You should suggest as many different types of meal as possible to cook with their suggested time window.
            Include in the response all the ingredients required, the number of portions that the recipe will make and freezing instructions.
            Plan out the cooking session for the specified date using the user's current date and time.
            Include a friendly summary message that the user will read after browsing the recipes.
            Avoid meal suggestions that include food that the user doesn't like. Check all the ingredients in the recipes.
            ###
            {userMessage}
            """ ).param( "userMessage", message ) )
    .tools( foodPreferences )
    .call()
    .entity(BatchMealPlan.class);
```

So we’ve seen here the ability for the AI model to ask the chat client for more information using the tools. This can also be extended to perform actions.

## Chat history - Where were we again?

Now that we’ve got a our planning session, the Product team are at it again with their great ideas and now want us to provide a shopping list for the user containing all the ingredients in the plan.

Now we could change the code to take the output from the previous response into a new message to send to the AI model, but spring is here to help us again here with a [Chat Memory](https://docs.spring.io/spring-ai/reference/api/chatclient.html#_chat_memory) feature which does that for us. It supports several backend implementations and use cases, but for now we will use the simple `InMemoryChatMemory`. 

Let's start with prompting the user to create the shopping list.

```java

/* onMessage() - state planning */

List<ChatMessage<?>> chatMessages = Utils.messagesForBatchMealPlan( content );

chatMessages.add( text( "What would you like to do with your plan?" ) );
chatMessages.add( text( "We could create a shopping list for you?" ) );
chatMessages.add( reminder( content.plannedDate() ) );

chatMessages.add(
        suggestion( "Try entering something like please create me a shopping list." ) );

```

And then, by adding the chat memory we can reference the previous plan when asking the AI model to create a simple shopping list of all the items in the plan.

```java
chatClient = chatClientBuilder
    .defaultAdvisors( new SimpleLoggerAdvisor(),  new MessageChatMemoryAdvisor( new InMemoryChatMemory() )  )
    .defaultTools( new LocalTools() )
    .defaultSystem( """
            You are an AI assistant that knows all about batch cooking.
            Your tone embodies the fun side traits of Joe Swash and 'The Batch Lady' Suzanne Mulholland from the popular channel 4 program, ‘Batch from Scratch’.
            You will help with time-saving tips and easy-to-follow recipes, to show how batch cooking can turn mealtime mayhem into a dinnertime dream.
            """ )
    .build();

/* onMessage() - state shopping */
				
final List<Ingredient> shoppingList = chatClient.prompt()
    .user( u -> u.text( """
        Given the recipes that we have already put together in the cooking session. Think about this step by step.
        Get the list of all the ingredients in all the cooking session recipes.
        The list should include total quantities of each ingredient by name so there are no duplicates.
        Each entry in the list should contain a name and quantity.
        Any further instructions should be incorporated.
        ###
        {userMessage}
        """ ).param( "userMessage", message ) )
    .call()
    .entity( new ParameterizedTypeReference<List<Ingredient>>() {} );

List<ChatMessage<?>> chatMessages = new ArrayList<>();

chatMessages.add( list( "shoppingList", shoppingList ) );

return List.of( ChatStream.of( Profile.suzanne(), chatMessages ) );
```

![bfs-tutorial-image-4.png]({{site.baseurl}}/images/bfs-tutorial-image-4.png)

The product team are all go, go, go and have spoken to the commercial team and arranged a partnership with a leading grocery store to allow the user to order their shopping directly from the chat bot. So how are we going to integrate?

## MCP - A common API for models

The [Model Context Protocol](https://modelcontextprotocol.org/docs/concepts/architecture) (MCP) is a standardized protocol that enables AI models to interact with external tools and resources in a structured way.

We are going use the [Spring AI MCP support](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html) to enable a new shopping cart feature into the bot. We are going to use a third party grocery service that supports MCP clients.

Let's take a quick peek at the MCP enabled server, which exposes an endpoint for the AI model to use complete the task of placing a order for a shopping delivery.

This [server implementation](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html) is made available via HTTP.

```java
/* GroceryStoreApplication */

@Bean
ToolCallbackProvider toolCallbackProvider( GroceryOrders tools ) {
    return MethodToolCallbackProvider.builder().toolObjects( tools ).build();
}

@Component
class GroceryOrders {

	@Tool(description = "Place an order for a list of ingredients. The will be before a certain date. The ingredient list contains names and quantities. Will confirm order, price and delivery date.")
	public OrderConfirmation placeOrder(
			@ToolParam(description = "The delivery date for the shopping.") LocalDate deliveryDate,
			@ToolParam(description = "The ingredients list that needs to be ordered and delivered. Contains names and quantities") List<Ingredient> ingredients ) {

		System.out.println( "order for " + deliveryDate + " of " + ingredients );

		return new OrderConfirmation( true, deliveryDate.minusDays( 1 ), BigDecimal.TEN );

	}

	record OrderConfirmation(boolean confirmed, LocalDate deliveryDate, BigDecimal price) {}

	// Ingredient record to hold ingredient details
	public record Ingredient(String name, String quantity) {}

}
```

If we start running this application, it will be available on [http://localhost:8081](http://localhost:8081).

Next we need to add support into the chat bot to integrate with the service. Again Spring makes this real easy. Firstly, we'll add the feature prompt into the bot.

```java

/* onMessage() - state shopping */

chatMessages.add( list( "shoppingList", shoppingList ) );

chatMessages.add( text( "What would you like to do with your shopping list?" ) );
chatMessages.add( text( "Shall we get the shopping list ordered for you?" ) );

chatMessages.add( suggestion(
        "Try entering something like please place my order for delivery." ) );
			
```

And finally wire in the '[Standard MCP Client](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html#_standard_mcp_client)' into our chat bot which will give our AI model order delivery super powers 🚚.

```java
private final McpSyncClient mcpSyncClient;

public BatchMealPlanning( ChatClient.Builder chatClientBuilder, FoodPreferences foodPreferences, McpSyncClient mcpSyncClient ) {
    chatClient = chatClientBuilder
        .defaultAdvisors( new SimpleLoggerAdvisor() )
        .defaultTools( new LocalTools() )
        .defaultSystem( """
                You are an AI assistant that knows all about batch cooking.
                Your tone embodies the fun side traits of Joe Swash and 'The Batch Lady' Suzanne Mulholland from the popular channel 4 program, ‘Batch from Scratch’.
                You will help with time-saving tips and easy-to-follow recipes, to show how batch cooking can turn mealtime mayhem into a dinnertime dream.
                """ )
        .build();
    this.foodPreferences = foodPreferences;
    this.mcpSyncClient = mcpSyncClient;
}

/* onMessage() - state ordering */

final OrderConfirmation orderConfirmation = chatClient.prompt()
        .user( u -> u.text( """
            Get the ingredient list that we already created for the cooking session.
            Place an order for the shopping.
            The order must be delivered before the planned cooking session.
            Confirm the order can be delivered and what the delivery details are.
            Take into account any further instructions from the following user message.
            Add a jovial confirmation message to the response.
            ###
            {userMessage}
            """ ).param( "userMessage", message ) )
        .tools( new SyncMcpToolCallbackProvider( mcpSyncClient ) )
        .call()
        .entity( OrderConfirmation.class );

List<ChatMessage<?>> chatMessages = new ArrayList<>();

chatMessages.add( object( "orderConfirmation", orderConfirmation ) );

chatMessages.add( text( "I think we're done with this demo now!" ) );
chatMessages.add( text( "Time for some questions..." ) );

return List.of( ChatStream.of( Profile.joe(), chatMessages ) );

```

![bfs-tutorial-image-5.png]({{site.baseurl}}/images/bfs-tutorial-image-5.png)

## That's a (batch cooked) wrap 🌯

If you got this far, thanks for following along 🎆.

I hope that this tutorial has been a good introduction to the [Spring AI framework](https://docs.spring.io/spring-ai/reference/).

* [Spring AI Docs](https://docs.spring.io/spring-ai/reference/index.html)
* [Baitch From Scratch Github Project](https://github.com/teggr/baitch-from-scratch)

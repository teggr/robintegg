---
layout: post
title: "Self-Contained Systems for AI Coding Agents"
date: "2026-01-07"
image: /images/scs-ai-agents.jpg
tags:
  - architecture
  - ai
  - scs
  - microservices
---

Martin Simonellis recently shared an interesting observation on Bluesky: there's anecdotal evidence that AI coding agents seem to perform better when working with repositories that contain both UI and domain logic together. This observation sparked my thinking about why this might be the case and how it relates to broader architectural patterns.

The answer lies in the Self-Contained Systems (SCS) architecture pattern. AI coding agents—LLMs that generate or modify code—work best when they can reason about a complete, self-contained slice of functionality. SCS aligns naturally with how AI agents understand and modify code, making it an ideal architectural approach for codebases that will be maintained or extended by AI assistants.

## Why SCS matters for AI agents

AI coding agents perform optimally when several conditions are met:

1. **Context is localized** — they can see relevant code, UI, and data together in one place
2. **Cause and effect are visible** — changes have clear, testable outcomes within the system
3. **Dependency boundaries are explicit** — agents don't need to reason across opaque layers
4. **Contracts are clear** — URLs, APIs, events, and data schemas are well-defined

The SCS architecture pattern satisfies all of these conditions. Each system owns a full vertical slice—encompassing UI, domain logic, and database—and can be reasoned about in isolation. Systems communicate with each other only through explicit contracts or events.

## Key patterns that help agents

### UI and domain logic together

When UI components and backend logic live in the same codebase, AI agents can see the complete picture. For example, an Orders SCS containing `OrderController`, HTML templates, and database access in one repository allows an agent to:

- Understand how user actions map to backend operations
- Generate UI changes that correctly call backend endpoints
- Validate that data flows correctly from UI to persistence

This co-location reduces hallucinations and improves the correctness of AI-generated code.

```java
@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/{orderId}")
    public String viewOrder(@PathVariable Long orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "order-detail";
    }
    
    @PostMapping
    public String createOrder(@RequestParam Long customerId, RedirectAttributes attrs) {
        Order order = orderService.createOrder(customerId);
        attrs.addFlashAttribute("message", "Order created successfully");
        return "redirect:/orders/" + order.getId();
    }
}
```

An agent working with this code can see the complete flow from HTTP request to template rendering, making it easier to add new endpoints or modify existing ones correctly.

### Explicit contracts

AI agents benefit from explicit contracts between systems. These contracts can take several forms:

**REST APIs** with clear request/response schemas:

```java
@RestController
@RequestMapping("/api/customers")
public class CustomerApi {
    
    @GetMapping("/{customerId}")
    public CustomerDto getCustomer(@PathVariable Long customerId) {
        return customerService.findById(customerId);
    }
}
```

**Event schemas** for asynchronous communication:

```java
@Data
public class OrderCreatedEvent {
    private Long orderId;
    private Long customerId;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
}
```

**URL contracts** for navigation:

```java
// Orders system links to Customers system
String customerUrl = "/customers/" + order.getCustomerId();
```

These explicit contracts allow agents to safely generate code that calls other systems without needing to understand their internal implementation.

### Server-side rendering and HTML fragments

Server-side rendering (SSR) patterns are particularly agent-friendly. When one SCS needs to embed content from another, it can request HTML fragments:

```java
@GetMapping("/customers/{customerId}/orders-summary")
public String getOrdersSummary(@PathVariable Long customerId) {
    List<Order> orders = orderService.findByCustomerId(customerId);
    return renderOrdersSummaryFragment(orders);
}
```

The consuming system can embed this fragment without understanding the Orders system's internal logic:

```html
<div id="customer-orders">
    <!-- Fragment from Orders SCS -->
    <div th:replace="~{ordersFragment}"></div>
</div>
```

AI agents can reason about these fragment contracts without needing to understand the full implementation details of each system.

### Navigation patterns

Navigation between systems can be handled through simple URL patterns, redirects, and HTTP headers. This maintains autonomy while preserving user experience:

```java
@PostMapping("/customers/{customerId}/orders/new")
public String createOrderForCustomer(
        @PathVariable Long customerId,
        @RequestParam(required = false) String returnTo,
        HttpServletRequest request) {
    
    Order order = orderService.createOrder(customerId);
    
    // Use explicit returnTo or fall back to Referer
    String redirectUrl = returnTo != null 
        ? returnTo 
        : request.getHeader("Referer");
    
    return "redirect:" + redirectUrl;
}
```

This pattern allows AI agents to maintain proper navigation flows without complex cross-system coordination.

## Advantages for AI-assisted development

When working with SCS architecture, AI agents gain several advantages:

1. **Localized reasoning** — agents read, update, and test one SCS without worrying about others
2. **Autonomous testing** — unit and integration tests live entirely within the SCS
3. **Predictable integration** — events, APIs, and URL contracts reduce guesswork
4. **Safe deployment** — agents can generate changes in one SCS independently
5. **Reduced coupling** — agents don't need to understand global state or shared databases

Consider how an agent might add a new feature to display customer order history:

```java
// Agent can work entirely within the Orders SCS
@GetMapping("/orders/customer/{customerId}")
public String customerOrderHistory(@PathVariable Long customerId, Model model) {
    List<Order> orders = orderService.findByCustomerId(customerId);
    model.addAttribute("orders", orders);
    model.addAttribute("customerId", customerId);
    return "customer-orders";
}
```

The agent doesn't need to coordinate with the Customers SCS or understand its internal structure. It simply uses the customer ID as a contract.

## Challenges to consider

While SCS architecture works well with AI agents, there are some challenges:

**Cross-system workflows** require careful design. When a business process spans multiple systems, agents need clear guidance about event ordering and eventual consistency:

```java
// Publishing an event for other systems
@Transactional
public Order createOrder(Long customerId, List<OrderItem> items) {
    Order order = orderRepository.save(new Order(customerId, items));
    eventPublisher.publish(new OrderCreatedEvent(order));
    return order;
}
```

**Data duplication** means agents must understand when data is cached versus authoritative:

```java
// Customer name cached in Order for display
@Entity
public class Order {
    private Long customerId;
    private String customerName; // cached from Customer system
    // ...
}
```

**Contract evolution** requires versioning strategies that agents can follow:

```java
@GetMapping("/api/v2/orders/{orderId}")
public OrderDtoV2 getOrderV2(@PathVariable Long orderId) {
    // New version with additional fields
}
```

## Mental model for AI-assisted SCS development

A useful mental model is to think of each SCS as a mini-product or website. AI agents are like fast junior developers working inside it. Contracts and fragments are the only bridges to other systems.

This model explains why agents perform better with SCS than with heavily layered microservices or monoliths:

- Everything needed for reasoning is co-located
- Hidden dependencies are minimized
- Navigation and UI integration are explicit
- Testing can be done in isolation

## Recommended patterns

| Pattern | Why it helps AI agents |
|---------|----------------------|
| Vertical slices with UI + domain | Agents see full behavior in one codebase |
| Contracts for URLs / APIs / events | Agents can safely generate calls or links |
| SSR / HTML fragments for cross-SCS UI | Agents reason about embedding without internal logic |
| Redirection with Referer / returnTo | Agents maintain navigation autonomously |
| Event-driven integration | Agents don't need consumer details, only event structure |

## Practical example

Here's how an agent might add a "recent orders" widget to the customer page:

**In the Orders SCS:**

```java
@RestController
@RequestMapping("/api/fragments")
public class OrderFragmentController {
    
    @GetMapping("/recent-orders")
    public String recentOrders(@RequestParam Long customerId) {
        List<Order> orders = orderService
            .findByCustomerId(customerId)
            .stream()
            .limit(5)
            .collect(Collectors.toList());
        return renderRecentOrdersFragment(orders);
    }
}
```

**In the Customers SCS:**

```java
@GetMapping("/customers/{customerId}")
public String viewCustomer(@PathVariable Long customerId, Model model) {
    Customer customer = customerService.findById(customerId);
    model.addAttribute("customer", customer);
    
    // Fetch orders fragment
    String ordersHtml = restTemplate.getForObject(
        "http://orders-service/api/fragments/recent-orders?customerId=" + customerId,
        String.class
    );
    model.addAttribute("ordersFragment", ordersHtml);
    
    return "customer-detail";
}
```

The agent working in either system has a clear understanding of the contract and can make changes safely.

## Takeaways

Self-Contained Systems architecture improves AI coding agent productivity by co-locating UI, domain logic, and tests. The key success factors are:

1. **Contracts** (URLs, APIs, events, HTML fragments) serve as safe boundaries
2. **Navigation via redirects** and Referer/back patterns preserves UX without breaking autonomy
3. **Cross-SCS workflows** remain the main challenge but can be handled with events or sagas
4. **SSR patterns** and server-side composition are particularly agent-friendly

When building systems that will be maintained or extended by AI coding agents, the SCS architecture pattern provides a natural fit. It creates clear boundaries, explicit contracts, and localized reasoning—exactly what AI agents need to generate correct, maintainable code.

## References

- [Martin Simonellis' Bluesky post on AI agents and repository structure](https://bsky.app/profile/martinsimonellis.bsky.social)
- [Self-Contained Systems](https://scs-architecture.org/)
- [Microservices patterns](https://microservices.io/patterns/index.html)
- [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)

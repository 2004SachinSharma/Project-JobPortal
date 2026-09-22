# Project-JobPortal

# CORS (Cross-Origin Resource Sharing)

## Definition

CORS is a browser security mechanism that controls how resources are shared between different origins.

An Origin consists of:
- Protocol
- Domain
- Port

If any one of these changes, the request becomes cross-origin.

---

# Example of Cross-Origin

```txt
Frontend -> http://localhost:3000
Backend  -> http://localhost:8080
```

Here:
- Protocol same -> `http`
- Domain same -> `localhost`
- Port different

Therefore:
# Cross-Origin Request

---

# Why CORS Exists

Without CORS, any malicious website could access another website’s data using the user's browser.

Example:
- User is logged into `bank.com`
- Browser already contains session cookies
- User opens `evil.com`

`evil.com` can run:

```js
fetch("https://bank.com/account")
```

Browser may automatically attach:
- Cookies
- Session Information

Backend may think:

```txt
Valid logged-in user
```

and may return sensitive data.

To prevent this type of attack, browsers use CORS.

---

# Main Purpose of CORS

CORS allows the server to specify:
- Which origins are allowed
- Which HTTP methods are allowed
- Which headers are allowed

Browser then enforces these rules.

---

# Important Concept

CORS is:
# Browser Security

CORS is NOT:
- Authentication
- Authorization
- Complete Backend Security

The browser enforces CORS rules using response headers sent by the server.

---

# Working of CORS

## Step 1: Frontend Sends Request

Example:

```js
fetch("http://localhost:8080/users")
```

---

## Step 2: Browser Detects Cross-Origin

Browser checks:
- Protocol
- Domain
- Port

If different:

```txt
Cross-Origin Request Detected
```

---

## Step 3: Request Goes to Backend

Browser usually allows the request to go to backend.

---

## Step 4: Backend Sends Response with CORS Headers

Example:

```http
Access-Control-Allow-Origin: http://localhost:3000
```

Meaning:

```txt
"This frontend is allowed to access my response."
```

---

## Step 5: Browser Checks Headers

Browser verifies:
- Is this origin allowed?
- Are methods allowed?
- Are headers allowed?

If allowed:

```txt
Browser gives response to frontend JavaScript
```

If not allowed:

```txt
Browser blocks the response
```

---

# Actual Meaning of CORS

Even if backend successfully sends response, browser can still block frontend JavaScript from reading it.

In simple words:

```txt
“Bhai response aa toh gaya hai,
but server ne tujhe allow nahi kiya dekhne ke liye,
toh main tujhe nahi dikhaunga.”
```

Important:
- Decision taken by Server
- Enforcement done by Browser

---

# Simple GET Request Flow

Example:

```js
fetch("http://localhost:8080/users")
```

Browser generally sends request directly.

Backend responds.

Browser checks:

```http
Access-Control-Allow-Origin
```

If allowed:

```txt
Response accessible
```

Otherwise:

```txt
CORS Error
```

---

# Dangerous Requests

These requests are considered sensitive:
- POST
- PUT
- DELETE
- PATCH

because they can:
- Modify database
- Delete resources
- Update accounts
- Perform payments

Therefore browser performs additional checking.

---

# Preflight Request

Before sending certain dangerous cross-origin requests, browser sends a:

# Preflight Request

Purpose:

```txt
Permission check before actual request
```

---

# OPTIONS Method

Preflight uses:

# OPTIONS HTTP Method

Common HTTP Methods:

| Method | Purpose |
|---|---|
| GET | Read Data |
| POST | Create/Send Data |
| PUT | Update Data |
| DELETE | Delete Data |
| OPTIONS | Ask server what is allowed |

---

# Full Preflight Flow

## Actual Frontend Request

```js
fetch("http://localhost:8080/user", {
    method: "PUT",
    headers: {
        "Authorization": "token",
        "Content-Type": "application/json"
    }
})
```

---

## Step 1: Browser Sends OPTIONS Request First

```http
OPTIONS /user
Origin: http://localhost:3000
Access-Control-Request-Method: PUT
Access-Control-Request-Headers: Authorization, Content-Type
```

Meaning:

```txt
"Hello Server,
frontend wants to send PUT request with these headers.
Is it allowed?"
```

---

## Step 2: Server Responds with Permissions

```http
Access-Control-Allow-Origin: http://localhost:3000

Access-Control-Allow-Methods: PUT

Access-Control-Allow-Headers: Authorization, Content-Type
```

Meaning:

```txt
"Yes, this frontend is allowed."
```

---

## Step 3: Browser Decision

If allowed:

```txt
Browser sends actual PUT request
```

If not allowed:

```txt
Browser blocks request before actual PUT
```

---

# Important Interview Point

CORS does not always stop request from reaching server.

Mostly:
- Request may reach server
- Browser blocks response access

Therefore backend security is still necessary.

---

# Backend Security Still Required

CORS alone is NOT enough.

Backend should still implement:
- Authentication
- Authorization
- CSRF Protection
- Validation
- Token Security

---

# Common CORS Headers

## 1. Allow Origin

```http
Access-Control-Allow-Origin: http://localhost:3000
```

Allows specific frontend.

---

## 2. Allow Methods

```http
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
```

Allowed HTTP methods.

---

## 3. Allow Headers

```http
Access-Control-Allow-Headers: Content-Type, Authorization
```

Allowed custom headers.

---

## 4. Allow Credentials

```http
Access-Control-Allow-Credentials: true
```

Allows cookies/session credentials.

---

# Final Definition

```txt
CORS is a browser security mechanism in which the server specifies which cross-origin requests are allowed, and the browser enforces those rules using CORS headers.
```

Here is a complete, polished `README.md` containing clear explanations of **Cross-Cutting Concerns** and all **Core AOP Jargon**, enriched with practical code examples. You can copy and paste the markdown block directly into your repository.

```markdown
# Spring AOP: Logging & Performance Interceptor

This module implements a centralized Aspect-Oriented Programming (AOP) interceptor designed to track execution performance and log application traffic across business layers without polluting core domain logic.

---

## 🔍 Understanding Cross-Cutting Concerns

In any enterprise application, your code naturally falls into two distinct categories:

1. **Core Concerns (Primary Business Logic):** The primary responsibility of a module or class.
   * *Examples:* Processing an order, validating job applications, calculating tax.
2. **Cross-Cutting Concerns (System-Wide Behaviors):** Features required across multiple layers (Controller, Service, Repository) that are not part of the core business logic.
   * *Examples:* Logging, security checks, transaction management, performance auditing.

### Without AOP vs. With AOP

Without AOP, cross-cutting concerns repeat across every single service method, causing **code duplication** and **tight coupling**:

```java
// WITHOUT AOP: Business logic is buried under repetitive boilerplate
@Service
public class JobService {

    public Job getJobTitle(Long id) {
        long start = System.currentTimeMillis(); // ❌ Logging/Performance Concern
        log.info("Entering getJobTitle with ID: {}", id); // ❌ Logging Concern

        Job job = jobRepository.findById(id); // ✅ Core Business Logic

        long executionTime = System.currentTimeMillis() - start; // ❌ Logging/Performance Concern
        log.info("Exiting getJobTitle. Execution time: {} ms", executionTime); // ❌ Logging Concern

        return job;
    }
}

```

With AOP, cross-cutting concerns are extracted into an isolated class (an **Aspect**). The core business logic remains clean, readable, and maintainable:

```java
// WITH AOP: Pure, unpolluted business logic
@Service
public class JobService {

    public Job getJobTitle(Long id) {
        return jobRepository.findById(id); // ✅ Only Core Business Logic
    }
}

```

---

## 📚 Core AOP Terminology (The Jargon Explained)

### 1. Aspect

* **Definition:** A class that modularizes a cross-cutting concern spanning multiple classes.
* **Analogy:** A security guard at a building entrance monitoring everyone who enters.
* **Example:**

```java
@Aspect
@Component
public class LoggingAndPerformanceAspect {
    // Contains all logging and performance timing logic
}

```

---

### 2. Join Point

* **Definition:** A specific candidate point in the execution of a program where an aspect can plug in. In Spring AOP, a Join Point **always represents a method execution**.
* **Analogy:** Any door in a building where a security guard *could* stand.
* **Example:**

```java
// Every public method in your application is a potential JoinPoint
public Job getJobTitle(Long id) { ... }
public void createJob(JobDto dto) { ... }

```

Inside your aspect code, Spring provides a `ProceedingJoinPoint` object to give you access to metadata about the intercepted method (e.g., method name, parameters):

```java
String methodName = joinPoint.getSignature().getName(); // "getJobTitle"
Object[] args = joinPoint.getArgs();                   // [101L]

```

---

### 3. Pointcut

* **Definition:** A predicate or expression that matches specific Join Points. It tells Spring **where** to apply the Advice.
* **Analogy:** An instruction sheet telling the security guard: *"Only stand at the main entrance on the 3rd floor."*
* **Example:**

```java
// Matches ALL methods inside 'com.myproject.jobportal' and any of its sub-packages
@Pointcut("execution(* com.myproject.jobportal..*.*(..))")
public void applicationPackagePointcut() {
    // Pointcut signature
}

```

#### Pointcut Syntax Breakdown: `execution(* com.myproject.jobportal..*.*(..))`

* `*` — Matches any return type (`void`, `String`, `Job`, etc.).
* `com.myproject.jobportal..` — Target package and all sub-packages (`.service`, `.controller`, etc.).
* `*` — Matches any class name.
* `.*` — Matches any method name.
* `(..)` — Matches any method parameters (zero or more).

---

### 4. Advice

* **Definition:** The actual code/action executed at a particular Join Point matched by a Pointcut.
* **Analogy:** What the security guard actually does (e.g., checks ID cards, logs entry times).
* **Example:**

```java
@Around("applicationPackagePointcut()")
public Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    // THIS BODY IS THE ADVICE
    long start = System.currentTimeMillis();

    Object result = joinPoint.proceed(); // Executes target method

    long elapsed = System.currentTimeMillis() - start;
    log.info("Method executed in {} ms", elapsed);

    return result;
}

```

---

### 5. Advice Types

Spring AOP provides five types of advice depending on **when** you want your action to run relative to the target method execution:

| Advice Type | Execution Timing | Use Case |
| --- | --- | --- |
| `@Before` | Runs *before* the target method executes. | Input validation, security checks. |
| `@AfterReturning` | Runs *after* the method returns successfully (without throwing exceptions). | Audit logging, caching results. |
| `@AfterThrowing` | Runs *after* the method throws an exception. | Centralized exception logging, alert dispatching. |
| `@After` | Runs *after* the method finishes (regardless of success or failure; like a `finally` block). | Resource cleanup. |
| **`@Around`** | **Surrounds** the method execution completely. Can run code before and after, alter return values, or prevent execution entirely. | Performance timing, transaction management. |

---

### 6. Target Object

* **Definition:** The underlying Spring bean containing the actual business logic being advised.
* **Analogy:** The employee being inspected by the security guard.
* **Example:**

```java
// Target Object instance containing pure business logic
@Service
public class JobService {
    public String getJobTitle(Long id) {
        return "Senior Java Engineer";
    }
}

```

---

### 7. AOP Proxy

* **Definition:** An intermediate object dynamically created by Spring AOP that wraps the Target Object to intercept calls and execute advice.
* **Analogy:** A receptionist sitting in front of an executive who answers incoming phone calls first before forwarding them.

When another component asks Spring for `JobService` via `@Autowired`, Spring injects the **Proxy**, not the original Target Object.

# Pointcut Designators Guide

## Introduction

**Pointcut Designators (PCD)** tell your aspect exactly which methods or classes to intercept. They act as the filtering criteria for your cross-cutting concerns, allowing you to selectively apply logic like logging, transaction management, caching, and security.

---

## Most Commonly Used Designators

### `execution()`
This is the absolute king of designators. It matches the structural signature of method executions, including modifiers, return types, package names, class names, method names, and arguments.

* **Purpose:** Used to target specific methods or entire packages universally.
* **Example:** `execution(* com.myproject.service..*.*(..))` targets every method in the `service` package and its sub-packages.

### `@annotation()`
This is the cleanest and most highly recommended designator for flexible, selective targeting. It matches methods that are tagged with a specific custom runtime annotation.

* **Purpose:** Used for conditional features like caching, custom security checks, or performance tracking on specific methods.
* **Example:** `@annotation(com.myproject.aspect.RedisCache)` targets only methods marked with `@RedisCache`.

---

## Other Crucial Designators

| Designator Category | Designator | Purpose & Description | Example |
| :--- | :--- | :--- | :--- |
| **Type Matching** | `within()` | Restricts matching to join points within certain types or classes. Filters at the class or package type level rather than the method signature level. | `within(com.myproject.controller..*)` matches everything inside the controller layer. |
| **Proxy & Target Matching** | `this()` | Matches join points where the AOP proxy object reference is an instance of the specified type. | `this(com.myproject.service.OrderService)` |
| | `target()` | Matches join points where the actual underlying target business object being proxied is an instance of the specified type. | `target(com.myproject.service.OrderServiceImpl)` |
| **Argument Matching** | `args()` | Limits matching to join points where runtime argument instances match specified classes. | `args(java.lang.String, ..)` matches methods where the first argument is a `String`. |
| | `@args()` | Limits matching to join points where the runtime arguments passed to the method are annotated with a specific class-level annotation. | `@args(com.myproject.annotation.ValidatedEntity)` |
| **Class Annotation Matching** | `@within()` | Matches any join point inside a class that is tagged with a specific class-level annotation. | `@within(org.springframework.web.bind.annotation.RestController)` |
| | `@target()` | Matches any join point where the executing target class object has a specific class-level annotation. | `@target(org.springframework.stereotype.Service)` |

---

## Best Practices for Designators

1. **Combine for Performance:** Always combine broad designators like `within()` or `execution()` with narrow ones like `@annotation()` using logical operators (`&&`, `||`, `!`). This helps the Spring AOP engine evaluate and compile faster by ruling out irrelevant classes early.
2. **Avoid Over-Interception:** Avoid running broad patterns like `execution(* *.*(..))` without package boundaries, as this will accidentally intercept internal framework proxies, getters, setters, and configuration classes.
3. **Prefer Explicit Pointcut Expressions:** Reuse named `@Pointcut` definitions to keep advice logic clean, readable, and easy to maintain across large codebases.


```
[Caller Component]
       │
       ▼ (Calls: jobService.getJobTitle(101L))
┌────────────────────────────────────────────────────────┐
│ 1. PROXY OBJECT (Intercepts call & packs JoinPoint)    │
└──────┬─────────────────────────────────────────────────┘
       │
       ▼ (Proxy passes execution context to Aspect)
┌────────────────────────────────────────────────────────┐
│ 2. ASPECT BEAN (LoggingAndPerformanceAspect)           │
│                                                        │
│    log.info("➡️ Entering method...");                  │
│    Object result = joinPoint.proceed(); ───┐           │
└────────────────────────────────────────────┼───────────┘
                                             │ (Triggers actual execution)
                                             ▼
                              ┌──────────────────────────┐
                              │ 3. REAL TARGET METHOD    │
                              │    (Fetches from DB)     │
                              └──────────────┬───────────┘
                                             │
                                             ▼ (Returns "Senior Java Engineer")
┌────────────────────────────────────────────────────────┐
│ 4. ASPECT BEAN (Resumes right after proceed())         │
│                                                        │
│    log.info("⏱ Execution time...");                    │
│    return result; ─────────────────────────┐           │
└────────────────────────────────────────────┼───────────┘
                                             │
                                             ▼ (Returns result via Proxy)
[Caller Component receives: "Senior Java Engineer"]
```

* **Important Points:** 
* *Cross Cutting:* concerns means the features which depends across other classes and is not the part of the main buisness logic but are very crucial in proper backend management.

* *Issue:* is that Cross cutting concerns introduces two main issues:
1. Scattering: occurs when the code for a single cross-cutting concern (like logging or security) is duplicated and spread out across multiple files or classes, creating a maintenance nightmare.
2. Tangling: occurs when a single method or class contains a mixture of core business logic and unrelated technical infrastructure code, making the code complex and hard to read.

* **What AOP Actually Features:**

* *Separation of Concerns:* The primary superpower of AOP is extracting scattered infrastructure code out of your business files and isolating it into dedicated modules called Aspects.
* *Injecting Custom Actions:* Once isolated, AOP allows you to define any custom action you want to take after or before that main logic execution. This includes actions like formatting input arguments, validating data, or modifying the returned value seamlessly.
* *Advanced Flow Control:* It enables advanced run-time behaviors like looping a method call automatically until an exception goes away or a retry limit is exceeded.
* *The Core Purpose Reminder:* Though all of these things (like loops or formatting) can technically be done directly inside the main logic section or method as well, the whole purpose of AOP is to separate these cross-cutting concerns and pull them completely out of the main business logic things only!
* **Best Practice to Write Aspects:**
* *1. Organize by Concern, Not Layers:* Aspects should be created based strictly on their functional concern (like security, logging, or transactions) rather than architectural layers (like creating a single generic ControllerAspect or ServiceAspect).
* *2. Layer-Specific Versions Inside One Concern:* Each aspect defining a specific concern can have different versions or logic patterns tailored for each distinct layer of your application.
* *3. Flexible Implementation Choices:* For example, a single LoggingAspect may define common or completely different logging patterns based on your choice: the Controller layer may have a high-level request logging pattern, the Service layer may log business execution metrics, and the Repository layer may have a different pattern focused strictly on database connectivity queries.



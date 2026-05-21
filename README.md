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
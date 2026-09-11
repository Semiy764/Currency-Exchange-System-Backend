# Currency Exchange System (Sarafi) — Backend API

A REST API backend for managing a currency exchange office (*sarafi*): user accounts, exchange rates, buy/sell transactions, and vault (cash reserve) tracking. Built with Spring Boot, secured with JWT, and backed by SQLite.

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Authentication](#authentication)
- [Roles & Permissions](#roles--permissions)
- [API Reference](#api-reference)
  - [Auth](#1-auth-apiauth)
  - [Users](#2-users-apiusers)
  - [Currencies](#3-currencies-apicurrencies)
  - [Exchange Rates](#4-exchange-rates-apirates)
  - [Customers](#5-customers-apicustomers)
  - [Tellers](#6-tellers-apitellers)
  - [Transactions](#7-transactions-apitransactions)
  - [Vault](#8-vault-apivault)
  - [Reports](#9-reports-apireports)
- [Transaction Workflow](#transaction-workflow)
- [Error Handling](#error-handling)

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.4 |
| Database | SQLite (via Hibernate Community Dialect) |
| ORM | Spring Data JPA / Hibernate |
| Auth | Spring Security + JWT (`io.jsonwebtoken`) |
| Build tool | Maven |

## Project Structure

```
src/main/java/org/example/
├── config/            # DataSource and Security configuration
├── controller/         # REST controllers (one per resource)
├── dto/
│   ├── request/        # Incoming request bodies
│   └── response/        # Outgoing response shapes
├── enums/               # UserRole, TxStatus, TxType, LedgerReason
├── exception/            # Custom exceptions
├── model/                # JPA entities
├── repository/           # Data access layer
├── security/             # JWT filter, JwtUtil, AuthenticatedUser principal
└── service/              # Business logic (interfaces + impl)
```

## Getting Started

### Prerequisites

- JDK 17+
- Maven 3.8+

### 1. Set the JWT secret

The app reads the signing key for JWTs from the `JWT_SECRET` environment variable:

```bash
export JWT_SECRET=your-secret-key-here
```

### 2. Run the application

```bash
./mvnw spring-boot:run
```

The SQLite database file is created automatically at `database/exchange_project.db` (schema is kept up to date via `spring.jpa.hibernate.ddl-auto=update`). The server starts on port `8080` by default.

### 3. Run the tests

```bash
./mvnw test
```

## Authentication

The API uses stateless JWT authentication.

1. Call `POST /api/auth/login` (or one of the register endpoints) to obtain a token.
2. Send the token on every subsequent request in the `Authorization` header:

```
Authorization: Bearer <token>
```

A request filter reads this header, validates the token, and attaches the caller's user id, username, and role to the request context. Endpoints that don't require a token (`login`, `register-customer`) can be called anonymously; everything else returns `401/403` without a valid token of the right role.

## Roles & Permissions

There are three roles: `ADMIN`, `TELLER`, `CUSTOMER`.

| Area | ADMIN | TELLER | CUSTOMER |
|---|:---:|:---:|:---:|
| Login / customer self-registration | ✅ | ✅ | ✅ (no token needed) |
| User management | ✅ | ❌ | ❌ |
| View currencies & rates | ✅ | ✅ | ✅ |
| Create/edit currencies | ✅ | ❌ | ❌ |
| Set exchange rates | ✅ | ✅ | ❌ |
| Manage customers | ✅ | ✅ | ❌ |
| Manage tellers | ✅ | ❌ | ❌ |
| Record direct transactions (buy/sell) | ✅ | ✅ | ❌ |
| Submit a transaction request | ➖ | ➖ | ✅ |
| Approve / reject a transaction request | ✅ | ✅ | ❌ |
| View own transactions / cancel own request | ➖ | ➖ | ✅ |
| View vault balances & ledger | ✅ | ✅ | ❌ |
| Deposit / withdraw / reconcile vault | ✅ | ❌ | ❌ |
| Reports | ✅ (daily) | ✅ (profit-loss, vault summary) | ❌ |

## API Reference

All endpoints are prefixed with `/api`. Unless noted as "no token required", every call needs the `Authorization: Bearer <token>` header, and the caller's role must match what's listed.

### 1. Auth — `/api/auth`

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/auth/register/register-teller` | ADMIN | Creates a new teller account. Only an admin can call this. |
| POST | `/auth/register/register-customer` | Public | Self-registration for a new customer — no token needed. |
| POST | `/auth/login` | Public | Authenticates a user and returns a JWT. |
| POST | `/auth/change-password` | Any authenticated user | Changes the password of the currently logged-in user. |

**`POST /api/auth/login`**
Authenticates with a username/password pair and issues a JWT for use on subsequent requests.

Request body:
```json
{
  "username": "string",
  "password": "string"
}
```

Response:
```json
{
  "role": "ADMIN | TELLER | CUSTOMER",
  "token": "string",
  "id": 1,
  "username": "string"
}
```

**`POST /api/auth/register/register-customer`**
Registers a new customer account (self-service, no token required) and immediately returns a token, same shape as login.

Request body:
```json
{
  "username": "string",
  "password": "string",
  "fullName": "string",
  "phone": "string",
  "nationalId": "string"
}
```

**`POST /api/auth/register/register-teller`**
Same body shape as customer registration, but requires an `ADMIN` token and creates the account with the `TELLER` role.

**`POST /api/auth/change-password`**
Called by the logged-in user to change their own password.

Request body:
```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```

### 2. Users — `/api/users`

Manages login accounts (as opposed to customer/teller profile data).

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/users` | ADMIN | Lists every user account in the system. |
| GET | `/users/{id}` | ADMIN, TELLER | Returns one user's account details (username, role, active flag). |
| PUT | `/users/{id}` | ADMIN | Updates a user's username and active flag. |
| POST | `/users/{id}/deactivate` | ADMIN | Disables a user's account (blocks login). |
| POST | `/users/{id}/activate` | ADMIN | Re-enables a previously deactivated account. |

`PUT /api/users/{id}` request body:
```json
{
  "username": "string",
  "isActive": true
}
```

### 3. Currencies — `/api/currencies`

Manages the list of currencies the office trades.

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/currencies` | Any authenticated user | Lists all currencies. |
| POST | `/currencies` | ADMIN | Adds a new currency (created as active). |
| PUT | `/currencies/{id}` | ADMIN | Updates a currency's name, code, or symbol. |
| POST | `/currencies/{id}/deactivate` | ADMIN | Marks a currency inactive (stops it being tradeable). |
| POST | `/currencies/{id}/activate` | ADMIN | Reactivates a currency. |

`POST /api/currencies` / `PUT /api/currencies/{id}` request body:
```json
{
  "code": "USD",
  "name": "US Dollar",
  "symbol": "$"
}
```

### 4. Exchange Rates — `/api/rates`

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/rates` | Any authenticated user | Returns the current buy/sell rate for every currency. |
| POST | `/rates` | ADMIN, TELLER | Records a new rate for a currency (becomes the new "current" rate). |
| GET | `/rates/history/{currencyId}` | Any authenticated user | Returns the full rate history for one currency. |

`POST /api/rates` request body:
```json
{
  "currencyId": 1,
  "buyRate": 580000,
  "sellRate": 590000
}
```

### 5. Customers — `/api/customers`

Customer profile data (separate from their login `User` account).

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/customers` | ADMIN, TELLER | Lists all customers. |
| GET | `/customers/{id}` | ADMIN, TELLER | Returns one customer's profile. |
| GET | `/customers/search?nationalCode=` | ADMIN, TELLER | Finds a customer by national ID. |
| GET | `/customers/me` | CUSTOMER | Returns the profile of the currently logged-in customer. |

### 6. Tellers — `/api/tellers`

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/tellers` | ADMIN | Lists all teller profiles. |
| GET | `/tellers/{tellerId}` | ADMIN | Returns one teller's profile. |
| GET | `/tellers/me` | TELLER | Returns the profile of the currently logged-in teller. |

### 7. Transactions — `/api/transactions`

Handles both transactions entered directly by staff (in-person, already completed) and transactions requested by a customer (which need approval).

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/transactions/buy` | ADMIN, TELLER | Records a completed currency purchase on behalf of a walk-in customer. |
| POST | `/transactions/sell` | ADMIN, TELLER | Records a completed currency sale on behalf of a walk-in customer. |
| GET | `/transactions` | ADMIN | Lists every transaction, newest first. |
| GET | `/transactions/{id}` | ADMIN, TELLER | Returns the details of one transaction. |
| POST | `/transactions/request` | CUSTOMER | Submits a buy/sell request that starts in `PENDING` status. |
| GET | `/transactions/pending` | ADMIN, TELLER | Lists all requests awaiting approval. |
| POST | `/transactions/{id}/approve` | ADMIN, TELLER | Approves a pending request; moves it to `COMPLETED` and updates the vault. |
| POST | `/transactions/{id}/reject` | ADMIN, TELLER | Rejects a pending request; moves it to `REJECTED`. |
| POST | `/transactions/{id}/cancel` | CUSTOMER | Lets the requesting customer cancel their own request while it's still `PENDING`. |
| GET | `/transactions/my` | CUSTOMER | Returns the logged-in customer's own transaction history. |

**`POST /api/transactions/buy`** and **`POST /api/transactions/sell`**
Used by staff to record a transaction that is already finalized (e.g. a customer at the counter). Created directly with status `COMPLETED`.

Request body:
```json
{
  "currencyId": 1,
  "customerId": 5,
  "amountCurrency": 100,
  "amountToman": 59000000,
  "requestedRate": 590000,
  "rateUsed": 590000
}
```

**`POST /api/transactions/request`**
Used by a customer to submit a request that a staff member must approve later. Created with status `PENDING`; the customer is inferred from the JWT, not from the request body.

Request body:
```json
{
  "type": "BUY | SELL",
  "currencyId": 1,
  "amountCurrency": 100,
  "amountToman": 59000000,
  "requestedRate": 590000
}
```

**`POST /api/transactions/{id}/approve`**
Approves a `PENDING` request, applying it to the vault balance and marking it `COMPLETED`.

**`POST /api/transactions/{id}/reject`**
Rejects a `PENDING` request without touching the vault; marks it `REJECTED`.

**`POST /api/transactions/{id}/cancel`**
Only the customer who created the request can cancel it, and only while it's still `PENDING`. Marks it `CANCELED`.

### 8. Vault — `/api/vault`

Tracks the office's on-hand balance of each currency and every change made to it.

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/vault/balances` | ADMIN, TELLER | Returns the current balance of every currency in the vault. |
| GET | `/vault/balances/{id}` | ADMIN, TELLER | Returns the current balance of one currency. |
| GET | `/vault/balances/low/{threshold}` | ADMIN, TELLER | Returns currencies whose balance has fallen below `threshold`. |
| POST | `/vault/deposit` | ADMIN | Manually adds funds to the vault (e.g. cash brought in). |
| POST | `/vault/withdraw` | ADMIN | Manually removes funds from the vault. |
| GET | `/vault/ledger/{currencyId}` | ADMIN, TELLER | Returns the full history of balance changes for one currency. |
| GET | `/vault/reconcile/{currencyId}` | ADMIN | Checks whether the current balance matches the sum of the ledger entries; returns `true`/`false`. |

`POST /api/vault/deposit` / `POST /api/vault/withdraw` request body:
```json
{
  "currencyId": 1,
  "amount": 1000,
  "performedByUserId": 2
}
```

> Every approved transaction and every manual deposit/withdrawal automatically writes an entry to the vault ledger with a reason (`DEPOSIT`, `WITHDRAW`, `TX_BUY`, `TX_SELL`, or `ADJUSTMENT`), so the balance can always be reconciled against its history.

### 9. Reports — `/api/reports`

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/reports/daily` | ADMIN | Returns all transactions created today. |
| GET | `/reports/profit-loss` | ADMIN, TELLER | Returns total buy/sell volume and net profit for a given period. |
| GET | `/reports/vault-summary/{threshold}` | ADMIN, TELLER | Returns a vault-wide summary, flagging currencies under `threshold`. |

**`GET /api/reports/profit-loss`**
Takes a JSON body describing the period to analyze (note: this is a `GET` request with a body).

Request body:
```json
{
  "start": "2026-09-01T00:00:00",
  "end": "2026-09-30T23:59:59"
}
```

Response:
```json
{
  "periodStart": "2026-09-01T00:00:00",
  "perionEnd": "2026-09-30T23:59:59",
  "totalBuyAmount": 120000000,
  "totalSellAmount": 135000000,
  "profit": 15000000
}
```

## Transaction Workflow

```
Customer submits request          Staff reviews             Outcome
POST /transactions/request  ──▶   GET /transactions/pending
      (status: PENDING)                   │
                                           ├─▶ POST /{id}/approve ──▶ COMPLETED (vault updated)
                                           └─▶ POST /{id}/reject  ──▶ REJECTED

Customer can instead:
POST /transactions/{id}/cancel  ──▶ CANCELED   (only while still PENDING, only by the requester)
```

Every transaction that reaches `COMPLETED` (whether entered directly by staff or approved from a customer request) writes a corresponding entry to the vault ledger (`TX_BUY` or `TX_SELL`) and updates the vault balance for that currency.

## Error Handling

The API returns standard HTTP status codes:

- `400 Bad Request` — invalid or missing fields in the request body
- `401 Unauthorized` — missing or invalid JWT
- `403 Forbidden` — valid JWT, but the caller's role isn't allowed to access the endpoint
- `404 Not Found` — the requested resource (user, transaction, currency, etc.) doesn't exist

## Configuration Notes

- Local overrides live in `application-local.properties`, which is git-ignored so local settings and secrets never get committed.
- The SQLite database file (`*.db`) is also git-ignored.

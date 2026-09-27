# Akash Hotels --- Full-Stack Food Ordering Application

## 1. Project Purpose

**Akash Hotels** is a learning-focused full-stack food-ordering
application.

The goal is not just to build a food-ordering website. The project is
designed to make us understand the complete flow of a modern Java/Spring
Boot application:

``` text
Angular Frontend
       ↓
REST APIs
       ↓
Spring Boot
       ↓
Controller
       ↓
Service
       ↓
Repository / JPA
       ↓
Hibernate
       ↓
PostgreSQL
```

Later we will add:

``` text
Spring Security + JWT
        ↓
Role-based authorization

Jenkins
        ↓
CI/CD

Docker
        ↓
Containerization

Cloud deployment
        ↓
AWS
```

The application starts as a clean Spring Boot backend so that each
concept can be learned properly before adding complexity.

------------------------------------------------------------------------

# 2. Business Scenario

Akash Hotels is a restaurant located in **Guindy, Chennai**.

The restaurant has around **10--15 dishes**.

A customer can:

-   View available dishes
-   View dish pictures
-   View prices and descriptions
-   Add dishes to a cart
-   Place an order
-   Select a delivery location from predefined Chennai locations
-   Track the order status
-   See the assigned delivery partner
-   See the final order amount

The hotel management team can:

-   Add dishes
-   Update dishes
-   Remove/deactivate dishes
-   View incoming orders
-   Update order status
-   Configure delivery partners
-   See available/busy delivery partners
-   Assign delivery partners
-   View delivery information

Delivery partners can:

-   Have their own profile
-   Become available/unavailable
-   Receive assigned deliveries
-   Accept/complete deliveries
-   See delivery details
-   Receive delivery earnings

------------------------------------------------------------------------

# 3. Important Business Rule --- Delivery Partner Availability

A delivery partner can handle **only one active delivery at a time**.

Example:

``` text
Partner A → Order #101 → DELIVERING
Partner B → AVAILABLE
Partner C → AVAILABLE
```

Order #102 cannot be assigned to Partner A.

It can be assigned to Partner B or C.

When Order #101 is delivered:

``` text
Partner A → AVAILABLE
```

and Partner A can receive another order.

This is one of the important business rules we will implement in the
service layer.

------------------------------------------------------------------------

# 4. Delivery Location Model

For the first version, we will NOT integrate Google Maps or another map
provider.

The restaurant is fixed at:

``` text
Akash Hotels
Guindy, Chennai
```

Customers choose from predefined Chennai delivery locations.

Example locations:

-   Guindy
-   T. Nagar
-   Adyar
-   Velachery
-   Anna Nagar
-   Nungambakkam
-   Saidapet
-   Mylapore
-   Tambaram
-   Porur
-   Sholinganallur
-   OMR
-   Egmore
-   Kodambakkam
-   Besant Nagar

Each location will have a **static configured distance from the
restaurant**.

Example:

``` text
Guindy       → 2 km
Saidapet     → 4 km
T. Nagar     → 5 km
Adyar        → 7 km
Velachery    → 8 km
```

These are application data for the learning version, not real-time map
calculations.

Later, this can be replaced by a map API.

------------------------------------------------------------------------

# 5. Delivery Earnings

The delivery partner's earnings have two components:

``` text
Delivery Earnings
=
Base Distance Amount
+
Time-based Bonus
```

## 5.1 Distance-based base amount

Example configuration:

``` text
0–3 km       → ₹30
3–5 km       → ₹40
5–8 km       → ₹50
8–12 km      → ₹65
12+ km       → ₹80
```

These values are configurable rather than hard-coded into business
logic.

------------------------------------------------------------------------

# 6. Time-based Bonus

The faster the delivery, the higher the bonus.

Example:

``` text
Delivery time <= 15 minutes
→ +₹20

16–30 minutes
→ +₹15

31–45 minutes
→ +₹10

46–60 minutes
→ +₹5

> 60 minutes
→ No bonus
```

Therefore:

``` text
Example:

Distance = 6 km
Base = ₹50

Delivery completed in 12 minutes
Bonus = ₹20

Total partner earning = ₹70
```

Another example:

``` text
Distance = 6 km
Base = ₹50

Delivery completed in 75 minutes
Bonus = ₹0

Total partner earning = ₹50
```

The bonus tiers should be stored/configured so that management can
change them later.

------------------------------------------------------------------------

# 7. Main Actors / Stakeholders

## Customer

Uses the customer-facing Angular application.

Responsibilities:

-   Register/login
-   Browse dishes
-   View dish details
-   Manage cart
-   Place orders
-   View order history
-   Track order
-   View delivery information

------------------------------------------------------------------------

## Hotel Management

Uses the management portal.

Responsibilities:

-   Manage dishes
-   Manage delivery partners
-   View orders
-   Assign delivery partners
-   Configure delivery rules
-   Update order status
-   View operational information

------------------------------------------------------------------------

## Delivery Partner

Uses the delivery-partner interface.

Responsibilities:

-   Maintain profile
-   Set availability
-   View assigned order
-   Accept/complete delivery
-   View delivery earnings
-   View delivery history

------------------------------------------------------------------------

## System

The backend is responsible for:

-   Authentication
-   Authorization
-   Order processing
-   Delivery assignment
-   Availability validation
-   Earnings calculation
-   Database operations
-   Validation
-   Exception handling

------------------------------------------------------------------------

# 8. Technology Stack

## Backend

``` text
Java 21
Spring Boot
Spring Web
Spring Data JPA
Hibernate
Spring Security
JWT
Bean Validation
Maven
Lombok
```

## Database

``` text
PostgreSQL
```

## Frontend

``` text
Angular
TypeScript
HTML
CSS
RxJS
HTTP Client
Route Guards
HTTP Interceptors
```

## API Testing

``` text
Swagger / OpenAPI
Postman
```

## Version Control

``` text
Git
GitHub
```

## CI/CD

``` text
Jenkins
```

## Future Infrastructure

``` text
Docker
Docker Compose
AWS
Kubernetes
```

------------------------------------------------------------------------

# 9. Initial Architecture

For learning, the first version will be a **modular Spring Boot
application**, not multiple microservices.

Logical modules:

``` text
Akash Hotels Backend
│
├── Authentication
│
├── Customer
│
├── Hotel Management
│
├── Dish/Menu
│
├── Cart
│
├── Order
│
├── Delivery
│
├── Delivery Partner
│
├── Payment
│
└── Earnings
```

This lets us understand Spring Boot deeply before introducing
microservice complexity.

Later, selected modules can be split into microservices.

------------------------------------------------------------------------

# 10. Database Design

## 10.1 Users

Stores authentication and common user information.

``` text
users
-------------------------
id PK
name
email UNIQUE
password_hash
phone
role
created_at
updated_at
active
```

Possible roles:

``` text
CUSTOMER
HOTEL_ADMIN
DELIVERY_PARTNER
```

------------------------------------------------------------------------

# 11. Hotel

``` text
hotels
-------------------------
id PK
name
address
city
latitude
longitude
active
created_at
```

For the first version:

``` text
name = Akash Hotels
city = Chennai
location = Guindy
```

------------------------------------------------------------------------

# 12. Dish

``` text
dishes
-------------------------
id PK
hotel_id FK → hotels.id
name
description
price
image_url
category
available
created_at
updated_at
```

Relationship:

``` text
Hotel 1 ─────────── * Dish
```

One hotel has many dishes.

Each dish belongs to one hotel.

------------------------------------------------------------------------

# 13. Customer Profile

We can keep common login information in `users` and customer-specific
information here.

``` text
customer_profiles
-------------------------
id PK
user_id FK → users.id
default_location_id FK
address_details
```

Relationship:

``` text
User 1 ───────── 1 CustomerProfile
```

------------------------------------------------------------------------

# 14. Delivery Location

``` text
delivery_locations
-------------------------
id PK
name
city
distance_from_hotel_km
active
```

Example:

``` text
1 | Guindy       | Chennai | 2.0
2 | Saidapet     | Chennai | 4.0
3 | T. Nagar     | Chennai | 5.0
4 | Adyar        | Chennai | 7.0
```

Relationship:

``` text
DeliveryLocation 1 ───────── * Orders
```

One location can be used by many orders.

------------------------------------------------------------------------

# 15. Delivery Partner

``` text
delivery_partners
-------------------------
id PK
user_id FK → users.id UNIQUE
vehicle_type
vehicle_number
availability_status
joined_at
active
```

Possible availability:

``` text
AVAILABLE
BUSY
OFFLINE
```

Relationship:

``` text
User 1 ───────── 1 DeliveryPartner
```

------------------------------------------------------------------------

# 16. Orders

``` text
orders
-------------------------
id PK
customer_id FK → users.id
hotel_id FK → hotels.id
delivery_location_id FK → delivery_locations.id
delivery_partner_id FK → delivery_partners.id NULL
order_status
subtotal
delivery_fee
total_amount
placed_at
accepted_at
delivered_at
```

Possible order statuses:

``` text
PLACED
CONFIRMED
PREPARING
READY_FOR_PICKUP
ASSIGNED
OUT_FOR_DELIVERY
DELIVERED
CANCELLED
```

Relationships:

``` text
Customer 1 ───────── * Orders

Hotel 1 ───────── * Orders

DeliveryLocation 1 ───────── * Orders

DeliveryPartner 1 ───────── * Orders
```

The delivery partner relationship can initially be nullable because an
order may exist before assignment.

------------------------------------------------------------------------

# 17. Order Items

An order can contain multiple dishes.

``` text
order_items
-------------------------
id PK
order_id FK → orders.id
dish_id FK → dishes.id
quantity
unit_price
subtotal
```

Relationships:

``` text
Order 1 ───────── * OrderItem

Dish 1 ───────── * OrderItem
```

Important design decision:

`unit_price` is stored in `order_items`.

Why?

Suppose:

``` text
Burger today = ₹150
```

Customer orders it.

Tomorrow:

``` text
Burger = ₹180
```

The old order must still show:

``` text
₹150
```

Therefore, we store the price at the time of purchase.

------------------------------------------------------------------------

# 18. Delivery

We should separate delivery information from the order itself.

``` text
deliveries
-------------------------
id PK
order_id FK → orders.id UNIQUE
delivery_partner_id FK → delivery_partners.id
pickup_time
delivery_start_time
delivered_time
distance_km
base_earning
bonus_amount
total_earning
delivery_status
```

Possible status:

``` text
ASSIGNED
ACCEPTED
PICKED_UP
DELIVERING
DELIVERED
FAILED
```

Relationship:

``` text
Order 1 ───────── 1 Delivery

DeliveryPartner 1 ───────── * Delivery
```

------------------------------------------------------------------------

# 19. Delivery Earning Rules

Store configurable rules rather than hard-coding everything.

## Distance pricing

``` text
delivery_distance_rules
-------------------------
id PK
min_distance_km
max_distance_km
base_amount
active
```

## Time bonus

``` text
delivery_bonus_rules
-------------------------
id PK
max_minutes
bonus_amount
active
```

Example:

``` text
15 min → ₹20
30 min → ₹15
45 min → ₹10
60 min → ₹5
```

If delivery time is greater than 60 minutes:

``` text
bonus = ₹0
```

This makes the system configurable.

------------------------------------------------------------------------

# 20. Cart

For a real application, customers need a cart before placing an order.

``` text
carts
-------------------------
id PK
customer_id FK → users.id UNIQUE
created_at
updated_at
```

``` text
cart_items
-------------------------
id PK
cart_id FK → carts.id
dish_id FK → dishes.id
quantity
```

Relationships:

``` text
Customer 1 ───────── 1 Cart

Cart 1 ───────── * CartItem

Dish 1 ───────── * CartItem
```

------------------------------------------------------------------------

# 21. Payment

For the learning project, payment can initially be simulated.

``` text
payments
-------------------------
id PK
order_id FK → orders.id UNIQUE
amount
payment_method
payment_status
transaction_reference
paid_at
```

Payment methods:

``` text
CARD
UPI
CASH
MOCK_PAYMENT
```

Payment status:

``` text
PENDING
SUCCESS
FAILED
REFUNDED
```

Later, this can be replaced by a real payment gateway.

------------------------------------------------------------------------

# 22. Complete Entity Relationship Overview

``` text
                         ┌──────────────┐
                         │    USERS     │
                         └──────┬───────┘
                                │
                  ┌─────────────┼──────────────┐
                  │             │              │
                  ▼             ▼              ▼
             CUSTOMER       DELIVERY       HOTEL ADMIN
             PROFILE        PARTNER
                  │             │
                  │             │
                  ▼             │
                CART             │
                  │             │
                  ▼             │
             CART ITEMS         │
                  │             │
                  └──────┐      │
                         │      │
                         ▼      ▼
                      ORDER ─ DELIVERY PARTNER
                       │
              ┌────────┼─────────┐
              │        │         │
              ▼        ▼         ▼
         ORDER ITEMS  PAYMENT  DELIVERY
              │
              ▼
            DISH
              │
              ▼
            HOTEL

ORDER ───────── DELIVERY LOCATION
```

------------------------------------------------------------------------

# 23. Important Relationships to Learn

This project intentionally contains several relationship types.

## One-to-One

``` text
User → CustomerProfile
User → DeliveryPartner
Order → Payment
Order → Delivery
Customer → Cart
```

## One-to-Many

``` text
Hotel → Dishes
Customer → Orders
Order → OrderItems
Cart → CartItems
DeliveryPartner → Deliveries
DeliveryLocation → Orders
```

## Many-to-One

``` text
Dish → Hotel
OrderItem → Order
OrderItem → Dish
Order → Customer
Order → Hotel
Order → DeliveryLocation
```

The `OrderItem` table effectively represents the many-to-many business
relationship between:

``` text
Orders ↔ Dishes
```

because:

``` text
One Order → many Dishes
One Dish → many Orders
```

------------------------------------------------------------------------

# 24. Order Lifecycle

Normal order flow:

``` text
CUSTOMER
   ↓
Browse dishes
   ↓
Add to cart
   ↓
Checkout
   ↓
Place Order
   ↓
PLACED
   ↓
Hotel confirms
   ↓
CONFIRMED
   ↓
Hotel prepares food
   ↓
PREPARING
   ↓
Food ready
   ↓
READY_FOR_PICKUP
   ↓
System/Hotel assigns available partner
   ↓
ASSIGNED
   ↓
Partner picks up
   ↓
OUT_FOR_DELIVERY
   ↓
Customer receives food
   ↓
DELIVERED
```

------------------------------------------------------------------------

# 25. Delivery Assignment Logic

When an order becomes ready:

``` text
1. Find AVAILABLE delivery partners.

2. Remove BUSY/OFFLINE partners.

3. Determine eligible partners.

4. Select a partner based on configured logic.

5. Assign the delivery.

6. Change partner:
   AVAILABLE → BUSY

7. Change order:
   READY_FOR_PICKUP → ASSIGNED

8. Create delivery record.

9. Partner accepts/picks up.

10. Partner delivers.

11. Calculate delivery duration.

12. Calculate base distance earning.

13. Calculate time bonus.

14. Save total earning.

15. Change partner:
    BUSY → AVAILABLE
```

------------------------------------------------------------------------

# 26. Initial Assignment Strategy

For version 1:

``` text
Choose an AVAILABLE partner
```

Later:

``` text
AVAILABLE partners
       ↓
Distance
       ↓
Current workload
       ↓
Estimated delivery time
       ↓
Best partner
```

Eventually this can become a proper delivery-dispatch algorithm.

------------------------------------------------------------------------

# 27. REST API Design

## Authentication

``` http
POST /api/auth/register
POST /api/auth/login
```

Login returns:

``` json
{
  "token": "JWT_TOKEN"
}
```

------------------------------------------------------------------------

## Dish APIs

``` http
GET    /api/dishes
GET    /api/dishes/{id}
POST   /api/dishes
PUT    /api/dishes/{id}
DELETE /api/dishes/{id}
```

Management-only operations:

``` text
POST
PUT
DELETE
```

------------------------------------------------------------------------

# 28. Cart APIs

``` http
GET    /api/cart
POST   /api/cart/items
PUT    /api/cart/items/{id}
DELETE /api/cart/items/{id}
DELETE /api/cart
```

------------------------------------------------------------------------

# 29. Order APIs

``` http
POST /api/orders
GET  /api/orders
GET  /api/orders/{id}
POST /api/orders/{id}/cancel
```

Hotel management:

``` http
GET   /api/admin/orders
PATCH /api/admin/orders/{id}/status
POST  /api/admin/orders/{id}/assign
```

------------------------------------------------------------------------

# 30. Delivery Partner APIs

``` http
GET   /api/delivery-partners/{id}
PATCH /api/delivery-partners/{id}/availability
GET   /api/delivery-partners/{id}/deliveries
GET   /api/delivery-partners/{id}/earnings
```

Management:

``` http
POST   /api/admin/delivery-partners
GET    /api/admin/delivery-partners
PUT    /api/admin/delivery-partners/{id}
DELETE /api/admin/delivery-partners/{id}
```

------------------------------------------------------------------------

# 31. Location APIs

``` http
GET /api/locations
POST /api/admin/locations
PUT /api/admin/locations/{id}
DELETE /api/admin/locations/{id}
```

------------------------------------------------------------------------

# 32. Delivery Rule APIs

Management can configure distance pricing:

``` http
GET  /api/admin/delivery-rules/distance
POST /api/admin/delivery-rules/distance
PUT  /api/admin/delivery-rules/distance/{id}
```

And time bonuses:

``` http
GET  /api/admin/delivery-rules/bonus
POST /api/admin/delivery-rules/bonus
PUT  /api/admin/delivery-rules/bonus/{id}
```

------------------------------------------------------------------------

# 33. Security Design

After the basic CRUD flow works, we will implement:

``` text
Spring Security
       ↓
JWT Authentication
       ↓
Login
       ↓
JWT token
       ↓
Angular stores token
       ↓
Angular HTTP Interceptor
       ↓
Authorization: Bearer <token>
       ↓
Spring Security Filter
       ↓
Authenticate user
       ↓
Check role
```

Roles:

``` text
ROLE_CUSTOMER
ROLE_HOTEL_ADMIN
ROLE_DELIVERY_PARTNER
```

Examples:

``` text
Customer:
GET /api/dishes              → allowed

Customer:
POST /api/dishes             → forbidden

Hotel Admin:
POST /api/dishes             → allowed

Delivery Partner:
GET /api/delivery-partners/me → allowed
```

------------------------------------------------------------------------

# 34. Angular Integration

The Angular application will communicate through REST APIs.

Example:

``` text
Angular
   ↓
HttpClient
   ↓
POST /api/auth/login
   ↓
Spring Security
   ↓
JWT returned
```

For later requests:

``` text
Angular HTTP Interceptor
        ↓
Authorization: Bearer JWT
        ↓
Spring Security
        ↓
JWT validation
        ↓
Controller
```

We will implement this ourselves rather than hiding it behind libraries
so that the complete authentication flow is understood.

------------------------------------------------------------------------

# 35. Backend Package Structure

Target structure:

``` text
com.akash.akashhotels
│
├── config
│
├── controller
│
├── dto
│
├── entity
│
├── repository
│
├── service
│   └── impl
│
├── security
│
├── exception
│
├── mapper
│
└── util
```

We will not create all these packages immediately.

They will be introduced when the relevant concept is learned.

------------------------------------------------------------------------

# 36. Layered Architecture

Every request should conceptually flow like:

``` text
Client
  ↓
Controller
  ↓
DTO
  ↓
Service
  ↓
Repository
  ↓
JPA/Hibernate
  ↓
PostgreSQL
```

Example:

``` text
POST /api/dishes
        ↓
DishController
        ↓
DishService
        ↓
DishRepository
        ↓
Hibernate
        ↓
INSERT INTO dishes ...
        ↓
PostgreSQL
```

The controller should not contain business logic.

Business rules belong primarily in the service layer.

------------------------------------------------------------------------

# 37. Validation

Examples:

Dish:

``` text
name → required
price → greater than 0
description → required
```

User:

``` text
email → valid email
password → minimum length
phone → valid format
```

Order:

``` text
quantity → greater than 0
delivery location → required
cart → cannot be empty
```

Spring Validation will be used with DTOs.

------------------------------------------------------------------------

# 38. Exception Handling

We will implement global exception handling.

Examples:

``` text
DishNotFoundException
OrderNotFoundException
DeliveryPartnerNotAvailableException
InvalidOrderStateException
UnauthorizedException
```

Instead of returning random error responses, the API will provide
consistent responses.

Example:

``` json
{
  "timestamp": "...",
  "status": 404,
  "message": "Dish not found",
  "path": "/api/dishes/100"
}
```

------------------------------------------------------------------------

# 39. DTO Design

We will avoid exposing JPA entities directly from controllers.

Example:

``` text
DishRequestDTO
DishResponseDTO
OrderRequestDTO
OrderResponseDTO
LoginRequestDTO
LoginResponseDTO
```

This helps us understand:

``` text
Entity ≠ API contract
```

------------------------------------------------------------------------

# 40. Transaction Management

Important operations will use transactions.

Example placing an order:

``` text
Start transaction
      ↓
Validate cart
      ↓
Calculate amount
      ↓
Create order
      ↓
Create order items
      ↓
Create payment
      ↓
Clear cart
      ↓
Commit
```

If something fails:

``` text
Rollback
```

We will explicitly learn:

-   `@Transactional`
-   Atomicity
-   Commit
-   Rollback
-   Isolation
-   Propagation

------------------------------------------------------------------------

# 41. JPA / Hibernate Concepts We Will Learn

This project will cover:

``` text
@Entity
@Id
@GeneratedValue
@Column
@OneToOne
@OneToMany
@ManyToOne
@JoinColumn
Enum mapping
Cascade
FetchType.LAZY
FetchType.EAGER
orphanRemoval
```

And important Hibernate concepts:

``` text
Persistence Context
EntityManager
Dirty Checking
First-level Cache
Lazy Loading
N+1 Query Problem
```

------------------------------------------------------------------------

# 42. Repository Layer

We will use Spring Data JPA.

Example concept:

``` java
public interface DishRepository
        extends JpaRepository<Dish, Long> {
}
```

Then Spring automatically provides operations such as:

``` text
save()
findById()
findAll()
deleteById()
existsById()
```

Later we will write custom queries.

------------------------------------------------------------------------

# 43. Query Concepts

We will learn:

``` text
Derived Query Methods
JPQL
Native SQL
@Query
Pagination
Sorting
Specifications
```

Example:

``` text
findByAvailableTrue()
findByCategory(...)
findByPriceLessThan(...)
```

------------------------------------------------------------------------

# 44. Database Concepts Covered

The project will teach:

``` text
Primary Keys
Foreign Keys
Unique Constraints
NOT NULL
Indexes
One-to-One
One-to-Many
Many-to-One
Transactions
Isolation
Normalization
Joins
Aggregations
Pagination
```

We will also inspect the SQL generated by Hibernate.

------------------------------------------------------------------------

# 45. API Testing

Before Angular, APIs will be tested using:

``` text
Swagger UI
Postman
```

The development sequence will be:

``` text
Spring Boot
      ↓
REST API
      ↓
Swagger/Postman
      ↓
Verify backend
      ↓
Angular
```

This is important because it separates frontend problems from backend
problems.

------------------------------------------------------------------------

# 46. Testing

Later we will add:

``` text
JUnit 5
Mockito
Spring Boot Test
MockMvc
Integration Tests
Testcontainers
```

We will test:

``` text
Service logic
Controller endpoints
Repository/database behavior
Security
Order placement
Delivery assignment
Earning calculation
```

------------------------------------------------------------------------

# 47. Example End-to-End Scenario

Customer wants a Chicken Biryani.

### Step 1

Angular requests:

``` http
GET /api/dishes
```

Backend returns available dishes.

### Step 2

Customer adds:

``` text
Chicken Biryani × 2
```

### Step 3

Angular sends:

``` http
POST /api/cart/items
```

### Step 4

Customer checks out.

Backend:

``` text
Validate customer
Validate dish
Validate quantity
Get delivery location
Calculate subtotal
Calculate delivery fee
Create order
Create order items
Create payment
```

### Step 5

Hotel sees:

``` text
Order #1001
Chicken Biryani × 2
Location: Adyar
```

### Step 6

Hotel prepares food.

Status:

``` text
PREPARING
```

### Step 7

Food becomes ready.

Status:

``` text
READY_FOR_PICKUP
```

### Step 8

System finds:

``` text
Partner A → BUSY
Partner B → AVAILABLE
Partner C → AVAILABLE
```

Partner B is assigned.

``` text
Partner B → BUSY
Order → ASSIGNED
```

### Step 9

Partner picks up.

``` text
PICKED_UP
```

### Step 10

Partner starts delivery.

``` text
OUT_FOR_DELIVERY
```

### Step 11

Delivery completed in 22 minutes.

Distance:

``` text
7 km
```

Base:

``` text
₹50
```

Bonus:

``` text
₹15
```

Total partner earning:

``` text
₹65
```

### Step 12

Order:

``` text
DELIVERED
```

Partner:

``` text
AVAILABLE
```

------------------------------------------------------------------------

# 48. Development Roadmap

We will build the application in this exact learning order.

## Phase 1 --- Spring Boot Fundamentals

``` text
Spring Boot project
Application properties
Dependency Injection
Beans
Controller
Service
Repository
```

## Phase 2 --- JPA/Hibernate

``` text
Hotel entity
Dish entity
Relationships
Hibernate
Database tables
CRUD
```

## Phase 3 --- REST API

``` text
GET
POST
PUT
PATCH
DELETE
DTOs
Validation
Exception handling
```

## Phase 4 --- Business Logic

``` text
Cart
Orders
Order items
Order lifecycle
Transactions
Delivery assignment
```

## Phase 5 --- Security

``` text
Spring Security
Authentication
Authorization
Password hashing
JWT
Security filters
Roles
```

## Phase 6 --- Angular

``` text
Components
Services
Routing
Forms
HttpClient
Observables
Interceptor
Route guards
JWT integration
```

## Phase 7 --- Advanced Backend

``` text
Pagination
Caching
Redis
Async processing
Kafka basics
Advanced JPA
Query optimization
```

## Phase 8 --- Testing

``` text
JUnit
Mockito
MockMvc
Integration testing
Testcontainers
```

## Phase 9 --- DevOps

``` text
Git
Jenkins
Docker
Docker Compose
AWS
```

## Phase 10 --- Production-Level Improvements

``` text
Logging
Actuator
Prometheus
Grafana
API documentation
Security hardening
Performance
```

------------------------------------------------------------------------

# 49. Future Microservice Version

After understanding the monolithic version, we can evolve it into:

``` text
API Gateway
      ↓
Authentication Service
      ↓
Customer Service
      ↓
Restaurant/Menu Service
      ↓
Order Service
      ↓
Delivery Service
      ↓
Payment Service
```

Then introduce:

``` text
Eureka
Feign Client
API Gateway
Circuit Breaker
Kafka
Docker
Kubernetes
```

This is intentionally a **later phase**.

The objective is to understand why these technologies are needed instead
of simply copying a microservice architecture.

------------------------------------------------------------------------

# 50. Final Learning Objective

By the end of Akash Hotels, you should be able to explain this entire
request without memorizing it:

``` text
Angular
   ↓
HTTP request
   ↓
JWT Interceptor
   ↓
Spring Security Filter
   ↓
Controller
   ↓
DTO Validation
   ↓
Service
   ↓
Business Rules
   ↓
@Transactional
   ↓
Repository
   ↓
JPA
   ↓
Hibernate
   ↓
SQL
   ↓
PostgreSQL
   ↓
Response
   ↓
Angular
```

You should also be able to explain:

``` text
Why DTO?
Why Service layer?
Why Repository?
Why JPA?
Why Hibernate?
Why transactions?
Why lazy loading?
Why JWT?
Why an interceptor?
Why role-based authorization?
Why foreign keys?
Why indexes?
Why Docker?
Why CI/CD?
Why microservices?
```

That is the actual purpose of this project: **not merely finishing an
application, but understanding what is happening at every layer.**

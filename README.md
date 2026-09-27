# Akash Hotels — Full-Stack Food Ordering Platform

A food ordering web application inspired by **Swiggy**, built with **Spring Boot** (Java 17, Spring Security with JWT, PostgreSQL, Spring Data JPA) and **Angular 21** (Standalone Components, Signals).

---

## 🌟 Key Features

### 👤 Customer Experience
- **Interactive Menu**: Filter dishes by category (*Biryani, Tiffin, Starters, Curry, etc.*) with live search and veg/non-veg markers.
- **Cart & Ordering**: Real-time quantity adjustments directly from dish cards, subtotal calculation.
- **Location & Distance Calculation**: Select delivery locations across Chennai with distance-based delivery fees.
- **Live Order Tracking**: Visual 7-step timeline (`PLACED` → `CONFIRMED` → `PREPARING` → `READY_FOR_PICKUP` → `ASSIGNED` → `OUT_FOR_DELIVERY` → `DELIVERED`).
- **Cancellation**: Ability to cancel orders before dispatch.

### ⚙️ Hotel Management (Admin Panel)
- **Real-Time Metrics**: Total orders, active deliveries, and revenue stats.
- **Order Management**: Update status lifecycle and assign orders to available delivery partners.
- **Menu Management**: Full CRUD operations to add, edit, toggle availability, or delete dishes.
- **Partner Fleet Tracking**: Monitor delivery partner availability status (`AVAILABLE`, `BUSY`, `OFFLINE`).

### 🛵 Delivery Partner Portal
- **Profile & Availability**: One-click toggle between `AVAILABLE` and `OFFLINE`.
- **Delivery Lifecycle**: Step-by-step workflow (`Accept` → `Picked Up` → `Mark Delivered`).
- **Earnings & Speed Bonus**: Dynamic calculation of distance-based earnings plus delivery speed bonuses.

---

## 🛠️ Architecture & Tech Stack

```text
Angular 21 (Frontend)
       ↓  (HTTP + JWT Bearer Token)
Spring Boot 4.x / Spring Security (REST API)
       ↓
Service Layer (Business Logic & Transactions)
       ↓
Spring Data JPA / Hibernate
       ↓
PostgreSQL Database
```

| Layer | Technologies |
| :--- | :--- |
| **Frontend** | Angular 21, TypeScript, HTML5, CSS3, Signals |
| **Backend** | Java 17, Spring Boot, Spring Security (JWT), Spring Data JPA |
| **Database** | PostgreSQL |
| **Documentation** | Swagger / OpenAPI (`springdoc-openapi`) |

---

## 🚀 Getting Started

### 1. Database Setup
Ensure PostgreSQL is running locally with database `akash_hotels`:
```sql
CREATE DATABASE akash_hotels;
CREATE USER akash_hotels_user WITH ENCRYPTED PASSWORD 'Akash@123';
GRANT ALL PRIVILEGES ON DATABASE akash_hotels TO akash_hotels_user;
```

### 2. Run Backend
```bash
cd akash-hotels
./mvnw clean spring-boot:run
```
- Server: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

### 3. Run Frontend
```bash
cd akash-hotels-frontend
npm install
npm start
```
- Web Application: `http://localhost:4200`

---

## 👥 Demo Credentials

| Role | Email | Password |
| :--- | :--- | :--- |
| **Customer** | `customer@gmail.com` | `Customer@123` |
| **Admin** | `admin@akashhotels.com` | `Admin@123` |
| **Delivery Partner** | `partner1@akashhotels.com` | `Partner@123` |

# Full-Stack Food Ordering Platform

A food ordering web application inspired by **Swiggy**, built with **Spring Boot** (Java 17, Spring Security with JWT, PostgreSQL, Spring Data JPA) and **Angular 21** (Standalone Components, Signals).

---

## 🌟 Key Features

### 👤 Customer Experience

* **Interactive Menu**: Filter dishes by category (*Biryani, Tiffin, Starters, Curry, etc.*) with live search and veg/non-veg markers.
* **Cart & Ordering**: Adjust quantities directly from dish cards with real-time subtotal calculation.
* **Location & Distance Calculation**: Select delivery locations across Chennai with distance-based delivery fees.
* **Live Order Tracking**: Visual 7-step order timeline:
  `PLACED` → `CONFIRMED` → `PREPARING` → `READY_FOR_PICKUP` → `ASSIGNED` → `OUT_FOR_DELIVERY` → `DELIVERED`
* **Cancellation**: Cancel orders before they are dispatched.

### ⚙️ Hotel Management — Admin Panel

* **Real-Time Metrics**: View total orders, active deliveries, and revenue statistics.
* **Order Management**: Update order status throughout the delivery lifecycle and assign orders to available delivery partners.
* **Menu Management**: Perform full CRUD operations to add, edit, toggle availability, or delete dishes.
* **Partner Fleet Tracking**: Monitor delivery partner availability:
  `AVAILABLE`, `BUSY`, `OFFLINE`

### 🛵 Delivery Partner Portal

* **Profile & Availability**: Toggle availability between `AVAILABLE` and `OFFLINE`.
* **Delivery Lifecycle**: Follow the delivery workflow:
  `Accept` → `Picked Up` → `Mark Delivered`
* **Earnings & Speed Bonus**: Dynamic calculation of distance-based earnings and delivery speed bonuses.

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

| Layer             | Technologies                                                 |
| :---------------- | :----------------------------------------------------------- |
| **Frontend**      | Angular 21, TypeScript, HTML5, CSS3, Signals                 |
| **Backend**       | Java 17, Spring Boot, Spring Security (JWT), Spring Data JPA |
| **Database**      | PostgreSQL                                                   |
| **Documentation** | Swagger / OpenAPI (`springdoc-openapi`)                      |

---

## 🚀 Getting Started

### Prerequisites

Make sure the following are installed before setting up the application:

* Java 17
* Node.js and npm
* Angular CLI
* PostgreSQL
* Git

---

### 1. Database Setup

**Before running the backend, you must create the PostgreSQL database and configure the database connection in `application.properties`.**

Ensure PostgreSQL is running locally, then create the required database and user:

```sql
CREATE DATABASE akash_hotels;
CREATE USER akash_hotels_user WITH ENCRYPTED PASSWORD 'Akash@123';
GRANT ALL PRIVILEGES ON DATABASE akash_hotels TO akash_hotels_user;
```

> **Important:** The database name, username, and password used here must match the PostgreSQL configuration in the backend's `application.properties` file.

After creating the database, open:

```text
akash-hotels/src/main/resources/application.properties
```

Configure the PostgreSQL connection properties with the credentials you created above.

For example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/akash_hotels
spring.datasource.username=akash_hotels_user
spring.datasource.password=Akash@123
```

**Complete this database setup and `application.properties` configuration before running the backend.**

---

### 2. Run the Backend

Navigate to the backend directory:

```bash
cd akash-hotels
```

Then start the Spring Boot application:

```bash
./mvnw clean spring-boot:run
```

On Windows, you can use:

```powershell
.\mvnw.cmd clean spring-boot:run
```

The backend will be available at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

---

### 3. Run the Frontend

Navigate to the frontend directory:

```bash
cd akash-hotels-frontend
```

Install the required dependencies:

```bash
npm install
```

Start the Angular development server:

```bash
npm start
```

The web application will be available at:

```text
http://localhost:4200
```

---

## 🔐 Demo Credentials

| Role                 | Email                      | Password       |
| :------------------- | :------------------------- | :------------- |
| **Customer**         | `customer@gmail.com`       | `Customer@123` |
| **Admin**            | `admin@akashhotels.com`    | `Admin@123`    |
| **Delivery Partner** | `partner1@akashhotels.com` | `Partner@123`  |

---

## 📌 Setup Order

For a successful local setup, follow these steps in order:

```text
1. Install prerequisites
        ↓
2. Start PostgreSQL
        ↓
3. Create the database and PostgreSQL user
        ↓
4. Configure application.properties
        ↓
5. Start the Spring Boot backend
        ↓
6. Install Angular dependencies
        ↓
7. Start the Angular frontend
```

The backend must be connected to the PostgreSQL database before the application can be run successfully.

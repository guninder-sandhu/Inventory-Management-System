# Inventory Management System 🧾

The **Inventory Management System** is a microservices-based application designed to streamline inventory operations for small shops and warehouses. It offers user and product management through modular, scalable services.

## 🧩 Microservices

### 1. `user-service`
- Handles user registration, update, deletion, and retrieval.
- Stores user information in a **MySQL** database.
- RESTful APIs for all user-related operations.

### 2. `product-service`
- Manages product inventory, including creation, updates, and deletion.
- Uses PostgreSQL as its database.
- Communicates with stock-service to retrieve current stock levels for products.
- RESTful APIs to handle product-related data.

### 3. `stock-service`

- Manages the stock levels and quantities of products.
- Uses **MySQL** as its database.
- Provides RESTful APIs for:
    - Retrieving stock levels.
    - Filtering stock based on quantity.
    - Updating stock quantities (overriding or adjusting).
    - Creating new stock entries.
    - Deleting stock entries.

### 4. `service-registry`

- Acts as a **Netflix Eureka Server**, providing a central registry for all microservices within the Inventory
  Management System.
- Enables **service discovery**, allowing microservices to dynamically register themselves and discover the network
  locations of other registered services.
- No dedicated database is typically required as Eureka Server manages its state in memory.
- Provides a web-based dashboard (usually accessible at `http://localhost:8761` by default) for monitoring registered
  services.
  **Note: The Eureka Server should be the first service started before running other services.**

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL & PostgreSQL**
- **Maven**
- **Swagger / OpenAPI** for API documentation
- **Eureka Server** 
- **Spring Cloud Gateway** 

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL & PostgreSQL running locally or in Docker
- IntelliJ or any IDE

##📬 Postman Collection
A ready-to-use Postman collection for testing all microservice endpoints is available in the (https://github.com/guninder-sandhu/Inventory-Management-System/tree/dev/postman) folder.

### Clone the repo

```bash
git clone https://github.com/your-username/Inventory-Management-System.git
cd Inventory-Management-System


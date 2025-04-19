# Inventory Management System 🧾

The **Inventory Management System** is a microservices-based application designed to streamline inventory operations for small shops and warehouses. It offers user and product management through modular, scalable services.

## 🧩 Microservices

### 1. `user-service`
- Handles user registration, update, deletion, and retrieval.
- Stores user information in a **MySQL** database.
- RESTful APIs for all user-related operations.

### 2. `product-service`
- Manages product inventory, including creation, updates, and deletion.
- Uses **PostgreSQL** as its database.
- RESTful APIs to handle product-related data.

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL & PostgreSQL**
- **Maven**
- **Docker** (optional setup)
- **Swagger / OpenAPI** for API documentation
- **Eureka Server** (for service registry - if applicable)
- **Spring Cloud Gateway** (optional)

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL & PostgreSQL running locally or in Docker
- IntelliJ or any IDE

### Clone the repo

```bash
git clone https://github.com/your-username/Inventory-Management-System.git
cd Inventory-Management-System

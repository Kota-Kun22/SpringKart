# SpringKart — E-Commerce Backend API 🛒

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Built with Spring Boot](https://img.shields.io/badge/Built%20with-Spring%20Boot%203-brightgreen)](https://spring.io/projects/spring-boot)
[![Java 21](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/downloads/)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue.svg)](https://www.postgresql.org/)
[![Spring Security](https://img.shields.io/badge/Security-Spring%20Security%206-green)](https://spring.io/projects/spring-security)
[![JWT Authentication](https://img.shields.io/badge/Authentication-JWT-red)](https://jwt.io/)
[![API Docs with Swagger UI](https://img.shields.io/badge/API%20Docs-Swagger%20UI-85EA2D.svg)](http://localhost:8080/swagger-ui/index.html)
[![Maven Build](https://img.shields.io/badge/Build-Maven-blue.svg)](https://maven.apache.org/)

---

A **comprehensive, production-ready backend** for an e-commerce application, **SpringKart** is built with **Spring Boot 3**, **Spring Security 6**, **JWT Authentication**, and **PostgreSQL**. It boasts a **clean architecture**, layered services, and robust security, serving as my deep dive into real-world backend development and deployment best practices.

---

## ✨ Key Features

SpringKart provides a robust set of features to power a full-fledged e-commerce platform, focusing on scalability, security, and maintainability:

* **User Authentication & Role-Based Authorization**
    * **Signup / Signin** with secure **JWT Token generation**.
    * Comprehensive **Role support** for `USER`, `ADMIN`, and `SELLER` roles.
    * **Cookie-based JWT Authentication** for seamless integration with Spring Security.
* **Product Management**
    * **CRUD operations** for products, including detailed product information.
    * **Image upload functionality** for product visuals.
    * Advanced **search capabilities** by keyword and category filtering.
* **Category Management**
    * Full **CRUD support** for product categories.
    * **Pagination, sorting, and filtering** to efficiently manage large datasets.
* **Cart Management**
    * Seamlessly **add products to cart** with specified quantities.
    * **Update quantity** of cart items and **remove products** from the cart.
    * **View cart details** specific to the authenticated user.
* **Order Placement & Management**
    * Ability to **place orders** with multiple products and manage addresses.
    * Designed to **handle payment gateway integration data** (e.g., payment ID, status).
    * Manages **order statuses** throughout the fulfillment process.
* **Address Management**
    * Complete **CRUD operations** for user addresses.
    * **Link addresses with users** for efficient order delivery.
* **Secure JWT Authentication & Stateless Session Handling**
    * Ensures secure API interactions without relying on server-side sessions, enhancing scalability.

---

## 🛠️ Tech Stack

* **Spring Boot 3.x**: Rapid application development and standalone deployment.
* **Spring Security 6.x**: Robust authentication and authorization framework.
* **JWT (JSON Web Token)**: Industry-standard for secure API authentication.
* **JPA/Hibernate**: Powerful ORM for efficient database interactions.
* **PostgreSQL**: Reliable and scalable relational database (local and AWS RDS).
* **Maven**: Project management and build automation tool.
* **Lombok**: Reduces boilerplate code (getters, setters, constructors).
* **ModelMapper**: Simplifies object-to-object mapping (DTOs to entities).
* **SpringDoc (Swagger/OpenAPI)**: Automated API documentation.
* **Java 21**: Latest LTS version for enhanced performance and features.

---

## 🗺️ API Endpoints Overview

While a detailed and interactive API documentation is available via **Swagger UI** when the application is running locally (at `http://localhost:8080/swagger-ui/index.html`), here's a comprehensive overview of the key endpoints and their functionalities:

---

### **Authentication & Authorization**

These endpoints handle user registration, login, logout, and fetching user details. Authentication is cookie-based JWT.

* `POST /api/auth/signup`
    * **Description**: Registers a new user with a username, email, and password. Allows specifying roles (`admin`, `seller`, `user`). If no role is specified, defaults to `USER`.
    * **Request Body**:
        ```json
        {
          "username": "newuser",
          "email": "newuser@example.com",
          "password": "password123",
          "roles": ["user"] // Optional: "admin", "seller"
        }
        ```
    * **Responses**: `200 OK` (User registered successfully) or `400 Bad Request` (Username/Email already taken).

* `POST /api/auth/signin`
    * **Description**: Authenticates a user and generates a JWT token stored in a cookie.
    * **Request Body**:
        ```json
        {
          "username": "user_or_email",
          "password": "password"
        }
        ```
    * **Responses**: `200 OK` with user info and JWT cookie, or `404 Not Found` (BAD CREDENTIALS).

* `POST /api/auth/signout`
    * **Description**: Logs out the current user by clearing the JWT cookie.
    * **Responses**: `200 OK` (Successfully signed out).

* `GET /api/auth/user`
    * **Description**: Retrieves details of the currently authenticated user.
    * **Responses**: `200 OK` with user ID, username, and roles.

---

### **Address Management**

Manages user addresses for order delivery.

* `POST /api/addresses`
    * **Description**: Adds a new address for the currently logged-in user.
    * **Roles**: `USER`, `ADMIN`, `SELLER`
    * **Request Body**:
        ```json
        {
          "street": "123 Main St",
          "buildingName": "Apartment 4B",
          "city": "Lucknow",
          "state": "Uttar Pradesh",
          "country": "India",
          "pinCode": "226001"
        }
        ```
    * **Responses**: `201 Created` with the saved address details.

* `GET /api/addresses`
    * **Description**: Retrieves all addresses in the system.
    * **Roles**: `ADMIN`
    * **Responses**: `200 OK` with a list of all addresses.

* `GET /api/addresses/{addressId}`
    * **Description**: Retrieves a specific address by its ID.
    * **Roles**: `ADMIN`
    * **Responses**: `200 OK` with address details or `404 Not Found`.

* `GET /api/users/addresses`
    * **Description**: Retrieves all addresses associated with the current user.
    * **Roles**: `USER`, `ADMIN`, `SELLER`
    * **Responses**: `200 OK` with a list of the user's addresses.

* `PUT /api/addresses/{addressId}`
    * **Description**: Updates an existing address by ID.
    * **Roles**: `USER`, `ADMIN`, `SELLER`
    * **Request Body**: (Same as `POST /api/addresses` with updated fields)
    * **Responses**: `200 OK` with the updated address details.

* `DELETE /api/addresses/{addressId}`
    * **Description**: Deletes an address by its ID.
    * **Roles**: `USER`, `ADMIN`, `SELLER`
    * **Responses**: `200 OK` with a success message.

---

### **Category Management**

Manages product categories.

* `POST /api/public/categories`
    * **Description**: Creates a new product category.
    * **Roles**: `USER`, `ADMIN`, `SELLER` (Note: `public` in path means accessible, but actual role enforcement is in controller)
    * **Request Body**:
        ```json
        {
          "categoryName": "Electronics"
        }
        ```
    * **Responses**: `201 Created` with the new category details.

* `GET /api/public/categories`
    * **Description**: Retrieves all product categories with pagination, sorting, and filtering options.
    * **Parameters**: `pageNumber` (default `0`), `pageSize` (default `10`), `sort` (`categoryId`, `categoryName`), `sortOrder` (`asc`, `desc`).
    * **Responses**: `200 OK` with a paginated list of categories.

* `PUT /api/admin/categories/{categoryId}`
    * **Description**: Updates an existing category by ID.
    * **Roles**: `ADMIN`
    * **Request Body**:
        ```json
        {
          "categoryName": "Updated Electronics"
        }
        ```
    * **Responses**: `200 OK` with the updated category details.

* `DELETE /api/admin/categories/{categoryId}`
    * **Description**: Deletes a category by its ID.
    * **Roles**: `ADMIN`
    * **Responses**: `200 OK` with the deleted category details.

---

### **Product Management**

Handles product creation, retrieval, updates, and deletion.

* `POST /api/admin/categories/{categoryId}/product`
    * **Description**: Adds a new product to a specified category.
    * **Roles**: `ADMIN`, `SELLER`
    * **Request Body**:
        ```json
        {
          "productName": "Smartphone X",
          "productDescription": "Latest model smartphone with advanced features.",
          "quantity": 100,
          "price": 799.99,
          "discount": 10.0,
          "specialPrice": 719.99
        }
        ```
    * **Responses**: `201 Created` with the saved product details.

* `GET /api/public/product`
    * **Description**: Retrieves all products with pagination and sorting.
    * **Parameters**: `pageNumber` (default `0`), `pageSize` (default `10`), `sort` (`productId`, `productName`, `price`), `sortOrder` (`asc`, `desc`).
    * **Responses**: `200 OK` with a paginated list of products.

* `GET /api/public/categories/{categoryId}/products`
    * **Description**: Retrieves products belonging to a specific category, with pagination and sorting.
    * **Parameters**: `categoryId`, `pageNumber`, `pageSize`, `sort`, `sortOrder`.
    * **Responses**: `200 OK` with a paginated list of products in the specified category.

* `GET /api/public/products/keyword/{keyword}`
    * **Description**: Searches for products by a given keyword in their name or description, with pagination and sorting.
    * **Parameters**: `keyword`, `pageNumber`, `pageSize`, `sort`, `sortOrder`.
    * **Responses**: `200 OK` with a paginated list of matching products.

* `PUT /api/admin/products/{productId}`
    * **Description**: Updates an existing product's details.
    * **Roles**: `ADMIN`, `SELLER`
    * **Request Body**: (Similar to `POST /api/admin/categories/{categoryId}/product` with updated fields)
    * **Responses**: `200 OK` with the updated product details.

* `DELETE /api/admin/products/{productId}`
    * **Description**: Deletes a product by its ID.
    * **Roles**: `ADMIN`, `SELLER`
    * **Responses**: `200 OK` with the deleted product details.

* `PUT /api/products/{productId}/image`
    * **Description**: Uploads or updates the image for a specific product.
    * **Roles**: `ADMIN`, `SELLER`
    * **Request Body**: `multipart/form-data` with `image` file.
    * **Responses**: `200 OK` with the product details including the updated image path.

---

### **Cart Management**

Manages the user's shopping cart.

* `POST /api/carts/products/{productId}/quantity/{quantity}`
    * **Description**: Adds a specified quantity of a product to the current user's cart.
    * **Roles**: `USER`, `ADMIN`, `SELLER`
    * **Responses**: `201 Created` with the updated cart details.

* `GET /api/carts`
    * **Description**: Retrieves all carts in the system.
    * **Roles**: `ADMIN`
    * **Responses**: `200 OK` with a list of all carts.

* `GET /api/carts/user/cart`
    * **Description**: Retrieves the currently logged-in user's shopping cart details.
    * **Roles**: `USER`, `ADMIN`, `SELLER`
    * **Responses**: `200 OK` with the user's cart details.

* `PUT /api/cart/products/{productId}/quantity/{operation}`
    * **Description**: Updates the quantity of a specific product in the current user's cart.
    * **Parameters**: `operation` can be `increase` or `decrease` (or `delete` for full removal).
    * **Roles**: `USER`, `ADMIN`, `SELLER`
    * **Responses**: `200 OK` with the updated cart details.

* `DELETE /api/carts/{cartId}/products/{productId}`
    * **Description**: Removes a specific product from a cart.
    * **Roles**: `USER`, `ADMIN`, `SELLER` (Note: Cart ID usually determined by logged-in user for non-admin)
    * **Responses**: `200 OK` with a success message.

---

### **Order Placement & Management**

Handles the ordering process.

* `POST /api/order/users/payments/{paymentMethod}`
    * **Description**: Places an order for the current user's cart, linking to a specified address and payment details.
    * **Roles**: `USER`, `ADMIN`, `SELLER`
    * **Parameters**: `paymentMethod` (e.g., "CashOnDelivery", "Online").
    * **Request Body**:
        ```json
        {
          "addressId": 1,
          "pgName": "Razorpay",         // Payment Gateway Name (optional)
          "pgPaymentId": "pay_xyz123",  // Payment Gateway Transaction ID (optional)
          "pgStatus": "success",        // Payment Gateway Status (optional)
          "pgResponseMessage": "Payment successful" // Payment Gateway Response (optional)
        }
        ```
    * **Responses**: `201 Created` with the newly placed order details.

---

## ⚙️ Local Setup & Running the Project

Follow these steps to get SpringKart up and running on your local machine:

### 1️⃣ Clone the Repository:

```bash
git clone [https://github.com/yourusername/SpringKart.git](https://github.com/yourusername/SpringKart.git)
cd SpringKart
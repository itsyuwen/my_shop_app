# Seller Service - Shopping Application

## Overview

This project is part of an online shopping application. The service being described here is the **Seller Service**, which allows the seller to manage product listings and view/manage customer orders. It is built using **Spring Boot**, with a layered architecture comprising of **RestController**, **Service**, and **Repository** layers. The seller can perform CRUD operations on products, view and update orders, and manage inventory.

## Technologies Used
- **Spring Boot** - For the backend application framework.
- **Spring Data JPA** - For interacting with the database.
- **Hibernate** - For ORM (Object-Relational Mapping) to handle database persistence.
- **MySQL** - As the relational database.
- **Spring Security + JWT** - For authentication and authorization.
- **Spring AOP** - To handle exception management in the application.
- **Lombok** - To reduce boilerplate code such as constructors and getters/setters.

## Features Implemented
1. **Product Management**:
   - **Add Product**: The seller can add new products to the listing by providing details such as name, description, wholesale price, retail price, and available quantity.
   - **Update Product**: The seller can update product details, including description, pricing, and stock quantity.

2. **Order Management**:
   - **View Orders**: The seller can view all customer orders, with the ability to paginate through the orders.
   - **View Order Details**: The seller can view detailed information for each specific order, including items and their quantities.
   - **Update Order Status**: The seller can update the status of orders, changing them between "Processing", "Completed", and "Canceled".

3. **Inventory Management**:
   - Automatically adjusts product quantities when orders are placed or canceled. If an order is canceled, the stock is restored accordingly.

4. **Order Validation**:
   - Prevents invalid status transitions, ensuring that a completed order cannot be changed to processing or canceled.

```

## Endpoints

| **HTTP Method** | **Endpoint**                   | **Description**                                           |
|-----------------|---------------------------------|-----------------------------------------------------------|
| POST            | `/products`                    | Adds a new product to the listing.                         |
| PUT             | `/products/{productId}`         | Updates the product details for the given product ID.      |
| GET             | `/orders?page={page}&size={size}` | Retrieves all orders with pagination.                      |
| GET             | `/orders/{orderId}`             | Retrieves detailed information about a specific order.     |
| PATCH           | `/orders/{orderId}/status`      | Updates the status of an order.                            |

## How to Run the Project

1. **Prerequisites**:
   - Java 17 or higher
   - MySQL database
   - Maven for dependency management
   - Postman for API testing

2. **Setup the Database**:
   - Ensure MySQL is installed and running.
   - Create a new database, e.g., `shopapp`.
   - Configure the database connection in the `application.properties` file located in the `src/main/resources` directory:
     ```
     spring.datasource.url=jdbc:mysql://localhost:3306/shopapp
     spring.datasource.username=root
     spring.datasource.password=yourpassword
     spring.jpa.hibernate.ddl-auto=update
     spring.jpa.show-sql=true
     ```

3. **Build and Run**:
   - Use Maven to build the project:
     ```
     mvn clean install
     ```
   - Run the Spring Boot application:
     ```
     mvn spring-boot:run
     ```

4. **Testing with Postman**:
   - Use Postman to test the API endpoints provided in the table above. Ensure to first register/login as a seller if authentication is enabled (depending on your setup with Spring Security and JWT).

## Further Development
This service is part of a larger shopping platform and can be integrated with other microservices or extended further:
- **Product Search**: Add a search feature for products based on filters (e.g., name, price range).
- **Order Analytics**: Provide analytical reports, such as the best-selling products, total sales, etc.
- **Email Notifications**: Implement email notifications for order updates.

## Contributors
- **Developer**: Yuwen Yuan


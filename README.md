# E-commerce API

This repository contains a RESTful API for an e-commerce platform developed using Java and the Spring Framework. The API provides features for managing products, user authentication, and supports CRUD operations, along with unit tests to ensure code reliability.

## Features

- **Product Management**:
  - Create new products.
  - List all products.
  - Retrieve product details by ID.
  - Update existing product information.
  - Delete products.

- **User Authentication**:
  - Secure access using **Spring Security**.
  - Role-based access control for endpoints.

- **Unit Testing**:
  - Comprehensive test cases to validate functionality and ensure reliability.

## Technologies Used

- **Java**
- **Spring Boot**
- **Spring Security**
- **JUnit** (for unit testing)
- **Maven** (for dependency management)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8.1 or higher
- Docker (optional, for containerized deployment)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/MatheusFidelisPE/Ecommerce_API.git
   cd Ecommerce_API
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### API Endpoints

#### Product Endpoints

| Method | Endpoint          | Description                   |
|--------|-------------------|-------------------------------|
| GET    | `/api/products`   | List all products             |
| GET    | `/api/products/{id}` | Get product details by ID     |
| POST   | `/api/products`   | Create a new product          |
| PUT    | `/api/products/{id}` | Update an existing product    |
| DELETE | `/api/products/{id}` | Delete a product by ID        |

#### Authentication Endpoints

| Method | Endpoint          | Description                   |
|--------|-------------------|-------------------------------|
| POST   | `/api/auth/login` | Authenticate user and get token |

### Security

- Authentication is implemented using **Spring Security**.
- Users must log in to access secure endpoints.
- JWT (JSON Web Token) is used for session management.

### Running Tests

Execute unit tests using Maven:
```bash
mvn test
```

## Contact

For questions or suggestions, feel free to reach out to [Matheus Fidelis](https://github.com/MatheusFidelisPE).

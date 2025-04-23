# IE303 - ClothingStore Backend Project
This repository contains the backend for a clothing store application with VNPay payment integration.
## Prerequisites
- Java 17
- MongoDB Atlas account (connection is pre-configured)
- Gradle
## How to Run the Project
### Method 1: Using Gradle
    ./gradlew bootRun
### Method 2: Using IntelliJ IDEA
1. Open the project in IntelliJ IDEA.
2. Make sure you have the necessary dependencies installed.
3. Run the main class `com.example.clothingstore.ClothingStoreApplication` to start the application.
### Method 3: Build and Run JAR
    ./gradlew build
    java -jar build/libs/BE_ClothingStore-0.0.1-SNAPSHOT.jar
## Key Features
- Spring Boot 3.4.3 backend
- MongoDB Atlas database integration
- RESTful API endpoints
- JWT authentication
- VNPay payment gateway integration
- Cloudinary for image hosting
## API Endpoints
  ### Orders
  - POST /customer/orders/create/{userId} - Create a new order
  ### Payments
  - POST /customer/payments - Submit payment for an order via VNPay
  - GET /customer/vnpay-payment - Handle VNPay payment callback
## Environment Configuration
- The application is pre-configured with:
  - MongoDB Atlas connection string
  - JWT security settings
  - VNPay sandbox configuration
  - Cloudinary credentials
## Note
- The application runs on port 8080 by default
- VNPay is configured in sandbox mode for testing
- Internet connection is required to access MongoDB Atlas 
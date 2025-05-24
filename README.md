# Electronics Ecommerce Project

This is a full-stack ecommerce project developed using Java Spring Boot for the backend and a combination of HTML, CSS, Bootstrap, JavaScript, and Thymeleaf for the frontend. This project was developed in Eclipse IDE.

The application is deployed on Render and can be accessed at: [https://prince-electronics-ecommerce-3.onrender.com](https://prince-electronics-ecommerce-3.onrender.com)

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This is a comprehensive ecommerce platform that allows users to browse and purchase electronic products online. It provides a user-friendly interface for customers to explore products, add them to their cart, and complete the checkout process. The project is built on a Java Spring Boot backend, which provides the necessary APIs for managing products, users, and orders.

## Features

- **ROLE BASED:** Access is restricted based on roles: ADMIN and USER. i.e: Users with ADMIN role, are directed to dashboard upon login, while users with role USER are directed to home page to help them navigate through products and shop accordingly.
- **Upon Registration:** You are able to register as an ADMIN or a USER/CUSTOMER
- **USERNAME CREATION:** Usernames must be in small letters, no spaces.
- **User Registration and Authentication:** Users can create accounts and log in to access ecommerce features.
- **Browse and Search Products:** Users can browse products, search for specific items, and view product details.
- **Product Details and Reviews:** Users can access detailed information about products and read reviews.
- **Shopping Cart Management:** Products can be added to the shopping cart, and users can manage cart contents.
- **Secure Checkout Process:** Users can proceed to checkout, enter shipping information, and complete orders.
- **Order History and Tracking:** Users can view their order history and track the status of their orders.
- **Admin Dashboard:** Admin users can manage products and user accounts through the admin dashboard.

## Technologies Used

### Backend:

- Java Spring Boot
- Spring Security for authentication and authorization
- Spring Data JPA for data access
- MySQL database for data storage

### Frontend:

- HTML, CSS, JavaScript for the user interface
- Bootstrap for responsive design
- Thymeleaf for server-side templating

### Development Tools:

- INTELLIJ IDEA for Java development
- Git for version control
- GitHub for code hosting

### INSTALLATION :

- Navigate on **code** and download the project's zip file
- Extract the zip file in your file explorer
- Open the extracted folder with INTELLIJ IDEA(Recommended)
- Navigate to the terminal
- **RUN:** 'mvn clean install' && 'mvn spring-boot:run'
- In your browser run: 'localhost:8084' and get started.
- Go to application.properties and change the database credentials to run the web app locally


## Deployment
The application is deployed on Render and can be accessed at: [https://prince-electronics-ecommerce-3.onrender.com](https://prince-electronics-ecommerce-3.onrender.com)
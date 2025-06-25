# Fraud Detection Service

## Overview

The Fraud Detection Service is a Spring Boot application designed to analyze financial transactions and detect potential fraud using a set of predefined rules. The application leverages multithreading for high performance under load and integrates with Kafka for anomaly event handling.

## Features

- **Transaction Analysis**: Evaluates transactions against a set of fraud rules.
- **Multithreading**: Utilizes Java's concurrency utilities to enhance performance.
- **Kafka Integration**: Publishes anomaly events to a Kafka topic.
- **Caching**: Implements caching strategies to optimize rule retrieval.
- **Custom Exception Handling**: Provides detailed error management with custom exceptions.

## Technologies Used

- **Java**: Core programming language.
- **Spring Boot**: Framework for building the application.
- **Kafka**: Used for messaging and event handling.
- **JUnit**: For testing the application.
- **SLF4J & Logback**: For logging.
- **ExecutorService**: For managing multithreading.

## Setup and Installation

### Prerequisites

- Java 11 or higher
- Apache Kafka
- Maven

### Installation Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/fraud-detection-service.git
   cd fraud-detection-service

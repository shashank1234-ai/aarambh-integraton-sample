# Aarambh Integration Sample - Spring Boot

This is a Spring Boot implementation of the Aarambh Integration sample application. It provides REST endpoints for settlement, reporting, and reconciliation operations.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Configuration

Configure the following properties in `src/main/resources/application.properties`:

```properties
aarambh.unique-key=your_unique_key
aarambh.subscriber-id=your_subscriber_id
aarambh.private-key=your_private_key
aarambh.access-key=your_access_key
aarambh.secret-key=your_secret_key
aarambh.sa-endpoint=your_sa_endpoint
```

## Building the Application

```bash
mvn clean package
```

## Running the Application

```bash
java -jar target/integration-0.0.1-SNAPSHOT.jar
```

The application will start on port 8000.

## Available Endpoints

- POST `/v1/settle` - Settle a transaction
- POST `/v1/report` - Get settlement report
- POST `/v1/recon` - Reconciliation
- POST `/v1/on_recon` - On reconciliation
- POST `/v1/on_settle` - On settlement
- POST `/v1/on_report` - On report

## Request Headers

For `/v1/recon` and `/v1/on_recon` endpoints, you can optionally include a `requesttype` header to specify whether it's an INBOUND or OUTBOUND request.

## Response Format

All endpoints return a JSON response with appropriate status codes. 
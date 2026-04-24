# Smart Campus API

## Overview
The Smart Campus API is a JAX-RS based RESTful web service designed to manage campus infrastructure, including rooms, sensors, and their readings. It provides a lightweight, in-memory solution for monitoring environmental data across a university campus.

## Build & Run Instructions

### Prerequisites
*   Java JDK 21
*   Apache Maven

### Installation
1. Clone the repository to your local machine.
2. Build the project:
   ```bash
   mvn clean install
   ```

### Running the API
Start the Grizzly HTTP server:
```bash
mvn exec:java
```
The API will be available at: `http://localhost:8080/api/v1/`

---

## Sample Curl Commands for Testing

### 1. API Discovery
Get an overview of the API version and available resources.
```bash
curl -X GET http://localhost:8080/api/v1/
```

### 2. Create a Room
Add a new room to the campus data store.
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
     -H "Content-Type: application/json" \
     -d '{"id": "L1", "name": "Main Library", "capacity": 100}'
```

### 3. Register a Sensor
Register a sensor and link it to an existing room.
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
     -H "Content-Type: application/json" \
     -d '{"id": "S1", "type": "CO2", "status": "ACTIVE", "roomId": "L1"}'
```

### 4. Add a Sensor Reading
Submit a new data point for a specific sensor.
```bash
curl -X POST http://localhost:8080/api/v1/sensors/S1/read \
     -H "Content-Type: application/json" \
     -d '{"id": "READ-01", "timestamp": 1713959008, "value": 45.0}'
```

### 5. Filter Sensors by Type
Retrieve all registered sensors of a specific type.
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=CO2"
```

# Smart Campus API

## Overview
The Smart Campus API is a JAX-RS based RESTful web service designed to manage campus infrastructure, including rooms, sensors, and their readings. It provides a lightweight, in-memory solution for monitoring environmental data across a university campus.

## Technology Stack
*   **Language:** Java 21
*   **Framework:** Jersey 2.39.1 (JAX-RS)
*   **Namespace:** Java EE 8 (javax.*)
*   **Server:** Apache Tomcat 9.0
*   **Build Tool:** Maven
*   **JSON:** Jackson

## Build & Run Instructions

### Prerequisites
*   Java JDK 21
*   Apache Maven
*   Apache Tomcat 9.x

### Running the API (NetBeans)
1.  Open the project in NetBeans.
2.  Right-click the project > **Properties** > **Run**.
3.  Ensure **Server** is set to **Apache Tomcat 9.0**.
4.  Ensure **Context Path** is set to `/SmartCampusAPI`.
5.  Ensure **Relative URL** is set to `api/v1/`.
6.  Click the **Run** button.

The API will be available at: `http://localhost:8080/SmartCampusAPI/api/v1/`

---

## Sample API Calls (Postman / Curl)

Note: All URLs assume the base is `http://localhost:8080/SmartCampusAPI/api/v1/`

## Sample Curl Commands for Testing

### 1. API Discovery
Get an overview of the API version and available resources.
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/
```

### 2. Create a Room
Add a new room to the campus data store.
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
     -H "Content-Type: application/json" \
     -d '{"id": "L1", "name": "Main Library", "capacity": 100}'
```

### 3. Register a Sensor
Register a sensor and link it to an existing room.
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
     -H "Content-Type: application/json" \
     -d '{"id": "S1", "type": "CO2", "status": "ACTIVE", "roomId": "L1"}'
```

### 4. Add a Sensor Reading
Submit a new data point for a specific sensor.
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/S1/read \
     -H "Content-Type: application/json" \
     -d '{"id": "READ-01", "timestamp": 1713959008, "value": 45.0}'
```

### 5. Filter Sensors by Type
Retrieve all registered sensors of a specific type.
```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2"
```
---

## Report Answers
### Part 1
#### Q1: Default lifecycle of a JAX-RS Resource class.
Answer: The default configuration is that JAX-RS resource classes are request-scoped, which implies that a new instance is created on each incoming HTTP request and destroyed as soon as the response has been sent. Since resources are not persistent between requests, they are not able to store state in instance variables. A Singleton pattern is needed on the data storage layer in order to guarantee the data persistence. With multiple threads, this requires thread-safe data structures like ConcurrentHashMap or synchronized blocks to avoid a race condition and maintain data integrity when two or more request threads access the in-memory store at the same time.

#### Q2: Why is Hypermedia (HATEOAS) a hallmark of advanced design?
Answer: Hypermedia (HATEOAS) enables an API to be self-describing by supplying dynamic links that indicate the client what transitions are allowed. This provides a loose binding between the client and the URI hierarchy of the server, so that in case internal pathing changes, the client will be able to work by finding out the new URLs on the fly instead of depending on predefined endpoints. This is better than pure documentation since it gives a real time single source of truth of actions available depending on the current state of the resource, greatly lowering the maintenance burden and risk of client-side breaking changes.

### Part 2
#### Q1: Implications of returning only IDs vs full room objects.
Answer: Retrieving only IDs reduces network bandwidth and increases the response time. But it makes the client-side processing more complex since the client will need to make several follow-up requests to get the information about each ID. Returning whole objects is more complex at first but results in fewer requests overall to render a UI.

#### Q2: Is the DELETE operation idempotent?
Answer: Yes, the implementation is idempotent. In REST, an operation is idempotent if multiple identical requests have the same effect on the server state. The initial DELETE command deletes the room. The same and immediately followed DELETE requests will get a response showing that the room has already disappeared, the response code may be different (not 204 (No Content) but 404 (Not Found)) but the condition of the server (the room is not there) is the same.

### Part 3
#### Q1: Technical consequences of sending data in a non-JSON format (e.g., text/plain).
Answer: Since the method is explicitly annotated with @Consumes(MediaType.APPLICATION_JSON), JAX-RS will reject any request with a different Content-Type header. The server will return an HTTP 415 Unsupported Media Type error. The JAX-RS runtime will not attempt to parse the body, preventing the resource method from executing.

#### Q2: Contrast QueryParam filtering v. Path parameters. 
Answer: Path parameters (e.g., sensors/type/CO2) are normally employed to indicate a particular resource or a sub-resource. Query parameters on the other hand, are conventional to filtering, searching, or sorting collections. The QueryParam method is better in this case since it leaves the URL structure uncluttered; you can combine several filters (e.g., ?type=CO2&status=ACTIVE) without having to generate very deeply nested, incomprehensible URLs.

### Part 4
#### Q1: Discuss the architectural benefits of the Sub-Resource Locator pattern. 
Answer: The Sub-Resource Locator pattern can allow a more effective isolating of concerns by the transfer of logic of nested resources (such as readings) to a special class. This will avoid the main SensorResource becoming a large controller that is hard to manage. It also renders the API hierarchy more logical and reflects the physical hierarchy of the campus infrastructure.

### Part 5
#### Q1: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?
Answer: The Sub-Resource Locator pattern can allow a more effective isolating of concerns by the transfer of logic of nested resources (such as readings) to a special class. This will avoid the main SensorResource becoming a large controller that is hard to manage. It also renders the API hierarchy more logical and reflects the physical hierarchy of the campus infrastructure.

#### Q2: Explain the cybersecurity risks of exposing internal Java stack traces and what specific information an attacker could gather.
Answer: It is a major security threat to expose internal stack traces to external consumers since it shows the internal layout of the application architecture. Sensitive information that an attacker may collect includes internal package layouts, class names, files that are located on the server and the exact versions of libraries (e.g., Jersey or Jackson) in use. This data can be used in footprinting, to determine which library version is known to be vulnerable or to trace the logic of an application to make specific exploits.

#### Q3: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging instead of manual statements?
Answer: JAX-RS filters can centrally apply cross-cutting concerns, meaning that logging logic is centrally applied throughout the API and not repeated in each resource method. This methodology maintains business logic pure and directed towards its main task. Moreover, it makes maintenance easier; when the logging format must be modified, it would only have to do so in a single class, as opposed to dozens of individual methods.


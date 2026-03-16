# Exchange Rate API

---

## Running with Docker

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.

### 1. Clone the repository
```bash
git clone <repository-url>
cd exchange-rate-chal
```

### 2. Build and run
```bash
docker-compose up --build
```

### To stop
```bash
docker-compose down
```

---

## Authentication

All `/api/rates` and `/api/convert` endpoints require a valid JWT token.

### 1. Register a new user
```
POST /api/auth/register
```
```json
{
  "username": "yourusername",
  "password": "yourpassword"
}
```

Your username is now registered. You can use these credentials to log in and obtain a JWT token for authenticated requests.

### 2. Login
```
POST /api/auth/login
```
```json
{
  "username": "yourusername",
  "password": "yourpassword"
}
```

The response will contain a JWT token that you can use for authenticated requests to the `/api/rates` and `/api/convert` endpoints.

### 3. Use the token in the requests
Include the token in the `Authorization` header for all protected requests:
```
Authorization: Bearer <your_token>
```

---

## API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/auth/register` | Register a new user | x             |
| `POST` | `/api/auth/login` | Login and receive JWT token | x             |

### Exchange Rate

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/api/rates/{from}/{to}` | Get exchange rate from A to B | ✓           |
| `GET` | `/api/rates/{from}` | Get all exchange rates for currency A | ✓           |
| `GET` | `/api/rates/convert/{from}/{to}?amount=X` | Convert amount from A to B | ✓           |
| `GET` | `/api/rates/convert/{from}/batch?amount=X&targets=A,B,C` | Convert amount from A to multiple currencies | ✓           |


---

## Additional Interfaces

| Interface | URL | Description |
|-----------|-----|-------------|
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` | REST API documentation and testing |
| GraphiQL | `http://localhost:8080/graphiql` | GraphQL interactive interface |
| Actuator Caches | `http://localhost:8080/actuator/caches` | Active cache information |


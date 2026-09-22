# StockTrack

A RESTful web application for automated stock price monitoring. It combines scheduled market data retrieval from an external API with custom alerts delivered via external webhooks (Discord, Slack).

Built with a Spring Boot backend featuring stateless JWT authentication, strict user data isolation, and a Vanilla JavaScript frontend.



## Getting Started

### Prerequisites

* Docker Desktop
* Java 21+ *(only if running locally outside Docker)*
* A valid [Finnhub API key](https://finnhub.io/)

### Run the Application

The recommended way to run the application is via Docker.

1. Create a `.env` file in the project root and add the required values:

```env
FINNHUB_API_KEY=your_api_key
JWT_SECRET_KEY=your_secret_key
```

2. Build and start the containers:

```bash
docker compose up --build
```

The application will be available at:

http://localhost:8080

First time using the application? Click Register on the login page to create a new account.

PostgreSQL will be available at:

`localhost:5433`

## Configuration

### Finnhub API

The application uses Finnhub as its external financial data provider. A valid API key must be provided through the `FINNHUB_API_KEY` environment variable.

### JWT Secret

The application requires a secure secret for signing JWT tokens.

A suitable 32-byte hexadecimal key can be generated using OpenSSL:

```bash
openssl rand -hex 32
```

Set the generated value as `JWT_SECRET_KEY` in the `.env` file.


## Main Features

* **Automated Price Monitoring** — Scheduled market data retrieval every 10 seconds with evaluation of user-defined price thresholds.
* **Webhook Notifications** — Alerts delivered via external webhooks when target prices are reached.
* **Multiple Webhooks** — Users can configure multiple notification webhooks for flexible alert delivery.
* **Secure Authentication** — Stateless JWT-based registration and login.
* **User Data Isolation** — Users can only access and modify their own alerts and webhooks.


<img width="90%" height="90%" alt="image" src="https://github.com/user-attachments/assets/e82a8bf4-0d8c-4312-85c2-948c0d25cb37" />


<!-- Add database schema image here -->

## Technologies

* **Backend:** Java, Spring Boot, REST API
* **Security:** Spring Security, JWT
* **Data Layer:** PostgreSQL, Spring Data JPA, Hibernate
* **Frontend:** Vanilla JavaScript (ES6), HTML5, CSS3
* **DevOps & Testing:** Docker, GitHub Actions, JUnit 5, Mockito

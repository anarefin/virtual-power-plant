# Virtual Power Plant System

The Virtual Power Plant System is a cloud-based energy provider that aggregates distributed power sources into a single platform. It provides a REST API for managing batteries and retrieving statistics based on postcode ranges.

## Technologies Used

- Java
- Spring Boot
- Spring Web
- Maven
- In-memory Database (H2)
- Docker
- Docker Compose

## Getting Started

These instructions will help you set up and run the Virtual Power Plant System on your local machine.

### Prerequisites

To run this project, you need to have the following installed on your machine:

- Java Development Kit (JDK) 17 or higher
- Docker (for running with Docker)

### Running Locally

Follow these steps to run the project locally:

1. Clone the repository:

   ```shell
   git clone https://github.com/anarefin/virtual-power-plant.git
   cd virtual-power-plant
   ```

2. Build and run the application using Maven wrapper:

   ```shell
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

   The application will start on `http://localhost:8080`.

### Running with Docker

To run the application using Docker, make sure you have Docker installed on your machine. Then, follow these steps:

1. Build the Docker image:

   ```shell
   docker build -t virtual-power-plant .
   ```

2. Start the application and the database container using docker-compose:

   ```shell
   docker-compose up
   ```

   The application will start on `http://localhost:8080`.

## API Endpoints

The following API endpoints are available:

- **POST /api/batteries**: Create new batteries. Accepts a list of batteries in the request body.

- **GET /api/batteries**: Retrieve batteries within a given postcode range. Accepts `startPostcode` and `endPostcode` as query parameters.

Refer to the API documentation or Postman collection for detailed usage examples.

## Testing

To run the automated tests, execute the following command:

```shell
./mvnw test
```

The tests include unit tests and integration tests for the API endpoints.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

- This project was developed as part of the virtual power plant system.


Feel free to update the README file with relevant information specific to your project, such as installation instructions, API documentation, deployment instructions, and additional details about the project's functionality or architecture.

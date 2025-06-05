# Project Title (Replace with your project's name)

This project is a Spring Boot application designed to be run with Docker and Docker Compose, using a PostgreSQL database.

## Prerequisites

- Docker ([Install Docker](https://docs.docker.com/get-docker/))
- Docker Compose ([Install Docker Compose](https://docs.docker.com/compose/install/))

## Getting Started

### 1. Configuration

-   **`Dockerfile`**:
    -   This file is set up for a Maven-based Spring Boot application using Java 17.
    -   It assumes your application's JAR file is produced in the `target/` directory (e.g., `target/my-app-0.0.1-SNAPSHOT.jar`).
    -   If you use Gradle, you will need to modify the build stage in the `Dockerfile`. A commented-out example for Gradle is provided.
    -   If your JAR file name or path is different, adjust the `ARG JAR_FILE` line or pass it as a build argument.

-   **`docker-compose.yml`**:
    -   **Database Credentials**: You **MUST** update the placeholder database credentials for both the `app` service (Spring Boot) and the `db` service (PostgreSQL).
        -   In the `app` service environment variables:
            -   `SPRING_DATASOURCE_URL`: Ensure the database name (e.g., `mydatabase`) matches.
            -   `SPRING_DATASOURCE_USERNAME`: Set your desired username.
            -   `SPRING_DATASOURCE_PASSWORD`: Set your desired password.
        -   In the `db` service environment variables:
            -   `POSTGRES_DB`: Must match the database name used in `SPRING_DATASOURCE_URL`.
            -   `POSTGRES_USER`: Must match `SPRING_DATASOURCE_USERNAME`.
            -   `POSTGRES_PASSWORD`: Must match `SPRING_DATASOURCE_PASSWORD`.
    -   **Application Port**: The application is exposed on port `8080`. If your Spring application runs on a different port, update the `EXPOSE` instruction in `Dockerfile` and the `ports` section in `docker-compose.yml`.

### 2. Build and Run

Once you have configured the files as needed, open your terminal in the project root directory (where `Dockerfile` and `docker-compose.yml` are located) and run:

```bash
docker-compose up --build
```

-   `--build`: This flag tells Docker Compose to build the image for your application (defined in `Dockerfile`) before starting the services. If you make changes to your application code or `Dockerfile`, use this flag to rebuild.
-   The first time you run this, Docker will download the `eclipse-temurin` and `postgres` images if you don't have them locally, which might take some time.

### 3. Accessing the Application

Once the containers are up and running, your Spring Boot application should be accessible at:
`http://localhost:8080` (or the host port you mapped in `docker-compose.yml`).

### 4. Accessing the Database (Optional)

If you need to connect to the PostgreSQL database directly from your host machine (e.g., using a SQL client like pgAdmin or DBeaver), you can connect to `localhost` on port `5432` (or the host port you mapped) using the credentials you set in `docker-compose.yml`.

## Stopping the Application

To stop the application and remove the containers, press `Ctrl+C` in the terminal where `docker-compose up` is running, and then run:

```bash
docker-compose down
```

This command stops and removes the containers, networks, and the default anonymous volumes. The named volume `postgres_data` (which stores your database data) will persist.

To stop the application and remove all volumes, including the `postgres_data` volume (your database data will be lost!):

```bash
docker-compose down -v
```

## Project Structure Overview

```
.
├── .mvn/                   # Maven wrapper files
├── src/                    # Your application source code
├── Dockerfile              # Defines how to build the application's Docker image
├── docker-compose.yml      # Defines the services (app, db), networks, and volumes
├── mvnw                    # Maven wrapper script (Linux/macOS)
├── mvnw.cmd                # Maven wrapper script (Windows)
├── pom.xml                 # Maven project file (or build.gradle for Gradle)
└── README.md               # This file
```

## Further Customization

-   **Spring Profiles**: You can activate specific Spring profiles by setting the `SPRING_PROFILES_ACTIVE` environment variable in the `app` service within `docker-compose.yml` (e.g., `SPRING_PROFILES_ACTIVE=docker,prod`).
-   **Java Options**: To pass additional JVM options to your Spring Boot application, you can modify the `ENTRYPOINT` in the `Dockerfile` or use an environment variable like `JAVA_OPTS` if your entrypoint script supports it.
    Example in `Dockerfile`:
    `ENTRYPOINT ["java", "-Xmx512m", "-jar", "application.jar"]`

Remember to replace placeholder values (like `mydatabase`, `myuser`, `mypassword`, and `Project Title`) with your actual project details.

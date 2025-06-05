# Stage 1: Build the application (assumes Maven)
# If you use Gradle, you'll need to adapt this stage.
# Example for Gradle: COPY build.gradle settings.gradle gradlew ./ ; COPY gradle ./gradle ; RUN ./gradlew build -x test
FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app

# Copy Maven wrapper and pom.xml to leverage Docker cache for dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Download dependencies
RUN ./mvnw dependency:resolve

# Copy source code
COPY src ./src

# Build the application, skipping tests for faster image build
RUN ./mvnw package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Argument for the JAR file name/path. Default assumes JAR in target/ directory.
# You can override this during docker build with --build-arg JAR_FILE=path/to/your.jar
ARG JAR_FILE=target/*.jar

# Copy the JAR from the builder stage
COPY --from=builder /app/${JAR_FILE} application.jar

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "application.jar"]

# Stage 1: Build the Quarkus application
FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app

# Copy the pom.xml file and source code into the container
COPY ../../../pom.xml .
COPY ../.. ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Setup the runtime environment
FROM registry.access.redhat.com/ubi8/openjdk-21:1.18
ENV LANGUAGE='en_US:en'

# Set the working directory in the runtime environment
WORKDIR /app

# Copy the compiled application from the build stage
COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

# Expose the correct port
EXPOSE 10000

# User and environment variables
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Dquarkus.http.port=10000 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
# Stage 1: Build the Quarkus application
FROM maven:3.8.1-openjdk-21 AS build
WORKDIR /app

# Copy the pom.xml file and source code into the container
COPY ../../../pom.xml .
COPY ../.. ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Setup the runtime environment
FROM registry.access.redhat.com/ubi8/openjdk-17:1.18
ENV LANGUAGE='en_US:en'
ENV APP_DB_HOST='jdbc:postgresql://dpg-cnmu0h021fec73992ff0-a.oregon-postgres.render.com:5432/saas_r51o'
ENV APP_DB_PASSWORD='xWVnTDxr2HcZlgzGqx2kzgTB0sjz8KQt'
ENV APP_DB_USER='vsviniciuslima'

# Set the working directory in the runtime environment
WORKDIR /app

# Copy the compiled application from the build stage
COPY --from=build /app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /app/target/quarkus-app/*.jar /deployments/
COPY --from=build /app/target/quarkus-app/app/ /deployments/app/
COPY --from=build /app/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
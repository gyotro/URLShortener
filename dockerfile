# Build stage
FROM gradle:8-jdk21 as builder
WORKDIR /app
COPY . .
RUN gradle build -x test

# Run stage
FROM gradle:8-jdk21
EXPOSE 8080
WORKDIR /app
COPY --from=builder /app/build/libs/shortURL-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# FROM alpine/java:21-jdk
# EXPOSE 8080
# COPY ./build/libs/shortURL-0.0.1-SNAPSHOT.jar /usr/app/
# WORKDIR /usr/app
# ENTRYPOINT ["java", "-jar", "shortURL-0.0.1-SNAPSHOT.jar"]
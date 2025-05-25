FROM maven:3.9-eclipse-temurin-21  AS builder
LABEL authors="guninder sandhu" description="dashboard-service in IMS"

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21

WORKDIR app

COPY --from=builder /build/target/dashboard-service-0.0.1-SNAPSHOT.jar app.jar

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
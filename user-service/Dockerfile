FROM maven:3.9-eclipse-temurin-21  AS builder
LABEL authors="guninder sandhu" description="userservice in IMS"

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21

WORKDIR app

COPY --from=builder /build/target/user-service-0.0.1-SNAPSHOT.jar app.jar

# Download and set up wait-for-it.sh
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Use wait-for-it.sh as ENTRYPOINT and pass the java command as arguments
ENTRYPOINT ["/wait-for-it.sh", "mysqldb:3306", "--timeout=60", "--", "java", "-jar", "/app/app.jar"]
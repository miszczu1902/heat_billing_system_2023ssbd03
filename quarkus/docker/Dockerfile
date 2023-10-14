FROM maven:3.8.4-openjdk-17-slim
WORKDIR /2023ssbd03
COPY ../pom.xml /2023ssbd03/pom.xml
COPY ../src /2023ssbd03/src
RUN mvn clean package -DskipTests=true && mvn quarkus:dev


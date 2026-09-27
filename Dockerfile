# Maven with JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
LABEL authors="EduJournal Team"

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

CMD ["java", "-cp", "target/edujournal-frontend-0.1.0.jar", "com.edujournal.Main"]
# Maven with JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
LABEL authors="EduJournal Team"

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

RUN apt-get update && apt-get install -y --no-install-recommends \
    xvfb \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgtk-3-0 \
    libgl1-mesa-glx \
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

CMD ["xvfb-run", "--auto-servernum", "java", "-cp", "target/edujournal-frontend-0.1.0.jar", "com.edujournal.Launcher"]
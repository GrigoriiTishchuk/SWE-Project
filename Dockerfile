# --- Stage 1: Building JAR-file by Maven ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- Этап 2: Preparing GUI environment of JavaFX ---
FROM eclipse-temurin:21-jdk
LABEL authors="EduJournal Team"
WORKDIR /app

# Installing graphical and X11 libraries
RUN apt-get update && apt-get install -y --no-install-recommends \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgtk-3-0 \
    mesa-utils \
    wget \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Download and unpack JavaFX SDK 21
RUN mkdir -p /javafx-sdk \
    && wget -O javafx.zip https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip \
    && unzip javafx.zip -d /javafx-sdk \
    && mv /javafx-sdk/javafx-sdk-21/lib /javafx-sdk/lib \
    && rm -rf /javafx-sdk/javafx-sdk-21 javafx.zip

# Копируем собранный JAR Copy from the build stage JAR file to the final image
COPY --from=build /app/target/edujournal-frontend-0.1.0.jar app.jar
COPY .env.docker .env
# Redirecting graphics output to Xming (Windows)
ENV DISPLAY=host.docker.internal:0.0

# Boot JavaFX application with explicit entry point using -cp Boot Java
CMD ["java", "--module-path", "/javafx-sdk/lib", "--add-modules", "javafx.controls,javafx.fxml", "-Dprism.order=sw", "-cp", "app.jar", "com.edujournal.Launcher"]


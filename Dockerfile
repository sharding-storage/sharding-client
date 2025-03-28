FROM openjdk:21-jdk-slim
WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src/ src/

RUN chmod +x ./gradlew && ./gradlew clean build --no-daemon

ENTRYPOINT ["java", "-jar", "build/libs/sharding-client-1.0-SNAPSHOT-all.jar"]
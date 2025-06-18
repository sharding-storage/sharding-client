FROM eclipse-temurin:21 as builder

WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon

COPY src/ src/
RUN ./gradlew clean shadowJar --no-daemon && \
    rm -rf /root/.gradle

FROM eclipse-temurin:21

WORKDIR /app

COPY --from=builder /app/build/libs/sharding-client-1.0-SNAPSHOT-all.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY happy.jar app.jar
COPY Web ./Web

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

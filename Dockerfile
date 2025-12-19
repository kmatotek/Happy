FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy all source code
COPY . .

# Build jar inside the container with HappyServer as entry point
RUN mkdir build \
    && javac -d build $(find . -name "*.java") \
    && jar cfe app.jar Web.HappyServer -C build .

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/app.jar app.jar

# Copy the Web directory
COPY Web ./Web

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
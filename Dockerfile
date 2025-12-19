FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy all source code
COPY . .

# Build happy.jar inside the container
RUN mkdir build && javac -d build $(find . -name "*.java")
RUN jar cfe app.jar Happy.HappyMain -C build .

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

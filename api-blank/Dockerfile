FROM eclipse-temurin:21-jdk
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests
ENTRYPOINT ["sh", "-c", "java -jar target/*.jar"]
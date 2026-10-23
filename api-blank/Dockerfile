FROM eclipse-temurin:21-jdk
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests
ENTRYPOINT ["java","-jar","target/tp3-0.0.1-SNAPSHOT.jar"]
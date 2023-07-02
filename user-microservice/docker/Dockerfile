FROM openjdk:17-alpine

WORKDIR /app

ADD ../build/libs/user-microservice-1.1.0.jar app/user-microservice-1.1.0.jar

ENTRYPOINT ["java", "-jar", "app/user-microservice-1.1.0.jar"]

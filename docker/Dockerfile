FROM openjdk:17-alpine

WORKDIR /app

ADD ../build/libs/news-1.1.0.jar app/news-1.1.0.jar

ENTRYPOINT ["java", "-jar", "app/news-1.1.0.jar"]

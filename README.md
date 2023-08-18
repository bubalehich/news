 ## Tech scope
 1. Java 17
 2. Postgres
 3. Docker 
 4. Redis
 5. [Lucene](https://docs.jboss.org/hibernate/search/6.1/reference/en-US/pdf/hibernate_search_reference.pdf)
 6. Gradle
 7. Spring Boot 3
 8. Spring Security 6
 9. [OpenApi](https://swagger.io/specification/)
 10. Lombok
 11. [Java Faker](https://github.com/DiUS/java-faker) & [Java Fixtures](https://github.com/piotrpolak/spring-boot-data-fixtures)
 12. Luquibase

 ## Dev tools
 1. Postman
 2. IntelliJ IDEA
 3. PgAdmin
 4. Docker Desktop
 5. Beer
 
 ## Swagger
 - Swagger UI: `http://localhost:8080/v3/api-docs`
 - API Docs: `http://localhost:8080/v3/api-docs`
  
 ---
 ## Build
 - `gradle clean build`
 - `docker-compose up -d`

 ## Fake data
 Can be loaded with DataFixtures.
 `data-fixtures.enabled : true` in configurations. OR can be loaded with liquibase


    

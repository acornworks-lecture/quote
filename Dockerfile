FROM amazoncorretto:11

COPY build/libs/quote-0.0.1-SNAPSHOT.jar boot.jar
COPY src/main/resources/application.properties application.properties

ENTRYPOINT ["java", "-jar", "boot.jar"]

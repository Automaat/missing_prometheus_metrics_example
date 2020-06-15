FROM adoptopenjdk/openjdk8
WORKDIR /opt/app
COPY build/libs/metrics-0.0.1-SNAPSHOT.jar order-service.jar
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "order-service.jar"]
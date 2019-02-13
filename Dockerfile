FROM openjdk:8-jdk-alpine
COPY /target/happy-employee-0.0.1-SNAPSHOT.jar happyemployee.jar
EXPOSE 8080
CMD ["java", "-jar", "happyemployee.jar"]


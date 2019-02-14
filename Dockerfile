FROM openjdk:8-jdk-alpine
COPY /target/happy-employee-0.0.1-SNAPSHOT.jar happyemployee.jar
EXPOSE 8080

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait /wait
RUN chmod +x /wait

CMD /wait && java -jar happyemployee.jar


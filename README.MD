# Employee API

### Project description

An application that handles the employees of a company. The application exposes a REST API, so that other services can communicate with it easily.

### Build the Project

Run in the project directory:
```
mvn clean install
```

### Run the project

To run the project you need to have installed [Docker](http://docker.com) with Docker-compose. Run in the project directory:
```
docker-compose up --build -d
```

### Swagger

Once the application is up and running its endpoints will be exposed at http://localhost:8080/swagger-ui.html

### API accessibility

API is accessible at http://localhost:8080 with an available endpoint: /employees
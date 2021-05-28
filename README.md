# user-info-api
REST API for Datasite code test

## Technologies Used
- Spring Boot
- Spock
- Groovy
- Docker
- Gradle

## Requirements
### If you haven't already, install these before downloading the project
- [Git](https://git-scm.com/downloads)
- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Docker](https://docs.docker.com/get-docker/)

## Install/Run
### To download and run the project locally, run these commands
```bash
$ git clone https://github.com/Patrick-Goggin/user-info-api.git
$ cd user-info-api
$ ./gradlew :bootRun
```

### To build and run image with Docker
1. Start your local Docker daemon
2. Run these commands at the project root:
```bash
$ ./gradlew bootBuildImage
$ docker run -it -p8080:8080 user-info-api:0.0.1-SNAPSHOT
```

## Smoke Test Endpoint
### In a browser or using a REST client, make a GET request to this url:
http://localhost:8080/userinfo

### Or run this cURL:
```bash
$ curl --request GET --url http://localhost:8080/userinfo
```


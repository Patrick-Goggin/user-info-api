# user-info-api
user-info-api

#Required components for running the application in development mode
Tell us what components we need to run your solution locally
- Git
- Java 11
- Docker

#Install
##Tell us what commands we have to run to install your solution dependencies locally
1. Open terminal at project root
2. Execute these commands:
```bash
$    git clone https://github.com/Patrick-Goggin/user-info-api.git
$    cd user-info-api
$    ./gradlew :bootRun
```

###To build and run image with Docker:
1. Start your local Docker daemon locally with either the Docker desktop app or on the command line
2. execute these commands:
```bash
$    ./gradlew bootBuildImage
$    docker run -it -p8080:8080 user-info-api:0.0.1-SNAPSHOT
```

#Tell us what commands/things we have to do to execute the endpoint locally
###In a browser or using a REST client, make a GET request to this url:
http://localhost:8080/userinfo

###Or run this Curl in the terminal:
```bash
$    curl --request GET --url http://localhost:8080/userinfo
```


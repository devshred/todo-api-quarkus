# todo-api-quarkus Project

This project uses Quarkus to provide a simple todo-app API.

## Running the application in dev mode
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

### prepare MariaDB
```shell script
docker network create todo-app
docker run --detach --name todo-db --network=todo-app --env MARIADB_DATABASE=todo-app --env MARIADB_USER=todo-app --env MARIADB_PASSWORD=password --env MARIADB_ROOT_PASSWORD=root-pw  mariadb:10.6
```

### Dockerized Uber-JAR
```shell
mvn package -DskipTests
docker build -f src/main/docker/Dockerfile.jvm -t todo-api-quarkus-jvm .
docker run --network="todo-app" --rm -p 7001:7001 todo-api-quarkus-jvm
```

### Dockerized native app
```shell
./mvnw package -Pnative -DskipTests
docker build -f src/main/docker/Dockerfile.native -t todo-api-quarkus-native .
docker run --network="todo-app" --rm -p 7001:7001 todo-api-quarkus-native
```

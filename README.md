# Todo-API based on Quarkus
This project uses Quarkus to provide a simple todo-app API and is used for sample deployments in the scope of an [OKD](https://www.okd.io) workshop.
It implements an [OpenAPI spec](https://raw.githubusercontent.com/devshred/todo-api-spring-kotlin/main/src/main/resources/todo-spec.yaml) and can be tested with a [frontend based on Vue.js](https://github.com/devshred/todo-web).

## Running the application in dev mode
```shell script
./mvnw compile quarkus:dev
```
_(The app uses an in-memory H2 database.)_

## Packaging and running the application

### Dockerize Uber-JAR
```shell
./mvnw clean package
docker build -f src/main/docker/Dockerfile.jvm -t todo-api-quarkus:jvm .
docker run --network="todo-app" --rm -p 8080:8080 --name todo-api todo-api-quarkus:jvm
```

### Start frontend, backend and database
```shell
docker compose -f src/main/docker/docker-compose.yaml up
```

## Build an run native app
```shell
./mvnw clean package -Dquarkus.profile=local -Pnative
target/todo-api-quarkus-*-runner -Dquarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/todo-app
```

## Deploy to OpenShift
set _quarkus.openshift.namespace_ in application.properties
```shell
oc login
oc new-project <your-namespace>
./mvnw clean package -Dquarkus.kubernetes.deploy=true -Dquarkus.openshift.expose=true
oc get is # show image streams
oc get pods # show pods
oc get svc # show kubernetes services
oc get routes # show routes
curl -s http://<route>/api/v1/todo/ | jq .
```

## Add ServiceMonitor and deploy
```shell
oc apply -f src/main/openshift/service-monitor.yaml
mvn clean package -Dquarkus.kubernetes.deploy=true -Dquarkus.openshift.expose=true -Dquarkus.openshift.labels.app-with-metrics=todo-api
curl -s http://<route>/q/metrics | grep create_counter_total
```

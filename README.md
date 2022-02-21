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
./mvnw clean package
docker build -f src/main/docker/Dockerfile.jvm -t quay.io/johschmidtcc/todo-api-quarkus:jvm .
docker run --network="todo-app" --rm -p 7001:7001 quay.io/johschmidtcc/todo-api-quarkus:jvm
```

### Dockerized native app
```shell
./mvnw clean package -Pnative
docker build -f src/main/docker/Dockerfile.native -t quay.io/johschmidtcc/todo-api-quarkus:native .
docker run --network="todo-app" --rm -p 7001:7001 quay.io/johschmidtcc/todo-api-quarkus:native
```

### Deploy to OpenShift
set _quarkus.openshift.namespace_ in application.properties
```shell
oc login
oc new-project <your-namespace>
./mvnw clean package -Dquarkus.kubernetes.deploy=true
oc get is # show image streams
oc get pods # show pods
oc get svc # show kubernetes services
oc expose svc/todo-api-quarkus # expose the service
oc get routes # show routes
curl -s http://<route>/api/v1/todo/ | jq .
```

#### Add ServiceMonitor and deploy
```shell
oc apply -f src/main/openshift/service-monitor.yaml
mvn clean package -Dquarkus.kubernetes.deploy=true -Dquarkus.openshift.expose=true -Dquarkus.openshift.labels.app-with-metrics=todo-api
curl -s http://<route>/q/metrics | grep create_counter_total
```

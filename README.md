# Deployment using Docker

### This should be created in docker
### create network
`docker network create my-net`

### create volume
`docker volume create postgres-data`

## Setup Database

```
FROM library/postgres

ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD admin
ENV POSTGRES_DB restaurant-picker
```
create dockerfile using above and then build it.
### build postgresql image
`docker build -t restaurant-picker-db .`

### run postgresql container
`docker run -d --name restaurant-picker-db-container --network my-net -v postgres-data:/var/lib/postgresql/data -p 5432:5432 restaurant-picker-db:v1`

## Setup API

### build image for api
`mvn package` before building image

`docker build -t restaurant-picker-api:v1 .`

### run api container
`docker run --name restaurant-picker-api-container -p 8080:8080 --network my-net --env-file dev.env restaurant-picker-api:v1`

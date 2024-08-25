FROM maven:3.9.8-eclipse-temurin-21-alpine AS build

WORKDIR /app

ADD pom.xml .
ADD src src

RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM azul/zulu-openjdk-alpine:21.0.4-jre-headless

ENV TZ=America/Fortaleza

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD java ${ADDITIONAL_OPS} -jar app.jar
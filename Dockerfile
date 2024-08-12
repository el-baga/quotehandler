FROM maven:3.9.8-sapmachine-21 AS build

WORKDIR /handler
COPY . /handler
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk

WORKDIR /quotehandler
COPY --from=build /handler/target/*.jar quotehandler.jar
COPY --from=build /handler/src/main/resources/application.yml application.yml

ENTRYPOINT ["java", "-jar", "quotehandler.jar"]

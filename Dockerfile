FROM openjdk:21-jdk-slim

WORKDIR /handler

COPY target/quotehandler-0.0.1-SNAPSHOT.jar /handler/quotehandler.jar

EXPOSE 8080

# "-Xms512m", "-Xmx1024m"
ENTRYPOINT ["java", "-jar", "/handler/quotehandler.jar"]

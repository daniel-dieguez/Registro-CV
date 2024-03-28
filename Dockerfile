FROM openjdk:21-slim
MAINTAINER daniel.dieguez
COPY target/registro-0.0.1-SNAPSHOT.jar registro-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/registro-0.0.1-SNAPSHOT.jar"]


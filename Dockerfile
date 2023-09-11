# Install maven and copy project for compilation
FROM maven:latest as builder

COPY src /home/app/src
COPY pom.xml /home/app
WORKDIR /home/app

RUN mvn clean install -DskipTests=true

# start from java base
FROM openjdk:17-oracle

LABEL maintainer="Jaroslav Svoboda <jarmil.sv@volny.cz>"

# Set the non-root user as the default user
USER 10001

# set environment var
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n"

WORKDIR /home/app

# copy application code
#COPY target/partners-bootcamp-1.0-SNAPSHOT.jar /home/app/partners-bootcamp-1.0-SNAPSHOT.jar
COPY --from=builder /home/app/target/partners-bootcamp-1.0-SNAPSHOT.jar /home/app/partners-bootcamp-1.0-SNAPSHOT.jar

# expose port
EXPOSE 8080

#start app
CMD ["java", "-jar", "partners-bootcamp-1.0-SNAPSHOT.jar"]

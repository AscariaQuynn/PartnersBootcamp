# start from java base
FROM openjdk:17-oracle

LABEL maintainer="Jaroslav Svoboda <jarmil.sv@volny.cz>"

# Set the non-root user as the default user
USER nonroot 10001

# copy application code
ADD --chown=nonroot:nonroot target/partners-bootcamp-1.0-SNAPSHOT.jar /app/partners-bootcamp-1.0-SNAPSHOT.jar
RUN chmod -R 755 /app/partners-bootcamp-1.0-SNAPSHOT.jar

WORKDIR /app

# expose port
EXPOSE 8080

#start app
CMD ["java", "./partners-bootcamp-1.0-SNAPSHOT.jar"]

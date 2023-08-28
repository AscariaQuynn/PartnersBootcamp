# start from java base
FROM openjdk:17-oracle

LABEL maintainer="Jaroslav Svoboda <jarmil.sv@volny.cz>"

# copy application code
ADD target/partners-bootcamp-1.0-SNAPSHOT.jar /app/partners-bootcamp-1.0-SNAPSHOT.jar
WORKDIR /app

# expose port
EXPOSE 8080

#start app
CMD ["java", "./partners-bootcamp-1.0-SNAPSHOT.jar"]

FROM openjdk:11
MAINTAINER Matheus Torres
COPY /target/*.jar /var/www/app.jar
WORKDIR /var/www
ENTRYPOINT java -jar app.jar
EXPOSE 8080
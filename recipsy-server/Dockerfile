FROM openjdk:11
EXPOSE 9191:9191
RUN mkdir /app
COPY ./build/libs/*-all.jar /app/server.jar
ENTRYPOINT ["java", "-jar", "/app/server.jar"]
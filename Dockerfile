FROM openjdk:8
VOLUME /tmp
ADD target/ratelimit-0.0.1-SNAPSHOT.jar ratelimit.jar
RUN bash -c 'touch /ratelimit.jar'
EXPOSE 8080
ENTRYPOINT ["java" , "-jar", "/ratelimit.jar"]

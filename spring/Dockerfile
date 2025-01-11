FROM openjdk:17-jdk-slim
ADD /build/libs/hls-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /usr/share/nginx/temp/hls && \
    chmod -R 755 /usr/share/nginx/temp/hls 
ENTRYPOINT ["java","-jar","/app.jar"]
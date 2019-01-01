FROM openjdk:8-jdk-alpine

RUN apk --no-cache add bash

COPY ./Dockerdir/ /

COPY ./target/rancher-config-backup-*.jar /

RUN ln -s /rancher-config-backup-*.jar /app.jar && \
    chmod +x /app.jar

CMD ["/run_app.sh"]
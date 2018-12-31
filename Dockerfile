FROM openjdk:8-jdk-alpine

COPY ./Dockerdir/ /

CMD ["/run_app.sh"]
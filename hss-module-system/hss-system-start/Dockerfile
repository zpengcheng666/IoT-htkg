FROM openjdk:8

MAINTAINER hss

WORKDIR /app

RUN mkdir "conf"
RUN mkdir "conf/data"
RUN mkdir "logs"
RUN mkdir "/var/upFiles"

ENV TZ Asia/Shanghai
ARG lib_path

#RUN apk add tzdata && cp /usr/share/zoneinfo/${TZ} /etc/localtime \
#    && echo ${TZ} > /etc/timezone \
#    && apk del tzdata

EXPOSE 8081

RUN apt-get update
RUN apt-get install net-tools

ADD ./src/lib/${lib_path}/libDongle_java.so /usr/lib/libDongle_java.so

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.config.location=/app/conf/application.yml","-Dspring.config.additional-location=/app/conf/application-mysql.yml","-Dlogging.config=/app/conf/logback-spring.xml","-jar","app.jar"]

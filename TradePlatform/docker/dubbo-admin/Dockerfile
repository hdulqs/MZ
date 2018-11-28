FROM java:8-jre-alpine

#设置时区
RUN  echo 'http://mirrors.ustc.edu.cn/alpine/v3.5/main' > /etc/apk/repositories \
    && echo 'http://mirrors.ustc.edu.cn/alpine/v3.5/community' >>/etc/apk/repositories \
&& apk update && apk add tzdata \
&& ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone

CMD mkdir dubbo-admin
ADD ./ dubbo-admin/
CMD chmod 755 -R dubbo-admin/
CMD cd dubbo-admin/dubbo-admin-backend/target && java -jar dubbo-admin-backend-0.0.1-SNAPSHOT.jar
#CMD mvn --projects dubbo-admin-backend spring-boot:run

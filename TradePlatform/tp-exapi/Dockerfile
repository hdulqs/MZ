FROM java:8-jre-alpine

COPY ./target/*.jar tp-exapi.jar
CMD java -jar -Ddruid.config.decrypt=true tp-exapi.jar

#设置时区
RUN  echo 'http://mirrors.ustc.edu.cn/alpine/v3.5/main' > /etc/apk/repositories \
    && echo 'http://mirrors.ustc.edu.cn/alpine/v3.5/community' >>/etc/apk/repositories \
&& apk update && apk add tzdata \
&& ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone

#设置kaptcha验证码所需字体
RUN apk --no-cache add ttf-dejavu fontconfig

EXPOSE 8087
FROM node:10-alpine

# 创建 app 目录
WORKDIR /kline

COPY kline_index/package*.json ./

RUN npm install

ADD kline_index/index.js ./

EXPOSE 3000
#ENTRYPOINT ["docker-entrypoint.sh"]
CMD [ "node", "index.js" ]

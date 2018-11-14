#!/bin/bash
set -e

mvn clean package -DskipTests -f tp-static/
mvn clean package -DskipTests -f tp-oauth/
mvn clean package -DskipTests -f tp-check/
mvn clean package -DskipTests -f tp-exapi/
mvn clean package -DskipTests -f tp-front/
mvn clean package -DskipTests -f tp-coin/
mvn clean package -DskipTests -f tp-trade/
mvn clean package -DskipTests -f tp-manage/

docker build --rm -t 192.168.1.102:5000/front/root:0.0.1-SNAPSHOT tp-front/
docker build --rm -t 192.168.1.102:5000/front/coin:0.0.1-SNAPSHOT tp-coin/
docker build --rm -t 192.168.1.102:5000/front/manage:0.0.1-SNAPSHOT tp-manage/
docker build --rm -t 192.168.1.102:5000/front/trade:0.0.1-SNAPSHOT tp-trade/
docker build --rm -t 192.168.1.102:5000/admin/static:0.0.1-SNAPSHOT tp-static/
docker build --rm -t 192.168.1.102:5000/admin/oauth:0.0.1-SNAPSHOT tp-oauth/
docker build --rm -t 192.168.1.102:5000/admin/check:0.0.1-SNAPSHOT tp-check/
docker build --rm -t 192.168.1.102:5000/admin/exapi:0.0.1-SNAPSHOT tp-front/

docker push 192.168.1.102:5000/admin/static:0.0.1-SNAPSHOT
docker push 192.168.1.102:5000/admin/oauth:0.0.1-SNAPSHOT
docker push 192.168.1.102:5000/admin/check:0.0.1-SNAPSHOT
docker push 192.168.1.102:5000/admin/exapi:0.0.1-SNAPSHOT
docker push 192.168.1.102:5000/front/root:0.0.1-SNAPSHOT
docker push 192.168.1.102:5000/front/coin:0.0.1-SNAPSHOT
docker push 192.168.1.102:5000/front/manage:0.0.1-SNAPSHOT
docker push 192.168.1.102:5000/front/trade:0.0.1-SNAPSHOT
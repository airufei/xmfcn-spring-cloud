#!/bin/bash

set -m

if [ `whoami` != "root" ];then
  echo "请使用root用户执行!"
  exit 1
else
  PIDS=$(ps -ef | grep java | grep xmfcn-spring-cloud-wechat-api-1.1.1.jar | grep -v grep | awk '{print $2}')

  if [ "$PIDS" == "" ]; then
    nohup java -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xms500m -Xmx500m -Xmn256m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -jar xmfcn-spring-cloud-wechat-api-1.1.1.jar --spring.profiles.active=prod&
    sleep 1
    tail -f nohup.out
  else
    echo "服务正在运行中，不要重复启动!"
  fi
fi
#!/bin/bash

tput sc
echo -n 请等待:

count=0

while true
do
     PIDS=$(ps -ef|grep java|grep xmfcn-spring-cloud-sys-redis-service-1.1.1.jar|grep -v grep|awk '{print $2}')
     if [ "$PIDS" == "" ]; then
       tput rc
       tput ed
       echo "服务已停止!"
       break
      else
       kill  $PIDS
       let count++
       sleep 1
       tput rc
       tput ed
       echo -n "$count"秒  
      fi
done
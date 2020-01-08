#!/bin/bash

# 变量
projectName=job-admin
version=1.1.1
port=8093


# 创建 Dockerfile
echo '=====>>>当前路径'
pwd
echo '=====>>>当前环境为 ' $CONFIG_PROFILE
echo '=====>>>设置内存为 ' $JAVA_OPTS_VALUE

rm -rf ./Dockerfile
touch Dockerfile

tee Dockerfile <<-'EOF'
FROM java:8-jre


COPY ./xmfcn*.jar /ms_service/PROJECT_NAME.jar

EXPOSE PORT

ENV JAVA_OPTS="JAVA_OPTS_VALUE"
ENV CONFIG_PROFILE=CONFIG_PROFILE_VALUE

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS \
-Dfile.encoding=UTF8 -Duser.timezone=GMT+08 \
-Djava.security.egd=file:/dev/./urandom \
-jar /ms_service/PROJECT_NAME.jar \
--spring.profiles.active=$CONFIG_PROFILE"]
EOF

sed -i "s/PROJECT_NAME/$projectName/g" Dockerfile
sed -i "s/PORT/$port/g" Dockerfile
sed -i "s/JAVA_OPTS_VALUE/$JAVA_OPTS_VALUE/g" Dockerfile
sed -i "s/CONFIG_PROFILE_VALUE/$CONFIG_PROFILE/g" Dockerfile

# kill old container
if docker ps | grep -i $projectName
    then
        echo 'kill old container'
        docker kill $projectName
fi

# remove old container
if docker ps -a | grep -i $projectName
    then
        echo 'rm old container'
        docker rm $projectName
fi

# remove old images
if docker images | grep $projectName
    then
        echo 'remove old image'
        docker rmi $projectName:$version
fi

firewall-cmd --zone=public --add-port=$port/tcp --permanent
firewall-cmd --reload

docker build -t $projectName:$version .
# 启动容器
docker run -d -p $port:$port --name $projectName -v /opt/ms_service/:/logs/ --net=host $projectName:$version
sleep 30s
docker logs --tail=100 $projectName


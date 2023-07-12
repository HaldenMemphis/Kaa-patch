#基础java镜像
FROM jasondennis12138/java-base

#创建目录
RUN mkdir -p /home/msc/conf/kaapatch

copy src/main/resources/application.properties /home/msc/conf/kaapatch/application.properties

#复制jar包以及相关配置文件
COPY target/*-fat.jar /app.jar

#添加进入docker容器后的目录
WORKDIR /home/msc/microserv/

CMD nohup sh -c 'java ${JVM_OPTS} -jar /app.jar --spring.config.location=/home/msc/conf/kaapatch/application.properties'

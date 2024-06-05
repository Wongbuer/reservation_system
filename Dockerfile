FROM openjdk:17-jdk-alpine AS runtime
LABEL authors="Wongbuer"

# 安装必要的字体和库以支持Java的字体渲染
RUN apk add --no-cache \
    freetype \
    fontconfig \
    ttf-dejavu \
    ttf-droid \
    ttf-liberation

# 将应用的jar文件复制到容器中
COPY ./target/reservation-system-0.0.1-SNAPSHOT.jar /opt/app.jar

# 配置容器启动后执行的命令
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Djasypt.encryptor.password=wongbuer", "-jar", "/opt/app.jar"]

# 可选：暴露应用端口
EXPOSE 8080
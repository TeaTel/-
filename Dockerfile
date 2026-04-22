# 使用多阶段构建
# 第一阶段：前端构建
FROM node:18-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ .
RUN npm run build

# 第二阶段：后端构建
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /app/backend
COPY backend/pom.xml .
COPY backend/src ./src
RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests

# 第三阶段：运行阶段
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 复制前端构建文件到静态资源目录
COPY --from=frontend-build /app/frontend/dist ./static

# 复制后端JAR文件
COPY --from=backend-build /app/backend/target/backend-0.0.1-SNAPSHOT.jar ./backend.jar

# 复制启动脚本
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

# 创建非root用户
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 暴露端口
EXPOSE 8080

# 使用启动脚本
CMD ["/app/start.sh"]
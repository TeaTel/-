# 单阶段Docker构建 - 校园二手交易平台 v2.0 (闲鱼风格)
# 策略：前端已预构建并放入backend/src/main/resources/static/
# DEPLOY: v2.0-FORCE-REBUILD-2026-04-24-T15

FROM maven:3.9-eclipse-temurin-21-jdk AS build

ARG CACHE_BUST=v2.0-refactor-20260424

WORKDIR /app/backend

RUN echo "Build timestamp: ${CACHE_BUST}"

# 复制pom.xml
COPY backend/pom.xml .

# 下载依赖
RUN mvn dependency:go-offline -B --no-transfer-progress || true

# 复制后端源码（包含已预构建的前端static目录）
COPY backend/src ./src

# 构建JAR包（前端静态资源会自动包含在JAR中）
RUN mvn clean package -DskipTests -B --no-transfer-progress

# ============================================
# 运行阶段 (JRE 21 Alpine)
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 复制后端JAR包（内含前端静态资源）
COPY --from=build /app/backend/target/backend-0.0.1-SNAPSHOT.jar ./backend.jar

COPY start.sh ./start.sh
RUN chmod +x ./start.sh

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8080

CMD ["./start.sh"]

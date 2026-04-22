#!/bin/sh

# 校园二手交易平台启动脚本
# 这个脚本会在Railway容器中执行

echo "========================================="
echo "Starting Campus Market Backend..."
echo "========================================="

# 显示环境变量（调试用）
echo "Environment variables:"
echo "SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE:-not set}"

# 检查多种可能的环境变量命名约定
# Railway MySQL插件通常提供MYSQL*变量，但也可能有其他命名
if [ -n "${MYSQLHOST}" ]; then
    echo "MYSQLHOST: ${MYSQLHOST}"
    export DB_HOST="${MYSQLHOST}"
elif [ -n "${DATABASE_URL}" ]; then
    echo "DATABASE_URL: (parsed)"
    # 从DATABASE_URL解析主机
    export DB_HOST=$(echo "${DATABASE_URL}" | sed -n 's/.*@\([^:]*\):.*/\1/p')
else
    echo "MYSQLHOST: not set"
    export DB_HOST="localhost"
fi

if [ -n "${MYSQLPORT}" ]; then
    echo "MYSQLPORT: ${MYSQLPORT}"
    export DB_PORT="${MYSQLPORT}"
elif [ -n "${DATABASE_URL}" ]; then
    export DB_PORT=$(echo "${DATABASE_URL}" | sed -n 's/.*:\([0-9]*\)\/.*/\1/p')
    echo "MYSQLPORT: ${DB_PORT:-3306}"
else
    echo "MYSQLPORT: not set"
    export DB_PORT="3306"
fi

if [ -n "${MYSQLDATABASE}" ]; then
    echo "MYSQLDATABASE: ${MYSQLDATABASE}"
    export DB_NAME="${MYSQLDATABASE}"
elif [ -n "${DATABASE_URL}" ]; then
    export DB_NAME=$(echo "${DATABASE_URL}" | sed -n 's/.*\/\([^?]*\).*/\1/p')
    echo "MYSQLDATABASE: ${DB_NAME}"
else
    echo "MYSQLDATABASE: not set"
    export DB_NAME="campus_market"
fi

if [ -n "${MYSQLUSER}" ]; then
    echo "MYSQLUSER: ${MYSQLUSER}"
    export DB_USER="${MYSQLUSER}"
elif [ -n "${DATABASE_URL}" ]; then
    export DB_USER=$(echo "${DATABASE_URL}" | sed -n 's/.*\/\/\([^:]*\):.*/\1/p')
    echo "MYSQLUSER: ${DB_USER}"
else
    echo "MYSQLUSER: not set"
    export DB_USER="root"
fi

# 检查多种可能的密码变量名
if [ -n "${MYSQLPASSWORD}" ]; then
    echo "MYSQLPASSWORD: ***** (from MYSQLPASSWORD)"
    export DB_PASSWORD="${MYSQLPASSWORD}"
elif [ -n "${MYSQL_ROOT_PASSWORD}" ]; then
    echo "MYSQLPASSWORD: ***** (from MYSQL_ROOT_PASSWORD)"
    export DB_PASSWORD="${MYSQL_ROOT_PASSWORD}"
elif [ -n "${MYSQL_PASSWORD}" ]; then
    echo "MYSQLPASSWORD: ***** (from MYSQL_PASSWORD)"
    export DB_PASSWORD="${MYSQL_PASSWORD}"
elif [ -n "${DATABASE_URL}" ]; then
    export DB_PASSWORD=$(echo "${DATABASE_URL}" | sed -n 's/.*:\([^@]*\)@.*/\1/p')
    echo "MYSQLPASSWORD: ***** (from DATABASE_URL)"
else
    echo "MYSQLPASSWORD: not set - 使用默认密码"
    export DB_PASSWORD="123456"
fi

# 显示最终使用的数据库配置
echo "========================================="
echo "Final database configuration:"
echo "DB_HOST: ${DB_HOST}"
echo "DB_PORT: ${DB_PORT}"
echo "DB_NAME: ${DB_NAME}"
echo "DB_USER: ${DB_USER}"
echo "DB_PASSWORD: *****"
echo "========================================="

# 检查jar文件是否存在
if [ ! -f "/app/backend.jar" ]; then
    echo "Error: JAR file not found at /app/backend.jar"
    exit 1
fi

echo "JAR file found. Starting application..."

# 显示Java版本
java -version

# 启动Spring Boot应用，启用调试日志
echo "Starting Spring Boot application with debug logging..."

# 设置数据库环境变量供Spring Boot使用
export DB_HOST="${DB_HOST}"
export DB_PORT="${DB_PORT}"
export DB_NAME="${DB_NAME}"
export DB_USER="${DB_USER}"
export DB_PASSWORD="${DB_PASSWORD}"

exec java -Xmx512m -Xms256m \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} \
    -Dlogging.level.com.campus.backend=DEBUG \
    -Dlogging.level.org.springframework=INFO \
    -Dlogging.level.org.springframework.jdbc=DEBUG \
    -Dlogging.level.org.springframework.boot.actuate=DEBUG \
    -Dmanagement.endpoint.health.show-details=always \
    -Dmanagement.endpoints.web.exposure.include=health,info,metrics \
    -jar /app/backend.jar
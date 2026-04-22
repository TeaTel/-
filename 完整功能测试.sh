#!/bin/bash

# 完整功能测试脚本
# 用于测试 c2cmarket.store 域名的所有功能

echo "=== 校园二手交易平台完整功能测试 ==="
echo "测试时间: $(date)"
echo "测试域名: c2cmarket.store"
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试函数
test_endpoint() {
    local url=$1
    local description=$2
    local expected_status=${3:-200}
    
    echo -n "测试: $description ... "
    
    # 发送HTTP请求
    response=$(curl -s -o /dev/null -w "%{http_code}" --max-time 10 "$url" 2>/dev/null)
    
    if [ "$response" = "$expected_status" ]; then
        echo -e "${GREEN}✓ 成功 (状态码: $response)${NC}"
        return 0
    else
        echo -e "${RED}✗ 失败 (状态码: $response, 预期: $expected_status)${NC}"
        return 1
    fi
}

echo "=== 阶段1: 基础访问测试 ==="
echo ""

# 测试前端访问
test_endpoint "https://c2cmarket.store" "前端主域名"
test_endpoint "https://www.c2cmarket.store" "前端www子域名"

# 测试后端API健康检查
test_endpoint "https://api.c2cmarket.store/actuator/health" "后端健康检查"
test_endpoint "https://api.c2cmarket.store/swagger-ui.html" "Swagger API文档" 200

echo ""
echo "=== 阶段2: API功能测试 ==="
echo ""

# 测试用户相关API
echo "1. 用户认证测试:"
test_endpoint "https://api.c2cmarket.store/api/auth/login" "登录接口" 400  # 需要POST数据，400是正常的
test_endpoint "https://api.c2cmarket.store/api/auth/register" "注册接口" 400

# 测试商品相关API
echo ""
echo "2. 商品功能测试:"
test_endpoint "https://api.c2cmarket.store/api/products" "商品列表" 200
test_endpoint "https://api.c2cmarket.store/api/products/1" "商品详情" 200

# 测试分类API
echo ""
echo "3. 分类功能测试:"
test_endpoint "https://api.c2cmarket.store/api/categories" "分类列表" 200

# 测试订单API
echo ""
echo "4. 订单功能测试:"
test_endpoint "https://api.c2cmarket.store/api/orders" "订单列表" 401  # 需要认证，401是正常的

echo ""
echo "=== 阶段3: 数据库连接测试 ==="
echo ""

# 测试数据库健康
db_status=$(curl -s "https://api.c2cmarket.store/actuator/health" 2>/dev/null | grep -o '"status":"[^"]*"' | head -1 | cut -d'"' -f4)

if [ "$db_status" = "UP" ]; then
    echo -e "${GREEN}✓ 数据库连接正常 (状态: $db_status)${NC}"
else
    echo -e "${RED}✗ 数据库连接异常 (状态: $db_status)${NC}"
fi

echo ""
echo "=== 阶段4: 前后端通信测试 ==="
echo ""

# 测试前端是否能正确调用API
echo "测试前端API配置:"
frontend_config=$(curl -s "https://c2cmarket.store" 2>/dev/null | grep -o "VITE_API_BASE_URL[^}]*" | head -1 || echo "")

if echo "$frontend_config" | grep -q "api.c2cmarket.store"; then
    echo -e "${GREEN}✓ 前端API配置正确${NC}"
else
    echo -e "${YELLOW}⚠ 前端API配置可能需要检查${NC}"
    echo "当前配置: $frontend_config"
fi

echo ""
echo "=== 阶段5: 性能测试 ==="
echo ""

# 测试响应时间
echo "测试API响应时间:"
for endpoint in "/actuator/health" "/api/products" "/api/categories"; do
    echo -n "  $endpoint ... "
    time=$(curl -s -o /dev/null -w "%{time_total}s" --max-time 5 "https://api.c2cmarket.store$endpoint" 2>/dev/null)
    if [ $(echo "$time < 2.0" | bc -l 2>/dev/null) -eq 1 ]; then
        echo -e "${GREEN}$time${NC}"
    else
        echo -e "${YELLOW}$time (较慢)${NC}"
    fi
done

echo ""
echo "=== 阶段6: 安全测试 ==="
echo ""

# 测试HTTPS强制
echo "测试HTTPS强制:"
http_response=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "http://c2cmarket.store" 2>/dev/null)
if [ "$http_response" = "301" ] || [ "$http_response" = "302" ] || [ "$http_response" = "308" ]; then
    echo -e "${GREEN}✓ HTTP自动重定向到HTTPS${NC}"
else
    echo -e "${YELLOW}⚠ HTTP重定向可能需要配置${NC}"
fi

# 测试安全头
echo ""
echo "测试安全HTTP头:"
headers=$(curl -s -I --max-time 5 "https://c2cmarket.store" 2>/dev/null)
security_headers=0

if echo "$headers" | grep -q "Strict-Transport-Security"; then
    security_headers=$((security_headers+1))
fi
if echo "$headers" | grep -q "X-Content-Type-Options"; then
    security_headers=$((security_headers+1))
fi
if echo "$headers" | grep -q "X-Frame-Options"; then
    security_headers=$((security_headers+1))
fi

echo "安全头数量: $security_headers/3"

echo ""
echo "=== 测试总结 ==="
echo ""

# 统计测试结果
total_tests=0
passed_tests=0

# 这里可以添加更详细的统计逻辑

echo "测试完成!"
echo ""
echo "=== 后续手动测试建议 ==="
echo ""
echo "1. 浏览器功能测试:"
echo "   - 访问 https://c2cmarket.store"
echo "   - 使用测试账号登录: student1 / password123"
echo "   - 浏览商品列表"
echo "   - 发布新商品"
echo "   - 创建订单"
echo "   - 测试消息功能"
echo ""
echo "2. 移动端测试:"
echo "   - 在手机浏览器访问"
echo "   - 测试响应式布局"
echo "   - 验证触摸操作"
echo ""
echo "3. 性能测试:"
echo "   - 使用浏览器开发者工具分析性能"
echo "   - 测试页面加载速度"
echo "   - 检查资源优化"
echo ""
echo "4. 安全测试:"
echo "   - 验证所有连接使用HTTPS"
echo "   - 检查无混合内容警告"
echo "   - 测试XSS防护"
echo ""
echo "=== 故障排除 ==="
echo ""
echo "如果测试失败，请检查:"
echo "1. DNS解析是否生效 (使用 ./测试DNS解析.sh)"
echo "2. SSL证书是否有效 (使用 ./测试SSL证书.sh)"
echo "3. Render部署日志"
echo "4. 后端应用日志"
echo "5. 数据库连接状态"
echo ""
echo "祝您测试顺利!"
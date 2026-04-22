#!/bin/bash

# DNS解析测试脚本
# 用于测试 c2cmarket.store 域名解析是否生效

echo "=== DNS解析测试开始 ==="
echo "测试时间: $(date)"
echo ""

# 测试主域名
echo "1. 测试主域名 c2cmarket.store:"
nslookup c2cmarket.store 2>/dev/null || echo "nslookup命令不可用，尝试使用dig..."
dig c2cmarket.store +short 2>/dev/null || echo "请手动测试: nslookup c2cmarket.store"
echo ""

# 测试www子域名
echo "2. 测试www子域名 www.c2cmarket.store:"
nslookup www.c2cmarket.store 2>/dev/null || echo "nslookup命令不可用，尝试使用dig..."
dig www.c2cmarket.store +short 2>/dev/null || echo "请手动测试: nslookup www.c2cmarket.store"
echo ""

# 测试api子域名
echo "3. 测试api子域名 api.c2cmarket.store:"
nslookup api.c2cmarket.store 2>/dev/null || echo "nslookup命令不可用，尝试使用dig..."
dig api.c2cmarket.store +short 2>/dev/null || echo "请手动测试: nslookup api.c2cmarket.store"
echo ""

# 测试Render原始域名（对比）
echo "4. 测试Render原始域名:"
echo "前端: campus-marketplace.onrender.com"
nslookup campus-marketplace.onrender.com 2>/dev/null || dig campus-marketplace.onrender.com +short 2>/dev/null
echo "后端: campus-marketplace-api.onrender.com"
nslookup campus-marketplace-api.onrender.com 2>/dev/null || dig campus-marketplace-api.onrender.com +short 2>/dev/null
echo ""

echo "=== 预期解析结果 ==="
echo "c2cmarket.store 应该解析到: campus-marketplace.onrender.com"
echo "www.c2cmarket.store 应该解析到: campus-marketplace.onrender.com"
echo "api.c2cmarket.store 应该解析到: campus-marketplace-api.onrender.com"
echo ""

echo "=== 测试建议 ==="
echo "1. 如果解析不生效，请等待10-30分钟（DNS传播需要时间）"
echo "2. 检查阿里云DNS配置是否正确"
echo "3. 清除本地DNS缓存:"
echo "   macOS: sudo dscacheutil -flushcache; sudo killall -HUP mDNSResponder"
echo "   Windows: ipconfig /flushdns"
echo "   Linux: sudo systemctl restart systemd-resolved"
echo ""

echo "=== 后续测试 ==="
echo "DNS解析生效后，测试以下地址:"
echo "1. https://c2cmarket.store (前端)"
echo "2. https://www.c2cmarket.store (前端备用)"
echo "3. https://api.c2cmarket.store/actuator/health (后端健康检查)"
echo "4. https://api.c2cmarket.store/swagger-ui.html (API文档)"
echo ""

echo "测试完成!"
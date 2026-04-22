#!/bin/bash

# SSL证书测试脚本
# 用于测试 c2cmarket.store 域名的SSL证书配置

echo "=== SSL证书测试开始 ==="
echo "测试时间: $(date)"
echo "注意: 请先确保DNS解析已生效"
echo ""

# 测试函数
test_ssl() {
    local domain=$1
    local description=$2
    
    echo "测试: $description ($domain)"
    echo "----------------------------------------"
    
    # 使用openssl测试证书
    echo "1. 证书基本信息:"
    timeout 10 openssl s_client -connect "$domain:443" -servername "$domain" < /dev/null 2>/dev/null | \
        openssl x509 -noout -subject -dates 2>/dev/null || echo "  无法获取证书信息"
    
    echo ""
    echo "2. 证书有效期:"
    timeout 10 openssl s_client -connect "$domain:443" -servername "$domain" < /dev/null 2>/dev/null | \
        openssl x509 -noout -enddate 2>/dev/null | cut -d= -f2 || echo "  无法获取有效期"
    
    echo ""
    echo "3. 证书颁发者:"
    timeout 10 openssl s_client -connect "$domain:443" -servername "$domain" < /dev/null 2>/dev/null | \
        openssl x509 -noout -issuer 2>/dev/null || echo "  无法获取颁发者"
    
    echo ""
    echo "4. HTTP访问测试:"
    curl -I --max-time 10 "https://$domain/" 2>/dev/null | head -5 || echo "  HTTPS访问失败"
    
    echo ""
    echo "5. 证书链验证:"
    timeout 10 openssl s_client -connect "$domain:443" -servername "$domain" -showcerts < /dev/null 2>/dev/null | \
        grep -c "BEGIN CERTIFICATE" | xargs echo "  证书链中的证书数量:" || echo "  无法验证证书链"
    
    echo "========================================"
    echo ""
}

# 测试主域名
test_ssl "c2cmarket.store" "主域名"

# 测试www子域名
test_ssl "www.c2cmarket.store" "www子域名"

# 测试api子域名
test_ssl "api.c2cmarket.store" "API子域名"

echo "=== SSL证书测试完成 ==="
echo ""
echo "=== 预期结果 ==="
echo "1. 证书颁发者应包含: Let's Encrypt"
echo "2. 证书有效期应为90天"
echo "3. HTTPS访问应返回200状态码"
echo "4. 证书链应包含2-3个证书"
echo ""
echo "=== 常见问题解决 ==="
echo "1. 证书未生效: 等待30分钟，Render会自动配置"
echo "2. DNS解析问题: 确保域名解析到Render服务器"
echo "3. 证书错误: 在Render控制台检查证书状态"
echo "4. 混合内容警告: 确保所有资源使用HTTPS"
echo ""
echo "=== 手动测试命令 ==="
echo "1. 浏览器访问: https://c2cmarket.store"
echo "2. 点击锁图标查看证书详情"
echo "3. 使用在线工具: https://www.ssllabs.com/ssltest/"
echo "4. 检查证书透明度: https://crt.sh/?q=c2cmarket.store"
echo ""
echo "=== Render SSL管理 ==="
echo "1. 登录Render控制台: https://dashboard.render.com"
echo "2. 选择对应服务"
echo "3. 进入Settings → Custom Domains"
echo "4. 查看SSL证书状态"
echo "5. 如有问题，点击'Renew Certificate'"
echo ""
echo "测试完成!"
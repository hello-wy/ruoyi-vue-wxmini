#!/usr/bin/env bash
set -e
sudo -i

REDIS_PASSWORD=''
REDIS_CONF="/etc/redis/redis.conf"

echo "===> 检查 Redis 是否已存在"

# 通过 lsof -i:6379 检查是否已有 redis 进程监听 6379
REDIS_FOUND=$(lsof -i:6379 2>/dev/null | awk 'NR>1 && $3=="redis" {print "yes"; exit}')

if [ "$REDIS_FOUND" = "yes" ]; then
  echo "检测到 Redis 已存在并运行："
  lsof -i:6379
else
  echo "未检测到 Redis，开始安装"

  echo "===> 更新软件源"
  sudo apt update

  echo "===> 安装 Redis"
  sudo apt install -y redis-server

  echo "===> 配置 Redis 密码"
  if grep -q "^# requirepass" "$REDIS_CONF"; then
    sudo sed -i "s/^# requirepass .*/requirepass ${REDIS_PASSWORD}/" "$REDIS_CONF"
  elif grep -q "^requirepass" "$REDIS_CONF"; then
    sudo sed -i "s/^requirepass .*/requirepass ${REDIS_PASSWORD}/" "$REDIS_CONF"
  else
    echo "requirepass ${REDIS_PASSWORD}" | sudo tee -a "$REDIS_CONF" >/dev/null
  fi

  echo "===> 设置开机自启并重启 Redis"
  sudo systemctl enable redis-server
  sudo systemctl restart redis-server

  echo "===> 检查 Redis 状态"
  sudo systemctl status redis-server --no-pager

  echo
  echo "Redis 安装完成"
  echo "Redis 密码: ${REDIS_PASSWORD}"
  echo "测试命令: redis-cli -a ${REDIS_PASSWORD} ping"
fi

echo
echo "===> 启动 Spring Boot 服务"
java -jar ruoyi-admin/target/ruoyi-admin.jar --spring.profiles.active=prod

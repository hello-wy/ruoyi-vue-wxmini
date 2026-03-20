#!/usr/bin/env bash
set -euo pipefail

JDK_VERSION="8u432b06"
INSTALL_DIR="/usr/local/java"
PROFILE_FILE="/etc/profile.d/java.sh"

if command -v java >/dev/null 2>&1; then
  echo ">>> 检测到系统已安装 Java，跳过下载与安装"
  java -version
else
  ARCH="$(uname -m)"
  case "$ARCH" in
    x86_64|amd64)
      PKG_NAME="OpenJDK8U-jdk_x64_linux_hotspot_${JDK_VERSION}.tar.gz"
      ;;
    aarch64|arm64)
      PKG_NAME="OpenJDK8U-jdk_aarch64_linux_hotspot_${JDK_VERSION}.tar.gz"
      ;;
    *)
      echo "不支持的架构: $ARCH"
      exit 1
      ;;
  esac

  BASE_URL="https://mirrors.aliyun.com/eclipse/temurin-compliance/temurin/8/%20jdk8u432-b06"
  DOWNLOAD_URL="${BASE_URL}/${PKG_NAME}"

  echo ">>> 下载地址: ${DOWNLOAD_URL}"
  echo ">>> 安装目录: ${INSTALL_DIR}"

  sudo mkdir -p "${INSTALL_DIR}"
  cd /tmp

  echo ">>> 下载 JDK 8 ..."
  curl -fL -o "${PKG_NAME}" "${DOWNLOAD_URL}"

  echo ">>> 解压 ..."
  sudo tar -xzf "${PKG_NAME}" -C "${INSTALL_DIR}"

  JAVA_HOME_DIR="$(tar -tzf "${PKG_NAME}" | awk -F/ 'NR==1{print $1; exit}')"
  if [ -z "${JAVA_HOME_DIR}" ]; then
    echo "无法识别解压后的目录名"
    exit 1
  fi

  FULL_JAVA_HOME="${INSTALL_DIR}/${JAVA_HOME_DIR}"

  echo ">>> 配置环境变量 ..."
  cat <<EOF | sudo tee "${PROFILE_FILE}" >/dev/null
export JAVA_HOME=${FULL_JAVA_HOME}
export PATH=\$JAVA_HOME/bin:\$PATH
EOF

  sudo chmod +x "${PROFILE_FILE}"

  echo ">>> 立即生效 ..."
  export JAVA_HOME="${FULL_JAVA_HOME}"
  export PATH="${JAVA_HOME}/bin:${PATH}"

  echo ">>> 验证版本 ..."
  java -version

  echo
  echo "安装完成"
  echo "JAVA_HOME=${FULL_JAVA_HOME}"
  echo "重新登录或执行: source ${PROFILE_FILE}"
fi


if swapon --noheadings --show=NAME 2>/dev/null | grep -q .; then
  echo "检测到 swap 已开启，跳过 swap 初始化"
else
  echo "未检测到 swap，开始创建 /swapfile"
  sudo fallocate -l 2G /swapfile
  sudo chmod 600 /swapfile
  sudo mkswap /swapfile
  sudo swapon /swapfile
  echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
  sudo sysctl vm.swappiness=10
  echo 'vm.swappiness=10' | sudo tee -a /etc/sysctl.conf
fi

free -h
swapon --show


echo "===> 检查 Redis 是否已存在"

# 通过 lsof -i:6379 检查是否已有 redis 进程监听 6379
REDIS_FOUND=$(lsof -i:6379 2>/dev/null | awk 'NR>1 && $3=="redis" {print "yes"; exit}')

if [ "$REDIS_FOUND" = "yes" ]; then
  echo "检测到 Redis 已存在并运行："
  lsof -i:6379
else
  echo "未检测到 Redis，开始安装"
  sudo apt update
  sudo apt install -y redis-server nginx

  echo
  echo "Redis 安装完成"
fi

#echo
#echo "===> 启动 Spring Boot 服务"
#java -jar ./ruoyi-admin.jar --spring.profiles.active=prod

# 基础镜像
FROM ubuntu:20.04

# 使用LABEL替代已弃用的MAINTAINER指令‌:ml-citation{ref="2,3" data="citationList"}
LABEL maintainer="ayound@48098911.qq.com"

# 复制中文字体文件
COPY fonts/* /usr/share/fonts/chinese/

# 配置系统环境
RUN sed -i 's/archive.ubuntu.com/mirrors.aliyun.com/g' /etc/apt/sources.list && \
    sed -i 's/security.ubuntu.com/mirrors.aliyun.com/g' /etc/apt/sources.list && \
    apt-get clean && \
    apt-get update && \
    # 安装基础工具与依赖‌:ml-citation{ref="1,4" data="citationList"}
    apt-get install -y --reinstall ca-certificates locales language-pack-zh-hans && \
    localedef -i zh_CN -c -f UTF-8 -A /usr/share/locale/locale.alias zh_CN.UTF-8 && \
    locale-gen zh_CN.UTF-8 && \
    # 配置时区‌:ml-citation{ref="2" data="citationList"}
    DEBIAN_FRONTEND=noninteractive apt-get install -y tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    # 安装字体与工具‌:ml-citation{ref="5,6" data="citationList"}
    apt-get install -y fontconfig ttf-mscorefonts-installer ttf-wqy-microhei ttf-wqy-zenhei xfonts-wqy wget && \
    # 清理缓存‌:ml-citation{ref="7" data="citationList"}
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/*

# 安装JDK（合并RUN指令以减少层数）
RUN cd /tmp && \
    wget --no-check-certificate https://repo.huaweicloud.com/java/jdk/11.0.2+9/jdk-11.0.2_linux-x64_bin.tar.gz && \
    tar -zxf jdk-11.0.2_linux-x64_bin.tar.gz && \
    mv jdk-11.0.2 /usr/local/ && \
    rm -rf /tmp/*

# 安装LibreOffice（优化路径与清理）
RUN apt-get update && \
    apt-get install -y libxrender1 libxinerama1 libxt6 libxext-dev libfreetype6-dev libcairo2 libcups2 libx11-xcb1 libnss3 && \
    cd /tmp && \
    wget --no-check-certificate https://mirrors.cloud.tencent.com/libreoffice/libreoffice/stable/7.6.5/deb/x86_64/LibreOffice_7.6.5_Linux_x86-64_deb.tar.gz && \
    tar -zxf LibreOffice_7.6.5_Linux_x86-64_deb.tar.gz && \
    cd LibreOffice_7.6.5.2_Linux_x86-64_deb/DEBS && \
    dpkg -i *.deb && \
    rm -rf /tmp/*

# 配置字体缓存
RUN cd /usr/share/fonts/chinese && \
    mkfontscale && \
    mkfontdir && \
    fc-cache -fv

# 创建应用目录（优化为单条命令）‌:ml-citation{ref="4" data="citationList"}
RUN mkdir -p /fsearch/{cache,files,data}

# 复制应用程序文件
COPY target/file-search-1.0.6-SNAPSHOT.jar /fsearch/

# 环境变量（使用键值对格式修复LegacyKeyValueFormat警告）‌:ml-citation{ref="6,8" data="citationList"}
ENV USER_NAME=admin \
    PASSWORD=123456 \
    KK_BASE_URL=default \
    JAVA_HOME=/usr/local/jdk-11.0.2 \
    PATH="$JAVA_HOME/bin:$PATH" \
    LANG=zh_CN.UTF-8 \
    LC_ALL=zh_CN.UTF-8

# 声明数据卷与端口‌:ml-citation{ref="3,7" data="citationList"}
VOLUME ["/fsearch/files", "/fsearch/data", "/fsearch/cache"]
EXPOSE 8012

# 启动命令
ENTRYPOINT ["java", "-jar", "/fsearch/file-search-1.0.6-SNAPSHOT.jar"]

# 1단계: React 앱 빌드
FROM node:latest as build

WORKDIR /app
COPY package.json ./
RUN npm install

COPY . ./
RUN npm run build

# 2단계: Nginx 설정 (RTMP 모듈 포함된 이미지를 사용)
FROM tiangolo/nginx-rtmp:latest

# Nginx 설정 파일을 커스터마이즈 할 수 있도록 복사
COPY nginx.conf /etc/nginx/nginx.conf

# React 빌드 결과물을 Nginx로 복사
COPY --from=build /app/build /usr/share/nginx/html

# 권한 변경을 루트 사용자로 실행
USER root

# 필요한 디렉토리 생성 및 권한 설정
RUN mkdir -p /usr/share/nginx/temp/hls && \
    mkdir -p /usr/local/nginx/logs && \
    touch /usr/local/nginx/logs/error.log && \
    chmod -R 755 /usr/share/nginx/html /usr/share/nginx/temp/hls /usr/local/nginx/logs

# 포트 1935 (RTMP) 및 8080 (HTTP)을 사용하도록 설정
EXPOSE 1935 8080

# Nginx를 시작하는 명령
CMD ["nginx", "-g", "daemon off;"]

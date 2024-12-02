# 스크립트 실행 중 오류 발생 시 중단
set -e

# Gradle로 shadowJar 생성
echo "Building shadow JAR with Gradle..."
./gradlew shadowJar

# Docker 이미지 빌드
echo "Building Docker image..."
docker build --platform linux/amd64 -t peter012677/np-team-project .

# Docker 이미지 푸시
echo "Pushing Docker image to repository..."
docker push peter012677/np-team-project

echo "Build and push completed successfully!"
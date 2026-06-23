# 1. 베이스 이미지 변경 (openjdk 대신 eclipse-temurin 추천)
FROM eclipse-temurin:17-jdk-alpine

# 컨테이너 내부의 /tmp 디렉토리 지정
VOLUME /tmp

# 빌드 위치 지정 (스프링부트 빌드가 먼저 완료되어 있어야 합니다)
ARG JAR_FILE=build/libs/*.jar

# 빌드된 .jar파일을 컨테이너 내부에 복사
COPY ${JAR_FILE} backendprojectex.jar

# 프로젝트 기본 실행 명령어
# windows PowerShell 창에
# java -jar backendprojectex.jar 입력
ENTRYPOINT ["java", "-jar", "/backendprojectex.jar"]
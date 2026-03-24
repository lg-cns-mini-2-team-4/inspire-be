# 1. 베이스 이미지 설정
FROM eclipse-temurin:17-jdk

# 2. 작업 디렉토리 생성
WORKDIR /app

# 3. 로컬에서 이미 빌드된 jar 파일을 컨테이너로 복사
# (build/libs 폴더에 있는 jar 파일을 app.jar라는 이름으로 가져옵니다)
COPY build/libs/*.jar app.jar

# 4. 앱 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
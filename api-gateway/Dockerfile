# 1. 자바 17 환경 베이스 이미지
FROM eclipse-temurin:17-jdk

# 2. 컨테이너 내 작업 디렉토리 설정
WORKDIR /app

# 3. 로컬에서 빌드된 gateway jar 파일을 컨테이너로 복사
# (build/libs 폴더에 생성된 jar 파일을 app.jar라는 이름으로 가져옵니다)
COPY build/libs/*.jar app.jar

# 4. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
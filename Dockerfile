FROM openjdk:11-jdk
LABEL authors="이동헌"

# 앱 디렉토리 생성
WORKDIR /app

# 앱 종속성 복사
COPY build/libs/rehab-0.0.1-SNAPSHOT.jar rehab-0.0.1-SNAPSHOT.jar

# 앱 실행을 위한 사용자 계정 생성
RUN addgroup --system dockeruser && adduser --system --ingroup dockeruser dockeruser

# /app 폴더 소유권을 신규 사용자 계정으로 변경
RUN chown -R dockeruser:dockeruser /app

#사용자 계정으로 전환
USER dockeruser

#노출할 포트 명시
EXPOSE 443

# 시스템 진입점 정의
ENTRYPOINT ["java","-jar","/app/rehab-0.0.1-SNAPSHOT.jar"]
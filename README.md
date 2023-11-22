# ReHab-BackEnd

비대면 재활치료를 돕는 웹서비스, "Re:Hab" 백엔드 팀 개발 저장소입니다.

## Deployment url

[https://re-hab.website](https://re-hab.website/) <br>

## 개발 기간
2023.07 ~ 2023.11

## Back-End Team ✨

|                                   이동헌                                     |                                       박주영                                        |                                                                                       
|:--------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|
| <img width="160px" src="https://avatars.githubusercontent.com/u/80760160?v=4" /> | <img width="160px" src="https://avatars.githubusercontent.com/u/52206904?v=4" /> |
|                 [@Sirius506775](https://github.com/Sirius506775)                 |                      [@jyp-on](https://github.com/jyp-on)                      |     
|                                한림대학교 빅데이터전공  4학년                                 |   한림대학교 콘텐츠IT전공 3학년   |               



### 1.  usage skills
#### 1.1. Environment
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Apache Tomcat](https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

#### 1.2 Development
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Naver Cloud Platform](https://img.shields.io/badge/Naver%20Cloud%20Platform-%2303C75A.svg?style=for-the-badge&logo=NAVER&logoColor=white)
  
#### 1.3 Dependencies
- Spring Data JPA
- Spring Security
- QueryDsl
- lombok
- Log4J2
- Querydsl
- Thumbnailator
- Gson
- Smtp
- Json Web Token
- WebSocket

---
### 2. Descriptions
Backend 개발 언어는 JAVA를 사용하며, Spring-Boot 프레임워크로 Client, AI Server와 통신하는 API 서버를 구축한다.

### 3. Package Structure
```
├── com.hallym.festival 
│ ├── domain 
│ |    ├── chart
│ |    |    ├── controller
│ |    |    ├── dto
│ |    |    ├── entity
│ |    |    ├── repository
│ |    |    └── service
| |    |
| |    ├── program
| |    ├── reservation
| |    ├── room
| |    ├── user
| |    └── video
| ├── global
| |    ├── advice
| |    ├── baseEntity
│ |    ├── config 
│ |    ├── exception
│ |    ├── pageDTO
│ |    ├── s3
│ |    └── security
│ |    └── util
└── RehabApplciation.java
```

### 4. Service Architecture

![Group 159](https://github.com/sync-without-async/Rehab-BackEnd/assets/52206904/113f970c-4615-4323-98d2-e2babdd4707d)




# ReHab-BackEnd

비대면 재활치료를 돕는 웹서비스, "Re:Hab" 백엔드 팀 개발 저장소입니다.

## Deployment url

~~[https://re-hab.website](https://re-hab.website/)~~ <br>

## 개발 기간
2023.07 ~ 2023.11

## Back-End Team ✨

|                                   이동헌                                     |                                       박주영                                        |                                                                                       
|:--------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|
| <img width="160px" src="https://avatars.githubusercontent.com/u/80760160?v=4" /> | <img width="160px" src="https://avatars.githubusercontent.com/u/52206904?v=4" /> |
|                 [@Sirius506775](https://github.com/Sirius506775)                 |                      [@jyp-on](https://github.com/jyp-on)                      |     
|                                한림대학교 빅데이터전공  4학년                                 |   한림대학교 콘텐츠IT전공 3학년   |               



### Usage skills
#### 1. Environment
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Apache Tomcat](https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)

#### 1. Development
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

### Package Structure
```
├── com.hallym.rehab 
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

-----

## 1. 프로젝트 명
비대면 재활 치료를 돕는 웹 서비스, Re:Hab

![image](https://github.com/osohyun0224/Capstone-Rehab-FrontEnd/assets/53892427/cfae25b7-cfd3-42da-a0ce-f902d15d43c8)

## 2. 프로젝트 소개
> 본 프로젝트는 병원을 도메인으로 하는 비대면 재활치료를 돕는 웹서비스이다.

> 전문의는 외래 진료시 환자 차트 작성하고, 재활 운동사에게 해당 환자에 대한 정보와 재활 운동 요청서를 작성한다. 재활치료사는 전문의의 재활 요청서를 바탕으로 해당 환자에게 맞는 과제를 할당해준다. 이와 같은 통합적인 EMR 기능을 제공한다.

> 환자는 본 웹사이트에서 재활 운동 강의를 수강해 재활 치료를 진행한다. 환자가 운동을 수강하면 AI는 유사도를 판정해 유사도를 반환해준다. 80%이상의 유사도를 받아야 합격으로, 다음 강의를 수강할 수 있다. 이는 환자가 올바른 자세로 운동을 수강할 수 있도록 효율적인 재활치료를 돕는다.

> 환자는 또한 본인의 담당 의료진에게 비대면 진료를 요청할 수 있고, 비대면 진료 내용을 AI가 요약해줘 요약본을 환자의 상세 차트에 기록해 추후 의료진이 환자를 진찰하는데 도움을 준다.

## 3. 프로젝트 Workflows
![image](https://github.com/osohyun0224/Capstone-Rehab-FrontEnd/assets/53892427/12a90f49-06ee-4281-a188-97bbc696b211)

## 4. 프로젝트 기획 의도
![image](https://github.com/osohyun0224/Capstone-Rehab-FrontEnd/assets/53892427/8144c422-3f15-4c35-a8b8-470c69555d2d)

## 5. 프로젝트 기능 소개
![image](https://github.com/osohyun0224/Capstone-Rehab-FrontEnd/assets/53892427/666192f1-f365-490b-9077-4661d5e120f3)

![image](https://github.com/osohyun0224/Capstone-Rehab-FrontEnd/assets/53892427/83f60c2f-c338-411b-b747-49e9c21f7c6a)

![image](https://github.com/osohyun0224/Capstone-Rehab-FrontEnd/assets/53892427/55aef1d6-acfc-410d-9b39-292b48d44dfb)

![image](https://github.com/osohyun0224/Capstone-Rehab-FrontEnd/assets/53892427/5b1aafba-0e3b-42be-aff1-2f5eecc7e320)

![image](https://github.com/osohyun0224/Capstone-Rehab-FrontEnd/assets/53892427/fa52a4c5-38cf-49b4-b38c-1715a51ada79)


## 6. 프로젝트 시연 영상

[시연영상 바로가기](https://youtu.be/HuXwZLn8_XQ)

## 7. 프로젝트 전체 아키텍쳐

![image](https://github.com/osohyun0224/Capstone-Rehab-FrontEnd/assets/53892427/3443a38d-a517-4b8d-9429-e5812d70bb4a)


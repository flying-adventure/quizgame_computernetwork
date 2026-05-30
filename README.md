# 퀴즈 게임 (TCP/IP 소켓 프로그래밍)

TCP/IP 소켓을 이용한 서버-클라이언트 구조의 객관식 퀴즈 게임입니다.  
서버가 문제를 출제하면 클라이언트가 답을 전송하고, 서버가 정답 여부와 최종 점수를 반환합니다.

> 컴퓨터네트워크 과목 텀 프로젝트 — 202235070 안수빈

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java |
| Network | TCP/IP 소켓 (`java.net.ServerSocket`, `Socket`) |
| I/O | `BufferedReader`, `PrintWriter` |
| 설정 | `Properties` 파일 (`server_info.dat`) |

---

## 동작 방식

```
[QuizServer]                        [QuizClient]
    |                                    |
    |<--- TCP 연결 (PORT 1235) ----------|
    |                                    |
    |--- "Welcome to the Quiz Game!" --->|
    |--- Q1 문제 + 보기 4개 ------------>|
    |<--- 사용자 답 입력 (a/b/c/d) ------|
    |--- 정답/오답 피드백 -------------->|
    |    (반복, 총 3문제)                |
    |--- 최종 점수 전송 ---------------->|
    |--- 연결 종료 --------------------->|
```

---

## 주요 기능

- **객관식 퀴즈** — 4지선다 문제 3개를 순서대로 출제
- **즉각 피드백** — 답 입력 즉시 정답/오답 안내
- **입력 유효성 검사** — a/b/c/d 외 입력 시 같은 문제 재출제
- **최종 점수 반환** — 전체 문제 수 대비 점수 (`n/3`) 출력 후 연결 종료
- **설정 파일 지원** — `server_info.dat`에 서버 IP·Port 지정 가능 (없으면 `localhost:1235` 기본값 사용)

---

## 설치 및 실행

### 사전 요구사항
- JDK 설치 (Java 8 이상)

### 컴파일

```bash
javac QuizServer.java
javac QuizClient.java
```

### 실행

터미널 2개를 열어 각각 실행합니다.

**서버 실행 (먼저)**
```bash
java QuizServer
```

**클라이언트 실행**
```bash
java QuizClient
```

### 서버 IP·Port 변경 (선택)

`server_info.dat` 파일을 클라이언트 실행 경로에 생성합니다.

```properties
SERVER_IP=192.168.0.10
SERVER_PORT=1235
```

---

## 디렉토리 구조

```
quizgame_computernetwork/
├── QuizServer.java   # 서버 — 퀴즈 출제, 채점, 클라이언트 연결 처리
└── QuizClient.java   # 클라이언트 — 서버 접속, 문제 수신, 답 전송
```

---

## 실행 화면

![서버 실행 화면](https://github.com/user-attachments/assets/23dc8937-6fec-483c-81cd-e55ec6a01c6c)

![클라이언트 실행 화면](https://github.com/user-attachments/assets/7ef10f6d-3034-4d86-9599-87ebdcc558b7)

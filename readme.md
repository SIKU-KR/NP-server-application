## 프로젝트 개요
**네트워크프로그래밍 (2024년 2학기) 팀프로젝트 과제 리포지토리입니다.**

스무고개 게임을 구현하여 2명 이상의 사용자가 채팅 서비스에 접속하여 대화를 주고받는 프로그램입니다. 문제의 출제자는 ‘네’, ‘아니오’, ‘정답입니다’ 에 대한 대답을 통해서 프로그램이 수행되며, 1명 이상의 참여자는 20번의 질문 기회를 통해 정답을 맞추며 프로그램을 수행할 수 있습니다. 이 프로그램은 2인 이상의 단체 채팅을 구현합니다.

**개발기간 : 2024년 10월 ~ 12월**

- JDK17
- IntelliJ IDEA 
- Gradle Project
- MySQL
- Docker

## 내용
- 통신 specification DTO 설계
- TCP/IP 프로토콜 통신 인터페이스
- 서버 소켓(PORT: 10001) 밑 요청 핸들러
- 챗 스레드를 관리하는 Map 자료구조
- 스레드 안전한 메시지 큐 시스템
- 명령어 처리 시스템
- 데이터베이스 및 인메모리 저장
- 테스트코드
- 도커파일

## 회고 블로그
[블로그 포스팅 링크](https://cseant.tistory.com/17)

## 시연 영상
[유튜브 링크](https://youtu.be/RykwKWamBDk)

--- 
> 아래 내용은 팀프로젝트에서 소통한 내용입니다. 

## DB 가이드라인

1. `src/main/resources/application.properties` 에서 DB 연결정보를 설정합니다.
2. `sql/create.sql`을 참고해서 데이터베이스 테이블을 만듭니다.
3. `sql/example_insert.sql`을 참고해서 임시 데이터를 삽입합니다.

## 현재 사용가능한 API

> core.dto 패키지는 기본적으로 복사해서 클라이언트 프로젝트에 붙여넣기 해주세요!
 
> Socket을 통해 DTO에 보내면 됩니다 (localhost:10001)

1. 현재 게임 방 목록 가져오기
```java
new DTO(RequestType.LISTROOM, null);
```

2. 새로운 게임 방 만들기 
```java
new DTO(RequestType.NEWROOM, new NewRoom("방장이름", "방제목"));
```

3. 채팅 연결하기
```java
int userId = 1;
int chatId = 1;
new DTO(RequestType.CONNECTCHAT, new ChatConnection(userId, chatId));
```

4. 로그인하기
```java
new DTO(RequestType.LOGIN, new UserLogin("Charlie"));
```

5. 사용자 목록가져오기
```java
int chatId = 1;
new DTO(RequestType.LISTUSER, Integer.of(chatId));
```

> 클라이언트 개발에 필요한 API 목록을 카톡으로 말해주시면 만들게요.

## json 주고 받기

> core.common 패키지와 core.view 패키지를 참고해주세요. 같은 방법으로 클라이언트에서도 구현하는 걸 추천드립니다.

> gson 라이브러리를 사용합니다. `build.gradle`을 통해 이를 설치해주세요.

### LocalDateTime 사용을 위한 Adapter 설정
```java
public class GsonLocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        out.value(value.format(formatter));
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        return LocalDateTime.parse(in.nextString(), formatter);
    }
}
```

### gson 객체 만들기
```java
public class GsonProvider {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
            .create();

    public static Gson getGson() {
        return gson;
    }
}
```
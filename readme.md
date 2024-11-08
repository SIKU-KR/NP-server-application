## 프로젝트 개요
- JDK17
- IntelliJ IDEA 
- Gradle Project
- MySQL(or MaridDB)

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
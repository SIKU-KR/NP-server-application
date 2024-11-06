# Project
- jdk 17
- IntelliJ IDEA 
- Gradle Project
- use MySQL

```
└── core
    ├── ServerApplication.java
    ├── common
    │   ├── AppLogger.java
    │   ├── MsgQueue.java
    │   └── RequestType.java
    ├── controller
    │   ├── ChatThreadsController.java
    │   └── ServerSocketController.java
    ├── dto
    │   ├── DTO.java
    │   ├── Message.java
    │   └── requestmsg
    │       ├── ChatConnection.java
    │       ├── ChatRoom.java
    │       ├── NewRoom.java
    │       └── User.java
    ├── model
    │   ├── ChatModel.java
    │   ├── DBConnection.java
    │   ├── RoomModel.java
    │   └── UserModel.java
    └── runnable
        ├── ConnectChatConnectionThread.java
        ├── ListRoomConnectionThread.java
        ├── MsgQueueConsumer.java
        └── NewRoomConnectionThread.java


```
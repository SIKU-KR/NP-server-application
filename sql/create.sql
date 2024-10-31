CREATE TABLE User
(
    id    INT PRIMARY KEY,
    name  VARCHAR(255),
    score INT
);

CREATE TABLE Chat
(
    id        INT PRIMARY KEY,
    title     VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Message
(
    id        INT PRIMARY KEY,
    text      VARCHAR(255),
    chat_id   INT,
    user_id   INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_id) REFERENCES Chat (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE ON UPDATE CASCADE
);
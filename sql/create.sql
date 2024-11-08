CREATE TABLE User
(
    name  VARCHAR(255) PRIMARY KEY,
    score INT
);

CREATE TABLE Chat
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    title     VARCHAR(255),
    creator   VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creator) REFERENCES User (name)
);

CREATE TABLE Message
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    text      VARCHAR(255),
    chat_id   INT,
    user_name VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_id) REFERENCES Chat (id),
    FOREIGN KEY (user_name) REFERENCES User (name)
);
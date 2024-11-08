INSERT INTO User (name, score)
VALUES
    ('Alice', 1200),
    ('Bob', 950),
    ('Charlie', 1100);

INSERT INTO Chat (title, creator)
VALUES
    ('General Discussion', 'Alice'),
    ('Gaming', 'Bob'),
    ('Programming', 'Charlie');

INSERT INTO Message (text, chat_id, user_name)
VALUES
    ('Hello everyone!', 1, 'Alice'),
    ('Welcome to the chat!', 1, 'Bob'),
    ('Anyone here into games?', 2, 'Bob'),
    ('Yes! What games do you play?', 2, 'Charlie'),
    ('This is a programming chat', 3, 'Charlie'),
    ('I need help with SQL', 3, 'Alice');
### database
```
CREATE TABLE users (
    id int auto_increment primary key,
    name varchar(50) not null,
    password varchar(255),
    role varchar(50),
    created_at timestamp default current_timestamp
    );

CREAT TABLE msgs (
    id int auto_increment primary key,
    user_name varchar(50),
    message text,
    created_at timestamp default current_timestamp
    );

INSERT INTO users (name,password,role) VALUES ('Bob','1234','ADMIN');
INSERT INTO users (name,password,role) VALUES ('Joe','1234','USER');
```

### nice to have, todo, open
* messages table: foreign key of user id, instead of 'unreferenced' user name
* check mysql auto increment bug of users.id and msgs.id (try change stragegy from AUTO to TABLE)

### test
localhost:8080

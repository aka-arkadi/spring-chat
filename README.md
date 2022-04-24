### framework dependencies (start.spring.io)
* spring web
* spring security
* spring data jpa
* mysql driver

### database
```
CREATE TABLE users (
    id int auto_increment primary key,
    name varchar(50) not null,
    password varchar(255),
    role varchar(50),
    created_at timestamp default current_timestamp
    );

CREATE TABLE msgs (
    id int auto_increment primary key,
    user_name varchar(50),
    message text,
    created_at timestamp default current_timestamp
    );

INSERT INTO users (name,password,role) VALUES ('Bob','1234','ADMIN');
INSERT INTO users (name,password,role) VALUES ('Joe','1234','USER');
INSERT INTO msgs (user_name,message) VALUES ('Bob','Hi, i am Bob.');
INSERT INTO msga (user_name,message) VALUES ('Joe','Hi Bob, im Joe.');
```

### test
localhost:8080

### nice to have, todo, open
* use views or resources instead of hardcoding html
* messages table: foreign key of user id, instead of unreferenced user name
* messages: make date look nice
* messages: replace url 'msgs' with 'chat/page/0'
* messages: add user authentication for msgs/chat
* user register: check for forbidden names
* html template: replace all mapping functions 'htmlStart()' -> 'start(request)' 
* check csrf security issue (now: disabled in security configuration)
* change password encoder (blank -> bcrypt)

### done
* mysql auto increment bug solved: strategy.IDENTITY
* admin: user overview with delete user function
* admin: message delete
* admin: make user admin
* user: account delete
* user: message delete
* include request object and authentication in url-mapping functions
* chat: page functionality for messages
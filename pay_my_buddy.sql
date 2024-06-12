CREATE DATABASE IF NOT EXISTS paymybuddy;

USE paymybuddy;

CREATE TABLE User (
    user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE UserConnection (
    connection_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user1_id INT NOT NULL,
    user2_id INT NOT NULL,
    FOREIGN KEY (user1_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (user2_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE Transaction (
    transaction_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES User(user_id) ON DELETE CASCADE,
    description VARCHAR(255),
    amount DECIMAL(16, 2) NOT NULL
);


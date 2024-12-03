use vcrts;

 CREATE TABLE users (
    userId VARCHAR(36) PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    username VARCHAR(50) UNIQUE,
    email VARCHAR(100),
    dob DATE,
    password VARCHAR(100),
    accountType VARCHAR(20)
); 
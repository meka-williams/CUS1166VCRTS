use vcrts;

CREATE TABLE jobrequests (
    jobId VARCHAR(36) PRIMARY KEY,
    clientId VARCHAR(36),
    jobDescription TEXT,
    duration INT,
    redundancyLevel INT,
    jobDeadline DATE,
    timestamp TIMESTAMP
); 
use vcrts;

CREATE TABLE jobrequests (
    generatedJobId VARCHAR(36) PRIMARY KEY,
    clientId VARCHAR(36),
    jobId TEXT,
    duration INT,
    redundancyLevel INT,
    jobDeadline DATE,
    timestamp TIMESTAMP
); 
use vcrts;

 CREATE TABLE carrentals (
    carId INT AUTO_INCREMENT PRIMARY KEY,
    ownerId VARCHAR(36),
    model VARCHAR(50),
    brand VARCHAR(50),
    plateNumber VARCHAR(20),
    serialNumber VARCHAR(50),
    vinNumber VARCHAR(50),
    residencyTime TIMESTAMP
); 
CREATE TABLE EstateAgent (
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL,
  login VARCHAR(255) PRIMARY KEY NOT NULL,
  password VARCHAR(255) NULL
);

CREATE TABLE Estate (
  ID int NOT NULL PRIMARY KEY,
  city VARCHAR(255) NOT NULL,
  postalCode VARCHAR(5) NOT NULL,
  street VARCHAR(255) NOT NULL,
  streetNumber int NOT NULL,
  squareArea int NOT NULL,
  agent VARCHAR(255),
  FOREIGN KEY (agent) REFERENCES EstateAgent(login)
);

CREATE TABLE Apartment (
  ID int NOT NULL PRIMARY KEY,
  floor int NOT NULL,
  rent int NOT NULL,
  rooms int NOT NULL,
  balcony SMALLINT NOT NULL,
  builtInKitchen SMALLINT NOT NULL ,
  FOREIGN KEY (ID) REFERENCES Estate(ID)
);

CREATE TABLE House (
  ID int NOT NULL PRIMARY KEY,
  price int NOT NULL,
  garden SMALLINT NOT NULL,
  FOREIGN KEY (ID) REFERENCES Estate(ID)
);

CREATE TABLE Contract (
  contractNumber int NOT NULL PRIMARY KEY,
  date DATE NOT NULL,
  place VARCHAR(255) NOT NULL
);

CREATE TABLE PurchaseContract (
  contractNumber int NOT NULL PRIMARY KEY,
  numberOfInstallments int NOT NULL,
  interestRate int NOT NULL,
  FOREIGN KEY (contractNumber) REFERENCES Contract(contractNumber)
);

CREATE TABLE TenancyContract (
  contractNumber int NOT NULL PRIMARY KEY,
  startDate DATE NOT NULL,
  duration int NOT NULL,
  additionalCosts int NOT NULL,
  FOREIGN KEY (contractNumber) REFERENCES Contract(contractNumber)
);

CREATE TABLE Person (
  ID int NOT NULL PRIMARY KEY,
  firstName VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL
);

CREATE TABLE Sales (
  contractNumber int NOT NULL PRIMARY KEY,
  personID int NOT NULL,
  house int UNIQUE NOT NULL,
  FOREIGN KEY (contractNumber) REFERENCES PurchaseContract(contractNumber),
  FOREIGN KEY (personID) REFERENCES Person(ID),
  FOREIGN KEY (house) REFERENCES House(ID)
);

CREATE TABLE Rentals (
  contractNumber int NOT NULL PRIMARY KEY,
  personID int NOT NULL,
  apartment int UNIQUE NOT NULL,
  FOREIGN KEY (contractNumber) REFERENCES TenancyContract(contractNumber),
  FOREIGN KEY (personID) REFERENCES Person(ID),
  FOREIGN KEY (apartment) REFERENCES Apartment(ID)
);
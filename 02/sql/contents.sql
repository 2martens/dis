INSERT INTO VSISP12.ESTATEAGENT (name, address, login, password) VALUES
  ('Max Mustermann', 'Musterstraße 12, 22527 Hamburg', 'muster', 'retsum134');

INSERT INTO VSISP12.ESTATE (city, postalCode, street, streetNumber, squareArea, agent) VALUES
  ('Hamburg', '20146', 'Von-Melle-Park', 5, 12000, 1),
  ('Hamburg', '22455', 'Paul-Sorge-Straße', 49, 124, 1)
;

INSERT INTO VSISP12.HOUSE (id, garden, price) VALUES
  (1, 1, 1000000);

INSERT INTO VSISP12.APARTMENT (id, floor, rent, rooms, balcony, builtInKitchen) VALUES
  (2, 2, 1000, 4, 1, 1);

INSERT INTO VSISP12.PERSON (name, firstName, address) VALUES
  ('Trump', 'Donald', 'WhiteHouse, Washington D.C.'),
  ('Merkel', 'Angela', 'Kanzleramt, Berlin');

INSERT INTO VSISP12.CONTRACT (date, place) VALUES
  (DATE('2017-01-20'), 'Washington, D.C.'),
  (DATE('1970-05-08'), 'Hamburg');

INSERT INTO VSISP12.PURCHASECONTRACT (contractNumber, numberOfInstallments, interestRate) VALUES
  (1, 42, 2);

INSERT INTO VSISP12.TENANCYCONTRACT (contractNumber, startdate, duration, additionalCosts) VALUES
  (2, TIMESTAMP('1970-06-01'), NULL, 0);

INSERT INTO VSISP12.RENTALS (contractNumber, person, apartment) VALUES
  (2, 2, 2);

INSERT INTO VSISP12.SALES (contractNumber, person, house) VALUES
  (1, 1, 1);
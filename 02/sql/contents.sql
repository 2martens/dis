INSERT INTO VSISP12.ESTATEAGENT (name, address, login, password) VALUES
  ('Max Mustermann', 'Musterstraße 12, 22527 Hamburg', 'muster', 'retsum134');

INSERT INTO VSISP12.ESTATE (id, city, postalCode, street, streetNumber, squareArea, agent) VALUES
  (1, 'Hamburg', '20146', 'Von-Melle-Park', 5, 12000, 'muster'),
  (2, 'Hamburg', '22455', 'Paul-Sorge-Straße', 49, 124, 'muster')
;

INSERT INTO VSISP12.HOUSE (id, garden, price) VALUES
  (1, 1, 1000000);

INSERT INTO VSISP12.APARTMENT (id, floor, rent, rooms, balcony, builtInKitchen) VALUES
  (2, 2, 1000, 4, 1, 1);

INSERT INTO VSISP12.PERSON (id, name, firstName, address) VALUES
  (1, 'Trump', 'Donald', 'WhiteHouse, Washington D.C.'),
  (2, 'Merkel', 'Angela', 'Kanzleramt, Berlin');

INSERT INTO VSISP12.CONTRACT (contractNumber, date, place) VALUES
  (42, DATE('2017-01-20'), 'Washington, D.C.'),
  (24, DATE('1970-05-08'), 'Hamburg');

INSERT INTO VSISP12.PURCHASECONTRACT (contractNumber, numberOfInstallments, interestRate) VALUES
  (42, 42, 2);

INSERT INTO VSISP12.TENANCYCONTRACT (contractNumber, startdate, duration, additionalCosts) VALUES
  (24, DATE('1970-06-01'), -1, 0);

INSERT INTO VSISP12.RENTALS (contractNumber, personID, apartment) VALUES
  (24, 2, 2);

INSERT INTO VSISP12.SALES (contractNumber, personID, house) VALUES
  (42, 1, 1);
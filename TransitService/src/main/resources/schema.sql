DROP TABLE IF EXISTS qrCodeReaders;
DROP TABLE IF EXISTS transits;

CREATE TABLE qrCodeReaders
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    password varchar (255),
    active INT
);

CREATE TABLE transits
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    timestamp DATE,
    UserID BIGINT,
    jws varchar (255)
);
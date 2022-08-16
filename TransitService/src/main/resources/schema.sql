DROP TABLE IF EXISTS qrCodeReaders;
DROP TABLE IF EXISTS transits;

CREATE TABLE qrCodeReaders
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    pwd varchar (255)
);

CREATE TABLE transits
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    timestamp DATE,
    UserID BIGINT,
    jws varchar (255),
    readerID BIGINT,
    valid BOOLEAN
);
DROP TABLE IF EXISTS qrCodeReaders;
-- DROP TABLE IF EXISTS transits;


CREATE TABLE IF NOT EXISTS qrCodeReaders
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    jwt varchar (255),
    zone varchar (255)
);

CREATE TABLE IF NOT EXISTS transits
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    timestamp TIMESTAMP,
    UserID BIGINT,
    jws varchar (255),
    readerID BIGINT,
    valid BOOLEAN
);
-- DROP TABLE IF EXISTS ticketOrder;
-- DROP TABLE IF EXISTS ticketType;

CREATE TABLE IF NOT EXISTS ticketType
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    ticketType varchar (255),
    price float,
    ageBelow int,
    duration int,
    zones varchar(255)
);

CREATE TABLE IF NOT EXISTS ticketOrder
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    userId BIGINT,
    ticketTypeId BIGINT,
    ticketNumber BIGINT,
    status VARCHAR(255),
    FOREIGN KEY (ticketTypeId) REFERENCES ticketType(id)
);
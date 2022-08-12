DROP TABLE IF EXISTS ticketPurchased;
DROP TABLE IF EXISTS userDetails;
DROP TABLE IF EXISTS userProfile;

CREATE TABLE userDetails
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    username varchar (255),
    roles varchar (255)
);

CREATE TABLE ticketPurchased
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    iat timestamp,
    exp timestamp,
    zid varchar (255),
    validFrom timestamp,
    ticketType varchar (255),
    jws varchar (255),
    userId BIGSERIAL,
    FOREIGN KEY (userId) REFERENCES userDetails(id)
);

CREATE TABLE userProfile
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name varchar (255),
    address varchar (255),
    date_of_birth timestamp,
    telephone_number varchar (255)
);
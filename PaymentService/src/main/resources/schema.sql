DROP TABLE IF EXISTS Payment;

CREATE TABLE Payment
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    userId BIGINT
);
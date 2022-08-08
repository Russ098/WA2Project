create table if not exists users
(
    id bigint not null
    constraint users_pkey
    primary key,
    email varchar(255),
    password varchar(255),
    pending boolean not null,
    username varchar(255)
    );

alter table users owner to postgres;

create table if not exists activation
(
    id uuid not null
    constraint activation_pkey
    primary key,
    activation_code varchar(255),
    attempts integer not null,
    expiration_time timestamp,
    user_id bigint
    constraint fk962x2w5lhpof6gmaply4y55wk
    references users
    );

alter table activation owner to postgres;

create sequence if not exists hibernate_sequence;

alter sequence hibernate_sequence owner to postgres;


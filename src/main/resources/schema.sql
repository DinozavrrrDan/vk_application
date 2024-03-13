create table if not exists user_accounts
(
    id serial primary key,
    name varchar unique,
    password varchar,
    roles varchar
);


create table if not exists audit
(
    id serial primary key,
    url varchar,
    request_type varchar,
    request_param text,
    response_param text,
    created_by varchar,
    created_by_role varchar,
    created_date timestamp
);


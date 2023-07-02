create table news
(
    is_archive boolean       not null,
    time       timestamp(6),
    id         uuid          not null
        primary key,
    title      varchar(50)   not null,
    text       varchar(1000) not null
);

alter table news
    owner to postgres;

create table comments
(
    time     timestamp(6),
    id       uuid          not null
        primary key,
    news_id  uuid
        constraint fkqx89vg0vuof2ninmn5x5eqau2
            references news,
    text     varchar(1000) not null,
    username varchar(255)  not null
);

alter table comments
    owner to postgres;


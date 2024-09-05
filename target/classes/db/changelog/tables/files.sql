create table files
(
    filename      VARCHAR(255) not null,
    date          TIMESTAMP(6)  not null,
    file_content  bytea     not null,
    size          BIGINT       not null,
    user_username VARCHAR(255),
    primary key (filename)
);
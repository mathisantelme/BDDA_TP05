connect 'jdbc:derby://localhost:1527/bookstore;create=true';

create table BOOK
(
    ISBN   VARCHAR(20) not null primary key,
    TITLE  VARCHAR(80),
    AUTHOR VARCHAR(20),
    PRICE  DOUBLE      not null
);

exit;
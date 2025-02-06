create table carros(

    id bigint not null auto_increment,
    modelo varchar(100) not null unique,
    valor_dia float not null,
    unidades Integer not null,
    ativo tinyint,
    disponivel Integer,

    primary key(id)

);
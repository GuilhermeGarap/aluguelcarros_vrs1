CREATE TABLE carros (
    id SERIAL PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL UNIQUE,
    valor_dia FLOAT NOT NULL,
    unidades INTEGER NOT NULL,
    ativo BOOLEAN,  
    disponivel INTEGER
);

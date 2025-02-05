CREATE TABLE alugueis (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, -- Para auto incremento
    data_inicio VARCHAR(10) NOT NULL,
    data_termino VARCHAR(10) NOT NULL,
    ativo BOOLEAN,  -- Substituindo TINYINT por BOOLEAN
    
    cliente_id BIGINT NOT NULL,
    carro_id BIGINT NOT NULL,

    CONSTRAINT fk_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id),

    CONSTRAINT fk_carro
        FOREIGN KEY (carro_id)
        REFERENCES carros(id)
);

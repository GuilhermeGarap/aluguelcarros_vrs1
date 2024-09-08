CREATE TABLE alugueis (
    id BIGINT NOT NULL AUTO_INCREMENT,
    data_inicio VARCHAR(10) NOT NULL,
    data_termino VARCHAR(10) NOT NULL,
    ativo TINYINT,
    
    cliente_id BIGINT NOT NULL,
    carro_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    
    CONSTRAINT fk_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id),

    CONSTRAINT fk_carro
        FOREIGN KEY (carro_id)
        REFERENCES carros(id)
);

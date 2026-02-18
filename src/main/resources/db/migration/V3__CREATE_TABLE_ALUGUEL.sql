CREATE TABLE alugueis (
                          id              BIGSERIAL PRIMARY KEY,
                          data_inicio     DATE NOT NULL,
                          data_termino    DATE NOT NULL,
                          ativo           BOOLEAN DEFAULT TRUE,
                          cliente_id      BIGINT NOT NULL,
                          carro_id        BIGINT NOT NULL,

                          CONSTRAINT fk_alugueis_cliente_id FOREIGN KEY (cliente_id) REFERENCES clientes (id) ON DELETE CASCADE,
                          CONSTRAINT fk_alugueis_carro_id FOREIGN KEY (carro_id) REFERENCES carros (id) ON DELETE RESTRICT,

                          CONSTRAINT check_datas CHECK (data_termino >= data_inicio)
);
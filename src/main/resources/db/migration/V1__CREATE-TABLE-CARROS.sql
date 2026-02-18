CREATE TABLE carros (
                        id         BIGSERIAL PRIMARY KEY,
                        modelo     VARCHAR(255) NOT NULL,
                        valor_dia  REAL NOT NULL,
                        unidades   INTEGER NOT NULL CHECK (unidades >= 0),
                        ativo      BOOLEAN NOT NULL DEFAULT TRUE,
                        disponivel INTEGER NOT NULL CHECK (disponivel <= unidades)
);
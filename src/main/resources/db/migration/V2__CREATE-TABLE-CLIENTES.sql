CREATE TABLE clientes
(
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(255) NOT NULL,
    telefone        VARCHAR(20),
    email           VARCHAR(100) NOT NULL UNIQUE,
    cpf             VARCHAR(14)  NOT NULL UNIQUE,
    data_nascimento DATE,
    ativo           BOOLEAN DEFAULT TRUE,

    logradouro      VARCHAR(255),
    bairro          VARCHAR(100),
    cep             VARCHAR(9),
    numero          VARCHAR(20),
    complemento     VARCHAR(100),
    cidade          VARCHAR(100),
    uf              CHAR(2)

);
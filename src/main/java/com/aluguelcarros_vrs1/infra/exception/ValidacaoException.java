package com.aluguelcarros_vrs1.infra.exception;


public class ValidacaoException extends RuntimeException {

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }

}
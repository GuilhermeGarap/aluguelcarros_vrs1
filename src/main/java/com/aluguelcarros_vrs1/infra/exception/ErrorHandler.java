package com.aluguelcarros_vrs1.infra.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ErrorHandler {

    // 400 - Erros de Validação de Campos (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorDto>> handle400(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors().stream()
                .map(ValidationErrorDto::new)
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    // 401 - Erros de Autenticação (Login)
    @ExceptionHandler({
            BadCredentialsException.class,
            AuthenticationException.class
    })
    public ResponseEntity<StandardErrorResponse> handleAuthErrors(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Usuário ou senha inválidos", request);
    }

    // 403 - Erros de Permissão
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "Forbidden", getMessageOrFallback(ex, "Acesso negado"), request);
    }

    // 404 - Recurso não encontrado
    @ExceptionHandler({EntityNotFoundException.class, NotFoundException.class})
    public ResponseEntity<StandardErrorResponse> handle404(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", getMessageOrFallback(ex, "Recurso não encontrado"), request);
    }

    // 400 - Erros de Lógica de Negócio ou Argumentos Inválidos
    @ExceptionHandler({ErrorDetailsException.class, IllegalArgumentException.class})
    public ResponseEntity<StandardErrorResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request);
    }

    // 400 - JSON malformado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardErrorResponse> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", "Corpo da requisição inválido ou malformado", request);
    }

    // 413 - Arquivo muito grande
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<StandardErrorResponse> handleMaxUploadSize(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.PAYLOAD_TOO_LARGE, "Payload Too Large", "Arquivo excede o limite permitido", request);
    }

    // 500 - Erro Genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Erro interno no servidor", request);
    }

    private String getMessageOrFallback(Exception ex, String fallback) {
        return (ex.getMessage() != null && !ex.getMessage().isBlank()) ? ex.getMessage() : fallback;
    }

    private ResponseEntity<StandardErrorResponse> buildResponse(HttpStatus status, String error, String message, HttpServletRequest request) {
        var response = new StandardErrorResponse(
                Instant.now(),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }

    public record ValidationErrorDto(String field, String message) {
        public ValidationErrorDto(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    public record StandardErrorResponse(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path
    ) {}
}
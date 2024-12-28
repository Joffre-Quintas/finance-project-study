package br.com.finance.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorRecordResponse> handlerNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorRecordResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(ConflictArgumentException.class)
    public ResponseEntity<ErrorRecordResponse> handlerConflictArgumentException(ConflictArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorRecordResponse(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorRecordResponse> handlerAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorRecordResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}

package com.ada.genealogyapp.exceptions;

import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ExceptionHandlers {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message("An unexpected error occurred: " + ex.getMessage()).build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message("Required request parameter '" + ex.getParameterName() + "' is not present").build());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ExceptionResponse> handleUserValidationException(UserValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(NodeAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleNodeAlreadyExistsException(NodeAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(NodeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNodeNotFoundException(NodeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(EventTypeApplicableException.class)
    public ResponseEntity<ExceptionResponse> handleEventTypeApplicableException(EventTypeApplicableException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<ExceptionResponse> handleRollbackException(RollbackException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(NodeAlreadyInNodeException.class)
    public ResponseEntity<ExceptionResponse> handleNodeAlreadyInNodeException(NodeAlreadyInNodeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ExceptionResponse> handleStorageException(StorageException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleStorageFileNotFoundException(StorageFileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(DateException.class)
    public ResponseEntity<ExceptionResponse> handleDateException(DateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(Neo4jException.class)
    public ResponseEntity<ExceptionResponse> handleNeo4jException(Neo4jException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .errorTime(LocalDateTime.now().format(formatter))
                        .message("A database error occurred: " + ex.getMessage()).build());
    }
}



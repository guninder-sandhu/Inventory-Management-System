package com.inventory.product.exceptions;

import com.inventory.product.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                null // No data
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeletionException.class)
    public ResponseEntity<ApiResponse<Void>> handleDeletionException(DeletionException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                null // No data
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CreationException.class)
    public ResponseEntity<ApiResponse<Void>> handleCreationException(CreationException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                ex.getMessage(),
                ex.getStatus().value(),
                LocalDateTime.now(),
                null // No data
        );
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                null // No data
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RetrievalException.class)
    public ResponseEntity<ApiResponse<Void>> handleRetrievalException(RetrievalException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                null // No data
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<ApiResponse<Void>> handleUpdateException(UpdateException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                null // No data
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

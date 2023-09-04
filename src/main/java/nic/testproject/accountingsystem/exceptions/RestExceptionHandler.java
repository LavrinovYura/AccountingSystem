package nic.testproject.accountingsystem.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        String message = exception.getMessage();
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.NOT_FOUND);
}

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException exception) {
        String message = exception.getMessage();
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        String message = exception.getMessage();
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> jwtException(ExpiredJwtException exception) {
        String message = exception.getMessage();
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException exception) {
        String message = exception.getMessage();
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(ConstraintViolationException exception) {
        List<ValidationError> validationErrors = new ArrayList<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            validationErrors.add(new ValidationError(field, message));
        }

        return new ResponseEntity<>(new ValidationErrorResponse(validationErrors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<ValidationError> validationErrors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach((error) -> validationErrors.add(new ValidationError(error.getField(), error.getDefaultMessage())));
        return new ResponseEntity<>(new ValidationErrorResponse(validationErrors), HttpStatus.BAD_REQUEST);
    }

    @Getter
    @Setter
    static class ErrorResponse {
        private String message;
        private long timestamp;

        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
    }

    @Getter
    static class ValidationErrorResponse {
        private final List<ValidationError> errors;

        public ValidationErrorResponse(List<ValidationError> errors) {
            this.errors = errors;
        }
    }

    @Getter
    @Setter
    static class ValidationError {
        private String field;
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}

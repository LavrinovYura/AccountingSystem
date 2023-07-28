package nic.testproject.accountingsystem.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String field, String message) {
        super(String.format("Validation error in field '%s': %s", field, message));
    }
}


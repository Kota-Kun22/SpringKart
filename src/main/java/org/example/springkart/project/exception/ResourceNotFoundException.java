package org.example.springkart.project.exception;

public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException() {

    }

    public ResourceNotFoundException(String message, String resourceName, String field, String fieldName) {//constructor 1
        super(message);
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {//constructor 2
        super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

}



package org.example.springkart.project.exception;

public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException() {

    }

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
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



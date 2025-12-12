package com.bank.uploadfileanddatapersistwithmongodb.exception;

public class SchemaValidationException extends FileProcessingException {
    public SchemaValidationException(String message) { super(message); }
    public SchemaValidationException(String message, Throwable cause) { super(message, cause); }
}
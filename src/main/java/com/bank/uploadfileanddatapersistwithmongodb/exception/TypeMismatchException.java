package com.bank.uploadfileanddatapersistwithmongodb.exception;

public class TypeMismatchException extends ValidationException {
    public TypeMismatchException(String message, Throwable cause) { super(message, cause); }
}

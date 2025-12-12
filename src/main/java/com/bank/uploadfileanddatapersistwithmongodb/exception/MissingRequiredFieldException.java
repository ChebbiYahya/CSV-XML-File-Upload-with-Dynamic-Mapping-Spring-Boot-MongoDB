package com.bank.uploadfileanddatapersistwithmongodb.exception;

public class MissingRequiredFieldException extends ValidationException {
    public MissingRequiredFieldException(String message) { super(message); }
}
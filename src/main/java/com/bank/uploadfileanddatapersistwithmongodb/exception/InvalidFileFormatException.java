package com.bank.uploadfileanddatapersistwithmongodb.exception;

public class InvalidFileFormatException extends RuntimeException {

    public InvalidFileFormatException(String message) {
        super(message);
    }
}
package com.hallym.rehab.global.exception;

public class IncorrectNameException extends Exception {

    public IncorrectNameException() {
        super("현재 이름이 일치하지 않습니다.");
    }

    public IncorrectNameException(String message) {
        super(message);
    }
}
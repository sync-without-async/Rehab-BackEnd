package com.hallym.rehab.global.exception;

public class IncorrectPasswordException extends Exception {

    public IncorrectPasswordException() {
        super("현재 비밀번호가 일치하지 않습니다.");
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
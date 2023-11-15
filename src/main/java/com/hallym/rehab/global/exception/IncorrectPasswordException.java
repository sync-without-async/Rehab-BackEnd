package com.hallym.rehab.global.exception;

/**
 * @author 이동헌
 * 비밀번호가 틀렸을 시 발생하는 예외.
 */
public class IncorrectPasswordException extends RuntimeException {

    /**
     * 비밀번호가 불일치인 경우 문자열을 출력한다.
     *
     */
    public IncorrectPasswordException() {
        super("현재 비밀번호가 일치하지 않습니다.");
    }

}
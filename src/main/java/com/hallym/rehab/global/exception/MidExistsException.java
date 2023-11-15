package com.hallym.rehab.global.exception;

/**
 * @author 이동헌
 * 해당 ID가 이미 존재할 때 발생하는 예외.
 */
public class MidExistsException extends RuntimeException{

    /**
     * 입력한 ID를 받아 예외를 생성한다.
     *
     * @param mid 회원가입 시, 사용하려는 ID
     */
    public MidExistsException(String mid) {
        super("해당 ID(" + mid + ")는 이미 존재합니다.");
    }
}

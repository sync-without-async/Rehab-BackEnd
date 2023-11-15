package com.hallym.rehab.global.exception;

/**
 * @author 이동헌
 * 특정 의료진 정보를 찾을 수 없을 때 발생하는 예외.
 */
public class StaffNotFoundException extends RuntimeException {

    /**
     * Staff ID를 받아 예외를 생성한다.
     *
     * @param mid 찾을 수 없는 Staff의 ID
     */
    public StaffNotFoundException(String mid, Throwable cause) {
        super("해당 Staff의 ID(" + mid + ")를 찾을 수 없습니다.", cause);
    }
}
package com.hallym.rehab.global.exception;

/**
 * @author 이동헌
 * 파일 업로드 시, 이미지 파일이 아닌 경우 발생하는 에러
 */
public class IsNotImageFileException extends RuntimeException {

    /**
     * 입력한 ID를 받아 예외를 생성한다.
     *
     */
    public IsNotImageFileException(String fileName) {
        super("선택한 파일 (" + fileName + ") 은 이미지 파일이 아닙니다.");
    }
}

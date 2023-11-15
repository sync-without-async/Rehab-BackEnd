package com.hallym.rehab.global.advice;


import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Log4j2
public class CustomRestControllerAdvice {

//    데이터 바인딩 실패 시, 에러가 발생한 필드 정보를 클라이언트에게 전달
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleBindException(BindException e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        if(e.hasErrors()){

            BindingResult bindingResult = e.getBindingResult();

            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }

        return ResponseEntity.badRequest().body(errorMap);
    }

//  JPA에서 제공하는 에러,  데이터의 무결성을 위반하려고 할 때 발생
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleFKException(Exception e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg",  "constraint fails");
        return ResponseEntity.badRequest().body(errorMap);
    }

//     데이터를 조회하려 했지만, 예상한 데이터가 없을 때 발생하는 예외
    @ExceptionHandler({
            NoSuchElementException.class,
            EmptyResultDataAccessException.class })
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED) //return 타입은 상태코드가 떠야함
    public ResponseEntity<Map<String, String>> handleNoSuchElement(Exception e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg",  "No Such Element Exception");
        return ResponseEntity.badRequest().body(errorMap);
    }


//    사용자 인증에 실패했을 때 발생, 모든 컨트롤러에서 발생하는 BadCredentialsException을 처리
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 Unauthorized 상태 코드 반환
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg",  "현재 비밀번호가 일치하지 않습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
    }
}

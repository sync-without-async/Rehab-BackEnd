package com.hallym.rehab;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class TestController {

    @RequestMapping("/test")
    public int[] test() throws Exception {
        int[] message = {1,2,3,4,5,6,7,8,9};

        return message;
    }
}

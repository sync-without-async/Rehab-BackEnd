package com.hallym.rehab;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootTest
@EnableJpaAuditing //AuditingEntityListener 활성화
class RehabApplicationTests {

	@Test
	void contextLoads() {
	}

}

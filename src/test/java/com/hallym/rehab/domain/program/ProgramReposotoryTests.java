package com.hallym.rehab.domain.program;

import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Position;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.program.repository.VideoRepository;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class ProgramReposotoryTests {

    @Autowired
    private ProgramRepository programRepository;

    @Test
    public void registerProgram() throws Exception {

        Program program = Program.builder()
                .programTitle("sample title")
                .description("Program test")
                .category(Category.ARMS)
                .position(Position.SITTING)
                .build();

        programRepository.save(program);

        log.info(program.toString());
    }
}

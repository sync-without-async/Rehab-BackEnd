package com.hallym.rehab.domain.program;

import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Position;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.repository.ProgramRepository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ProgramRepositoryTests {

    @Autowired
    private ProgramRepository programRepository;

    @Test
    public void registerProgramTest() throws Exception {

        Program program = Program.builder()
                .programTitle("sample title")
                .description("Program test")
                .category(Category.SHOULDERS)
                .position(Position.LYING)
                .build();

        programRepository.save(program);

        log.info(program.toString());
    }

    @Test
    public void testInsertDummyTest() throws Exception {

        IntStream.rangeClosed(1, 9).forEach(i -> {

            Program result = programRepository.save(Program.builder()
                    .programTitle("sample title")
                    .description("Program test")
                    .category(Category.THIGHS)
                    .position(Position.LYING)
                    .build());

            log.info(result.toString());
        });

    }
}

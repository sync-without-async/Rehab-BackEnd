package com.hallym.rehab.domain.program;

import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramVideo;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.program.repository.ProgramVideoRepository;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Log4j2
public class ProgramReposotoryTests {

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProgramVideoRepository programVideoRepository;

    @Test
    public void resgisterProgram() throws Exception{

        String id = "testuser";
        Optional<Member> result = memberRepository.findById(id);

        Member member = result.orElseThrow();

        Program program = Program.builder()
                .member(member)
                .programTitle("sample title")
                .Category(Category.UPPER)
                .description("Program test")
                .build();

        programRepository.save(program);

        log.info(program.toString());


        ProgramVideo programVideo =
                new ProgramVideo(1L,"tes-a2asd-videotest.mp4","satarew-asf-awe.json", "O1", "O2", program);

        programVideoRepository.save(programVideo);

        log.info(programVideo.toString());
    }

}

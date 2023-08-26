package com.hallym.rehab.repository;

import com.hallym.rehab.domain.program.entity.Category;
import com.hallym.rehab.domain.program.entity.Position;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.program.repository.VideoRepository;
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
    private VideoRepository videoRepository;

    @Test
    public void resisterProgram() throws Exception{

        String id = "adminuser";
        Optional<Member> result = memberRepository.findById(id);

        Member member = result.orElseThrow();

        Program program = Program.builder()
//                .member(member)
                .programTitle("sample title")
                .description("Program test")
                .category(Category.ARMS)
                .position(Position.SITTING)
                .build();

        programRepository.save(program);

        log.info(program.toString());

//        ProgramVideo programVideo = new ProgramVideo(1L,"tes-a2asd-videotest.mp4","satarew-asf-awe.json",program);
//
//        programVideoRepository.save(programVideo);
//
//        log.info(programVideo.toString());
    }

}

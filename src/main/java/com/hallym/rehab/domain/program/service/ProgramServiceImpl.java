package com.hallym.rehab.domain.program.service;


import com.hallym.rehab.domain.program.dto.act.ActResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramDetailResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramMainResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.Program_Member;
import com.hallym.rehab.domain.program.entity.Video;
import com.hallym.rehab.domain.program.entity.Video_Member;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.program.repository.Program_MemberRepository;
import com.hallym.rehab.domain.program.repository.Video_MemberRepository;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProgramServiceImpl implements ProgramService{

    private final MemberRepository memberRepository;
    private final ProgramRepository programRepository;
    private final Video_MemberRepository videoMemberRepository;
    private final ProgramHistoryRepository programHistoryRepository;

    @Override
    public ProgramDetailResponseDTO getProgramOne(Long pno, String mid) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));
        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new RuntimeException("Member not found for Id : " + mid));

        Set<Video> video = program.getVideo();
        List<ActResponseDTO> actResponseDTOList = new ArrayList<>();

        video.forEach(v -> {
            double metrics = 0;
            Optional<Video_Member> byMemberAndVideo = videoMemberRepository.findByMemberAndVideo(mid, v.getVno());
            if (byMemberAndVideo.isEmpty()) { // 비디오랑 멤버랑 연관 테이블이 없으면 생성
                videoMemberRepository.save(
                        Video_Member.builder()
                                .member(member)
                                .video(v).build());
            } else { // 비디오랑 멤버랑 연관 테이블이 있으면 metrics 값 변경
                Video_Member videoMember = byMemberAndVideo.get();
                metrics = videoMember.getMetrics();
            }

            actResponseDTOList.add(
                    ActResponseDTO.builder()
                            .vno(v.getVno())
                            .actName(v.getActName())
                            .metrics(metrics).build()
            );
        });

        return entitesToProgramDetailResponseDTO(program, actResponseDTOList);
    }

    @Override
    public ProgramHistoryDTO getProgramHistoryOne(Long pno, String mid) {

        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));
        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new RuntimeException("Member not found for Id : " + mid));

        Set<Video> video = program.getVideo();
        List<ActResponseDTO> actResponseDTOList = new ArrayList<>();

        video.forEach(v -> {
            double metrics = 0;
            Optional<Video_Member> byMemberAndVideo = videoMemberRepository.findByMemberAndVideo(mid, v.getVno());
            if (byMemberAndVideo.isEmpty()) { // 비디오랑 멤버랑 연관 테이블이 없으면 생성
                videoMemberRepository.save(
                        Video_Member.builder()
                                .member(member)
                                .video(v).build());
            } else { // 비디오랑 멤버랑 연관 테이블이 있으면 metrics 값 변경
                Video_Member videoMember = byMemberAndVideo.get();
                metrics = videoMember.getMetrics();
            }

            actResponseDTOList.add(
                    ActResponseDTO.builder()
                            .vno(v.getVno())
                            .actName(v.getActName())
                            .metrics(metrics).build()
            );
        });

        return ProgramHistoryDTO.builder()
                .programName(program.getProgramTitle())
                .position(program.getPosition())
                .category(program.getCategory())
                .actResponseDTO(actResponseDTOList)
                .mid(member.getMid())
                .regDate(member.getRegDate())
                .build();
    }

    @Override
    public String modifyProgramOne(Long pno, ProgramRequestDTO programRequestDTO) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        program.modifyProgram(programRequestDTO.getProgramTitle(), programRequestDTO.getDescription(),
                programRequestDTO.getCategory(), programRequestDTO.getPosition());
        programRepository.save(program);

        return "Program modify successfully.";
    }

    @Override
    public String deleteProgramOne(Long pno) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        program.setIs_deleted(Boolean.TRUE);
        programRepository.save(program);

        return "Program delete successfully.";
    }

    @Override
    public PageResponseDTO<ProgramMainResponseDTO> getProgramList(PageRequestDTO pageRequestDTO) {

        return null;
    }

    @Override
    public Long createProgram(ProgramRequestDTO programRequestDTO) {
        Program program = programRequestDtoToProgram(programRequestDTO);

        programRepository.save(program);

        return program.getPno();
    }

    @Override
    public String registerProgram(Long pno, String mid) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));
        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new RuntimeException("Member not found for Id : " + mid));

        Optional<Program_Member> byMemberAndProgram = programMemberRepository.findByMemberAndProgram(mid, pno);
        if (byMemberAndProgram.isPresent()) return "already Registered Program.";

        Program_Member program_member = Program_Member.builder()
                .member(member)
                .program(program)
                .build();

        programMemberRepository.save(program_member);

        return "Program Register Success for Member Id : " + mid;
    }

    @Override
    public String cancelProgram(Long pno, String mid) {
        programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));
        memberRepository.findById(mid)
                .orElseThrow(() -> new RuntimeException("Member not found for Id : " + mid));

        Optional<Program_Member> byMemberAndProgram = programMemberRepository.findByMemberAndProgram(mid, pno);
        if (byMemberAndProgram.isEmpty()) return "Don't Registered Program.";

        Program_Member programMember = byMemberAndProgram.get();

        programMemberRepository.delete(programMember);
        return "Program Cancel Success for Member Id : " + mid;
    }
}

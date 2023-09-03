package com.hallym.rehab.domain.program.service;


import com.hallym.rehab.domain.program.dto.act.ActResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramDetailResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramHistoryDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramListResponseDTO;
import com.hallym.rehab.domain.program.dto.program.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramHistory;
import com.hallym.rehab.domain.program.entity.Video;
import com.hallym.rehab.domain.program.entity.Video_Member;
import com.hallym.rehab.domain.program.repository.ProgramHistoryRepository;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.program.repository.Video_MemberRepository;
import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.repository.MemberRepository;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
                            .ord(v.getOrd())
                            .actName(v.getActName())
                            .playTime(v.getPlayTime())
                            .metrics(metrics)
                            .frame(v.getFrame())
                            .build()
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
                            .actName(v.getActName())
                            .playTime(v.getPlayTime())
                            .frame(v.getFrame())
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
    public String deleteProgramOne(Long pno, ProgramRequestDTO programRequestDTO) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));

        program.setIs_deleted(Boolean.TRUE);
        programRepository.save(program);

        return "Program delete successfully.";
    }

    @Override
    public PageResponseDTO<ProgramListResponseDTO> getProgramList(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("pno");

        Page<ProgramListResponseDTO> result = programRepository.searchProgramList(types, keyword, pageable);

        return PageResponseDTO.<ProgramListResponseDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<ProgramListResponseDTO> getProgramHistoryList(String mid) {
        List<Program> programs = programHistoryRepository.searchProgramListByMid(mid);

        return programs.stream()
                .map(program -> ProgramListResponseDTO.builder()
                        .pno(program.getPno())
                        .programTitle(program.getProgramTitle())
                        .description(program.getDescription())
                        .category(program.getCategory())
                        .position(program.getPosition())
                        .regDate(program.getRegDate())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public Long createProgram(ProgramRequestDTO programRequestDTO) {
        Program program = programRequestDtoToProgram(programRequestDTO);
        programRepository.save(program);

        return program.getPno();
    }

    @Override
    public String addProgramHistory(Long pno, String mid) {
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));
        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new RuntimeException("Member not found for Id : " + mid));

        Optional<ProgramHistory> byMemberAndProgram = programHistoryRepository.findByMemberWithProgram(mid, pno);
        if (byMemberAndProgram.isPresent()) return "already Registered ProgramHistory.";

        ProgramHistory programHistory = ProgramHistory.builder()
                .member(member)
                .program(program)
                .build();

        programHistoryRepository.save(programHistory);

        return "Add Program History for member Id : " + mid;
    }

    @Override
    public String cancelProgram(Long pno, String mid) {
        programRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Program not found for Id : " + pno));
        memberRepository.findById(mid)
                .orElseThrow(() -> new RuntimeException("Member not found for Id : " + mid));

        Optional<ProgramHistory> membersProgram = programHistoryRepository.findByMemberWithProgram(mid, pno);
        if (membersProgram.isEmpty()) return "Don't Registered Program.";

        ProgramHistory programHistory = membersProgram.get();

        programHistoryRepository.delete(programHistory);
        return "Program Cancel Success for Member Id : " + mid;
    }


    protected Program programRequestDtoToProgram(ProgramRequestDTO programRequestDTO) {

        Member member = memberRepository.findById(programRequestDTO.getMid())
                .orElseThrow(() -> new RuntimeException("Member not found for Id : " + programRequestDTO.getMid()));


        return Program.builder()
                .member(member)
                .programTitle(programRequestDTO.getProgramTitle())
                .description(programRequestDTO.getDescription())
                .category(programRequestDTO.getCategory())
                .position(programRequestDTO.getPosition())
                .build();
    }

    protected ProgramDetailResponseDTO entitesToProgramDetailResponseDTO(Program program, List<ActResponseDTO> actResponseDTOList){

        actResponseDTOList.sort(Comparator.comparing(ActResponseDTO::getOrd));

        return ProgramDetailResponseDTO.builder()
                .pno(program.getPno())
                .programTitle(program.getProgramTitle())
                .description(program.getDescription())
                .category(program.getCategory())
                .position(program.getPosition())
                .actResponseDTO(actResponseDTOList)
                .regDate(program.getRegDate())
                .build();
    }
}

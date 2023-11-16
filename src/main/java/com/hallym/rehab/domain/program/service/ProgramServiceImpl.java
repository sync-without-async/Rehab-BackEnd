package com.hallym.rehab.domain.program.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.program.dto.MetricsUpdateRequestDTO;
import com.hallym.rehab.domain.program.dto.ProgramRequestDTO;
import com.hallym.rehab.domain.program.entity.Program;
import com.hallym.rehab.domain.program.entity.ProgramDetail;
import com.hallym.rehab.domain.program.repository.ProgramDetailRepository;
import com.hallym.rehab.domain.program.repository.ProgramRepository;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import com.hallym.rehab.domain.video.entity.Video;
import com.hallym.rehab.domain.video.entity.VideoMetrics;
import com.hallym.rehab.domain.video.repository.VideoMetricsRepository;
import com.hallym.rehab.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService{
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final VideoRepository videoRepository;
    private final VideoMetricsRepository videoMetricsRepository;
    private final ProgramRepository programRepository;
    private final ProgramDetailRepository programDetailRepository;

    @Override
    public String createProgramAndDetail(ProgramRequestDTO requestDTO) {
        String adminId = requestDTO.getStaff_id();
        String userId = requestDTO.getPatient_id();

        Staff staff = staffRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("not found admin for Id : " + adminId));
        Patient patient = patientRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("not found user for Id : " + userId));

        Program program = Program.builder()
                .staff(staff)
                .patient(patient)
                .description(requestDTO.getDescription())
                .build();

        Program savedProgram = programRepository.save(program);

        for (Map.Entry<Integer, Long> entry : requestDTO.getOrd_map().entrySet()) {
            int ord = entry.getKey();
            Long vno = entry.getValue();

            Video video = videoRepository.findById(vno)
                    .orElseThrow(() -> new RuntimeException("Video not found for Id : " + vno));

            ProgramDetail programDetail = ProgramDetail.builder()
                    .program(savedProgram)
                    .video(video)
                    .ord(ord)
                    .build();

            VideoMetrics videoMetrics = VideoMetrics.builder()
                    .programDetail(programDetail)
                    .metrics(0)
                    .patient(patient)
                    .build();

            VideoMetrics savedMetrics = videoMetricsRepository.save(videoMetrics);
            programDetail.setVideoMetrics(savedMetrics);
            programDetailRepository.save(programDetail);
        }

        return "success";
    }

    @Override
    public String updateProgramAndDetail(ProgramRequestDTO requestDTO, Long pno) {
        String adminId = requestDTO.getStaff_id();
        String userId = requestDTO.getPatient_id();

        staffRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("not found admin for adminId : " + adminId));
        patientRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("not found user for userId : " + userId));
        Program program = programRepository.findById(pno)
                .orElseThrow(() -> new NotFoundException("not found for pno : " + pno));

        program.changeDescription(requestDTO.getDescription()); // change description
        programRepository.save(program);

        for (Map.Entry<Integer, Long> entry : requestDTO.getOrd_map().entrySet()) {
            int ord = entry.getKey();
            Long vno = entry.getValue();

            Video video = videoRepository.findById(vno)
                    .orElseThrow(() -> new RuntimeException("Video not found for Id : " + vno));

            ProgramDetail programDetail = programDetailRepository.findByPnoAndOrd(pno, ord)
                    .orElseThrow(() -> new NotFoundException("Detail not found"));

            programDetail.changeOrd(ord, video); // change ord, vno

            programDetailRepository.save(programDetail);
        }

        return "success";
    }

    @Override
    public String updateMetrics(MetricsUpdateRequestDTO metricsUpdateRequestDTO) {
        String userId = metricsUpdateRequestDTO.getUserId();
        Long pno = metricsUpdateRequestDTO.getPno();
        Long vno = metricsUpdateRequestDTO.getVno();
        int ord = metricsUpdateRequestDTO.getOrd();
        double metrics = metricsUpdateRequestDTO.getMetrics();

        patientRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("not found user for Id : " + userId));

        programRepository.findById(pno)
                .orElseThrow(() -> new NotFoundException("not found for Id : " + pno));

        ProgramDetail programDetail = programDetailRepository.findByInfo(pno, vno, ord)
                .orElseThrow(() -> new NotFoundException("Not Found Detail for vno : " + vno + ", ord : " + ord));

        VideoMetrics videoMetrics = videoMetricsRepository.findByProgramDetail(programDetail)
                .orElseThrow(() -> new NotFoundException("Not Found Metrics for pdno : " + programDetail.getPdno()));

        videoMetrics.updateMetrics(metrics);

        videoMetricsRepository.save(videoMetrics);

        return "success";
    }
}

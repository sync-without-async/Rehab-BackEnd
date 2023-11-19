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
public class ProgramServiceImpl implements ProgramService {
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final VideoRepository videoRepository;
    private final VideoMetricsRepository videoMetricsRepository;
    private final ProgramRepository programRepository;
    private final ProgramDetailRepository programDetailRepository;

    @Override
    public String createProgramAndDetail(ProgramRequestDTO requestDTO) {
        String adminId = requestDTO.getStaff_id();
        String patient_id = requestDTO.getPatient_id();

        Staff staff = staffRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("not found admin for Id : " + adminId));
        Patient patient = patientRepository.findById(patient_id)
                .orElseThrow(() -> new NotFoundException("not found user for Id : " + patient_id));

        Program program = Program.builder()
                .staff(staff)
                .patient(patient)
                .description("")
                .build();

        Program savedProgram = programRepository.save(program);

        for (int i = 1; i <= 10; i++) {
            ProgramDetail programDetail = ProgramDetail.builder()
                    .program(savedProgram)
                    .video(null)
                    .ord(i)
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
    public String updateProgramAndDetail(ProgramRequestDTO requestDTO, String patient_id) {
        String adminId = requestDTO.getStaff_id();

        staffRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("not found admin for adminId : " + adminId));
        patientRepository.findById(patient_id)
                .orElseThrow(() -> new NotFoundException("not found user for patient_id : " + patient_id));
        Program program = programRepository.findByPatientId(patient_id)
                .orElseThrow(() -> new NotFoundException("not found for patient_id : " + patient_id));

        program.changeDescription(requestDTO.getDescription()); // change description
        programRepository.save(program);

        for (Map.Entry<Integer, Long> entry : requestDTO.getOrd_map().entrySet()) {
            int ord = entry.getKey();
            Long vno = entry.getValue();

            Video video = videoRepository.findById(vno)
                    .orElseThrow(() -> new RuntimeException("Video not found for Id : " + vno));

            ProgramDetail programDetail = programDetailRepository.findByPnoAndOrd(program.getPno(), ord)
                    .orElseThrow(() -> new NotFoundException("Detail not found"));

            programDetail.changeOrd(ord, video); // change ord, vno

            programDetailRepository.save(programDetail);
        }

        return "success";
    }

    @Override
    public String updateMetrics(MetricsUpdateRequestDTO metricsUpdateRequestDTO) {
        String patient_id = metricsUpdateRequestDTO.getPatient_id();
        Long pno = metricsUpdateRequestDTO.getPno();
        Long vno = metricsUpdateRequestDTO.getVno();
        int ord = metricsUpdateRequestDTO.getOrd();
        double metrics = metricsUpdateRequestDTO.getMetrics();

        patientRepository.findById(patient_id)
                .orElseThrow(() -> new NotFoundException("not found user for Id : " + patient_id));

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

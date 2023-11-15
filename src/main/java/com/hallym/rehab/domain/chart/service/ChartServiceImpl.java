package com.hallym.rehab.domain.chart.service;

import com.hallym.rehab.domain.chart.dto.ChartRequestDTO;
import com.hallym.rehab.domain.chart.dto.ChartResponseDTO;
import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.repository.ChartRepository;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChartServiceImpl implements ChartService{

    private final ChartRepository chartRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ChartResponseDTO getChartDetails(Long cno) {

        Optional<Chart> chartOne = chartRepository.findById(cno);

        Chart chart = chartOne.orElseThrow();

        ChartResponseDTO chartResponseDTO = entityToDTO(chart);

        return chartResponseDTO;
    }

    public ChartResponseDTO entityToDTO(Chart chart) {

        ChartResponseDTO chartResponseDTO = ChartResponseDTO.builder()
                .cno(chart.getCno())
                .cd(chart.getCd())
                .patientName(chart.getPatientName())
                .phone(chart.getPhone())
                .sex(chart.getSex())
                .birth(chart.getBirth())
                .medicalRecord(chart.getMedicalRecord())
                .exerciseRequest(chart.getExerciseRequest())
                .doctor_id(chart.getDoctor().getName())
                .therapist_id(chart.getTherapist().getName())
                .build();

           return chartResponseDTO;
    }

    public Chart dtoToEntity(ChartRequestDTO chartRequestDTO) {

        String mid;
        do {
            String uuidPrefix = UUID.randomUUID().toString().substring(0, 3);
            mid = "hallym" + uuidPrefix;
        } while (patientRepository.existsByMid(mid));

        Patient newPatientDetail = Patient.builder()
                .mid(mid)
                .birth(chartRequestDTO.getBirth())
                .password(passwordEncoder.encode("1111"))
                .sex(chartRequestDTO.getSex())
                .phone(chartRequestDTO.getPhone())
                .name(chartRequestDTO.getPatientName())
                .build();

        patientRepository.save(newPatientDetail);

        String doctor_id = chartRequestDTO.getDoctor_id();
        String therapist_id = chartRequestDTO.getTherapist_id();

        Staff doctor = staffRepository.findByIdWithImages(doctor_id);
        Staff therapist = staffRepository.findByIdWithImages(therapist_id);

        return Chart.builder()
                .cd(chartRequestDTO.getCd())
                .patientName(chartRequestDTO.getPatientName())
                .phone(chartRequestDTO.getPhone())
                .sex(chartRequestDTO.getSex())
                .birth(chartRequestDTO.getBirth())
                .medicalRecord(chartRequestDTO.getMedicalRecord())
                .exerciseRequest(chartRequestDTO.getExerciseRequest())
                .patient(newPatientDetail)
                .doctor(doctor)
                .therapist(therapist)
                .build();
    }


    @Override
    public void registerChartDetails(ChartRequestDTO registerDTO) {

        Chart chart = dtoToEntity(registerDTO);

        chartRepository.save(chart);

    }

    @Override
    public String modifyChartDetails(ChartRequestDTO modifyDTO) {
        return null;
    }

    @Override
    public String deleteChartDetails(Long cno) {
        return null;
    }


}

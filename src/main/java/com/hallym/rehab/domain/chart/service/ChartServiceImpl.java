package com.hallym.rehab.domain.chart.service;

import com.hallym.rehab.domain.chart.dto.ChartRequestDTO;
import com.hallym.rehab.domain.chart.dto.ChartResponseDTO;
import com.hallym.rehab.domain.chart.dto.RecordDTO;
import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.entity.Record;
import com.hallym.rehab.domain.chart.repository.ChartRepository;
import com.hallym.rehab.domain.chart.repository.RecordRepository;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChartServiceImpl implements ChartService{

    private final ChartRepository chartRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final RecordRepository recordRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 환자 차트 정보 단일 조회
     */
    @Override
    public ChartResponseDTO getChartDetails(Long cno) {

        Optional<Chart> chartOne = chartRepository.findById(cno);

        Chart chart = chartOne.orElseThrow();

        return entityToDTO(chart);
    }

    /**
     * 환자 차트 최초 등록
     */
    @Override
    public String registerChartDetails(ChartRequestDTO registerDTO) {

        Chart chart = dtoToEntity(registerDTO);

        String newPatientId = chart.getPatient().getMid();

        log.info("---------" + newPatientId);

        chartRepository.save(chart);

        return newPatientId;
    }

    /**
     * 환자 차트 삭제
     */
    @Override
    public void deleteChartDetails(Long cno) {

        chartRepository.deleteById(cno);
    }

    /**
     * 환자 차트 목록 조회
     */
    @Override
    public PageResponseDTO<ChartResponseDTO> getChartList(String doctor_id, PageRequestDTO pageRequestDTO) {

        Page<Chart> result = chartRepository.searchChartWithRecord(doctor_id, pageRequestDTO);

        List<ChartResponseDTO> dtoList = result
                .get()
                .map(this::entityToDTO).collect(Collectors.toList());

        PageResponseDTO<ChartResponseDTO> responseDTO =
                PageResponseDTO.<ChartResponseDTO>withAll()
                        .dtoList(dtoList)
                        .pageRequestDTO(pageRequestDTO)
                        .total(result.getNumberOfElements())
                        .build();

        return responseDTO;
    }


    public ChartResponseDTO entityToDTO(Chart chart) {

        List<Record> recordList = recordRepository.findRecordByChart(chart);

        List<RecordDTO> recordDTOList = recordList.stream()
                .map(RecordDTO::of)
                .collect(Collectors.toList());

        ChartResponseDTO chartResponseDTO = ChartResponseDTO.builder()
                .cno(chart.getCno())
                .cd(chart.getCd())
                .patientName(chart.getPatientName())
                .phone(chart.getPhone())
                .sex(chart.getSex())
                .birth(chart.getBirth())
                .doctor_id(chart.getDoctor().getName())
                .therapist_id(chart.getTherapist().getName())
                .medicalRecords(recordDTOList)
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

        Chart chart = Chart.builder()
                .cd(chartRequestDTO.getCd())
                .patientName(chartRequestDTO.getPatientName())
                .phone(chartRequestDTO.getPhone())
                .sex(chartRequestDTO.getSex())
                .birth(chartRequestDTO.getBirth())
                .patient(newPatientDetail)
                .doctor(doctor)
                .therapist(therapist)
                .build();

        Record record = Record.builder()
                .schedule(chartRequestDTO.getSchedule())
                .treatmentRecord(chartRequestDTO.getTreatmentRecord())
                .exerciseRequest(chartRequestDTO.getExerciseRequest())
                .chart(chart)
                .build();

        chart.addRecord(record);

        return chart;
    }
}

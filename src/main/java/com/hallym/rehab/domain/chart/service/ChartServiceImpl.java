package com.hallym.rehab.domain.chart.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.hallym.rehab.domain.chart.MetricsUtil;
import com.hallym.rehab.domain.chart.dto.*;
import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.entity.Record;
import com.hallym.rehab.domain.chart.repository.ChartRepository;
import com.hallym.rehab.domain.chart.repository.RecordRepository;
import com.hallym.rehab.domain.reservation.entity.Reservation;
import com.hallym.rehab.domain.reservation.repository.ReservationRepository;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.video.entity.VideoMetrics;
import com.hallym.rehab.domain.video.repository.VideoMetricsRepository;
import com.hallym.rehab.global.exception.StaffNotFoundException;
import com.hallym.rehab.global.pageDTO.PageRequestDTO;
import com.hallym.rehab.global.pageDTO.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChartServiceImpl implements ChartService {

    private final ChartRepository chartRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final RecordRepository recordRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;
    private final MetricsUtil metricsUtil;


    @Override
    public ChartResponseDTO getChartDetailByPatient(String patient_id) {

        Chart chart = chartRepository.findByPatientMid(patient_id);

        if (chart == null) {
            throw new NotFoundException("해당 환자의 차트를 찾을 수 없습니다.");
        }

        return entityToDTO(chart);
    }

    @Override
    public ChartResponseDTO getChartDetailByStaff(Long cno) {

        Optional<Chart> result = chartRepository.findById(cno);

        Chart chart = result.orElseThrow();

        return entityToDTO(chart);
    }

    @Override
    public List<AIRecordDTO> getAIRecords(String patient_id) {
        List<Reservation> reservations = reservationRepository.findAllByPatientMidWithSummary(patient_id);

        List<AIRecordDTO> aiRecords = new ArrayList<>();

        for (Reservation reservation : reservations) {
            String staff_id = reservation.getStaff().getMid();
            String summary = reservation.getRoom().getAudio().getSummary();
            LocalDate summaryDate = LocalDate.from(reservation.getRoom().getRegDate());

            AIRecordDTO aiRecord = AIRecordDTO.builder()
                    .staff_id(staff_id)
                    .summary(summary)
                    .regDate(summaryDate)
                    .build();

            aiRecords.add(aiRecord);
        }

        return aiRecords;
    }

    /**
     * 환자 차트 신규 등록
     *
     * @param registerDTO
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
     *
     * @param cno chart의 pk
     */
    @Override
    public void deleteChartDetails(Long cno) {

        chartRepository.deleteById(cno);
    }


    /**
     * 의료진은 나의 담당 환자 차트 목록을 조회할 수 있다.
     *
     * @param mid
     * @param pageRequestDTO
     * @return
     */
    @Override
    public PageResponseDTO<ChartListAllDTO> getChartList(String mid, PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        String sortBy = pageRequestDTO.getSortBy();
        Pageable pageable = pageRequestDTO.getPageable("cno");

        Staff staff = staffRepository.findByIdWithImages(mid);

        if (staff == null) {
            throw new StaffNotFoundException(mid);
        }

        MemberRole role = staff.getRoleSet().iterator().next();

        Page<ChartListAllDTO> result = chartRepository.searchChartWithRecord(mid, role, types, keyword, sortBy, pageable);

        return PageResponseDTO.<ChartListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }


    public ChartResponseDTO entityToDTO(Chart chart) {

        List<Record> recordList = recordRepository.findRecordByChart(chart);

        List<RecordDTO> recordDTOList = recordList.stream()
                .map(RecordDTO::of)
                .collect(Collectors.toList());

        double metrics_rate = metricsUtil.getRateMetricsByPatientId(chart.getPatient().getMid());

        return ChartResponseDTO.builder()
                .cno(chart.getCno())
                .cd(chart.getCd())
                .phone(chart.getPhone())
                .sex(chart.getSex())
                .birth(chart.getBirth())
                .patient_id(chart.getPatient().getMid())
                .patient_name(chart.getPatientName())
                .doctor_name(chart.getDoctor().getName())
                .therapist_name(chart.getTherapist().getName())
                .medicalRecords(recordDTOList)
                .metrics_rate(metrics_rate)
                .regDate(LocalDate.from(chart.getRegDate()))
                .build();
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
                .roleSet(Collections.singleton(MemberRole.PATIENT))
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

        recordRepository.save(record);

        return chart;
    }

}

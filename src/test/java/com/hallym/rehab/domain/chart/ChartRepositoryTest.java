package com.hallym.rehab.domain.chart;

import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.repository.ChartRepository;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.entity.StaffRole;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@DataJpaTest //JPA 관련 빈과 config만 로드
@SpringBootTest
@Log4j2
public class ChartRepositoryTest {

    @Autowired
    private ChartRepository chartRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Chart의 담당 의료진 정보 insert test")
    public void testCreateChart() {
        // Given
        Patient patient = Patient.builder()
                .mid("patient1")
                .birth(LocalDate.of(1997, 1, 21))
                .password(passwordEncoder.encode("1111"))
                .sex("MALE")
                .phone("0105142102")
                .name("홍길동")
                .build();

        Staff doctor = Staff.builder()
                .mid("doctor3")
                .name("이동헌")
                .password(passwordEncoder.encode("1111"))
                .hospital("Hallym Hospital")
                .department("재활의학과")
                .email("doctor1@hallym.com")
                .phone("01012345678")
                .roleSet(Collections.singleton(StaffRole.DOCTOR))
                .build();

        Staff therapist = Staff.builder()
                .mid("therapist3")
                .name("홍길동")
                .password(passwordEncoder.encode("1111"))
                .hospital("Hallym Hospital")
                .department("정형외과")
                .email("therapist3@hallym.com")
                .phone("01098765432")
                .roleSet(Collections.singleton(StaffRole.THERAPIST))
                .build();

        patientRepository.save(patient);
        staffRepository.save(doctor);
        staffRepository.save(therapist);

        Chart chart = Chart.builder()
                .cno(1L)
                .cd("MC38D")
                .patientName(patient.getName())
                .phone("01055556666")
                .sex("M")
                .birth(LocalDate.of(1997, 1, 21))
                .doctor(doctor)
                .therapist(therapist)
                .patient(patient)
                .medicalRecord("3주 전부터 미세한 통증이 시작되었음")
                .exerciseRequest("슬개골 재활 치료 요망")
                .build();

        // When
        Chart savedChart = chartRepository.save(chart);

        // Then
        assertThat(savedChart).isNotNull();
        assertThat(savedChart.getDoctor().getMid()).isEqualTo(doctor.getMid());
        assertThat(savedChart.getTherapist().getMid()).isEqualTo(therapist.getMid());
    }


}

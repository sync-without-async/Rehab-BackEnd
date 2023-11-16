package com.hallym.rehab.domain.chart;

import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.entity.Record;
import com.hallym.rehab.domain.chart.repository.ChartRepository;
import com.hallym.rehab.domain.chart.repository.RecordRepository;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.entity.Staff;
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

    String text = "계엄을 선포한 때에는 대통령은 지체없이 국회에 통고하여야 한다. 국회에서 의결된 법률안은 정부에 이송되어 15일 이내에 대통령이 공포한다. 국회에 제출된 법률안 기타의 의안은 회기중에 의결되지 못한 이유로 폐기되지 아니한다. 다만, 국회의원의 임기가 만료된 때에는 그러하지 아니하다.\n" +
            "\n" +
            "헌법재판소 재판관은 정당에 가입하거나 정치에 관여할 수 없다. 국무회의는 정부의 권한에 속하는 중요한 정책을 심의한다. 모든 국민은 근로의 권리를 가진다. 국가는 사회적·경제적 방법으로 근로자의 고용의 증진과 적정임금의 보장에 노력하여야 하며, 법률이 정하는 바에 의하여 최저임금제를 시행하여야 한다.\n" +
            "\n" +
            "국회의원의 수는 법률로 정하되, 200인 이상으로 한다. 대법원은 법률에 저촉되지 아니하는 범위안에서 소송에 관한 절차, 법원의 내부규율과 사무처리에 관한 규칙을 제정할 수 있다.";

    @Autowired
    private ChartRepository chartRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Chart의 담당 의료진 정보 insert test")
    public void testCreateChart() {
        // Given

        Staff doctor = Staff.builder()
                .mid("doctor3")
                .name("이동헌")
                .password(passwordEncoder.encode("1111"))
                .hospital("Hallym Hospital")
                .department("재활의학과")
                .email("doctor1@hallym.com")
                .phone("01012345678")
                .roleSet(Collections.singleton(MemberRole.DOCTOR))
                .build();

        Staff therapist = Staff.builder()
                .mid("therapist3")
                .name("홍길동")
                .password(passwordEncoder.encode("1111"))
                .hospital("Hallym Hospital")
                .department("정형외과")
                .email("therapist3@hallym.com")
                .phone("01098765432")
                .roleSet(Collections.singleton(MemberRole.THERAPIST))
                .build();


        staffRepository.save(doctor);
        staffRepository.save(therapist);

        Chart chart = Chart.builder()
                .cno(1L)
                .cd("MC38D")
                .patientName("이동헌")
                .phone("01055556666")
                .sex("M")
                .birth(LocalDate.of(1997, 1, 21))
                .doctor(doctor)
                .therapist(therapist)
                .build();

        Record record = Record.builder()
                .record_no(1L)
                    .chart(chart)
                    .schedule(LocalDate.of(2023, 11, 21))
                .treatmentRecord(text)
                .exerciseRequest(text)
                .build();

        Patient patient = Patient.builder()
                .mid("patient1")
                .birth(chart.getBirth())
                .password(passwordEncoder.encode("1111"))
                .sex(chart.getSex())
                .phone(chart.getPhone())
                .name(chart.getPatientName())
                .build();

        chart.addPatient(patient);
        patientRepository.save(patient);
        chartRepository.save(chart);
        recordRepository.save(record);


    }


}

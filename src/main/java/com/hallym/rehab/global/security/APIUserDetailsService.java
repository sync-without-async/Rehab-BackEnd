package com.hallym.rehab.global.security;

import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.repository.PatientRepository;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.global.security.dto.APIUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Staff staff = staffRepository.findById(username).orElse(null);

        if (staff != null) {
            APIUserDTO dto = new APIUserDTO(
                    staff.getMid(),
                    staff.getPassword(),
                    staff.getName(),
                    staff.getPhone(),
                    staff.getRoleSet()
                            .stream()
                            .map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                            .collect(Collectors.toList())
            );
            log.info("Loading user ---- " + dto);
            return dto;
        } else {
            // Staff 테이블에 해당 아이디가 없는 경우
            // Patient 테이블에서 아이디를 찾습니다.
            // 있다면 Patient로 APIUserDTO 객체를 만들어 반환합니다.
            Patient patient = patientRepository.findById(username).orElse(null);
            if (patient != null) {
                APIUserDTO dto = new APIUserDTO(
                        patient.getMid(),
                        patient.getPassword(),
                        patient.getName(),
                        patient.getPhone(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_PATIENT"))
                );
                log.info("Loading user ---- " + dto);
                return dto;
            } else {
                // Patient 테이블에도 해당 아이디가 없는 경우
                throw new UsernameNotFoundException("해당 아이디는 없는 사용자입니다.");
            }
        }
    }
}

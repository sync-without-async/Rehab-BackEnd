package com.hallym.rehab.global.security;

import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.global.security.dto.APIUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
    public class APIUserDetailsService implements UserDetailsService {

    //주입
    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Staff staff = staffRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디는 없는 사용자입니다."));

        APIUserDTO dto =
                new APIUserDTO(
                        staff.getMid(),
                        staff.getPassword(),
                        staff.getName(),
                        staff.getPhone(),
                        staff.getRoleSet()
                                .stream().map(staffRole -> new SimpleGrantedAuthority("ROLE_" + staffRole.name()))
                                .collect(Collectors.toList())
                );

        log.info("Loading user----" + dto);

        return dto;
    }
}

package com.hallym.rehab.global.security;

import com.hallym.rehab.domain.user.entity.Member;
import com.hallym.rehab.domain.user.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디는 없는 사용자입니다."));

        log.info("APIUserDetailsService apiUser-------------------------------------");

        APIUserDTO dto =
                new APIUserDTO(
                        member.getMid(),
                        member.getPassword(),
                        member.getName(),
                        member.getPhone(),
                        member.getRoleSet()
                                .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                                .collect(Collectors.toList())
                );

        log.info(dto);

        return dto;
    }
}

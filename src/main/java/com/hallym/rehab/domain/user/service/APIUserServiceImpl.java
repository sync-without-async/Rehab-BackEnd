package com.hallym.rehab.domain.user.service;

import com.hallym.rehab.domain.chart.entity.Chart;
import com.hallym.rehab.domain.chart.repository.ChartRepository;
import com.hallym.rehab.domain.user.dto.*;
import com.hallym.rehab.domain.user.entity.Patient;
import com.hallym.rehab.domain.user.entity.Staff;
import com.hallym.rehab.domain.user.entity.StaffImage;
import com.hallym.rehab.domain.user.repository.StaffRepository;
import com.hallym.rehab.domain.user.entity.MemberRole;
import com.hallym.rehab.domain.user.repository.PatientRepository;

import com.hallym.rehab.global.exception.IncorrectPasswordException;
import com.hallym.rehab.global.exception.MidExistsException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ToString
@Service
@Log4j2
public class APIUserServiceImpl implements APIUserService {

    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;
    private final ChartRepository chartRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StaffResponseDTO getStaffInfo(String mid) {

        Staff staff = staffRepository.findByIdWithImages(mid);

        StaffResponseDTO staffResponseDTO = StaffResponseDTO.builder()
                .mid(staff.getMid())
                .name(staff.getName())
                .hospital(staff.getHospital())
                .department(staff.getDepartment())
                .email(staff.getEmail())
                .phone(staff.getPhone())
                .staffRole(staff.getRoleSet().toString())
                .build();

        if (staff.getStaffImage() != null) {
            StaffImage staffImage = staff.getStaffImage();
            String fileName = staffImage.getProfileUrl();
            staffResponseDTO.setFileName(fileName);
        }

        return staffResponseDTO;
    }

    /**
     * 환자 정보 단일 조회
     * @param mid
     * @return
     */
    @Override
    public PatientDTO getPatientInfo(String mid) {

        Optional<Patient> result = patientRepository.findById(mid);

        Patient patient = result.orElseThrow();

        return PatientDTO.builder()
                .mid(patient.getMid())
                .name(patient.getName())
                .sex(patient.getSex())
                .birth(patient.getBirth())
                .phone(patient.getPhone())
                .build();
    }

    /**
     * mid를 검색해서 role 정보를 가져오고 jwt 토큰 발행을 합니다.
     *
     * @param mid
     * @return
     */
    public String getRoleSetByMid(String mid) {
        Optional<Staff> staffOptional = staffRepository.findById(mid);
        if (staffOptional.isPresent()) {
            Staff staff = staffOptional.get();
            String role = String.join(",",
                    staff.getRoleSet().stream().map(MemberRole::getValue).collect(Collectors.toList()));
            log.info("해당 유저는 " + role + " 권한을 가지고 있습니다.");
            return role;
        } else {
            Optional<Patient> patientOptional = patientRepository.findById(mid);
            if (patientOptional.isPresent()) {
                Patient patient = patientOptional.get();
                log.info("해당 유저는 일반 환자입니다.");
                return "ROLE_PATIENT";
            } else {
                throw new UsernameNotFoundException("해당 아이디는 없는 사용자입니다.");
            }
        }
    }


    public void changePassword(String mid, PasswordChangeDTO passwordChangeDTO) {
        Staff staff = staffRepository.findById(mid)
                .orElseThrow(() -> new RuntimeException("해당 아이디는 없는 사용자입니다."));

        if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), staff.getPassword())) {
            throw new IncorrectPasswordException();
        }

        staff.changePassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        staffRepository.updatePassword(mid, staff.getPassword());
    }

    public void registerUser(StaffRequestDTO staffRequestDTO) {

        String mid = staffRequestDTO.getMid();

        boolean exists = staffRepository.existsById(mid);

        if (exists) {
            throw new MidExistsException(mid);
        }

        Staff staff = Staff.builder()
                .mid(staffRequestDTO.getMid())
                .name(staffRequestDTO.getName())
                .password(staffRequestDTO.getPassword())
                .hospital(staffRequestDTO.getHospital())
                .department(staffRequestDTO.getDepartment())
                .email(staffRequestDTO.getEmail())
                .phone(staffRequestDTO.getPhone())
                .build();

        if (staffRequestDTO.getProfileUrl() != null) {
            staff.addImage(staffRequestDTO.getProfileUrl());
        }

        staff.changePassword(passwordEncoder.encode(staffRequestDTO.getPassword()));

        if (staffRequestDTO.getStaffRole().equals(MemberRole.DOCTOR.getValue())) {
            staff.addRole(MemberRole.DOCTOR);
        } else if (staffRequestDTO.getStaffRole().equals(MemberRole.THERAPIST.getValue())) {
            staff.addRole(MemberRole.THERAPIST);
        }

        staffRepository.save(staff);
    }

    @Override
    public List<TherapistDTO> getTherapistList() {

        List<Staff> therapistList = staffRepository.findTherapists(MemberRole.THERAPIST);

        List<TherapistDTO> list = therapistList.stream()
                .map(staff -> new TherapistDTO(staff))
                .collect(Collectors.toList());

        return list;
    }

    @Override
    public MyStaffDetailDTO getMyStaffInformation(String patient_id) {

        Chart chart = chartRepository.findByPatientMid(patient_id);

        Staff my_doctor = chart.getDoctor();
        Staff my_therapist = chart.getTherapist();

        MemberRole role_doctor = my_doctor.getRoleSet().iterator().next();
        MemberRole role_therapist = my_therapist.getRoleSet().iterator().next();

        StaffResponseDTO doctorInfo = StaffResponseDTO.builder()
                .mid(my_doctor.getMid())
                .name(my_doctor.getName())
                .hospital(my_doctor.getHospital())
                .department(my_doctor.getDepartment())
                .phone(my_doctor.getPhone())
                .email(my_doctor.getEmail())
                .staffRole(role_doctor.getValue())
                .fileName(my_doctor.getStaffImage().getProfileUrl())
                .build();

        StaffResponseDTO therapistInfo = StaffResponseDTO.builder()
                .mid(my_therapist.getMid())
                .name(my_therapist.getName())
                .hospital(my_therapist.getHospital())
                .department(my_therapist.getDepartment())
                .phone(my_therapist.getPhone())
                .email(my_therapist.getEmail())
                .staffRole(role_therapist.getValue())
                .fileName(my_therapist.getStaffImage().getProfileUrl())
                .build();


        return MyStaffDetailDTO.builder()
                .doctor_info(doctorInfo)
                .therapist_info(therapistInfo)
                .build();
    }


}

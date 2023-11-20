package com.hallym.rehab.domain.user.service;

import com.hallym.rehab.domain.user.dto.*;
import com.hallym.rehab.global.exception.IncorrectPasswordException;

import java.util.List;


public interface APIUserService {

    StaffResponseDTO getStaffInfo(String mid);

    PatientDTO getPatientInfo(String mid);

    String getRoleSetByMid(String mid);

    void changePassword(String mid, PasswordChangeDTO passwordChangeDTO) throws IncorrectPasswordException;

    void registerUser(StaffRequestDTO staffRequestDTO);

    List<TherapistDTO> getTherapistList();

    MyStaffDetailDTO getMyStaffInformation(String patientMid);
}

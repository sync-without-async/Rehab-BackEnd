package com.hallym.rehab.domain.user.service;

import com.hallym.rehab.domain.user.dto.PasswordChangeDTO;
import com.hallym.rehab.domain.user.dto.StaffRequestDTO;
import com.hallym.rehab.domain.user.dto.StaffResponseDTO;
import com.hallym.rehab.domain.user.dto.TherapistDTO;
import com.hallym.rehab.global.exception.IncorrectPasswordException;

import java.util.List;


public interface APIUserService {

    StaffResponseDTO getStaffInfo(String mid);

    String getRoleSetByMid(String mid);

    void changePassword(String mid, PasswordChangeDTO passwordChangeDTO) throws IncorrectPasswordException;

    void registerUser(StaffRequestDTO staffRequestDTO);

    List<TherapistDTO> getTherapistList();
}

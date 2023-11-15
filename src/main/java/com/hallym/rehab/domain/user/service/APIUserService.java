package com.hallym.rehab.domain.user.service;

import com.hallym.rehab.domain.user.dto.PasswordChangeDTO;
import com.hallym.rehab.domain.user.dto.MemberJoinDTO;
import com.hallym.rehab.global.exception.IncorrectPasswordException;


public interface APIUserService {
    class MidExistsException extends Exception {

    }

    String getRoleSetByMid(String mid);

    void changePassword(String mid, PasswordChangeDTO passwordChangeDTO) throws IncorrectPasswordException;

    void registerUser(StaffRequestDTO staffRequestDTO);

}

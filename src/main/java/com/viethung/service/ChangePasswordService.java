package com.viethung.service;

import com.viethung.dto.ChangePasswordDto;

import java.security.Principal;

public interface ChangePasswordService {
    boolean handleChangePassword(ChangePasswordDto changePasswordDto, Principal principal);
}

package com.viethung.controller.common;


import com.viethung.dto.ChangePasswordDto;
import com.viethung.service.ChangePasswordService;
import com.viethung.service.impl.ChangePasswordServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class ChangPasswordController {
    @Autowired
    private ChangePasswordService changePasswordService;

    @PostMapping("/change-password")
    public String doChangePassword(@Valid @ModelAttribute ChangePasswordDto changePasswordDto,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes,
                                   Principal principal) {
        // newPassword != confirmPassword
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "invalid", "Mật khẩu phải giống mật khẩu mới");
        }
        // newPassword == oldPassword
        if (changePasswordDto.getNewPassword().equals(changePasswordDto.getOldPassword())) {
            result.rejectValue("newPassword", "invalid", "Mật khẩu mới phải khác mật khẩu cũ");
        }
        if (result.hasErrors()) {
            return "common/profile";
        }
        //Change password
        if (changePasswordService.handleChangePassword(changePasswordDto, principal)) {
            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");
        }else {
            redirectAttributes.addFlashAttribute("fail", "Đổi mật khẩu thất bại");
        }
        return "redirect:/logout";
    }

}

package com.viethung.controller.common;

import com.viethung.dto.ChangePasswordDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyProfileController {
    @GetMapping("/my-profile")
    public String displayView(Model model){
        model.addAttribute("changePasswordDto", ChangePasswordDto.builder().build());
        return "common/profile";
    }
}

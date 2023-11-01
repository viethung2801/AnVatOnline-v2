package com.viethung.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForgotPasswordController {
    @GetMapping("/forgot-password")
    public String displayView(Model model){
        return "common/forgot-password";
    }
}

package com.viethung.controller.common;

import com.viethung.service.ForgotPasswordServiceImpl;
import com.viethung.service.MailServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class ForgotPasswordController {
    @Autowired
    private ForgotPasswordServiceImpl forgotPasswordService;



    @GetMapping("/forgot-password")
    public String displayView(Model model){
        return "common/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email,
                                       Model model,
                                       RedirectAttributes redirectAttributes) throws MessagingException {
        //Kiểm tra email đã tồn tại chưa
        if (!forgotPasswordService.existsByEmail(email)){
            model.addAttribute("fail","Email không hợp lệ!");
            return "common/forgot-password";
        }
        //Gửi lại mật khẩu mới
        forgotPasswordService.sendPassword(email);

        //Lỗi
        redirectAttributes.addFlashAttribute("success","Password đã được gửi đến email của bạn");
        return "redirect:/login";
    }
    @GetMapping("/forgot-password/{id}/{password}")
    public String onForgotPassword(@PathVariable UUID id,
                                   @PathVariable String password) throws MessagingException {

        forgotPasswordService.confirmPassword(id,password);
        return "redirect:/login";
    }
}

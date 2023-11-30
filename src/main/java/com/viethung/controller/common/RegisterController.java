package com.viethung.controller.common;

import com.viethung.dto.UserRegisterDto;
import com.viethung.service.RegisterService;
import com.viethung.service.impl.RegisterServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @GetMapping("/register")
    public String displayView(Model model) {
        UserRegisterDto userRegisterDto = UserRegisterDto.builder().build();
        model.addAttribute("userRegisterDto", userRegisterDto);
        return "common/register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute UserRegisterDto userRegisterDto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        //Check email or phone number is exist
        if (registerService.existsByPhoneNumber(userRegisterDto.getPhoneNumber())) {
            result.rejectValue("phoneNumber", "invalid", "Số điện thoại đã tồn tại");
        }
        if (registerService.existsByEmail(userRegisterDto.getEmail())) {
            result.rejectValue("email", "invalid", "Email đã tồn tại");
        }
        if (result.hasErrors()) {

            model.addAttribute("userRegisterDto", userRegisterDto);
            model.addAttribute("fail", "Vui lòng thử lại!");
            return "common/register";
        }
        //Save account
        registerService.save(userRegisterDto);
        redirectAttributes.addFlashAttribute("success", "Tạo tài khoản thành công! Đăng nhập ngay.");
        return "redirect:/login";
    }
}

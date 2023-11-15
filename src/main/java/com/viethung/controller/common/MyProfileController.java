package com.viethung.controller.common;

import com.viethung.config.security.CustomUserDetails;
import com.viethung.dto.AdminUserDto;
import com.viethung.dto.ChangePasswordDto;
import com.viethung.dto.OrderDto;
import com.viethung.service.AdminUserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class MyProfileController {
    @Autowired
    private AdminUserServiceImpl adminUserService;
    @GetMapping("/my-profile")
    public String displayView(Model model,
                              @RequestParam Optional<Integer> page,
                              Authentication principal){
        Pageable pageable = PageRequest.of(page.orElse(0),50);
        CustomUserDetails customUserDetails = (CustomUserDetails) principal.getPrincipal();
        AdminUserDto userDto = adminUserService.findById(customUserDetails.getId());
        Page<OrderDto> orderDtos = adminUserService.findAllOrderByUserId(customUserDetails.getId(),pageable);

        model.addAttribute("userDto", userDto);
        model.addAttribute("changePasswordDto", ChangePasswordDto.builder().build());
        model.addAttribute("orderDtos", orderDtos);
        return "common/profile";
    }

    @PostMapping("/my-profile/update")
    public String onUpdate(@Valid @ModelAttribute AdminUserDto adminUserDto,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        //
        try {
            //check value
            if (adminUserService.countByCodeAndIdNot(adminUserDto.getCode(), adminUserDto.getId()) > 0) {
                result.rejectValue("code", "duplicate", "Mã đã tồn tại");
            }
            if (adminUserService.countByEmailAndIdNot(adminUserDto.getEmail(), adminUserDto.getId()) > 0) {
                result.rejectValue("email", "duplicate", "Email đã tồn tại");
            }
            if (adminUserService.countByPhoneNumberAndIdNot(adminUserDto.getPhoneNumber(), adminUserDto.getId()) > 0) {
                result.rejectValue("phoneNumber", "duplicate", "Số điện thoại đã tồn tại");
            }
            //check format
            if (!result.hasErrors()) {
                if (adminUserService.handleUpdate(adminUserDto)) {
                    redirectAttributes.addFlashAttribute("success", "Lưu thành công");
                    return "redirect:/my-profile";
                }
            }


        } catch (Exception e) {
            model.addAttribute("fail", "Kích thước của hình ảnh không được vượt quá 20MB");
            e.printStackTrace();
        }
        AdminUserDto userDto = adminUserService.findById(adminUserDto.getId());
        model.addAttribute("userDto", userDto);
        model.addAttribute("changePasswordDto", ChangePasswordDto.builder().build());
        model.addAttribute("fail", "Có lỗi xảy ra vui lòng thử lại");
        return "common/profile";
    }
}

package com.viethung.controller.admin;

import com.viethung.dto.AdminDto;
import com.viethung.entity.User;
import com.viethung.service.AdminsServiceImpl;
import com.viethung.service.RegisterServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    @Autowired
    private AdminsServiceImpl adminsService;

    @Autowired
    private RegisterServiceImpl registerService;


    @GetMapping("/admins")
    public String showAllAdmin(@RequestParam Optional<Integer> page,
                                  Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 50);
        Page<User> admins = adminsService.findAll(pageable);
        model.addAttribute("admins", admins);
        return "/admin/page/admins";
    }

    @GetMapping("/admins/{id}")
    public String displayAdminDetail(@PathVariable UUID id, Model model) {
        AdminDto adminDto = adminsService.findById(id);
        model.addAttribute("adminDto", adminDto);
        return "/admin/page/admin-form";
    }

    @GetMapping("/admins/save")
    public String onAdd(Model model) {
        AdminDto adminDto = AdminDto.builder()
                .code(registerService.generateUserCode())
                .gender(true)
                .build();
        model.addAttribute("adminDto", adminDto);
        return "/admin/page/admin-form";
    }

    @PostMapping("/admins/add")
    public String onAdd(@Valid @ModelAttribute AdminDto  adminDto,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        //
        try {
            //check value
            if (adminsService.existsByCode(adminDto.getCode())) {
                result.rejectValue("code", "duplicate", "Mã đã tồn tại");
            }
            if (adminsService.existsByEmail(adminDto.getEmail())) {
                result.rejectValue("email", "duplicate", "Email đã tồn tại");
            }
            if (adminsService.existsByPhoneNumber(adminDto.getPhoneNumber())) {
                result.rejectValue("phoneNumber", "duplicate", "Số điện thoại đã tồn tại");
            }
            if (adminDto.getPassword().isEmpty()) {
                result.rejectValue("password", "invalid", "Mật khẩu không được trống");
            }
            //check format
            if (result.hasErrors()) {
                model.addAttribute("adminDto", adminDto);
                return "/admin/page/admin-form";
            }
            // Save
            if (adminsService.handleAdd(adminDto)) {
                redirectAttributes.addFlashAttribute("success", "Lưu thành công");
                return "redirect:/admin/admins";
            }


        } catch (Exception e) {
            model.addAttribute("fail", "Kích thước của hình ảnh không được vượt quá 20MB");
            e.printStackTrace();
        }
        model.addAttribute("adminDto", adminDto);
        return "/admin/page/admin-form";
    }

    @PostMapping("/admins/update")
    public String onUpdate(@Valid @ModelAttribute AdminDto adminDto,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        //
        try {
            //check value
            if (adminsService.countByCodeAndIdNot(adminDto.getCode(), adminDto.getId()) > 0) {
                result.rejectValue("code", "duplicate", "Mã đã tồn tại");
            }
            if (adminsService.countByEmailAndIdNot(adminDto.getEmail(), adminDto.getId()) > 0) {
                result.rejectValue("email", "duplicate", "Email đã tồn tại");
            }
            if (adminsService.countByPhoneNumberAndIdNot(adminDto.getPhoneNumber(), adminDto.getId()) > 0) {
                result.rejectValue("phoneNumber", "duplicate", "Số điện thoại đã tồn tại");
            }
            //check format
            if (result.hasErrors()) {
                model.addAttribute("adminDto", adminDto);
                return "/admin/page/admin-form";
            }
            //save
            if (adminsService.handleUpdate(adminDto)) {
                redirectAttributes.addFlashAttribute("success", "Lưu thành công");
                return "redirect:/admin/admins";
            }

        } catch (Exception e) {
            model.addAttribute("fail", "Kích thước của hình ảnh không được vượt quá 20MB");
            e.printStackTrace();
        }
        model.addAttribute("adminDto", adminDto);
        return "/admin/page/admin-form";
    }

    //delete
    @GetMapping("/admins/delete/{id}")
    public String onDeleteById(@PathVariable UUID id,
                               RedirectAttributes redirectAttributes) {
        try {
            adminsService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("fail", "Xóa thất bại");
        }
        return "redirect:/admin/admins";
    }

    //search
    @GetMapping("/admins/search")
    public String search(@RequestParam Optional<Integer> page,
                         @RequestParam Optional<String> keys,
                         Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 50);
        Page<User> admins = adminsService.findAllByKeys(pageable,keys.orElse(""));
        model.addAttribute("admins", admins);
        return "/admin/page/admin-search";
    }
}

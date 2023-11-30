package com.viethung.controller.admin;

import com.viethung.dto.AdminUserDto;
import com.viethung.dto.OrderDto;
import com.viethung.entity.User;
import com.viethung.service.AdminUserService;
import com.viethung.service.RegisterService;
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
public class UsersController {
    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private RegisterService registerService;


    @GetMapping("/users")
    public String showAllUser(@RequestParam Optional<Integer> page,
                               Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 50);
        Page<User> users = adminUserService.findAll(pageable);
        model.addAttribute("users", users);
        return "/admin/page/users";
    }

    @GetMapping("/users/{id}")
    public String displayAdminDetail(@PathVariable UUID id, Model model) {
        AdminUserDto adminUserDto = adminUserService.findById(id);
        model.addAttribute("adminUserDto", adminUserDto);
        return "/admin/page/user-form";
    }

    @GetMapping("/users/save")
    public String onAdd(Model model) {
        AdminUserDto adminUserDto = AdminUserDto.builder()
                .code(registerService.generateUserCode())
                .position(0)
                .gender(true)
                .build();
        model.addAttribute("adminUserDto", adminUserDto);
        return "/admin/page/user-form";
    }
//
    @PostMapping("/users/add")
    public String onAdd(@Valid @ModelAttribute AdminUserDto  adminUserDto,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        try {
            //check value
            if (adminUserService.existsByCode(adminUserDto.getCode())) {
                result.rejectValue("code", "duplicate", "Mã đã tồn tại");
            }
            if (adminUserService.existsByEmail(adminUserDto.getEmail())) {
                result.rejectValue("email", "duplicate", "Email đã tồn tại");
            }
            if (adminUserService.existsByPhoneNumber(adminUserDto.getPhoneNumber())) {
                result.rejectValue("phoneNumber", "duplicate", "Số điện thoại đã tồn tại");
            }
            if (adminUserDto.getPassword().isEmpty()) {
                result.rejectValue("password", "invalid", "Mật khẩu không được trống");
            }
            //check format
            if (result.hasErrors()) {
                model.addAttribute("adminUserDto", adminUserDto);
                return "/admin/page/user-form";
            }
             // Save
            if (adminUserService.handleAdd(adminUserDto)) {
                redirectAttributes.addFlashAttribute("success", "Lưu thành công");
                return "redirect:/admin/users";
            }


        } catch (Exception e) {
            model.addAttribute("fail", "Kích thước của hình ảnh không được vượt quá 20MB");
            e.printStackTrace();
        }
        model.addAttribute("adminUserDto", adminUserDto);
        return "/admin/page/user-form";
    }

    @PostMapping("/users/update")
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
            if (result.hasErrors()) {
                model.addAttribute("adminUserDto", adminUserDto);
                return "/admin/page/user-form";
            }
            //save
            if (adminUserService.handleUpdate(adminUserDto)) {
                redirectAttributes.addFlashAttribute("success", "Lưu thành công");
                return "redirect:/admin/users";
            }

        } catch (Exception e) {
            model.addAttribute("fail", "Kích thước của hình ảnh không được vượt quá 20MB");
            e.printStackTrace();
        }
        model.addAttribute("adminUserDto", adminUserDto);
        return "/admin/page/user-form";
    }

    //delete
    @GetMapping("/users/delete/{id}")
    public String onDeleteById(@PathVariable UUID id,
                               RedirectAttributes redirectAttributes) {
        try {
            adminUserService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("fail", "Xóa thất bại");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/user-detail/{id}")
    public String showUserDetail(@PathVariable UUID id,
                                 @RequestParam Optional<Integer> page ,
                                 Model model) {
        User user = adminUserService.finUserById(id);
        Pageable pageable = PageRequest.of(page.orElse(0),50);
        Page<OrderDto> orderDtos = adminUserService.findAllOrderByUserId(user.getId(),pageable);
        model.addAttribute("user",user);
        model.addAttribute("orderDtos",orderDtos);
        return "admin/page/user-detail";
    }
    @GetMapping("/user-detail/")
    public String showUserDetail(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("fail","Người dùng không tồn tại");
        return "redirect:/admin/orders";
    }
//
    //search
    @GetMapping("/users/search")
    public String search(@RequestParam Optional<Integer> page,
                         @RequestParam Optional<String> keys,
                         Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 50);
        Page<User> users = adminUserService.findAllByKeys(pageable,keys.orElse(""));
        model.addAttribute("users", users);
        return "/admin/page/users";
    }
}

package com.viethung.controller.admin;

import com.viethung.dto.CustomerDto;
import com.viethung.entity.User;
import com.viethung.service.CustomerServiceImpl;
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
public class CustomersController {
    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private RegisterServiceImpl registerService;

    @GetMapping("/customers")
    public String showAllCustomer(@RequestParam Optional<Integer> page,
                                  Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 50);
        Page<User> customers = customerService.findAll(pageable);
        model.addAttribute("customers", customers);
        return "/admin/page/customers";
    }

    //create
    @GetMapping("/customers/save")
    public String onAdd(Model model) {
        CustomerDto customerDto = CustomerDto.builder()
                .code(registerService.generateUserCode())
                .gender(true)
                .build();
        model.addAttribute("customerDto", customerDto);
        return "/admin/page/customer-form";
    }

    @PostMapping("/customers/add")
    public String onAdd(@Valid @ModelAttribute CustomerDto customerDto,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        //
        try {
            //check value
            if (customerService.existsByCode(customerDto.getCode())) {
                result.rejectValue("code", "duplicate", "Mã đã tồn tại");
            }
            if (customerService.existsByEmail(customerDto.getEmail())) {
                result.rejectValue("email", "duplicate", "Email đã tồn tại");
            }
            if (customerService.existsByPhoneNumber(customerDto.getPhoneNumber())) {
                result.rejectValue("phoneNumber", "duplicate", "Số điện thoại đã tồn tại");
            }
            if (customerDto.getPassword().isEmpty()) {
                result.rejectValue("password", "invalid", "Mật khẩu không được trống");
            }
            //check format
            if (result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                return "/admin/page/customer-form";
            }
            // Save
            if (customerService.handleAdd(customerDto)) {
                redirectAttributes.addFlashAttribute("success", "Lưu thành công");
                return "redirect:/admin/customers";
            }


        } catch (Exception e) {
            model.addAttribute("fail", "Kích thước của hình ảnh không được vượt quá 20MB");
            e.printStackTrace();
        }
        model.addAttribute("customerDto", customerDto);
        return "/admin/page/customer-form";
    }

    //update
    @PostMapping("/customers/update")
    public String onUpdate(@Valid @ModelAttribute CustomerDto customerDto,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        //
        try {
            //check value
            if (customerService.countByCodeAndIdNot(customerDto.getCode(), customerDto.getId()) > 0) {
                result.rejectValue("code", "duplicate", "Mã đã tồn tại");
            }
            if (customerService.countByEmailAndIdNot(customerDto.getEmail(), customerDto.getId()) > 0) {
                result.rejectValue("email", "duplicate", "Email đã tồn tại");
            }
            if (customerService.countByPhoneNumberAndIdNot(customerDto.getPhoneNumber(), customerDto.getId()) > 0) {
                result.rejectValue("phoneNumber", "duplicate", "Số điện thoại đã tồn tại");
            }
            //check format
            if (result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                return "/admin/page/customer-form";
            }
            //save
            if (customerService.handleUpdate(customerDto)) {
                redirectAttributes.addFlashAttribute("success", "Lưu thành công");
                return "redirect:/admin/customers";
            }

        } catch (Exception e) {
            model.addAttribute("fail", "Kích thước của hình ảnh không được vượt quá 20MB");
            e.printStackTrace();
        }
        model.addAttribute("customerDto", customerDto);
        return "/admin/page/customer-form";
    }

    // show detail
    @GetMapping("/customers/{id}")
    public String displayCustomerDetail(@PathVariable UUID id, Model model) {
        CustomerDto customerDto = customerService.findById(id);
        model.addAttribute("customerDto", customerDto);
        return "/admin/page/customer-form";
    }

    //delete
    @GetMapping("/customers/delete/{id}")
    public String onDeleteById(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("fail", "Xóa thất bại");
        }
        return "redirect:/admin/customers";
    }

    //search
    @GetMapping("/customers/search")
    public String search(@RequestParam Optional<Integer> page,
                         @RequestParam Optional<String> keys,
                         Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 50);
        Page<User> customers = customerService.findAllByKeys(pageable,keys.orElse(""));
        model.addAttribute("customers", customers);
//        model.addAttribute("keys", keys);
        return "/admin/page/customers-search";
    }
}

package com.viethung.controller.admin;

import com.viethung.dto.CategoryDto;
import com.viethung.entity.Category;
import com.viethung.service.CategoryServiceImpl;
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
public class CategoriesController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/categories")
    public String displayView(@RequestParam Optional<Integer> page,
                              Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 50);
        Page<Category> categories = categoryService.findAll(pageable);

        model.addAttribute("categories", categories);
        return "/admin/page/categories";
    }

    @GetMapping("/categories/save")
    public String displayViewForm(Model model) {
        CategoryDto categoryDto = CategoryDto.builder().code(categoryService.generateCategoryCode()).build();
        model.addAttribute("categoryDto", categoryDto);
        return "/admin/page/category-form";
    }

    @PostMapping("/categories/save")
    public String onSave(@Valid @ModelAttribute CategoryDto categoryDto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        //Khi edit sẽ không check mã trùng
        if (categoryDto.getId() == null){
            if (categoryService.existsByCode(categoryDto.getCode())) {
                result.rejectValue("code", "duplicate", "Mã đã tồn tại");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("categoryDto", categoryDto);
            return "/admin/page/category-form";
        }
        //save
        if (categoryService.save(categoryDto).getId() == null) {
            redirectAttributes.addFlashAttribute("fail", "Thất bại! Vui lòng thử lại");
        } else {
            redirectAttributes.addFlashAttribute("success", "Thành công");
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/{id}")
    public String onEdit(Model model,
                         @PathVariable UUID id) {
        CategoryDto categoryDto = categoryService.findById(id);
        model.addAttribute("categoryDto", categoryDto);
        return "/admin/page/category-form";
    }

    @GetMapping("/categories/delete/{id}")
    public String onDelete(@PathVariable UUID id,
                           RedirectAttributes redirectAttributes) {
        boolean check = categoryService.deleteById(id);
        if (check){
            redirectAttributes.addFlashAttribute("success", "Thành công");
        }else {
            redirectAttributes.addFlashAttribute("fail", "Thất bại! Vui lòng thử lại");
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/search")
    public String onSearch(@RequestParam Optional<Integer> page,
                           @RequestParam Optional<String> keys,
                           Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 50);
        Page<Category> categories = categoryService.search(keys.orElse(""),pageable);

        model.addAttribute("categories", categories);
        return "/admin/page/categories";
    }
}

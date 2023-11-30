package com.viethung.controller.admin;

import com.viethung.dto.OrderDto;
import com.viethung.dto.ProductFormDto;
import com.viethung.dto.ProductListDto;
import com.viethung.entity.Category;
import com.viethung.entity.Product;
import com.viethung.service.CategoryService;
import com.viethung.service.OrderService;
import com.viethung.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class ProductsController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;


    @GetMapping("/products")
    public String showAllProduct(@RequestParam Optional<Integer> page,
                                 Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0),50,Sort.by("createdDate").descending());
        Page<ProductListDto> productListDtos = productService.findProducts(pageable);
        model.addAttribute("productListDtos",productListDtos);
        return "admin/page/products";
    }

    @GetMapping("/products/save")
    public String showProductForm(Model model) {
        List<Category> categories = categoryService.findAll();
        ProductFormDto productFormDto = ProductFormDto.builder()
                .code(productService.generateCode())
                .price(BigDecimal.valueOf(0))
                .cost(BigDecimal.valueOf(0))
                .status(1)
                .weight(Float.valueOf(0))
                .build();
        model.addAttribute("productFormDto", productFormDto);
        model.addAttribute("categories", categories);
        return "admin/page/product-form";
    }

    @PostMapping("/products/add")
    public String onAdd(@Valid @ModelAttribute ProductFormDto productFormDto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        //validate
        if (productService.existsByCode(productFormDto.getCode())) {
            result.rejectValue("code", "duplicate", "Mã đã tồn tại");
        }
        if (productFormDto.getImages().size() < 2) {
            result.rejectValue("images", "invalid", "Vui lòng chọn 3 ảnh của sản phẩm");
        }
        if (productFormDto.getPrice().longValue() < productFormDto.getCost().longValue()) {
            result.rejectValue("price", "invalid", "Giá bán phải lớn hơn giá nhập");
        }
        if (result.hasErrors()) {
            List<Category> categories = categoryService.findAll();
            model.addAttribute("productFormDto", productFormDto);
            model.addAttribute("categories", categories);
            return "admin/page/product-form";
        }
        if (productService.handleAdd(productFormDto)) {
            redirectAttributes.addFlashAttribute("success", "Lưu thành công");
        } else {
            redirectAttributes.addFlashAttribute("fail", "Lưu thất bại. Vui lòng thử lại");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/{id}")
    public String showProductForm(Model model, @PathVariable UUID id) {
        List<Category> categories = categoryService.findAll();
        ProductFormDto productFormDto = productService.findProductFormById(id);
        model.addAttribute("productFormDto", productFormDto);
        model.addAttribute("categories", categories);
        return "admin/page/product-form";
    }
    @GetMapping("/product-detail/{id}")
    public String showProductDetail(Model model,
                                    @RequestParam Optional<Integer> page,
                                    @PathVariable UUID id) {
        Product product = productService.findProductDetailById(id);
        Pageable pageable = PageRequest.of(page.orElse(0),50);
        Page<OrderDto> orderDtos = orderService.findAllOrderByProduct(product,pageable);
        model.addAttribute("product", product);
        model.addAttribute("orderDtos", orderDtos);
        return "admin/page/product-detail";
    }

    @GetMapping("/products/delete/{id}")
    public String onDeleteProduct(@PathVariable UUID id,RedirectAttributes redirectAttributes) {
        if (productService.deleteById(id)){
            redirectAttributes.addFlashAttribute("success","Xóa thành công");
        }else {
            redirectAttributes.addFlashAttribute("fail","Xóa thất bại vui lòng thử lại");
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/save")
    public String onSave(@Valid @ModelAttribute ProductFormDto productFormDto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        //validate
        if (productService.countByCodeAndIdNot(productFormDto.getCode(),productFormDto.getId()) > 0) {
            result.rejectValue("code", "duplicate", "Mã đã tồn tại");
        }
        if (productFormDto.getPrice().longValue() < productFormDto.getCost().longValue()) {
            result.rejectValue("price", "invalid", "Giá bán phải lớn hơn giá nhập");
        }
        if (result.hasErrors()) {
            List<Category> categories = categoryService.findAll();
            model.addAttribute("productFormDto", productFormDto);
            model.addAttribute("categories", categories);
            return "admin/page/product-form";
        }
        System.out.println(productFormDto);
        if (productService.handleUpdate(productFormDto)) {
            redirectAttributes.addFlashAttribute("success", "Lưu thành công");
        } else {
            redirectAttributes.addFlashAttribute("fail", "Lưu thất bại. Vui lòng thử lại");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/search")
    public String searchProduct(@RequestParam Optional<Integer> page,
                                 @RequestParam Optional<String> keys,
                                 Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0),50,Sort.by("createdDate").descending());
        Page<ProductListDto> productListDtos = productService.searchProducts(keys.orElse(""),pageable);
        model.addAttribute("productListDtos",productListDtos);
        return "admin/page/products";
    }
}

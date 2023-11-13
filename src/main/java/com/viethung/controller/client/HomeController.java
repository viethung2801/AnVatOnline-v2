package com.viethung.controller.client;

import com.viethung.dto.ProductCardDto;
import com.viethung.entity.Category;
import com.viethung.service.CategoryServiceImpl;
import com.viethung.service.client.HomeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private HomeServiceImpl homeService;

    @GetMapping("/")
    public String home(Model model) {
        List<Category> categories = homeService.findAll();
        List<ProductCardDto> top12Bests = homeService.findTop12BestSeller();
        List<ProductCardDto> top8News = homeService.findTop8ProductNew();

        model.addAttribute("categories", categories);
        model.addAttribute("top12Bests", top12Bests);
        model.addAttribute("top8News", top8News);
        return "client/page/home";
    }
}

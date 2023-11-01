package com.viethung.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductAllController {
    @GetMapping("/product-all")
    public String displayView(Model model){
        return "client/page/product-all";
    }
}

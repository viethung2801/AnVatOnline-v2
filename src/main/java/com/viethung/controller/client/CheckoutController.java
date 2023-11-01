package com.viethung.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {
    @GetMapping("/checkout")
    public String displayView(Model model){
        model.addAttribute("message","Home Page");
        return "client/page/checkout";
    }
}

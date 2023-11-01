package com.viethung.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("message","Home Page");
        return "home";
    }
}

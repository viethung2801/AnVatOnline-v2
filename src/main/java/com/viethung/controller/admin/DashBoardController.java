package com.viethung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashBoardController {
    @GetMapping("/dashboard")
    public String displayView(Model model){
        return "admin/page/dashboard";
    }
    @GetMapping("")
    public String displayView(){
        return "redirect:/admin/dashboard";
    }
}

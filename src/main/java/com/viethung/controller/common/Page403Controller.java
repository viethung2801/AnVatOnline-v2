package com.viethung.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Page403Controller {
    @GetMapping("/403")
    public String displayView(Model model){
        return "common/403";
    }
}

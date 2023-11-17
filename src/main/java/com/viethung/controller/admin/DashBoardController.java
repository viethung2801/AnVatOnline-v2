package com.viethung.controller.admin;

import com.viethung.dto.ProductReportDto;
import com.viethung.service.DashBoardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class DashBoardController {
    @Autowired
    private DashBoardServiceImpl dashBoardService;

    @GetMapping("/dashboard")
    public String displayView(@RequestParam Optional<Integer> dayBefore,
                              @RequestParam Optional<String> bestSale,
                              Model model) {
        int revenue = dashBoardService.getRevenueToday();
        int orderProcess = dashBoardService.getOrderProcessToday();
        int orderCancel = dashBoardService.getOrderCancelToday();
        int orderSuccess = dashBoardService.getOrderSuccessToday();

        List data = dashBoardService.getData(dayBefore.orElse(7));
        List<ProductReportDto> productReportDtos = dashBoardService.getProductReports(bestSale.orElse("today"));


        model.addAttribute("revenue", revenue);
        model.addAttribute("orderProcess", orderProcess);
        model.addAttribute("orderCancel", orderCancel);
        model.addAttribute("orderSuccess", orderSuccess);

        model.addAttribute("labels", data.get(0));
        model.addAttribute("profitData", data.get(1));
        model.addAttribute("revenueData", data.get(2));
        model.addAttribute("productReportDtos", productReportDtos);

        return "admin/page/dashboard";
    }

    @GetMapping("")
    public String displayView() {
        return "redirect:/admin/dashboard";
    }
}

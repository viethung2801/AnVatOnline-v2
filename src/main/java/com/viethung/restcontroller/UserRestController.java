package com.viethung.restcontroller;

import com.viethung.dto.AdminUserDto;
import com.viethung.service.AdminUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private AdminUserServiceImpl adminUserService;

    @Autowired
    public void setAdminUserService(AdminUserServiceImpl adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/users/top7")
    public ResponseEntity<List<AdminUserDto>> find7Product(@RequestParam(defaultValue = "") String keys) {
        List<AdminUserDto> adminUserDtos = adminUserService.findByKeys(keys);
        return ResponseEntity.ok(adminUserDtos);
    }
}

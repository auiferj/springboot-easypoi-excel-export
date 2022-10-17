package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lenovo
 * @since 2022-10-13
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * excel批量用户导出
     */
    @GetMapping("export")
    public void exportUsers(HttpServletResponse response) {
        userService.exportUsers(response);
    }

    /**
     * excel批量用户导出
     */
    @GetMapping("/exportImage")
    public void exportUsersToExcel(HttpServletResponse response) {
        userService.exportUsersImage(response);
    }
}

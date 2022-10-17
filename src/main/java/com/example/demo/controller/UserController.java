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
     * 单sheet
     * excel批量用户导出
     */
    @GetMapping("export")
    public void exportUsers(HttpServletResponse response) {
        userService.exportUsers(response);
    }

    /**
     * 单sheet
     * excel批量用户导出
     */
    @GetMapping("/exportImage")
    public void exportUsersToExcel(HttpServletResponse response) {
        userService.exportUsersImage(response);
    }

    /**
     * 多sheet
     * excel多sheet导出
     */
    @GetMapping("/export-for-sheets")
    public void exportSheetUsers(HttpServletResponse response) {
        userService.exportSheetUsers(response);
    }

    /**
     * 导出pdf文件
     */
    @GetMapping("/export-for-pdf")
    public void exportPdfUsers(HttpServletResponse response) {
        userService.exportPdfUsers(response);
    }
}

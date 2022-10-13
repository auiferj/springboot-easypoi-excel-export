package com.example.demo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.example.demo.entity.ExportUser;
import com.example.demo.entity.User;
import com.example.demo.dao.UserMapper;
import com.example.demo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lenovo
 * @since 2022-10-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public void exportUsers(HttpServletResponse response) {
        try {
            //从数据库查询到数据
            List<User> users = this.list();
            //设置信息头，告诉浏览器内容为excel类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            //文件名称
            String fileName = "学生信息表.xls";
            //sheet名称
            String sheetName = "学生列表";
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            //设置下载名称
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            //字节流输出
            ServletOutputStream out = response.getOutputStream();
            //设置excel参数
            ExportParams params = new ExportParams();
            //设置sheet名
            params.setSheetName(sheetName);
            //设置标题
            params.setTitle("学生信息表");
            //转成对应的类型;要不然会报错，虽然也可以导出成功。
            List<ExportUser> exportUsers = changeType(users);
            //导入excel
            Workbook workbook = ExcelExportUtil.exportExcel(params, ExportUser.class,exportUsers );
            //写入
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转成导出vo
     *
     * @param users  users集合
     */
    private List<ExportUser> changeType(List<User> users) {
        if(CollectionUtils.isNotEmpty(users)){
            List<ExportUser> list = new ArrayList<>();
            for (User user : users) {
                ExportUser exportUser = new ExportUser();
                BeanUtils.copyProperties(user,exportUser);
                list.add(exportUser);
            }
            return list;
        }
        return Collections.emptyList();
    }
}

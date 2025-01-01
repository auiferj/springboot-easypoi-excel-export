package com.example.demo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.*;
import com.example.demo.dao.UserMapper;
import com.example.demo.enums.ContentTypeEnum;
import com.example.demo.service.OperationLogService;
import com.example.demo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.utils.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Autowired
    private OperationLogService operationLogService;

    @Override
    public void exportUsers(HttpServletResponse response) {
        //从数据库查询到数据
        List<User> users = this.list();
        if(CollectionUtils.isEmpty(users)){
            throw new RuntimeException("暂无数据");
        }
        //转成对应的类型;要不然会报错，虽然也可以导出成功。
        List<ExportUser> exportList = changeTypeToExportUser(users);
        try (ServletOutputStream out = response.getOutputStream()){
            FileUtils.prepareResponseHeaders(response, "学生信息表-" +System.currentTimeMillis() + ".xlsx", ContentTypeEnum.XLSX);
            //设置excel参数
            ExportParams params = new ExportParams();
            params.setSheetName("学生列表");
            params.setTitle("学生信息表");
            try (Workbook workbook = ExcelExportUtil.exportExcel(params, ExportUser.class, exportList)) {
                workbook.write(out);
            } catch (IOException e) {
                log.error("导出Excel时发生异常", e);
                throw new RuntimeException("导出失败");
            }
        } catch (Exception e) {
            log.error("获取输出流时发生异常", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportUsersImage(HttpServletResponse response) {
        //从数据库查询到数据
        List<User> users = this.list();
        if(CollectionUtils.isEmpty(users)){
            throw new RuntimeException("暂无数据");
        }
        //转成对应的类型;要不然会报错，虽然也可以导出成功
        List<ExportUserImage> exportList = this.changeTypeToExportUserImage(users);
        try (ServletOutputStream out = response.getOutputStream()){
            FileUtils.prepareResponseHeaders(response, "学生信息表-" +System.currentTimeMillis() + ".xlsx", ContentTypeEnum.XLSX);
            //设置excel参数
            ExportParams params = new ExportParams();
            params.setSheetName("学生列表");
            params.setTitle("学生信息表");
            try (Workbook workbook = ExcelExportUtil.exportExcel(params, ExportUserImage.class, exportList)) {
                workbook.write(out);
            } catch (IOException e) {
                log.error("导出Excel时发生异常", e);
                throw new RuntimeException("导出失败");
            }
        } catch (Exception e) {
            log.error("导出Excel时发生异常", e);
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public void exportSheetUsers(HttpServletResponse response) {
        //功能描述：把同一个表格多个sheet测试结果重新输出.
        Workbook workBook = null;
        try {
            // 创建参数对象（用来设定excel的sheet1内容等信息）
            ExportParams userExportParams = new ExportParams();
            // 设置sheet得名称
            userExportParams.setSheetName("用户表");
            // 设置sheet表头名称
            userExportParams.setTitle("用户列表");
            // 创建sheet1使用得map
            Map<String, Object> userExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            userExportMap.put("title", userExportParams);
            // 模版导出对应得实体类型
            userExportMap.put("entity", ExportUser.class);
            //转成导出vo类型
            List<ExportUser> users = this.changeTypeToExportUser(this.list());
            // sheet1中要填充得数据
            userExportMap.put("data", users);
            // 创建参数对象（用来设定excel的sheet2内容等信息）
            ExportParams logInfoExportParams = new ExportParams();
            logInfoExportParams.setTitle("日志列表");
            logInfoExportParams.setSheetName("日志表");
            // 创建sheet2使用的map
            Map<String, Object> logInfoExportMap = new HashMap<>();
            logInfoExportMap.put("title", logInfoExportParams);
            logInfoExportMap.put("entity", ExportLog.class);
            //查询log数据
            List<OperationLog> operationLogs = operationLogService.list();
            //转成导出vo类型
            List<ExportLog> logInfos = this.changeTypeToExportLog(operationLogs);
            // sheet2中要填充得数据
            logInfoExportMap.put("data", logInfos);
            // 将sheet1、sheet2使用得map进行包装
            List<Map<String, Object>> sheetsList = new ArrayList<>();
            //后续增加sheet组，则后面继续追加即可;
            sheetsList.add(userExportMap);
            sheetsList.add(logInfoExportMap);
            // 执行方法
            workBook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            //设置编码格式
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            //设置内容类型
            response.setContentType("application/octet-stream");
            //设置头及文件命名。
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户及操作日志导出.xls", StandardCharsets.UTF_8.name()));
            //写出流
            workBook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workBook != null) {
                try {
                    //强行关流
                    workBook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void exportPdfUsers(HttpServletResponse response) {
        //准备导出数据
        Map<String, Object> mapList = new HashMap<>();
        //准备导出数据
        List<Map<String, Object>> listUsers = new ArrayList<>();
        try {
            //指定excel模板；我这是在项目根目录下创建了一个template文件夹存放excel导出模板文件
            TemplateExportParams params = new TemplateExportParams("templates/user_export_template.xlsx");
            //从数据库查询到数据
            List<User> users = this.list();
            //定义一个原子序列
            AtomicInteger atomicInteger = new AtomicInteger(1);
            users.forEach(user -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", atomicInteger.getAndIncrement());
                map.put("name", user.getName());
                map.put("age", user.getAge());
                map.put("sex", user.getSex());
                map.put("address", user.getAddress());
                map.put("describes", user.getDescribes());
                //添加到集合中，一个map就是一行
                listUsers.add(map);
            });
            mapList.put("users", listUsers);
            //调用exportExcel()
            Workbook workbook = ExcelExportUtil.exportExcel(params, mapList);
            //定义一个字节输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //将excel文件写入到新的输出流
            workbook.write(outputStream);
            //将字节数组放置到内存里面
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            //导入xls包引用Workbook区分poi提供的Workbook
            com.spire.xls.Workbook wb = new com.spire.xls.Workbook();
            wb.loadFromStream(inputStream);
            //设置字段在一页中显示全
            wb.getConverterSetting().setSheetFitToPage(true);
            //指定并指定目录保存文件
            wb.saveToFile("D:/pdf/学生基本信息表.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转成导出vo
     *
     * @param users  users集合
     * @return
     */
    private List<ExportUser> changeTypeToExportUser(List<User> users) {
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

    /**
     * 转成导出vo
     *
     * @param users
     * @return
     */
    private List<ExportUserImage> changeTypeToExportUserImage(List<User> users) {
        if(CollectionUtils.isNotEmpty(users)){
            List<ExportUserImage> list = new ArrayList<>();
            for (User user : users) {
                ExportUserImage exportUserImage = new ExportUserImage();
                BeanUtils.copyProperties(user,exportUserImage);
                //正常情况是获取数据库中每个用户对象的头像地址 --> this.image = user.Img();
                exportUserImage.setImage("static/image/avatar.png");
                list.add(exportUserImage);
            }
            return list;
        }
        return Collections.emptyList();
    }

    private List<ExportLog> changeTypeToExportLog(List<OperationLog> operationLogs) {
        if(CollectionUtils.isNotEmpty(operationLogs)){
            List<ExportLog> list = new ArrayList<>();
            for (OperationLog operationLog : operationLogs) {
                ExportLog exportUser = new ExportLog();
                BeanUtils.copyProperties(operationLog,exportUser);
                list.add(exportUser);
            }
            return list;
        }
        return Collections.emptyList();
    }
}

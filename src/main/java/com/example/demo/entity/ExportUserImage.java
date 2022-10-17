package com.example.demo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lenovo
 */
@Data
public class ExportUserImage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * @Excel 作用在一个filed上面，对列的描述
     * @param name 列名
     * @param orderNum 下标，从0开始。
     */
    @Excel(name = "姓名", width = 10.0)
    private String name;
    @Excel(name = "年龄", width = 10.0)
    private Integer age;
    /**
     * 字段是Date类型则不需要设置databaseFormat
     * format：excel中输出时间格式
     */
    @Excel(name = "出生年月", format = "yyyy-MM-dd", width = 20.0)
    private Date bornDate;
    /**
     * 如果数据库是string类型,这个需要设置这个数据库时间格式
     * format：excel中输出时间格式
     */
    @Excel(name = "入学时间", databaseFormat = "yyyyMMdd", format = "yyyy-MM-dd", width = 20.0)
    private String enterSchoolTime;
    /**
     * replace：单元格下拉框，_0表示下拉顺序   suffix：文字后缀 比如：男->男生
     */
    @Excel(name = "性别", width = 10.0, replace = {"男_0", "女_1"}, suffix = "生", addressList = true)
    private Integer sex;
    @Excel(name = "地址", width = 30.0)
    private String address;
    /**
     * type ：导出类型 1 是文本 2 是图片,3 是函数,10 是数字 默认是文本
     * imageType 导出来源类型;1:从file读取;2:是从数据库中读取,默认是文件;同样导入也是一样的
     */
    @Excel(name = "头像", type = 2, width = 30.0, height = 30.0, imageType = 1)
    private String image;
    @Excel(name = "用户描述", width = 20.0)
    private String describes;
}

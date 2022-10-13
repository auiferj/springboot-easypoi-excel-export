package com.example.demo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lenovo
 */
@Data
public class ExportUser implements Serializable {
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
    @Excel(name = "性别", width = 5.0, replace = {"男_1","女_2","_null"}, addressList = true)
    private Integer sex;
    @Excel(name = "地址", width = 30.0)
    private String address;
    @Excel(name = "用户描述", width = 20.0)
    private String describes;
}

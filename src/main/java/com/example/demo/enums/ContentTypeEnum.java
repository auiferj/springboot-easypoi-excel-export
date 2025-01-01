package com.example.demo.enums;

public enum ContentTypeEnum {
    PDF("application/pdf"),
    EXCEL("application/vnd.ms-excel"),//Microsoft Excel 97-2003 (.xls) 文件格式
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), //Microsoft Excel 2007 及更高版本 (.xlsx) 文件格式
    DOC("application/msword"),//Microsoft Word 97-2003 (.doc) 文件格式
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document");//Microsoft Word 2007 及更高版本(.docx) 文件格式

    private final String value;

    ContentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

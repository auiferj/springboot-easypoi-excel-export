package com.example.demo.utils;


import com.example.demo.enums.ContentTypeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class FileUtils {
    private FileUtils() {
        // 私有构造函数，防止实例化
    }

    public static void prepareResponseHeaders(HttpServletResponse response, String fileName, ContentTypeEnum contentType) throws UnsupportedEncodingException {
        //设置信息头，告诉浏览器内容为excel类型
        response.setHeader("Content-Type", contentType.getValue());
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
    }
}

package cn.moon.lang.web;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class ServletTool {

    public static void setDownloadFileHeader(String filename, HttpServletResponse response) throws IOException {
        String fileName = URLEncoder.encode(filename,"UTF-8");
        response.reset();
        response.setHeader("Access-Control-Expose-Headers", "content-disposition");
        response.setHeader("content-disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("application/octet-stream;charset=UTF-8");
    }
}

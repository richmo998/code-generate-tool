package com.xhg.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class DownloadUtil {

    public static void download(HttpServletResponse response, String filePath, String fileName) {
        //1.获取要下载的文件的绝对路径
        File file = new File(filePath);
        if (file.exists()) {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            //3.设置content-disposition响应头控制浏览器以下载的形式打开文件
            response.setHeader("Content-Disposition", "attachment;fileName=" + (fileName + ".zip"));
            //5.创建数据缓冲区
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                //6.通过response对象获取OutputStream流
                os = response.getOutputStream();
                //4.根据文件路径获取要下载的文件输入流
                bis = new BufferedInputStream(new FileInputStream(file));
                //7.将FileInputStream流写入到buffer缓冲区
                int i = bis.read(buff);
                while (i != -1) {
                    //8.使用将OutputStream缓冲区的数据输出到客户端浏览器
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

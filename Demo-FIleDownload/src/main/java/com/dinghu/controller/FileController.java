package com.dinghu.controller;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 13:58
 * @Description:
 */

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    public void testDownLoadFile(HttpServletResponse response){
        String basePath = "/basepath/";
        String filename = "test.txt";
        String filePath = basePath + filename;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 读取文件
            File file = new File(filePath);
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        }catch (Exception e) {
            log.error("文件下载异常");
        }
    }


    public void testDownLoadFileFromServer(HttpServletResponse response){
        String username = "mobei";
        String host = "192.188.1.1";
        Integer port = 22;
        String password = "123456";

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channel = null;

        String basePath = "/basepath";
        String filename = "test.txt";

        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            // 建立远程服务器连接
            session.connect();
            // 建立通道
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            // 读取文件
            File file = new File(basePath + filename);
            InputStream inputStream = channel.get(basePath + filename);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }catch (Exception e) {
            log.error(e.getMessage());
        }finally {
            if (null != channel) {
                channel.disconnect();
            }
            if (null != session) {
                session.disconnect();
            }
        }
    }



}

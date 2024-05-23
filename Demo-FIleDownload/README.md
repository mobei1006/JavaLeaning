文件导出这里分为两种进行说明：

1. 读取本地文件，从浏览器进行导出
2. 从远程服务器读取文件，从浏览器导出
#### 一、读取本地文件，从浏览器进行导出
该功能比较简单，主要有以下几步：

1. 获取和处理文件所在的地址
2. 构建输入输出流，将文件通过输入流读取
3. 设置好相应的response，并将文件内容写到response 的输出流中
4. 关闭相应的连接
```java
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
```
#### 从远程服务器读取文件，从浏览器导出
从远程文件读取进行下载主要的难点在于如何连接到远程服务器，这里使用JSch，进行连接。首先我们需要导入依赖
```java
<dependency>
    <groupId>com.jcraft</groupId>
    <artifactId>jsch</artifactId>
    <version>0.1.55</version>
</dependency>
```
后面的步骤如下：

1. 构建服务器的连接信息，比如主机 ip、端口等
2. 新建JSch 对象，并设置好对应的参数
3. 建立连接和通道读取到远程服务器的文件到输入流
4. 设置response 相应的属性
5. 将文件写到response 的输出流
6. 关闭相应的资源连接
```java
    public void testDownLoadFile(HttpServletResponse response){
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
```

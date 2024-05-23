需求：定时任务发送 data-api 统计报表给各个人员
SpringBoot 的邮件发送功能流程如下：

1. 配置邮箱信息
```java
spring.mail.host = 发送邮箱的主机号
spring.mail.username = 发送邮箱的用户名
spring.mail.password = 授权的密码
spring.mail.default-encoding = utf-8

sendMail.to = 收件人的邮箱
sendMail.cc = 抄送的邮箱
```

2. 引入依赖
```java
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
```

3. 注入邮箱配置
```java
@Resource
private MailProperties mailProperties;

@Resource
private JavaMailSender javaMailSender;

private void addSslConfig(JavaMailSender mailSender) {
        if (!(mailSender instanceof JavaMailSenderImpl)) {
            return;
        }
        JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
        if ("true".equals(impl.getJavaMailProperties().getProperty("mail.smtp.ssl.enable"))) {
            return;
        }
        Properties props = new Properties();
        props.put("mail.smtp.sendpartial", true);
        props.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        props.put("mail.smtp.ssl.socketFactory", sf);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        impl.setJavaMailProperties(props);
    }
```

4. 发送邮件
```java
public boolean sendWithHtml(String to, String cc, String subject, String html) {
        if (StringUtils.isEmpty(to)) {
            log.info("未配置收件人!");
        }

        addSslConfig(javaMailSender);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            // 邮件发送来源
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            // 发送目标
            mimeMessageHelper.setTo(to.split(","));
            // 抄送人
            if (StringUtils.isNotBlank(cc)) {
                mimeMessageHelper.setCc(cc.split(","));
            }
            // 标题
            mimeMessageHelper.setSubject(subject);
            // 内容，并设置内容 html 格式为 true
            mimeMessageHelper.setText(html, true);

            javaMailSender.send(mimeMessage);
            log.info("## Send the mail with html success ...");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Send html mail error: ", e);
            return false;
        }
        return true;
    }
```
**注意：发送邮件的时候如果出现权限问题，很可能是邮箱没有开始相应的权限，登录网页邮箱开启即可**

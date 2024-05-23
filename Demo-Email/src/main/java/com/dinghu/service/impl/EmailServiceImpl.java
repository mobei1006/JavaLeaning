package com.dinghu.service.impl;

import com.dinghu.service.EmailService;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 11:08
 * @Description:
 */


@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Resource
    private MailProperties mailProperties; //邮件配置类，会自动的读取

    @Resource
    private JavaMailSender javaMailSender;

    @Override
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


}

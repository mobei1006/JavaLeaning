package com.dinghu.service.impl;

import com.dinghu.service.EmailService;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

}

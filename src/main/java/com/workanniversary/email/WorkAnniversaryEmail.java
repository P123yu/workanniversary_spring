package com.workanniversary.email;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Configuration
@Component
public class WorkAnniversaryEmail {

    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.port}")
    private int mailPort;

    @Value("${mail.username}")
    private String mailUsername;

    @Value("${mail.password}")
    private String mailPassword;

    private final TemplateEngine templateEngine;

    public WorkAnniversaryEmail(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return mailSender;
    }

    public boolean sendEmail(String to, String subject, String body, String empName) {
        boolean isSent = false;

        Context context = new Context();
        context.setVariable("body", body);
        context.setVariable("empName", empName);

        String process = templateEngine.process("MailTemplate.html", context);

        try {
            MimeMessage mimeMessage = javaMailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(process, true);

            javaMailSender().send(mimeMessage);
            isSent = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSent;
    }
}

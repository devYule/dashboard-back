package com.yule.dashboard.pbl.utils;

import com.yule.dashboard.pbl.exception.ServerException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailAuthenticationUtils {

    private final JavaMailSender javaMailSender;
    private final String title = "Dashboard 에서 보낸 이메일 인증 메일입니다.";

    private String genRandomCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }

    private String genRandomKey(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String sendAuthMail(String mail) {
        String code = genRandomCode();
        String msg = " Dashboard 에서 보낸 인증 메일입니다. 5분 이내에 입력해주세요.\n인증번호: " + code;
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(mail);
            helper.setSubject(title);
            helper.setText(msg);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("error", e);
            throw new ServerException();
        }
        return code;
    }

}

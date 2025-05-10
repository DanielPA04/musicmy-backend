package com.musicmy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.musicmy.dto.EmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private final String VERF_URL = "http://localhost:4200/verify/";
    private final String RESET_URL = "http://localhost:4200/reset-password/";

    public void sendMail(EmailDTO emailDTO) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setTo(emailDTO.getAddressee());
            helper.setSubject(emailDTO.getSubject());

            Context context = new org.thymeleaf.context.Context();
            context.setVariable("message", emailDTO.getMessage());
            String html = templateEngine.process("email", context);

            helper.setText(html, true);
            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + e);
        }
    }

    public void sendVerificationEmail(EmailDTO emailDTO, String verificationCode) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setTo(emailDTO.getAddressee());
            helper.setSubject(emailDTO.getSubject());

            Context context = new org.thymeleaf.context.Context();
            context.setVariable("message", emailDTO.getMessage());
            // TODO centralizar url
            context.setVariable("code",
                    "Para verificar su cuenta introduzca este codigo: " + VERF_URL + verificationCode);
            context.setVariable("time", "Tiene 1 semana para verificar su cuenta");

            String html = templateEngine.process("email", context);
            helper.setText(html, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void sendChangePassword(EmailDTO emailDTO, String verificationCode) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setTo(emailDTO.getAddressee());
            helper.setSubject(emailDTO.getSubject());

            Context context = new org.thymeleaf.context.Context();
            context.setVariable("message", emailDTO.getMessage());
            context.setVariable("code",
                    "Para cambiar su contraseña introduzca este codigo: " + RESET_URL + verificationCode);
            context.setVariable("time", "Tiene 24h para cambiar su contraseña");

            String html = templateEngine.process("email", context);
            helper.setText(html, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // public void verifyEmail(EmailDTO emailDTO) {
    // if (verifyCode(emailDTO.getAddressee(), emailDTO.getCode())) {
    // // oUsuarioverfRepository.deleteByEmail(emailDTO.getAddressee());
    // }
    // }

    // public boolean verifyCode(String email, String code) {
    // UsuarioverfEntity userVerification =
    // oUsuarioverfRepository.findByEmail(email).get();
    // if (userVerification != null) {
    // if (userVerification.getExp().isBefore(LocalDate.now())) {
    // return false; // Código expirado
    // }
    // return userVerification.getCode().equals(code);
    // }
    // return false; // Usuario no encontrado
    // }

    // public boolean isEmailPresent(String email) {
    // return oUsuarioverfRepository.findByEmail(email).isPresent();
    // }

}

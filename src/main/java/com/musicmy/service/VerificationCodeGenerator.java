package com.musicmy.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:email.properties")
public class VerificationCodeGenerator {

     @Value("${email.characters}")
    private String characters;

    @Value("${email.code_length}")
    private int codeLength;

    // @Autowired
    // private SecureRandom random;

    private final SecureRandom random = new SecureRandom();
    public String generateVerificationCode() {
        StringBuilder code = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }
}
package com.musicmy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.musicmy.dto.EmailDTO;
import com.musicmy.service.EmailService;

import jakarta.mail.MessagingException;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    EmailService emailService;
    
    // TODO
    // @Autowired
    // private VerificationCodeGenerator verificationCodeGenerator;

    @PostMapping("/sendmail")
    public ResponseEntity<String> sendMail(@RequestBody EmailDTO emailDTO) throws MessagingException {
        emailService.sendMail(emailDTO);
        return new ResponseEntity<String>("Email sent successfully", HttpStatus.OK);
    }

    // @PostMapping("/sendverificationemail")
    // public ResponseEntity<String> sendVerificationEmail(@RequestBody EmailDTO
    // emailDTO) throws MessagingException {
    // emailService.sendVerificationEmail(emailDTO,
    // verificationCodeGenerator.generateVerificationCode());
    // return new ResponseEntity<String>("Email sent successfully", HttpStatus.OK);
    // }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody EmailDTO emailDTO) {
        if (emailService.verifyCode(emailDTO.getAddressee(), emailDTO.getCode())) {
            return new ResponseEntity<String>("Code verified successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Invalid code", HttpStatus.BAD_REQUEST);
        }
    }
}

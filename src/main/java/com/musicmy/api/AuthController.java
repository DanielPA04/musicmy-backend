package com.musicmy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.musicmy.bean.LogindataBean;
import com.musicmy.dto.EmailDTO;
import com.musicmy.service.AuthService;
import com.musicmy.service.EmailService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LogindataBean credentials) {
        if (authService.checkLogin(credentials)) {
            if (!authService.checkVerification(credentials.getIdentifier())) {
                return ResponseEntity.ok("\"" + "No esta verificado, por favor verifique su correo" + "\"");
            }
            return ResponseEntity.ok("\"" + authService.getToken(credentials.getIdentifier()) + "\"");
        } else {
            return ResponseEntity.status(401).body("\"" + "Unauthorized" + "\"");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody EmailDTO emailDTO) {
        if (emailService.verifyCode(emailDTO.getAddressee(), emailDTO.getCode())) {
            authService.verify(emailDTO);
            return new ResponseEntity<String>("Code verified successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Invalid code", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resend/{credential}")
    public ResponseEntity<String> resendCode(@PathVariable String credential) {
        if (!authService.checkRegistered(credential)) {
            return ResponseEntity.status(401).body("\"" + "Unauthorized" + "\"");
        }
        //TODO
        authService.resendCode(credential);
        return ResponseEntity.ok("\"" + "Email sent successfully" + "\"");
    }

    @GetMapping("/isVerified/{credential}")
    public ResponseEntity<Boolean> isVerified(@PathVariable String credential) {
        if (!authService.checkRegistered(credential)) {
            // TODO
            return ResponseEntity.status(401).body(false);
        }
        return ResponseEntity.ok(authService.checkVerification(credential));
    }

    @GetMapping("/isSessionActive")
    public ResponseEntity<String> isSessionActive() {
        return ResponseEntity.ok("\"" + authService.RestrictedArea() + "\"");

    }

}
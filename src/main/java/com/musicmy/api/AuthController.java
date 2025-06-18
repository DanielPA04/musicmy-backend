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
import com.musicmy.dto.ChangePwdDTO;
import com.musicmy.dto.TokenDTO;
import com.musicmy.exception.VerifyException;
import com.musicmy.service.AuthService;
import com.musicmy.service.EmailService;

@RestController
@RequestMapping("/initial/auth")
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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("\"" + "No esta verificado, por favor verifique su correo" + "\"");
            }
            return ResponseEntity.ok("\"" + authService.getToken(credentials.getIdentifier()) + "\"");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("\"" + "Credenciales incorrectas" + "\"");
        }
    }

    @GetMapping("/resendCode/verify/{credential}")
    public ResponseEntity<String> resendCode(@PathVariable String credential) {
        if (authService.checkRegistered(credential)) {
            if (authService.checkVerification(credential)) {
                throw new VerifyException("\"" + "Ya verificado" + "\"");
            }
            authService.resendCodeValidation(credential);
            return ResponseEntity.ok("\"" + "Code sent successfully" + "\"");
        } else {
            return ResponseEntity.status(401).body("\"" + "No registered" + "\"");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody TokenDTO token) {
        if (authService.checkVerificationToken(token.getToken())) {
            authService.verify(token.getToken());
            return ResponseEntity.ok("\"" + authService.getTokenByValidation(token) + "\"");
        } else {
            return ResponseEntity.status(401).body("\"" + "Unauthorized, token invalido" + "\"");
        }
    }

    @GetMapping("/isVerified/{credential}")
    public ResponseEntity<Boolean> isVerified(@PathVariable String credential) {
        if (!authService.checkRegistered(credential)) {
            throw new VerifyException("\"" + "No esta registrado o verificado" + "\"");
        }
        return ResponseEntity.ok(authService.checkVerification(credential));
    }

    @GetMapping("/isSessionActive")
    public ResponseEntity<String> isSessionActive() {
        return ResponseEntity.ok("\"" + authService.RestrictedArea() + "\"");

    }

    @GetMapping("/sendCode/resetPassword/{credential}")
    public ResponseEntity<String> sendCodeResetPassword(@PathVariable String credential) {
        if (authService.checkRegistered(credential)) {
            authService.sendCodeResetPassword(credential);
            return ResponseEntity.ok("\"" + "Code sent successfully" + "\"");
        } else {
            return ResponseEntity.status(401).body("\"" + "Unauthorized" + "\"");
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePwdDTO changePwdDTO) {
        if (changePwdDTO.getOldPassword() != null && !changePwdDTO.getOldPassword().isEmpty()) {
            authService.changePassword(changePwdDTO);
            return ResponseEntity.ok().body("\"" + "Contraseña cambiada correctamente" + "\"");
        } else if (changePwdDTO.getToken() != null && !changePwdDTO.getToken().isEmpty()) {
            authService.changePasswordWhithToken(changePwdDTO);
            return ResponseEntity.ok().body("\"" + "Contraseña cambiada correctamente" + "\"");
        } else {
            return ResponseEntity.status(401).body("\"" + "Unauthorized" + "\"");
        }
    }

}
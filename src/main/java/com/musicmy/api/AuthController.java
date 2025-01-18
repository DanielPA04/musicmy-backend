package com.musicmy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musicmy.bean.LogindataBean;
import com.musicmy.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    AuthService authService;



    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LogindataBean credentials) {
        if (authService.checkLogin(credentials)) {
            return ResponseEntity.ok("\"" + authService.getToken(credentials.getEmail()) + "\"");
        } else {
            return ResponseEntity.status(401).body("\"" + "Unauthorized" + "\"");
        }
    }

    @GetMapping("/isSessionActive")
    public ResponseEntity<String> isSessionActive() {
        return ResponseEntity.ok("\"" + authService.RestrictedArea() + "\"");
       
    }

}
package com.musicmy.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.musicmy.bean.LogindataBean;
import com.musicmy.entity.UsuarioEntity;
import com.musicmy.helper.JWTHelper;
import com.musicmy.repository.TipousuarioRepository;
import com.musicmy.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Autowired
    HttpServletRequest request;

    @Autowired
    private JWTHelper oJWTHelper;

    @Autowired
    private TipousuarioRepository oTipousuarioRepository;

    @Autowired
    private UsuarioRepository oUsuarioRepository;

    public boolean checkLogin(LogindataBean credentials) {
        return oUsuarioRepository.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword())
                .isPresent();

    }

    private Map<String, String> getClaims(String email) {
        Map<String, String> claims = new HashMap<>();
        claims.put("email", email);
        return claims;
    }

    public String getToken(String email) {

        return oJWTHelper.generateToken(getClaims(email));

    }

    public UsuarioEntity getUsuarioFromToken() {
        if (request.getAttribute("email") == null) {
            //TODO: return throw exception
            return null;
        } else {
        String email = request.getAttribute("email").toString();
        return oUsuarioRepository.findByEmail(email).get();
        }
    }

    public boolean isSessionActive() {
        return request.getAttribute("email") != null;
    }

    public boolean isAdministrador() {
        return getUsuarioFromToken().getTipousuario().getId() == oTipousuarioRepository.findByNombre("Administrador").get().getId();
    }

    public boolean isUsuario() {
        return getUsuarioFromToken().getTipousuario().getId() == oTipousuarioRepository.findByNombre("Usuario").get().getId();
    }

    public boolean isOneSelf(Long id) {
        return  getUsuarioFromToken().getId().equals(id); 
    }

    public String RestrictedArea() {
        if (request.getAttribute("email") == null) {
            return "No tienes permisos para acceder a esta zona";
        } else {
            return "Bienvenido a la zona restringida";
        }
    }

}
package com.musicmy.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.musicmy.bean.LogindataBean;
import com.musicmy.dto.ChangePwdDTO;
import com.musicmy.dto.EmailDTO;
import com.musicmy.dto.TokenDTO;
import com.musicmy.entity.UsuarioEntity;
import com.musicmy.exception.ErrorChangingPassword;
import com.musicmy.exception.NoSessionException;
import com.musicmy.exception.UnauthorizedAccessException;
import com.musicmy.helper.JWTHelper;
import com.musicmy.repository.TipousuarioRepository;
import com.musicmy.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final String EMAIL_SUBJECT = "MusicMy - Verificación de correo electrónico";
    private final String EMAIL_MESSAGE = "Tu cuenta ha sido registrada";

    private final String EMAIL_SUBJECT_PWD = "MusicMy - Cambio de contraseña";
    private final String EMAIL_MESSAGE_PWD = "Se ha solicitado un cambio de contraseña";

    @Autowired
    HttpServletRequest request;

    @Autowired
    private JWTHelper oJWTHelper;

    @Autowired
    private TipousuarioRepository oTipousuarioRepository;

    @Autowired
    private UsuarioRepository oUsuarioRepository;

    @Autowired
    private EmailService oEmailService;

    public static boolean isEmail(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(input).matches();
    }

    public boolean checkLogin(LogindataBean credentials) {
        return oUsuarioRepository
                .findByIdentifierAndPassword(credentials.getIdentifier(),
                        credentials.getPassword())
                .isPresent();
    }

    private Map<String, String> getClaims(String email) {
        Map<String, String> claims = new HashMap<>();
        claims.put("email", email);
        return claims;
    }

    public String getTokenByValidation(TokenDTO token) {
        String email = oJWTHelper.validateTokenValidation(token.getToken());

        return oJWTHelper.generateToken(getClaims(email));

    }

    public String getToken(String email) {
        if (!isEmail(email)) {
            email = oUsuarioRepository.findByUsername(email).get().getEmail();
        }

        return oJWTHelper.generateToken(getClaims(email));

    }

    public String generateVerificationCode(String email) {
        if (!isEmail(email)) {
            email = oUsuarioRepository.findByUsername(email).get().getEmail();
        }

        return oJWTHelper.generateVerificationToken(getClaims(email));
    }

    public String generateResetPasswordCode(String email) {
        if (!isEmail(email)) {
            email = oUsuarioRepository.findByUsername(email).get().getEmail();
        }

        return oJWTHelper.generateResetPasswordToken(getClaims(email));
    }

    public UsuarioEntity getUsuarioFromToken() {
        if (request.getAttribute("email") == null) {
            throw new NoSessionException("No hay session activa");
        } else {
            String email = request.getAttribute("email").toString();
            return oUsuarioRepository.findByEmail(email).get();
        }
    }

    public UsuarioEntity getUsuarioFromValidateToken(String token) {
        String email = oJWTHelper.validateTokenValidation(token);
        return oUsuarioRepository.findByEmail(email).get();
    }

    public UsuarioEntity getUsuarioFromResetPwdToken(String token) {
        String email = oJWTHelper.validateTokenResetPwd(token);
        return oUsuarioRepository.findByEmail(email).get();
    }

    public boolean isSessionActive() {
        return request.getAttribute("email") != null;
    }

    public boolean isAdministrador() {
        return getUsuarioFromToken().getTipousuario().getId() == oTipousuarioRepository.findByNombre("Administrador")
                .get().getId();
    }

    public boolean isUsuario() {
        return getUsuarioFromToken().getTipousuario().getId() == oTipousuarioRepository.findByNombre("Usuario").get()
                .getId();
    }

    public boolean isOneSelf(Long id) {
        return getUsuarioFromToken().getId().equals(id);
    }

    public String RestrictedArea() {
        if (request.getAttribute("email") == null) {
            return "No tienes permisos para acceder a esta zona";
        } else {
            return "Bienvenido a la zona restringida";
        }
    }

    public boolean checkVerification(String credential) {
        UsuarioEntity user = oUsuarioRepository.findByEmailOrUsername(credential, credential).get();
        if (user.getCodverf() == null || user.getCodverf().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkVerificationToken(String token) {
        UsuarioEntity user = getUsuarioFromValidateToken(token);
        if (user.getCodverf() == null || user.getCodverf().isEmpty()) {
            // Error provisional, ya registrado
            throw new UnauthorizedAccessException("El usuario ya esta verificado");
        }
        if (user.getCodverf().equals(token)) {
            return true;
        } else {
            return false;
        }
    }

    public void verify(String token) {
        UsuarioEntity user = getUsuarioFromValidateToken(token);
        user.setCodverf(null);
        oUsuarioRepository.save(user);
    }

    public boolean checkRegistered(String credential) {
        return oUsuarioRepository.findByEmailOrUsername(credential, credential).isPresent();
    }

    public void resendCodeValidation(String credential) {
        UsuarioEntity user = oUsuarioRepository.findByEmailOrUsername(credential, credential).get();
        user.setCodverf(null);
        oUsuarioRepository.save(user);

        sendCodeValidation(user);

    }

    public void sendCodeValidation(UsuarioEntity user) {
        user.setCodverf(generateVerificationCode(user.getEmail()));
        oUsuarioRepository.save(user);

        oEmailService.sendVerificationEmail(
                new EmailDTO(user.getEmail(), this.EMAIL_SUBJECT, this.EMAIL_MESSAGE),
                user.getCodverf());
    }

    public void sendCodeResetPassword(String credential) {
        UsuarioEntity user = oUsuarioRepository.findByEmailOrUsername(credential, credential).get();
        user.setCodresetpwd(generateResetPasswordCode(user.getEmail()));
        oUsuarioRepository.save(user);

        oEmailService.sendChangePassword(
                new EmailDTO(user.getEmail(), this.EMAIL_SUBJECT_PWD, this.EMAIL_MESSAGE_PWD),
                user.getCodresetpwd());
    }

    public void sendCodeResetPassword(UsuarioEntity user) {
        user.setCodverf(generateVerificationCode(user.getEmail()));
        oUsuarioRepository.save(user);

        oEmailService.sendChangePassword(
                new EmailDTO(user.getEmail(), this.EMAIL_SUBJECT_PWD, this.EMAIL_MESSAGE_PWD),
                user.getCodverf());
    }

    public void changePassword(ChangePwdDTO changePwdDTO) {
        UsuarioEntity user = oUsuarioRepository.findByEmail(changePwdDTO.getEmail()).get();
        if (user.getPassword().equals(changePwdDTO.getOldPassword())) {
            user.setPassword(changePwdDTO.getNewPassword());
            oUsuarioRepository.save(user);
        } else {
            throw new ErrorChangingPassword("La contraseña actual no coincide");
        }
    }

    public void changePasswordWhithToken(ChangePwdDTO changePwdDTO) {
        String token = changePwdDTO.getToken();
        UsuarioEntity user = getUsuarioFromResetPwdToken(token);
        if (token.equals(user.getCodresetpwd())) {
            user.setPassword(changePwdDTO.getNewPassword());
            user.setCodresetpwd(null);
            oUsuarioRepository.save(user);
        } else {
            throw new ErrorChangingPassword("Toeken no coincide");

        }
    }

}
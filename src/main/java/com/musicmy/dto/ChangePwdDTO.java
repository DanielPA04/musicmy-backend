package com.musicmy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePwdDTO {
    private String email;
    private String oldPassword;
    private String newPassword;
    private String token;
}

package com.musicmy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {
    private String addressee;

    private String subject;

    private String message;

    private String code;

    public EmailDTO(String addressee, String subject, String message) {
        this.addressee = addressee;
        this.subject = subject;
        this.message = message;
    }
    
}

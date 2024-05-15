package com.registor.registro.newEmailRegister.domain;

import com.registor.registro.newEmailRegister.infa.MailManager;
import org.springframework.stereotype.Service;


@Service
public class AuthenticateEmail {

    MailManager mailManager; // dependecia de spring

    public AuthenticateEmail(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    public void sendMessageUser(String email, String message){
        mailManager.sendMessage(email,message);
    }
}

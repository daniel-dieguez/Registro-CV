package com.registor.registro.newEmailRegister.infa;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class MailManager {

    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") //value notation
    private String sender; // la variabe

    public MailManager(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    //creamos metodo para enviar

    public void sendMessage(String email, String messageEmail){

        MimeMessage message1 = javaMailSender.createMimeMessage();

        //Contruiremos que queremos enviar
        try{
            message1.setSubject("prueba de correo");
            MimeMessageHelper helper = new MimeMessageHelper(message1,true);
            //aquien le enviaremos el correo
            helper.setTo(email);
            helper.setText(messageEmail);
            helper.setFrom(sender); //traemos de donde estaremos enviando el corre

        }catch(MessagingException e){
            throw  new RuntimeException(e);

        }

    }


}

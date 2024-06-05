package com.registor.registro.newEmailRegister.infa;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class MailManager {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") // llamamos del spring.name de propietis
    private String fromMail; // la variabe



    //creamos metodo para enviar

    public void sendMessage(String email, String nombreUsuario, String comentarioUsuario){

        MimeMessage message1 = javaMailSender.createMimeMessage();


        try{
            MimeMessageHelper helper = new MimeMessageHelper(message1, true, "UTF-8");
            helper.setSubject("Prueba de Email");

            String content = "<p>Nombre del usuario: " + nombreUsuario + "</p>" +
                    "<p>Comentario del usuario: " + comentarioUsuario + "</p>";

            helper.setTo(email);
            helper.setText(content, true); // Indicamos que el contenido es HTML
            helper.setFrom(fromMail);

            javaMailSender.send(message1); // Enviamos el correo

        }catch(MessagingException e){
            throw  new RuntimeException(e);

        }

    }


}

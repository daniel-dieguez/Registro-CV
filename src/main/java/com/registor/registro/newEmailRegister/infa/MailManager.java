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
            helper.setSubject("Contacto Daniel Dieguez");

            String content =

                    "<!DOCTYPE html>" +
                            "<html lang=\"en\">" +
                            "<head>" +
                            "    <meta charset=\"UTF-8\">" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                            "    <title>Correo de Contacto</title>" +
                            "    <style>" +
                            "        .contenedor {" +
                            "            width: 80%;" +
                            "            margin: 0 auto;" +
                            "            background-color: #fff;" +
                            "            padding: 20px;" +
                            "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                            "            border-radius: 8px;" +
                            "            font-family: Arial, sans-serif;" +
                            "        }" +
                            "        .titulo {" +
                            "            color: #0056b3;" +
                            "        }" +
                            "        .primerParrafo, .segundoParrafo, .tercerParrafo {" +
                            "            line-height: 1.6;" +
                            "        }" +
                            "        .preguntas {" +
                            "            list-style-type: square;" +
                            "            padding-left: 20px;" +
                            "        }" +
                            "        .preguntas li {" +
                            "            margin-bottom: 10px;" +
                            "        }" +
                            "    </style>" +
                            "</head>" +
                            "<body>" +
                            "    <div class=\"contenedor\">" +
                            "        <h1 class=\"titulo\">Gracias por contactarme</h1>" +
                            "        <p class=\"primerParrafo\">En breves momentos estaré respondiendo su comentario por medio.</p>" +
                            "        <h3 class=\"titulo\">Me interesa saber más sobre su empresa</h3>" +
                            "        <p class=\"segundoParrafo\">Estas serían mis consultas sobre lo siguiente:</p>" +
                            "        <ul class=\"preguntas\">" +
                            "            <li>¿En qué parte de Guatemala se ubican?</li>" +
                            "            <li>¿Les interesa mi perfil para contratación?</li>" +
                            "            <li>¿Qué es lo que más destaca sobre su empresa?</li>" +
                            "            <li>¿Quisiera agendar una reunión virtual?</li>" +
                            "        </ul>" +
                            "        <h4>Desde ya, es un gusto tener este primer acercamiento con usted</h4>" +
                            "         <p class=\"tercerParrafo\">Su comentario: </p> " +
                            "        <p>" + comentarioUsuario + "</p>" +
                            "    </div>" +
                            "</body>" +
                            "</html>";


            helper.setTo(email);
            helper.setText(content, true); // Indicamos que el contenido es HTML
            helper.setFrom(fromMail);

            javaMailSender.send(message1); // Enviamos el correo

        }catch(MessagingException e){
            throw  new RuntimeException(e);

        }

    }


}

package com.registor.registro.controller;


import com.registor.registro.dao.services.IRegistroServicesImp;
import com.registor.registro.models.model.Registro;
import com.registor.registro.newEmailRegister.infa.MailManager;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/contacto")
@CrossOrigin(origins = "*") // para poder dar permiso y uitlizarlo
public class RegistroController {

    @Autowired
    private IRegistroServicesImp iRegistroServicesImp;

    private Logger logger = LoggerFactory.getLogger(Registro.class);

    // correo

    @Autowired
    MailManager mailManager;
    //AuthenticateEmail authenticateEmail;



    @GetMapping("/mensajes")
    public ResponseEntity<?>Listado(){
        Map<String, Object> response = new HashMap<>();
        this.logger.debug("Inicio de consulta");
        try{
            List<Registro> registros = this.iRegistroServicesImp.findAll();
            if(registros == null && registros.isEmpty()){
                logger.warn("No existe registro de peticion");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                logger.info("Se ejecuta la consulta");
                return new ResponseEntity<List<Registro>>(registros, HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e){
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }catch(DataAccessException e){
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<?> create(@Valid @RequestBody Registro value, BindingResult result){

        Map<String, Object> response = new HashMap<>();
        if(result.hasErrors() == true){
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList()); // getFielError me enlistara los campos que se encuentraron errores, El collector nos ayuda a enlistar lso errorres PREGUNTAR A CHATGPT
            response.put("errores", errores);
            logger.info("se encontraron errores al momento de validar");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }try{
            Registro registro = new Registro();
            registro.setId_persona(UUID.randomUUID().toString());
            registro.setNombre_usuario(value.getNombre_usuario());
            registro.setEmail_usuario(value.getEmail_usuario());
            registro.setComentario_usuario(value.getComentario_usuario());
            mailManager.sendMessage(registro.getEmail_usuario(), registro.getNombre_usuario(), registro.getComentario_usuario());
            this.iRegistroServicesImp.save(registro);
            logger.info("Se registrado una nueva persona");
            response.put("Mensaje", "Una nueva persona se registro con exito ");
            response.put("Registro", registro);
            logger.info("se envio un correo");
            response.put("mensaje","Se envio con exito el correo");

            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);

        }catch (CannotCreateTransactionException e){
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }catch(DataAccessException e){
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }

    }



    private Map<String, Object> getTransactionExepcion(Map<String,Object> response, CannotCreateTransactionException e){
        logger.error("Error al momento de conectarse a la base de datoss");
        response.put("mensajee", "error al moneotno de contectarse a la");
        response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
        return response;
    }

    private Map<String, Object> getDataAccessException(Map<String, Object> response, DataAccessException e){
        logger.error("El error al momento de ejecutlar la consulta ea  la base d adatos");
        response.put("mensaje", "Error al momenot de ejecutar ola consulta a la base de datos");
        response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
        return response;

    }
}

package com.registor.registro.controller;

import com.registor.registro.dao.services.IListadoNotasServicesImpl;
import com.registor.registro.models.entities.ListadoNotasDTO;
import com.registor.registro.models.model.ListadoNota;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping (value = "/lista/tareas")
@CrossOrigin(origins = "*") // para poder dar permiso y uitlizarlo
public class ListadoNotaController {


    @Autowired
    private IListadoNotasServicesImpl iListadoNotasServices;

    private Logger logger = LoggerFactory.getLogger(ListadoNota.class);



    @GetMapping
    public ResponseEntity<?>ListaNotas(){
        Map<String, Object> response = new HashMap<>();
        this.logger.debug("iniciando consulta");
        try{
            List<ListadoNota> listadoNotas =  this.iListadoNotasServices.findAll();
            if(listadoNotas == null && listadoNotas.isEmpty()){
                logger.warn("No existe registro de entidad");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                logger.info("se ejecuta la consulta sobre el listado de notas");
                return new ResponseEntity<List<ListadoNota>>(listadoNotas, HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e){
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }catch(DataAccessException e){
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }
    }


    @GetMapping("/page/{page}")
    public ResponseEntity<?> ListarMiembrosByPage(@PathVariable Integer page){
        Map<String, Object> response = new HashMap<>();
        try{
            Pageable pageable = PageRequest.of(page, 5);
            Page<ListadoNota> listadoNotasPage = iListadoNotasServices.findAll(pageable);
            if(listadoNotasPage == null || listadoNotasPage.getSize() == 0){
                logger.warn("no existe registro en la tabla de listado de notas");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                logger.info("se realizo la consulta para ver cuantas notas hay");
                return new ResponseEntity<Page<ListadoNota>>(listadoNotasPage, HttpStatus.OK);
            }

        }catch (CannotCreateTransactionException e){
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }catch(DataAccessException e){
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }
    }

    @GetMapping("{listadoId}")
    public  ResponseEntity<?> showlistado(@PathVariable String listadoId){ //VERIFIQUEMOS ESTE DATO
        Map<String, Object> response = new HashMap<>();
        logger.debug("inica el proceso para la busqueda del Id".concat(listadoId));
        try{
            ListadoNota listadoNota = this.iListadoNotasServices.findById(listadoId);
            if(listadoNota == null){
                logger.warn("no existe ese listado de notas Id");
                response.put("mesaje", "No existe el listado  con el id".concat(listadoId));
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                logger.info("se realizo consulta sobre la busqueda de Id");
                return new ResponseEntity<ListadoNota>(listadoNota, HttpStatus.OK);

            }

        }catch (CannotCreateTransactionException e){
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }catch(DataAccessException e){
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }
    }


    @PostMapping  //recuerda que el post es para agregar
    public ResponseEntity<?> create (@Valid @RequestBody ListadoNotasDTO value, BindingResult result){//el bindingresulta para valdiad el campo de Dto //el body lo podremos ver en la parte del post
        Map<String, Object> response = new HashMap<>();
        if(result.hasErrors() == true){ //.hasError para ver los errores
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList()); // getFielError me enlistara los campos que se encuentraron errores, El collector nos ayuda a enlistar lso errorres PREGUNTAR A CHATGPT
            response.put("errores", errores);
            logger.info("se encontraron errores al momento de validar");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            ListadoNota  listadoNota= new ListadoNota();
            listadoNota.setId_listado(UUID.randomUUID().toString());
            listadoNota.setNotas(value.getNotas());
            this.iListadoNotasServices.save(listadoNota);
            logger.info("se acaba de creaer un nueva nueva listado   ");
            response.put("mensaje", "Una nueva lista fue creado con exito ");
            response.put("listado", listadoNota);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
        }catch (CannotCreateTransactionException e){
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }catch (DataAccessException e){
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }
    }

    ///---------------------------------

    @PutMapping("/{listadoId}")
    public ResponseEntity<?>update(@Valid @RequestBody ListadoNotasDTO value, BindingResult result, @PathVariable String listadoId){
        Map<String, Object> response = new HashMap<>();
        if(result.hasErrors()){
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
            response.put("eorrres", errores);
            logger.info("Se encotraron errores en la peticion en la peticion de repetiaciones ");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            ListadoNota listadoNota = this.iListadoNotasServices.findById(listadoId);
            if(listadoNota == null){
                response.put("mensaje", "el nuevo listado con el id".concat(listadoId).concat("no existe"));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else {
                listadoNota.setNotas(value.getNotas());
                this.iListadoNotasServices.save(listadoNota);
                response.put("mensaje","la nueva nota fue actualizado");
                response.put("listado", listadoNota);
                logger.info("La tarea fue actualizada con exito ");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            }

        }catch (CannotCreateTransactionException e){
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }catch (DataAccessException e){
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }
    }



    @DeleteMapping("/{listadoId}")
    public ResponseEntity<?> delete(@PathVariable String listadoId){
        Map<String, Object> response = new HashMap<>();
        try {
            ListadoNota listadoNota = this.iListadoNotasServices.findById(listadoId);
            if(listadoNota == null){
                response.put("mensaje", "La nota con el Id".concat(listadoId).concat("no existe"));
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
            }else {
                this.iListadoNotasServices.detele(listadoNota);
                response.put("mensaje","La nota con el id".concat(listadoId).concat("fue eliminado "));
                response.put("listado", listadoNota);
                logger.info("la tarea fue eliminada con exito ");
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e){
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }catch (DataAccessException e){
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }
    }



    @GetMapping("/search")
    public ResponseEntity<?> ListarNotaPorTerminino(@RequestParam("termino") String termino){
        Map<String, Object> response = new HashMap<>();
        try{
            List<ListadoNota> listadoNota = this.iListadoNotasServices.findListaByTermino(termino);
            if(listadoNota == null && listadoNota.size() == 0){
                logger.warn("mensaje", "no existe nunguna concidencia");
                response.put("mensaje", "no existe nunguna concidencia");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            }else{
                logger.info("se ejecuto la consulta de manera exitosa");
                return new ResponseEntity<List<ListadoNota>>(listadoNota, HttpStatus.OK);

            }
        }catch (CannotCreateTransactionException e){
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }catch (DataAccessException e){
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }
    }

//------------------------------

    private Map<String, Object> getTransactionExepcion(Map<String,Object> response, CannotCreateTransactionException e){
        logger.error("Error al momento de conectarse a la base de datos");
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

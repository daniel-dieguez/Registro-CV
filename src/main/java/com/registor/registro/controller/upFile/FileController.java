package com.registor.registro.controller.upFile;

import com.registor.registro.dao.services.upFile.FileService;
import com.registor.registro.dao.services.upFile.IFileImple;
import com.registor.registro.models.model.upFile.FileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/file")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private IFileImple iFileImple;

    @Autowired
    private FileService fileService;

    private Logger logger = LoggerFactory.getLogger(FileEntity.class);


    @GetMapping("/view")
    public ResponseEntity<?> viewAll(){
    Map<String, Object> response = new HashMap<>();
    try{
        List<FileEntity> fileEntities = this.iFileImple.findAll();
        logger.info("Se a realizado consulta de datos");
        return new ResponseEntity<List<FileEntity>>(fileEntities, HttpStatus.OK);

    }catch (CannotCreateTransactionException e) {
        response = this.getTransactionExepcion(response, e);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
        response = this.getDataAccessException(response, e);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);

        }
    }

    @PostMapping("/Upfile")
    public ResponseEntity<?> createFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("nombre_completo") String nombreCompleto,
                                        @RequestParam("comentario_user") String comentarioUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            FileEntity fileEntity = new FileEntity();

            fileService.store(file, nombreCompleto, comentarioUser);
            logger.info("Se acaba de crear un nuevo archivo");
            response.put("mensaje", "Nuevo archivo creado con éxito");
            response.put("listado", fileEntity);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            response.put("mensaje", "Error al leer el archivo");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CannotCreateTransactionException e) {
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }


    @GetMapping("/view/{id}")
    public ResponseEntity<?> showIdFile(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        logger.debug("inica el proceso para la busqueda del Id".concat(id));
        try{

            FileEntity fileEntity = fileService.findById(id);
            logger.info("se realizo consulta sobre la busqueda de Id");
            return new ResponseEntity<FileEntity>(fileEntity, HttpStatus.OK);
        }catch (CannotCreateTransactionException e){
        response = this.getTransactionExepcion(response, e);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }catch(DataAccessException e){
        response = this.getDataAccessException(response, e);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @DeleteMapping("/deliteImages/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            FileEntity fileEntity = fileService.findById(id);
            this.fileService.detele(fileEntity);
            response.put("mensaje","La imagen del perfil con el id".concat(id).concat("fue eliminado "));
            response.put("listado", id);
            logger.info("Fue eliminada con exito");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);

        }catch (CannotCreateTransactionException e){
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }catch(DataAccessException e){
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }


    @PutMapping(value = "/UpFile/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateFile( @RequestParam("file") MultipartFile file,
                                         @RequestParam("nombre_completo") String nombreCompleto,
                                         @RequestParam("comentario_user") String comentarioUser,
                                         @PathVariable String id){

        Map<String, Object> response = new HashMap<>();
        try {
            FileEntity fileEntity = fileService.findById(id);
            fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
            fileEntity.setType(file.getContentType());
            fileEntity.setData(file.getBytes());
            fileEntity.setNombre_Completo(nombreCompleto);
            fileEntity.setComentario_user(comentarioUser);


            fileService.storeFile(fileEntity);

            response.put("mensaje", "El archivo ha sido actualizado con éxito!");
            response.put("file", fileEntity);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (CannotCreateTransactionException e) {
            response = this.getTransactionExepcion(response, e);
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            response = this.getDataAccessException(response, e);
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (IOException e) {
            response.put("mensaje", "Error al subir el archivo");
            response.put("error", e.getMessage().concat(": ").concat(e.getCause() != null ? e.getCause().getMessage() : ""));
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }



    private Map<String, Object> getTransactionExepcion(Map<String,Object> response, CannotCreateTransactionException e){
        logger.error("Error al momento de conectarse a la base de datos");
        response.put("mensajee", "error al momento de  de contectarse a la base de datos");
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

package com.registor.registro.dao;

import com.registor.registro.models.model.ListadoNota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IListaNotasDAO extends JpaRepository<ListadoNota, Object> {

    @Query("select c from ListadoNota c where  c.notas like %?1%")
    public List<ListadoNota> findListadoNotasByTermino(String termino);



}

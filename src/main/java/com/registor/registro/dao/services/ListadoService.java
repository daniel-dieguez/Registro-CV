package com.registor.registro.dao.services;

import com.registor.registro.dao.IListaNotasDAO;
import com.registor.registro.models.model.ListadoNota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListadoService implements IListadoNotasServicesImpl {

    @Autowired
    private IListaNotasDAO iListaNotasDAO;

    @Override
    public List<ListadoNota> findAll() {
        return this.iListaNotasDAO.findAll();
    }

    @Override
    public Page<ListadoNota> findAll(Pageable pageable) {
        return this.iListaNotasDAO.findAll(pageable);
    }

    @Override
    public ListadoNota findById(String id_listado) {
        return this.iListaNotasDAO.findById(id_listado).orElse(null);
    }

    @Override
    public ListadoNota save(ListadoNota listadoNota) {
        return this.iListaNotasDAO.save(listadoNota);
    }

    @Override
    public void detele(ListadoNota listadoNota) {
        this.iListaNotasDAO.delete(listadoNota);

    }

    @Override
    public List<ListadoNota> findListaByTermino(String termino) {
        return this.iListaNotasDAO.findListadoNotasByTermino(termino);
    }
}

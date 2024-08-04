package com.registor.registro.dao.services.upFile;

import com.registor.registro.models.model.upFile.FileEntity;

import java.util.List;

public interface IFileImple {


    public List<FileEntity> findAll();
    public FileEntity findById(String id);
    public FileEntity save(FileEntity file);
    public void detele (FileEntity fileEntity);

}

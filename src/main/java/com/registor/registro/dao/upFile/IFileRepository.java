package com.registor.registro.dao.upFile;

import com.registor.registro.models.model.upFile.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFileRepository extends JpaRepository<FileEntity, String> {




}

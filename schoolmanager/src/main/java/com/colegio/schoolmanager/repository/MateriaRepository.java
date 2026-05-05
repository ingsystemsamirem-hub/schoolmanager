package com.colegio.schoolmanager.repository;

import com.colegio.schoolmanager.model.Materia;
import com.colegio.schoolmanager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
    List<Materia> findByDocente(Usuario docente);
    boolean existsByCodigo(String codigo);
}
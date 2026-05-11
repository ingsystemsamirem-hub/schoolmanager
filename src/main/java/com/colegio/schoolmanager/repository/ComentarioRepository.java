package com.colegio.schoolmanager.repository;

import com.colegio.schoolmanager.model.Comentario;
import com.colegio.schoolmanager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByDocente(Usuario docente);
    List<Comentario> findByAlumno(Usuario alumno);
}
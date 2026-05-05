package com.colegio.schoolmanager.repository;

import com.colegio.schoolmanager.model.Alumno;
import com.colegio.schoolmanager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Optional<Alumno> findByUsuario(Usuario usuario);
}
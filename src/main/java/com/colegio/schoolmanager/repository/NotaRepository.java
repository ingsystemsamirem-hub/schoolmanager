package com.colegio.schoolmanager.repository;

import com.colegio.schoolmanager.model.Materia;
import com.colegio.schoolmanager.model.Nota;
import com.colegio.schoolmanager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByAlumno(Usuario alumno);
    List<Nota> findByMateria(Materia materia);
    Optional<Nota> findByAlumnoAndMateriaAndCorte(Usuario alumno, Materia materia, String corte);
}
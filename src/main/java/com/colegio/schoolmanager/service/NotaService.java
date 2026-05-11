package com.colegio.schoolmanager.service;

import com.colegio.schoolmanager.model.Materia;
import com.colegio.schoolmanager.model.Nota;
import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    public Nota guardar(Nota nota) {
        return notaRepository.save(nota);
    }

    public List<Nota> buscarPorAlumno(Usuario alumno) {
        return notaRepository.findByAlumno(alumno);
    }

    public List<Nota> buscarPorMateria(Materia materia) {
        return notaRepository.findByMateria(materia);
    }

    public Optional<Nota> buscarPorAlumnoMateriaYCorte(Usuario alumno, Materia materia, String corte) {
        return notaRepository.findByAlumnoAndMateriaAndCorte(alumno, materia, corte);
    }

    public void eliminar(Long id) {
        notaRepository.deleteById(id);
    }
}
package com.colegio.schoolmanager.service;

import com.colegio.schoolmanager.model.Comentario;
import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    public Comentario guardar(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    public List<Comentario> listarTodos() {
        return comentarioRepository.findAll();
    }

    public Optional<Comentario> buscarPorId(Long id) {
        return comentarioRepository.findById(id);
    }

    public List<Comentario> buscarPorDocente(Usuario docente) {
        return comentarioRepository.findByDocente(docente);
    }

    public List<Comentario> buscarPorAlumno(Usuario alumno) {
        return comentarioRepository.findByAlumno(alumno);
    }

    public void eliminar(Long id) {
        comentarioRepository.deleteById(id);
    }
}
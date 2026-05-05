package com.colegio.schoolmanager.service;

import com.colegio.schoolmanager.model.Alumno;
import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    public Alumno guardar(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }

    public List<Alumno> listarTodos() {
        return alumnoRepository.findAll();
    }

    public Optional<Alumno> buscarPorId(Long id) {
        return alumnoRepository.findById(id);
    }

    public Optional<Alumno> buscarPorUsuario(Usuario usuario) {
        return alumnoRepository.findByUsuario(usuario);
    }

    public void eliminar(Long id) {
        alumnoRepository.deleteById(id);
    }
}
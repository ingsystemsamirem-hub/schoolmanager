package com.colegio.schoolmanager.service;

import com.colegio.schoolmanager.model.Materia;
import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    public Materia guardar(Materia materia) {
        return materiaRepository.save(materia);
    }

    public List<Materia> listarTodos() {
        return materiaRepository.findAll();
    }

    public List<Materia> buscarPorDocente(Usuario docente) {
        return materiaRepository.findByDocente(docente);
    }

    public Optional<Materia> buscarPorId(Long id) {
        return materiaRepository.findById(id);
    }

    public boolean existeCodigo(String codigo) {
        return materiaRepository.existsByCodigo(codigo);
    }

    public void eliminar(Long id) {
        materiaRepository.deleteById(id);
    }
}
package com.colegio.schoolmanager.repository;

import com.colegio.schoolmanager.model.Bitacora;
import com.colegio.schoolmanager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {
    List<Bitacora> findByUsuarioOrderByFechaDesc(Usuario usuario);
    List<Bitacora> findTop10ByUsuarioOrderByFechaDesc(Usuario usuario);
}
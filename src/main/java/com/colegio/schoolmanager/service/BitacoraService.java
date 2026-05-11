package com.colegio.schoolmanager.service;

import com.colegio.schoolmanager.model.Bitacora;
import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.repository.BitacoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BitacoraService {

    @Autowired
    private BitacoraRepository bitacoraRepository;

    public void registrarAccion(Usuario usuario, String accion, String detalles) {
        Bitacora registro = new Bitacora();
        registro.setUsuario(usuario);
        registro.setAccion(accion);
        registro.setDetalles(detalles);
        bitacoraRepository.save(registro);
    }

    public List<Bitacora> obtenerUltimasAcciones(Usuario usuario, int limite) {
        List<Bitacora> acciones = bitacoraRepository.findTop10ByUsuarioOrderByFechaDesc(usuario);
        return acciones != null ? acciones : List.of();
    }

    public List<Bitacora> obtenerTodasLasAcciones(Usuario usuario) {
        return bitacoraRepository.findByUsuarioOrderByFechaDesc(usuario);
    }
}
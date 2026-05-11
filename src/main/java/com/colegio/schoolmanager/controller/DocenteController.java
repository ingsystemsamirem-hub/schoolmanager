package com.colegio.schoolmanager.controller;

import com.colegio.schoolmanager.model.Bitacora;
import com.colegio.schoolmanager.model.Comentario;
import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.service.BitacoraService;
import com.colegio.schoolmanager.service.ComentarioService;
import com.colegio.schoolmanager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BitacoraService bitacoraService;

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping("/inicio")
    public String inicio(Authentication auth, Model model) {
        String email = auth.getName();
        Usuario docente = usuarioService.buscarPorEmail(email).orElse(null);

        List<Bitacora> ultimasAcciones = bitacoraService.obtenerUltimasAcciones(docente, 10);
        model.addAttribute("ultimasAcciones", ultimasAcciones);

        return "docente/inicio";
    }
}
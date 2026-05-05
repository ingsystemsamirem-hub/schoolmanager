package com.colegio.schoolmanager.controller;

import com.colegio.schoolmanager.model.Comentario;
import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.service.ComentarioService;
import com.colegio.schoolmanager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/docente/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarComentarios(Authentication auth, Model model) {
        String email = auth.getName();
        Usuario docente = usuarioService.buscarPorEmail(email).orElse(null);

        List<Comentario> comentarios = comentarioService.buscarPorDocente(docente);
        model.addAttribute("comentarios", comentarios);

        return "docente/comentarios";
    }

    @PostMapping("/responder/{id}")
    public String responderComentario(@PathVariable Long id,
                                      @RequestParam String respuesta,
                                      Model model) {
        Comentario comentario = comentarioService.buscarPorId(id).orElse(null);

        if (comentario != null) {
            comentario.setRespuesta(respuesta);
            comentario.setFechaRespuesta(LocalDateTime.now());
            comentarioService.guardar(comentario);
            model.addAttribute("exito", "Respuesta enviada correctamente");
        } else {
            model.addAttribute("error", "Comentario no encontrado");
        }

        return "redirect:/docente/comentarios";
    }
}
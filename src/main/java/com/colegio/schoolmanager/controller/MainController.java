package com.colegio.schoolmanager.controller;

import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "index";
    }

    @PostMapping("/registro")
    public String registrar(Usuario usuario, Model model) {
        if (usuarioService.existeEmail(usuario.getEmail())) {
            model.addAttribute("error", "El correo ya está registrado");
            model.addAttribute("usuario", new Usuario());
            return "index";
        }
        usuarioService.registrar(usuario);
        model.addAttribute("exito", "Registro exitoso. Inicia sesión");
        model.addAttribute("usuario", new Usuario());
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        String rol = auth.getAuthorities().iterator().next().getAuthority();
        if (rol.equals("ROLE_DOCENTE")) {
            return "redirect:/docente/inicio";
        }
        return "redirect:/alumno/inicio";
    }

    @GetMapping("/test-login")
    @ResponseBody
    public String testLogin(@RequestParam String email, @RequestParam String password) {
        Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);
        if (usuario == null) {
            return "❌ Usuario no existe: " + email;
        }
        boolean coincide = passwordEncoder.matches(password, usuario.getPassword());
        return "🔍 Email: " + email + " | ¿Coincide? " + coincide;
    }

    @GetMapping("/generar-hash")
    @ResponseBody
    public String generarHash(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }
}
package com.colegio.schoolmanager.controller;

import com.colegio.schoolmanager.model.Materia;
import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.service.BitacoraService;
import com.colegio.schoolmanager.service.MateriaService;
import com.colegio.schoolmanager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/docente/materias")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BitacoraService bitacoraService;

    @GetMapping
    public String listarMaterias(Authentication auth, Model model) {
        String email = auth.getName();
        Usuario docente = usuarioService.buscarPorEmail(email).orElse(null);
        List<Materia> materias = materiaService.buscarPorDocente(docente);
        model.addAttribute("materias", materias);
        return "docente/lista_materias";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("materia", new Materia());
        return "docente/nueva_materia";
    }

    @PostMapping("/guardar")
    public String guardarMateria(@ModelAttribute Materia materia, Authentication auth, Model model) {
        if (materiaService.existeCodigo(materia.getCodigo())) {
            model.addAttribute("error", "El código ya existe");
            model.addAttribute("materia", materia);
            return "docente/nueva_materia";
        }
        String email = auth.getName();
        Usuario docente = usuarioService.buscarPorEmail(email).orElse(null);
        materia.setDocente(docente);
        materiaService.guardar(materia);

        bitacoraService.registrarAccion(docente, "CREAR MATERIA", "Materia: " + materia.getNombre() + " (" + materia.getCodigo() + ")");

        return "redirect:/docente/materias";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Materia materia = materiaService.buscarPorId(id).orElse(null);
        model.addAttribute("materia", materia);
        return "docente/editar_materia";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarMateria(@PathVariable Long id, @ModelAttribute Materia materiaActualizada, Authentication auth) {
        Materia materiaExistente = materiaService.buscarPorId(id).orElse(null);
        if (materiaExistente != null) {
            String nombreAntiguo = materiaExistente.getNombre();
            materiaExistente.setNombre(materiaActualizada.getNombre());
            materiaExistente.setCodigo(materiaActualizada.getCodigo());
            materiaService.guardar(materiaExistente);

            String email = auth.getName();
            Usuario docente = usuarioService.buscarPorEmail(email).orElse(null);
            bitacoraService.registrarAccion(docente, "EDITAR MATERIA", "Materia: " + nombreAntiguo + " → " + materiaActualizada.getNombre());
        }
        return "redirect:/docente/materias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMateria(@PathVariable Long id, Authentication auth) {
        Materia materia = materiaService.buscarPorId(id).orElse(null);
        if (materia != null) {
            String email = auth.getName();
            Usuario docente = usuarioService.buscarPorEmail(email).orElse(null);
            bitacoraService.registrarAccion(docente, "ELIMINAR MATERIA", "Materia: " + materia.getNombre() + " (" + materia.getCodigo() + ")");
        }
        materiaService.eliminar(id);
        return "redirect:/docente/materias";
    }
}
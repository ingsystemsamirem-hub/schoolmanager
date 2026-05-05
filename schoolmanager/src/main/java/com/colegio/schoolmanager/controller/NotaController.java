package com.colegio.schoolmanager.controller;

import com.colegio.schoolmanager.model.Materia;
import com.colegio.schoolmanager.model.Nota;
import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.service.BitacoraService;
import com.colegio.schoolmanager.service.MateriaService;
import com.colegio.schoolmanager.service.NotaService;
import com.colegio.schoolmanager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/docente/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BitacoraService bitacoraService;

    @GetMapping
    public String mostrarPanelNotas(Authentication auth, Model model) {
        String email = auth.getName();
        Usuario docente = usuarioService.buscarPorEmail(email).orElse(null);

        List<Materia> materias = materiaService.buscarPorDocente(docente);
        List<Usuario> alumnos = usuarioService.listarTodosAlumnos();

        model.addAttribute("materias", materias);
        model.addAttribute("alumnos", alumnos);
        model.addAttribute("nota", new Nota());
        model.addAttribute("cortes", new String[]{"Primer Corte", "Segundo Corte", "Tercer Corte", "Final"});

        return "docente/registro_notas";
    }

    @PostMapping("/guardar")
    public String guardarNota(@RequestParam Long alumnoId,
                              @RequestParam Long materiaId,
                              @RequestParam Double notaValor,
                              @RequestParam String corte,
                              Authentication auth,
                              Model model) {

        Usuario alumno = usuarioService.buscarPorId(alumnoId).orElse(null);
        Materia materia = materiaService.buscarPorId(materiaId).orElse(null);

        if (alumno == null || materia == null) {
            model.addAttribute("error", "Alumno o materia no encontrados");
            return "redirect:/docente/notas";
        }

        Nota notaExistente = notaService.buscarPorAlumnoMateriaYCorte(alumno, materia, corte).orElse(null);

        if (notaExistente != null) {
            notaExistente.setNota(notaValor);
            notaService.guardar(notaExistente);
        } else {
            Nota nuevaNota = new Nota();
            nuevaNota.setAlumno(alumno);
            nuevaNota.setMateria(materia);
            nuevaNota.setNota(notaValor);
            nuevaNota.setCorte(corte);
            notaService.guardar(nuevaNota);
        }

        String email = auth.getName();
        Usuario docente = usuarioService.buscarPorEmail(email).orElse(null);
        bitacoraService.registrarAccion(docente, "REGISTRAR NOTA",
                "Alumno: " + alumno.getNombre() + " | Materia: " + materia.getNombre() + " | Corte: " + corte + " | Nota: " + notaValor);

        return "redirect:/docente/notas?exito";
    }

    @GetMapping("/ver/{alumnoId}")
    @ResponseBody
    public List<Nota> verNotasPorAlumno(@PathVariable Long alumnoId) {
        Usuario alumno = usuarioService.buscarPorId(alumnoId).orElse(null);
        if (alumno != null) {
            return notaService.buscarPorAlumno(alumno);
        }
        return List.of();
    }
}
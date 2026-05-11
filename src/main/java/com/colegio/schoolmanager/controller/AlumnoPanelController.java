package com.colegio.schoolmanager.controller;

import com.colegio.schoolmanager.model.Comentario;
import com.colegio.schoolmanager.model.Materia;
import com.colegio.schoolmanager.model.Nota;
import com.colegio.schoolmanager.model.Usuario;
import com.colegio.schoolmanager.service.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alumno")
public class AlumnoPanelController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private NotaService notaService;

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping("/inicio")
    public String inicio(Authentication auth, Model model) {
        String email = auth.getName();
        Usuario alumno = usuarioService.buscarPorEmail(email).orElse(null);

        if (alumno == null) {
            return "redirect:/logout";
        }

        List<Nota> notas = notaService.buscarPorAlumno(alumno);

        Map<String, Map<String, Double>> notasPorMateria = new HashMap<>();
        for (Nota nota : notas) {
            String materiaNombre = nota.getMateria().getNombre();
            notasPorMateria.putIfAbsent(materiaNombre, new HashMap<>());
            notasPorMateria.get(materiaNombre).put(nota.getCorte(), nota.getNota());
        }

        Map<String, Double> promedios = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : notasPorMateria.entrySet()) {
            double suma = 0;
            int count = 0;
            for (Double nota : entry.getValue().values()) {
                suma += nota;
                count++;
            }
            promedios.put(entry.getKey(), count > 0 ? suma / count : 0);
        }

        List<Comentario> comentarios = comentarioService.buscarPorAlumno(alumno);

        model.addAttribute("alumno", alumno);
        model.addAttribute("notasPorMateria", notasPorMateria);
        model.addAttribute("promedios", promedios);
        model.addAttribute("materias", materiaService.listarTodos());
        model.addAttribute("comentarios", comentarios);

        return "alumno/inicio";
    }

    @PostMapping("/comentario")
    public String dejarComentario(Authentication auth,
                                  @RequestParam Long materiaId,
                                  @RequestParam String mensaje,
                                  Model model) {
        String email = auth.getName();
        Usuario alumno = usuarioService.buscarPorEmail(email).orElse(null);
        Materia materia = materiaService.buscarPorId(materiaId).orElse(null);

        if (alumno == null || materia == null) {
            model.addAttribute("error", "Error al enviar comentario");
            return "redirect:/alumno/inicio";
        }

        Comentario comentario = new Comentario();
        comentario.setAlumno(alumno);
        comentario.setDocente(materia.getDocente());
        comentario.setMateria(materia);
        comentario.setMensaje(mensaje);

        comentarioService.guardar(comentario);

        return "redirect:/alumno/inicio?comentarioEnviado";
    }

    @Autowired private PdfService pdfService;
    @GetMapping("/reporte-pdf")
    public void descargarReportePDF(Authentication auth, HttpServletResponse response) {
        String email = auth.getName();
        Usuario alumno = usuarioService.buscarPorEmail(email).orElse(null);

        if (alumno == null) return;

        List<Nota> notas = notaService.buscarPorAlumno(alumno);

        Map<String, Map<String, Double>> notasPorMateria = new HashMap<>();
        for (Nota nota : notas) {
            String materiaNombre = nota.getMateria().getNombre();
            notasPorMateria.putIfAbsent(materiaNombre, new HashMap<>());
            notasPorMateria.get(materiaNombre).put(nota.getCorte(), nota.getNota());
        }

        Map<String, Double> promedios = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : notasPorMateria.entrySet()) {
            double suma = 0;
            int count = 0;
            for (Double nota : entry.getValue().values()) {
                suma += nota;
                count++;
            }
            promedios.put(entry.getKey(), count > 0 ? suma / count : 0);
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_notas_" + alumno.getNombre() + ".pdf");

        pdfService.generarReporteNotas(alumno, notasPorMateria, promedios, response);
    }
}
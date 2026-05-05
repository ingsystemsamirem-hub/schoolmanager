package com.colegio.schoolmanager.service;

import com.colegio.schoolmanager.model.Nota;
import com.colegio.schoolmanager.model.Usuario;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PdfService {

    public void generarReporteNotas(Usuario alumno, Map<String, Map<String, Double>> notasPorMateria,
                                    Map<String, Double> promedios, HttpServletResponse response) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Reporte de Notas - " + alumno.getNombre(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Información del alumno
            document.add(new Paragraph("Correo: " + alumno.getEmail()));
            document.add(new Paragraph("Fecha: " + new java.util.Date()));
            document.add(Chunk.NEWLINE);

            // Tabla de notas
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            // Encabezados
            String[] headers = {"Materia", "Primer Corte", "Segundo Corte", "Tercer Corte", "Final"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Datos
            for (Map.Entry<String, Map<String, Double>> entry : notasPorMateria.entrySet()) {
                table.addCell(entry.getKey());
                table.addCell(entry.getValue().getOrDefault("Primer Corte", -1.0) == -1 ? "-" : String.valueOf(entry.getValue().get("Primer Corte")));
                table.addCell(entry.getValue().getOrDefault("Segundo Corte", -1.0) == -1 ? "-" : String.valueOf(entry.getValue().get("Segundo Corte")));
                table.addCell(entry.getValue().getOrDefault("Tercer Corte", -1.0) == -1 ? "-" : String.valueOf(entry.getValue().get("Tercer Corte")));
                table.addCell(entry.getValue().getOrDefault("Final", -1.0) == -1 ? "-" : String.valueOf(entry.getValue().get("Final")));
            }

            document.add(table);

            // Promedios
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Promedios por Materia:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            for (Map.Entry<String, Double> entry : promedios.entrySet()) {
                document.add(new Paragraph(entry.getKey() + ": " + String.format("%.2f", entry.getValue())));
            }

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
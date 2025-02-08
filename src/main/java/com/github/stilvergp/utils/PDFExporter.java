package com.github.stilvergp.utils;

import com.github.stilvergp.model.entities.Footprint;
import com.github.stilvergp.model.entities.Habit;
import com.github.stilvergp.model.entities.Recommendation;
import com.github.stilvergp.services.ActivityService;
import com.github.stilvergp.services.RecommendationService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class PDFExporter {
    public static void exportFootprintsToPDF(File pdfFile, List<Footprint> footprints) {
        try (PdfWriter writer = new PdfWriter(pdfFile)) {
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Table table = new Table(5);

            table.addHeaderCell("Actividad realizada");
            table.addHeaderCell("Cantidad de consumo");
            table.addHeaderCell("Unidad de consumo");
            table.addHeaderCell("Fecha de realización");
            table.addHeaderCell("Recomendación");

            for (Footprint footprint : footprints) {
                table.addCell(new ActivityService().getActivityByFootprint(footprint).getName());
                table.addCell(String.valueOf(footprint.getValue()));
                table.addCell(footprint.getUnit());
                LocalDateTime dateTime = footprint.getDate().atZone(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                table.addCell(dateTime.format(formatter));
                List<Recommendation> recommendations = new RecommendationService().getRecommendationsByFootprint(footprint);
                String randomRecommendationText = recommendations.get(new Random().nextInt(recommendations.size())).getDescription();
                table.addCell(randomRecommendationText);
            }
            document.add(table);
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exportHabitsToPDF(File pdfFile, List<Habit> habits) {
        try (PdfWriter writer = new PdfWriter(pdfFile)) {
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Table table = new Table(5);

            table.addHeaderCell("Actividad realizada");
            table.addHeaderCell("Frecuencia de realización");
            table.addHeaderCell("Intervalo de realización");
            table.addHeaderCell("Última fecha de realización");
            table.addHeaderCell("Recomendación");

            for (Habit habit : habits) {
                table.addCell(new ActivityService().getActivityByHabit(habit).getName());
                table.addCell(String.valueOf(habit.getFrequency()));
                table.addCell(habit.getType());
                LocalDateTime dateTime = habit.getLastDate().atZone(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                table.addCell(dateTime.format(formatter));
                List<Recommendation> recommendations = new RecommendationService().getRecommendationsByHabit(habit);
                String randomRecommendationText = recommendations.get(new Random().nextInt(recommendations.size())).getDescription();
                table.addCell(randomRecommendationText);
            }
            document.add(table);
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

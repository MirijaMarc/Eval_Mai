package com.evaluation.Eval.util;

import java.util.List;
import java.text.SimpleDateFormat;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.lang.reflect.Field;
import java.io.Writer;

import com.itextpdf.io.IOException;

public class ExportCsv {
    
    public ExportCsv()
    {

    }

    public <T> void writeToCsv(List<T> objects, Writer writer) {
        CSVPrinter printer = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            if (!objects.isEmpty()) {
                T firstObject = objects.get(0);
                Class<?> clazz = firstObject.getClass();
                Field[] fields = clazz.getDeclaredFields();
                String[] headers = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    headers[i] = fields[i].getName();
                }
                printer.printRecord((Object[]) headers);
                for (T object : objects) {
                    String[] record = new String[fields.length];
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        Object value = fields[i].get(object);
                        if (value != null) {
                            if (value instanceof java.sql.Date) {
                                record[i] = dateFormat.format((java.sql.Date) value).toString();
                            } else {
                                record[i] = value.toString();
                            }
                        } else {
                            record[i] = "";
                        }
                    }
                    printer.printRecord((Object[]) record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printer != null) {
                    printer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // public void writeToCsv(List<Seance> listSeance, Writer writer)
    // {
    //     CSVPrinter printer = null;
    //     try {
    //         printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
    //         printer.printRecord("id", "numSeance", "idFilm", "idCategorie", "idSalle", "dateJour", "heure");
    //         for (Seance seance : listSeance) {
    //         printer.printRecord(
    //             seance.getId(),
    //             seance.getNumSeance(),
    //             seance.getIdFilm(),
    //             seance.getIdCategorie(),
    //             seance.getIdSalle(),
    //             seance.getDateJour(),
    //             seance.getHeure());
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     } finally {
    //         try {
    //             printer.close();
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }
}

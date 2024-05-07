package com.evaluation.Eval.util;

import java.util.List;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ExportExcel<T> {
    
    // private List<Seance> listObjects;
    private List<T> listObjects;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    // public ExportExcel(List<Seance> listObjects)
    // {
    //     this.listObjects = listObjects;
    //     workbook = new XSSFWorkbook();
    // }

    public ExportExcel(List<T> listObjects) {
        this.listObjects = listObjects;
        workbook = new XSSFWorkbook();
    }

    // private void writeHeader() {
    //     sheet = workbook.createSheet("Seance");
    //     Row row = sheet.createRow(0);
    //     CellStyle style = workbook.createCellStyle();
    //     XSSFFont font = workbook.createFont();
    //     font.setBold(true);
    //     font.setFontHeight(16);
    //     style.setFont(font);
    //     createCell(row, 0, "ID", style);
    //     createCell(row, 1, "Numero séance", style);
    //     createCell(row, 2, "Id film", style);
    //     createCell(row, 3, "Id catégorie", style);
    //     createCell(row, 4, "Id salle", style);
    //     createCell(row, 5, "Date jour", style);
    //     createCell(row, 6, "Heure", style);
    // }

    private void writeHeader() {
        sheet = workbook.createSheet("Data");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        // Utilisation de la réflexion pour obtenir les noms des champs
        Field[] fields = listObjects.get(0).getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            createCell(row, i, fields[i].getName(), style);
        }
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof Double) {
            cell.setCellValue((Double) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else if (valueOfCell instanceof java.sql.Date) {
            java.sql.Date sqlDate = (java.sql.Date) valueOfCell;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Définir le format de date souhaité
            cell.setCellValue(sdf.format(sqlDate)); // Appliquer le format à la date
        } else if (valueOfCell instanceof java.sql.Time) {
            cell.setCellValue(valueOfCell.toString()); // Convert to string
        }else if (valueOfCell instanceof LocalTime){
            LocalTime time = (LocalTime) valueOfCell;
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("HH:mm:ss");
            cell.setCellValue(sdf.format(time));
        }else if (valueOfCell instanceof LocalDate){
            LocalDate date = (LocalDate) valueOfCell;
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            cell.setCellValue(sdf.format(date));
        }else if (valueOfCell instanceof LocalDateTime){
            LocalDateTime dateTime = (LocalDateTime) valueOfCell;
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            cell.setCellValue(sdf.format(dateTime));
        }else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    // private void write() {
    //     int rowCount = 1;
    //     CellStyle style = workbook.createCellStyle();
    //     XSSFFont font = workbook.createFont();
    //     font.setFontHeight(14);
    //     style.setFont(font);
    //     for (Seance seance: listObjects) {
    //         Row row = sheet.createRow(rowCount++);
    //         int columnCount = 0;
    //         createCell(row, columnCount++, seance.getId(), style);
    //         createCell(row, columnCount++, seance.getNumSeance(), style);
    //         createCell(row, columnCount++, seance.getIdFilm(), style);
    //         createCell(row, columnCount++, seance.getIdCategorie(), style);
    //         createCell(row, columnCount++, seance.getIdSalle(), style);
    //         createCell(row, columnCount++, seance.getDateJour(), style);
    //         createCell(row, columnCount++, seance.getHeure(), style);
    //     }
    // }

    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (T object : listObjects) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    createCell(row, columnCount++, value, style);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void generateExcelFile(HttpServletResponse response) throws Exception {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}

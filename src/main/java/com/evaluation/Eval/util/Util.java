package com.evaluation.Eval.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;

import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.servlet.http.HttpServletRequest;


public class Util {


    private static final List<String> DATE_FORMATS = Arrays.asList(
        "d/M/yyyy", "dd/MM/yyyy", "M/d/yyyy", "MM/dd/yyyy", "yyyy-MM-dd", "dd-MM-yyyy"
    );

     public static boolean isValidDate(String dateStr) {
        for (String format : DATE_FORMATS) {
            if (isValidFormat(dateStr, format)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidFormat(String dateStr, String format) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        try {
            LocalDate.parse(dateStr, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
  
    public static boolean hasError(ArrayList<Erreur> errors){
        for (Erreur erreur : errors) {
            if (erreur.getErreur() != "") return true;
        }
        return false;
    }

    public static String createRequeteMultiMot(Class classe, String mot){
        StringBuilder builder = new StringBuilder();
        String[] mots = mot.split("\\s+"); 
        Table table = (Table) classe.getAnnotation(Table.class);
        String nomTable = table.name();
        String select = "SELECT * FROM "+ nomTable + " WHERE ";
        builder.append(select);
        Column column= null;
        int j= 0;
        for (Field field : classe.getDeclaredFields()) {
            String where = "";
            if (field.getName() != "etat"){
                column = field.getAnnotation(Column.class);
                String nomColonne = column.name();
                String like = " CAST(col as VARCHAR) ILIKE '%mot%' ".replace("col", nomColonne);
                for (int i = 0; i<mots.length; i++) {
                    where += like.replace("mot", mots[i]);
                    if (i < mots.length-1){
                        where += "AND";
                    }
                }
                if (j < classe.getDeclaredFields().length -2){
                    where+= "OR";
                }
            }
            j++;
            builder.append(where);
        }
        builder.append(" UNION ");
        for (int i = 0; i<mots.length; i++) {
            select = "SELECT * from " + nomTable + " WHERE ";
            builder.append(select);
            j= 0;
            for (Field field : classe.getDeclaredFields()) {
                String where = "";
                if (field.getName() != "etat"){
                    column = field.getAnnotation(Column.class);
                    String nomColonne = column.name();
                    String like = " CAST(col as VARCHAR) ILIKE '%mot%' ".replace("col", nomColonne).replace("mot", mots[i]);
                    // System.out.println(like);
                    where+=like;
                    if (j < classe.getDeclaredFields().length -2){
                        where+= "OR";
                    }
                }
                j++;
                builder.append(where);
            }
            if (i < mots.length -1){
                builder.append(" UNION ");
            }

        }
        return builder.toString();
    }
    
    public static String uploadFile(MultipartFile file)throws Exception{
        String newFileName = UUID.randomUUID().toString() +"-"+ file.getOriginalFilename();
        file.transferTo(new File(Constante.UPLOAD_PATH, newFileName));
        return newFileName;

    }

    public static boolean isLoged(HttpServletRequest request){
        return request.getSession().getAttribute("user") != null;
    }


    public static void importer (EntityManager entityManager, String colonnes, String path){
        String sql = "COPY pilotes "+ colonnes +" FROM '"+ path +"' DELIMITER ',' CSV HEADER";
        Query query = entityManager.createNativeQuery(sql);
        try {
            query.getSingleResult();
        } catch (Exception e) {
        }
    }


    public static <T> ArrayList<T> getData(MultipartFile file ,Class<T> classe)throws Exception{
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        T objet = classe.getDeclaredConstructor().newInstance();
        CSVReader reader = new CSVReader(new FileReader(convFile));
        String[] lignes;
        ArrayList<T> listes = new ArrayList<>();
        Field[] fields = objet.getClass().getDeclaredFields();
        Class<?> type;
        Method methode;
        while ((lignes = reader.readNext()) != null) {
            objet = classe.getDeclaredConstructor().newInstance();
            for (int i = 0; i < lignes.length ; i++) {
                type = fields[i+1].getType();
                methode = objet.getClass().getDeclaredMethod("set"+ toCapitalize(fields[i+1].getName()), type);
                methode.invoke(objet,convertToObejct(lignes[i], type));
            }
            

            listes.add(objet);
        }
        reader.close();
        convFile.delete();
        return listes;
       
    }


    public static <T> ArrayList<T> getData(String csv ,Class<T> classe)throws Exception{
        T objet = classe.getDeclaredConstructor().newInstance();
        CSVReader reader = new CSVReader(new FileReader(csv));
        String[] lignes;
        ArrayList<T> listes = new ArrayList<>();
        Field[] fields = objet.getClass().getDeclaredFields();
        Class<?> type;
        Method methode;
        while ((lignes = reader.readNext()) != null) {
            objet = classe.getDeclaredConstructor().newInstance();
            for (int i = 0; i < lignes.length ; i++) {
                type = fields[i+1].getType();
                methode = objet.getClass().getDeclaredMethod("set"+ toCapitalize(fields[i+1].getName()), type);
                methode.invoke(objet,convertToObejct(lignes[i], type));
            }
            

            listes.add(objet);
        }
        reader.close();
        return listes;
       
    }
    

    public static LocalDate convert (String dateString)throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Instant instant = dateFormat.parse(dateString).toInstant();
        return LocalDate.ofInstant(instant, ZoneId.systemDefault());
    }


    public static ArrayList<Integer> getAnnees(){
        LocalDate d = LocalDate.now();
        int annee = d.getYear();
        ArrayList<Integer> rep = new ArrayList<Integer>();
        for(int i = annee ;  i>=2000 ; i--){
            rep.add(i);
        }
        return rep;
    }


    public static String toCapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    
    @SuppressWarnings("unchecked")
    public static <T> T convertToObejct(String obj, Class<T> classe){
        if (classe.equals(Integer.class)) {
            return (T) Integer.valueOf(obj);
        }else if (classe.equals(Double.class)){
            return (T) Double.valueOf(obj);
        }else if (classe.equals(Float.class)){
            return (T) Float.valueOf(obj);
        }else if (classe.equals(LocalDate.class)){
            return (T) LocalDate.parse(obj, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }else if (classe.equals(LocalDateTime.class)){
            return (T) LocalDateTime.parse(obj, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        }else if (classe.equals(Date.class)){
            return (T) LocalDate.parse(obj, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return (T) obj;
    }



    
}

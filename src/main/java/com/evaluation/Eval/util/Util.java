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
import java.util.ArrayList;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;


import com.opencsv.CSVReader;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;


public class Util {

    public static String CSV_PATH = "E:/Mirija/ITU/S6/evaluation/Eval/csv";




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

package com.evaluation.Eval.util;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.evaluation.Eval.repository.CsvRepository;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;



@Entity
@Data
public class Csv {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String num_seance;
    private String film;
    private String categorie;
    private String salle;
    private String date_jour;
    private String heure;

    



    public Csv() {
    }






    @Transactional
    public static ArrayList<Erreur> importer( CsvRepository csvRepository,String fileName){
        boolean hasError = false;
        ArrayList<Erreur> err = new ArrayList<Erreur>();
        String filePath = Constante.CSV_PATH + "/" + fileName;
        ArrayList<Csv> tab = new ArrayList<Csv>();
        CSVParser parser = new CSVParserBuilder()
        .withSeparator(',')
        .build();
        Field[] fields = Csv.class.getDeclaredFields();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(parser).build()) {
            reader.readNext();
            List<String[]> lignes = reader.readAll();
            for (int i= 0; i < lignes.size(); i++){
                Csv csv = new Csv();
                Erreur error = new Erreur(i+1);
                for (int j = 1; j< fields.length; j++){
                    String setterName = "set".concat(Util.toCapitalize(fields[j].getName()));
                    try {
                        Method setter = csv.getClass().getMethod(setterName, String.class);
                        setter.invoke(csv, lignes.get(i)[j-1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Miditra");
                        System.out.println(error.getErreur());
                        String virgule= ",";
                        if (error.getErreur() == "") virgule = "";
                        String message = (e.getCause() == null) ? e.getMessage() : e.getCause().getMessage() ;
                        
                        error.setErreur(error.getErreur() + virgule+ message );
                        hasError = true;
                    }
                }
                err.add(error);
                tab.add(csv);
            }
            System.err.println(hasError);
            if (!hasError){
                try {
                   csvRepository.saveAll(tab);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return err;

    }


    @Transactional
    public static ArrayList<Erreur> importerWithError( CsvRepository csvRepository,String fileName){

        ArrayList<Erreur> err = new ArrayList<Erreur>();
        String filePath = Constante.CSV_PATH + "/" + fileName;
        ArrayList<Csv> tab = new ArrayList<Csv>();

        Field[] fields = Csv.class.getDeclaredFields();
        CSVParser parser = new CSVParserBuilder()
        .withSeparator(',')
        .build();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(parser).build()) {
            reader.readNext();
            List<String[]> lignes = reader.readAll();
            for (int i= 0; i < lignes.size(); i++){
                Csv csv = new Csv();
                Erreur error = new Erreur(i+1);
                for (int j = 1; j< fields.length; j++){
                    String setterName = "set".concat(Util.toCapitalize(fields[j].getName()));
                    try {
                        Method setter = csv.getClass().getMethod(setterName, String.class);
                        setter.invoke(csv, lignes.get(i)[j-1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(error.getErreur());
                        System.out.println(e.getCause().getMessage());
                        String virgule =",";
                        if (error.getErreur() == "") virgule = "";
                        error.setErreur(error.getErreur() + virgule+ e.getCause().getMessage());
                    }
                }
                err.add(error);
                if (error.getErreur().equals("")){
                    tab.add(csv);
                    
                }
            }
            System.out.println(tab.size());
            csvRepository.saveAll(tab);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return err;

    }



    public void setId(int id) {
        this.id = id;
    }



    public void setNum_seance(String num_seance) throws Exception{
        try {
            Integer.parseInt(num_seance);
        } catch (Exception e) {
            throw new Exception("Nombre "+ num_seance + " invalide");
        }
        this.num_seance = num_seance;
    }



    public void setFilm(String film)throws Exception {
        if (film == null){
            System.out.println("Null izy ato");
            throw new Exception("Nom pilote invalide"); 
        }else{
            if (film.trim() == "") throw new Exception("Nom pilote invalide");
            this.film = film;
        }
    }



    public void setCategorie(String categorie)throws Exception {
        if (categorie == null){
            System.out.println("Null izy ato");
            throw new Exception("Nom pilote invalide"); 
        }else{
            if (categorie.trim() == "") throw new Exception("Nom pilote invalide");
            this.categorie = categorie;
        }
    }



    public void setSalle(String salle) throws Exception {
        if (salle == null){
            System.out.println("Null izy ato");
            throw new Exception("Nom pilote invalide"); 
        }else{
            if (salle.trim() == "") throw new Exception("Nom pilote invalide");
            this.salle = salle;
        }
    }



    public void setDate_jour(String date_jour)throws Exception {
        try {
            LocalDate d = LocalDate.parse(date_jour, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.err.println("zoky Tojo "+ date_jour);
        } catch (Exception e) {
            throw new Exception("Date "+ date_jour + " invalide");
        }
        this.date_jour = date_jour;
    }



    public void setHeure(String heure)throws Exception {
        try {
            LocalTime t = LocalTime.parse(heure);
        } catch (Exception e) {
            throw new Exception("Temps "+heure + " invalide");
        }
        this.heure = heure;
    }

    
    





    
    

    



    
}

package com.evaluation.Eval.util;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.evaluation.Eval.entity.Pilote;
import com.evaluation.Eval.repository.CsvRepository;
import com.opencsv.CSVReader;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import lombok.Data;



@Entity
@Data
public class Csv {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String nom;
    String datenaissance;
    String ecurie;
    String grandprix;
    String dategrandprix;
    String temps;

    



    public Csv() {
    }



    public static boolean hasError(ArrayList<Erreur> errors){
        for (Erreur erreur : errors) {
            if (erreur.getErreur() != "") return true;
        }
        return false;
    }


    @Transactional
    public static ArrayList<Erreur> importer( CsvRepository csvRepository,String fileName){
        boolean hasError = false;
        ArrayList<Erreur> err = new ArrayList<Erreur>();
        String filePath = Util.CSV_PATH + "/" + fileName;
        ArrayList<Csv> tab = new ArrayList<Csv>();

        Field[] fields = Csv.class.getDeclaredFields();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
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
                        error.setErreur(error.getErreur() + "," + e.getCause().getMessage());
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


    public void setNom(String n)throws Exception{
        if (n == null){
            System.out.println("Null izy ato");
            throw new Exception("Nom pilote invalide"); 
        }else{
            if (n.trim() == "") throw new Exception("Nom pilote invalide");
            nom = n;
        }
    }

    public void setDatenaissance(String dateNaissance) throws Exception{
        try {
            LocalDate d = LocalDate.parse(dateNaissance, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            System.err.println("zoky Tojo "+ dateNaissance);
        } catch (Exception e) {
            throw new Exception("Date "+ dateNaissance + " invalide");
        }
        this.datenaissance = dateNaissance;
    }


    public void setEcurie(String ecurie) {
        this.ecurie = ecurie;
    }


    public void setGrandprix(String grandPrix) {
        this.grandprix = grandPrix;
    }


    public void setDategrandprix(String dateGrandPrix)throws Exception {
        try {
            LocalDate d = LocalDate.parse(dateGrandPrix, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new Exception("Date "+ dateGrandPrix + " invalide");
        }
        this.dategrandprix = dateGrandPrix;
    }


    public void setTemps(String temps)throws Exception {
        try {
            LocalTime t = LocalTime.parse(temps);
        } catch (Exception e) {
            throw new Exception("Temps "+temps + " invalide");
        }
        this.temps = temps;
    }


    
    

    



    
}

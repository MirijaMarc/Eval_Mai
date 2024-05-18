package com.evaluation.Eval.entity;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.evaluation.Eval.repository.DevisCSVRepository;
import com.evaluation.Eval.repository.MaisonTravauxCSVRepository;
import com.evaluation.Eval.util.Constante;
import com.evaluation.Eval.util.Erreur;
import com.evaluation.Eval.util.Util;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.Data;

@Entity
@Table(name = "devis_csv")
@Data
public class DevisCSV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String client;
    private String ref_devis;
    private String type_maison;
    private String finition;
    private String taux_finition;
    private String date_devis;
    private String date_debut;
    private String lieu;

    
    public static HashMap<String, Object> verifyCSV(MultipartFile file){
        boolean hasError = false;
        ArrayList<Erreur> err = new ArrayList<Erreur>();
        // String filePath = Constante.CSV_PATH + "/" + fileName;
        ArrayList<DevisCSV> tab = new ArrayList<DevisCSV>();
        CSVParser parser = new CSVParserBuilder()
        .withSeparator(',')
        .build();
        Field[] fields = DevisCSV.class.getDeclaredFields();
        HashMap<String, Object> out = new HashMap<String,Object>();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream(), "UTF-8")).withCSVParser(parser).build()) {
            reader.readNext();
            List<String[]> lignes = reader.readAll();
            for (int i= 0; i < lignes.size(); i++){
                DevisCSV csv = new DevisCSV();
                Erreur error = new Erreur(i+1);
                error.setFichier(file.getOriginalFilename());
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
                if (error.getErreur() == "") tab.add(csv);
            }
            out.put("erreurs", err);
            out.put("data", tab);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;

    }



    public static HashMap<String, Object> verifyCSV(String fileName){
        boolean hasError = false;
        ArrayList<Erreur> err = new ArrayList<Erreur>();
        String filePath = Constante.CSV_PATH + "/" + fileName;
        ArrayList<DevisCSV> tab = new ArrayList<DevisCSV>();
        CSVParser parser = new CSVParserBuilder()
        .withSeparator(',')
        .build();
        Field[] fields = DevisCSV.class.getDeclaredFields();
        HashMap<String, Object> out = new HashMap<String,Object>();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(parser).build()) {
            reader.readNext();
            List<String[]> lignes = reader.readAll();
            for (int i= 0; i < lignes.size(); i++){
                DevisCSV csv = new DevisCSV();
                Erreur error = new Erreur(i+1);
                error.setFichier(fileName);
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
                if (error.getErreur() == "") tab.add(csv);
            }
            out.put("erreurs", err);
            out.put("data", tab);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;

    }




    public void setClient(String n)throws Exception {
        if (n == null) throw new Exception("Nom pilote invalide"); 
        if (n.trim() == "") throw new Exception("Nom pilote invalide");
        this.client = n.trim();
    }


    public void setRef_devis(String ref_devis) throws Exception{
        if (ref_devis == null) throw new Exception("Nom Référence invalide"); 
        if (ref_devis.trim() == "") throw new Exception("Nom Référence invalide");
        this.ref_devis = ref_devis.trim();
    }


    public void setType_maison(String type_maison)throws Exception {
        if (type_maison == null) throw new Exception("Nom Type Maison invalide"); 
        if (type_maison.trim() == "") throw new Exception("Nom Type Maison invalide");
        this.type_maison = type_maison.trim();
    }


    public void setFinition(String n)throws Exception {
        if (n == null) throw new Exception("Nom Finition invalide"); 
        if (n.trim() == "") throw new Exception("Nom Finition invalide");
        this.finition = n.trim();
    }


    public void setTaux_finition(String taux_finition)throws Exception {
        try {
            Double.parseDouble(taux_finition.replace(',', '.').replace("%", ""));
        } catch (Exception e) {
            throw new Exception("Nombre "+ taux_finition + " invalide");
        }
        double d = Double.parseDouble(taux_finition.replace(',', '.').replace("%", ""));
        if (d < 0) throw new Exception("Finiton "+ d + " doit est supérieur ou égale à 0");
        this.taux_finition = taux_finition.replace(',', '.').replace("%", "").trim();
    }


    public void setDate_devis(String date_devis)throws Exception {
        if (!Util.isValidDate(date_devis)) throw new Exception("Date "+ date_devis + " invalide");
        this.date_devis = date_devis.trim();
    }


    public void setDate_debut(String date_debut) throws Exception{
        if (!Util.isValidDate(date_debut)) throw new Exception("Date "+ date_devis + " invalide");
        this.date_debut = date_debut.trim();
    }


    public void setLieu(String n)throws Exception {
        if (n == null) throw new Exception("Nom Lieu invalide"); 
        if (n.trim() == "") throw new Exception("Nom Lieu invalide");
        this.lieu = n.trim();
    }

    
}

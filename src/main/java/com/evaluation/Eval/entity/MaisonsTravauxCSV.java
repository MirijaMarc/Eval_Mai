package com.evaluation.Eval.entity;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.evaluation.Eval.repository.CsvRepository;
import com.evaluation.Eval.repository.MaisonTravauxCSVRepository;
import com.evaluation.Eval.util.Constante;
import com.evaluation.Eval.util.Csv;
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
@Data
@Table(name = "maisons_travaux_csv")
public class MaisonsTravauxCSV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type_maison;
    private String description;
    private String surface;
    private String code_travaux;
    private String type_travaux;
    private String unite;
    private String prix_unitaire;
    private String quantite;
    private String duree_travaux;

    
    public static HashMap<String, Object> verifyCSV(MultipartFile file){
        boolean hasError = false;
        ArrayList<Erreur> err = new ArrayList<Erreur>();
        // String filePath = Constante.CSV_PATH + "/" + fileName;
        ArrayList<MaisonsTravauxCSV> tab = new ArrayList<MaisonsTravauxCSV>();
        CSVParser parser = new CSVParserBuilder()
        .withSeparator(',')
        .build();
        Field[] fields = MaisonsTravauxCSV.class.getDeclaredFields();
        HashMap<String, Object> out = new HashMap<String,Object>();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream(), "UTF-8")).withCSVParser(parser).build()) {
            reader.readNext();
            List<String[]> lignes = reader.readAll();
            for (int i= 0; i < lignes.size(); i++){
                MaisonsTravauxCSV csv = new MaisonsTravauxCSV();
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
        ArrayList<MaisonsTravauxCSV> tab = new ArrayList<MaisonsTravauxCSV>();
        CSVParser parser = new CSVParserBuilder()
        .withSeparator(',')
        .build();
        Field[] fields = MaisonsTravauxCSV.class.getDeclaredFields();
        HashMap<String, Object> out = new HashMap<String,Object>();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(parser).build()) {
            reader.readNext();
            List<String[]> lignes = reader.readAll();
            for (int i= 0; i < lignes.size(); i++){
                MaisonsTravauxCSV csv = new MaisonsTravauxCSV();
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
    public void setType_maison(String type_maison)throws Exception {
        if (type_maison == null) throw new Exception("Nom Type Maison invalide"); 
        if (type_maison.trim() == "") throw new Exception("Nom Type Maison invalide");
        this.type_maison = type_maison.trim();
    }

    public void setDescription(String description)throws Exception {
        if (description == null) throw new Exception("Nom Finition invalide"); 
        if (description.trim() == "") throw new Exception("Nom Finition invalide");
        this.description = description.trim();
    }

    public void setSurface(String surface)throws Exception {
        try {
            Double.parseDouble(surface);
        } catch (Exception e) {
            throw new Exception("Nombre "+ surface + " invalide");
        }
        double d = Double.parseDouble(surface.replace(',', '.'));
        if (d <= 0) throw new Exception("Surface "+ d + " doit est supérieur 0");
        this.surface = surface.trim();
    }

    public void setCode_travaux(String code_travaux)throws Exception {
        if (code_travaux == null) throw new Exception("Nom code_travaux invalide"); 
        if (code_travaux.trim() == "") throw new Exception("Nom code_travaux invalide");
        this.code_travaux = code_travaux.trim();
    }

    public void setType_travaux(String type_travaux)throws Exception {
        if (type_travaux == null) throw new Exception("Nom type_travaux invalide"); 
        if (type_travaux.trim() == "") throw new Exception("Nom type_travaux invalide");
        this.type_travaux = type_travaux.trim();
    }

    public void setUnite(String unite)throws Exception {
        if (unite == null) throw new Exception("Nom unite invalide"); 
        if (unite.trim() == "") throw new Exception("Nom unite invalide");
        this.unite = unite.trim();
    }

    public void setPrix_unitaire(String prix_unitaire)throws Exception {
        try {
            Double.parseDouble(prix_unitaire.replace(',', '.'));
        } catch (Exception e) {
            throw new Exception("Nombre "+ prix_unitaire + " invalide");
        }
        double d = Double.parseDouble(prix_unitaire.replace(',', '.'));
        if (d < 0) throw new Exception("Prix "+ d + " doit etre supérieur ou egale 0");
        this.prix_unitaire = prix_unitaire.replace(',', '.').trim();
    }

    public void setQuantite(String quantite)throws Exception {
        try {
            Double.parseDouble(quantite.replace(',', '.'));
        } catch (Exception e) {
            throw new Exception("Nombre "+ quantite + " invalide");
        }
        double d = Double.parseDouble(quantite.replace(',', '.'));
        if (d <= 0) throw new Exception("Quantite "+ d + " doit etre supérieur 0");
        this.quantite = quantite.replace(',', '.').trim();
    }

    public void setDuree_travaux(String duree_travaux)throws Exception {
        try {
            Double.parseDouble(duree_travaux.replace(',', '.'));
        } catch (Exception e) {
            throw new Exception("Nombre "+ duree_travaux + " invalide");
        }
        double d = Double.parseDouble(duree_travaux.replace(',', '.'));
        if (d <= 0) throw new Exception("Duree "+ d + " doit est supérieur 0");
        this.duree_travaux = duree_travaux.replace(',', '.').trim();
    }

    
}

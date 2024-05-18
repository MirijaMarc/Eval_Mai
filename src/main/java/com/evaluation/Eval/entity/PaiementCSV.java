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

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.evaluation.Eval.repository.DevisCSVRepository;
import com.evaluation.Eval.repository.PaiementCSVRepository;
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
import lombok.Data;

@Entity
@Table(name = "paiement_csv")
@Data
public class PaiementCSV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String ref_devis;
    private String ref_paiement;
    private String date_paiement;
    private String montant;


    public static HashMap<String, Object> verifyCSV(MultipartFile file){
        boolean hasError = false;
        ArrayList<Erreur> err = new ArrayList<Erreur>();
        // String filePath = Constante.CSV_PATH + "/" + fileName;
        ArrayList<PaiementCSV> tab = new ArrayList<PaiementCSV>();
        CSVParser parser = new CSVParserBuilder()
        .withSeparator(',')
        .build();
        Field[] fields = PaiementCSV.class.getDeclaredFields();
        HashMap<String, Object> out = new HashMap<String,Object>();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream(), "UTF-8")).withCSVParser(parser).build()) {
            reader.readNext();
            List<String[]> lignes = reader.readAll();
            for (int i= 0; i < lignes.size(); i++){
                PaiementCSV csv = new PaiementCSV();
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
        ArrayList<PaiementCSV> tab = new ArrayList<PaiementCSV>();
        CSVParser parser = new CSVParserBuilder()
        .withSeparator(',')
        .build();
        Field[] fields = PaiementCSV.class.getDeclaredFields();
        HashMap<String, Object> out = new HashMap<String,Object>();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(parser).build()) {
            reader.readNext();
            List<String[]> lignes = reader.readAll();
            for (int i= 0; i < lignes.size(); i++){
                PaiementCSV csv = new PaiementCSV();
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



    public void setRef_devis(String ref_devis)throws Exception {
        if (ref_devis == null) throw new Exception("Nom ref Devis invalide"); 
        if (ref_devis.trim() == "") throw new Exception("Nom ref Devis invalide");
        this.ref_devis = ref_devis.trim();
    }


    public void setRef_paiement(String ref_paiement) throws Exception{
        if (ref_paiement == null) throw new Exception("Nom ref Paiement invalide"); 
        if (ref_paiement.trim() == "") throw new Exception("Nom ref Paiement invalide");
        this.ref_paiement = ref_paiement.trim();
    }


    public void setDate_paiement(String date_paiement)throws Exception {
        // try {
        //     LocalDate d = LocalDate.parse(date_paiement, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        //     // LocalDate d = LocalDate.parse(date_paiement, DateTimeFormatter.ofPattern("dd/M/yyyy"));
        // } catch (Exception e) {
        //     throw new Exception("Date "+ date_paiement + " invalide");
        // }
        if (!Util.isValidDate(date_paiement)) throw new Exception("Date "+ date_paiement + " invalide"); 
        this.date_paiement = date_paiement.trim();
    }


    public void setMontant(String montant)throws Exception {
        try {
            double d = Double.parseDouble(montant.replace(',', '.'));

        } catch (Exception e) {
            throw new Exception("Nombre "+ montant + " invalide");
        }
        double d = Double.parseDouble(montant.replace(',', '.'));
        if (d <= 0) throw new Exception("Montant "+ d + " doit est supérieur à 0");
        this.montant = montant.replace(',', '.').trim();
    }


    


    


}

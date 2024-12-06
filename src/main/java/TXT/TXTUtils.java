/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TXT;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;


/**
 *
 * @author laura
 */
public class TXTUtils {
    private static final String FOLDER_PATH = "TXT"; // Carpeta donde se guardarán los archivos

    public static void saveDataToTXT(String patientName, Date date,  String physiologicalData) {
        // Crear el directorio si no existe
        createDirectoryIfNotExists(FOLDER_PATH);
        String patientNoSpaces = patientName.replaceAll("\\s+", "");
        // Nombre del archivo TXT
        String fileName = FOLDER_PATH + "/" + patientNoSpaces + "_" + date.toString() + "_monitoring.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Escribir los datos como una línea nueva
            writer.write("Patient Name: " + patientName);
            writer.newLine();
            writer.write("Date and Time: " + date.toString());
            writer.newLine();
            writer.write("-------------------------------");
            writer.newLine();
            writer.write(physiologicalData);

            System.out.println("Data saved to file: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void createDirectoryIfNotExists(String folderPath) {
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    
    public static void main(String[] args) {
        
        String patientName = "Andrea Martinez Palacios";
        String physiologicalData = "123/456/789";
        
        Date date = java.sql.Date.valueOf("2024-12-06");
        TXTUtils.saveDataToTXT(patientName, date, physiologicalData);
    
    }
    
}



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TXT;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;


/**
 *
 * @author laura
 */
public class TXTUtils {
    private static final String FOLDER_PATH = "TXT"; // Carpeta donde se guardarán los archivos
    
    public static File saveDataToTXT(String patientName, Date date,  String physiologicalData) {
        // Crear el directorio si no existe
        createDirectoryIfNotExists(FOLDER_PATH);
        String patientNoSpaces = patientName.replaceAll("\\s+", "");
        
        // Format the date and time for the file name and monitoring
        SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        SimpleDateFormat monitoringDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateForFile = fileDateFormat.format(date);
        String formattedDateForMonitoring = monitoringDateFormat.format(date);
        
        // Nombre del archivo TXT
        String fileName = FOLDER_PATH + "/" + patientNoSpaces + "_" + formattedDateForFile + "_monitoring.txt";
        File file=new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Escribir los datos como una línea nueva
            writer.write("Patient Name: " + patientName);
            writer.newLine();
            writer.write("Date and Time of Monitoring: " + formattedDateForMonitoring);
            writer.newLine();
            writer.write("-------------------------------");
            writer.newLine();
            writer.write("Physiological Data: " + physiologicalData);

            System.out.println("Data saved to file: " + fileName);
            
      
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        return file;
    }

    private static void createDirectoryIfNotExists(String folderPath) {
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}



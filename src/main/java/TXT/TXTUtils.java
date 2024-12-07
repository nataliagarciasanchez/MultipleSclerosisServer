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
    
    public static void saveDataToTXT(int report_id, String patientName, Date date,  String physiologicalData) {
        // Crear el directorio si no existe
        createDirectoryIfNotExists(FOLDER_PATH);
        String patientNoSpaces = patientName.replaceAll("\\s+", "");
        
        // Format the date and time for the file name and monitoring
        SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        SimpleDateFormat monitoringDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateForFile = fileDateFormat.format(date);
        String formattedDateForMonitoring = monitoringDateFormat.format(date);
        
        // Nombre del archivo TXT
        String fileName = FOLDER_PATH + "/" + report_id + "_" + patientNoSpaces + "_" + formattedDateForFile + "_monitoring.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Escribir los datos como una línea nueva
            writer.write("Patient Name: " + patientName);
            writer.newLine();
            writer.write("Date and Time of Monitoring: " + formattedDateForMonitoring);
            writer.newLine();
            writer.write("-------------------------------");
            writer.newLine();
            writer.write("Physiological Data: " + physiologicalData);

            System.out.println("Data saved to file: " + fileName);
            Connection connection = null;
            
             //Guarda el archivo como BLOB en la base de datos
            saveFileToDatabase(report_id, fileName, connection);
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
    
     private static void saveFileToDatabase(int bitalinoId, String filePath, Connection connection) {
        String insertQuery = "INSERT INTO Files (file_name, file_data, bitalino_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);

            pstmt.setString(1, file.getName());
            pstmt.setBinaryStream(2, fileInputStream, (int) file.length());
            pstmt.setInt(3, bitalinoId);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("File successfully saved to the database.");
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error saving file to database: " + e.getMessage());
        }
    }
    
    
    
    
}



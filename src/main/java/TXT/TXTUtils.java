/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TXT;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 *
 * @author laura
 */
public class TXTUtils {
    private static final String FOLDER_PATH = "TXT"; // Carpeta donde se guardarán los archivos

    public static void saveDataToTXT(String patientName, String physiologicalData) {
        // Crear el directorio si no existe
        createDirectoryIfNotExists(FOLDER_PATH);

        // Nombre del archivo TXT
        String fileName = FOLDER_PATH + "/" + patientName + "_monitoring.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Escribir los datos como una línea nueva
            writer.write("Patient Name: " + patientName);
            writer.newLine();
            writer.write("Data: " + physiologicalData);
            writer.newLine();
            writer.write("Date and Time: " + new java.util.Date());
            writer.newLine();
            writer.write("-------------------------------");
            writer.newLine();

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
}

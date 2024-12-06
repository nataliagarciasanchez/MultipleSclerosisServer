/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CSV;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author laura
 */
public class CSVUtils {
    private static final String FOLDER_PATH = "monitoring_data";

    public static void saveDataToCSV(String patientName, String physiologicalData) {
        // Crear el directorio si no existe
        createDirectoryIfNotExists(FOLDER_PATH);

        // Obtener la fecha y hora actuales
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Nombre del archivo CSV
        String fileName = FOLDER_PATH + "/" + patientName + "_monitoring.csv";

        try (FileWriter writer = new FileWriter(fileName, true)) {
            // Si el archivo está vacío, escribir encabezados
            File file = new File(fileName);
            if (file.length() == 0) {
                writer.append("Patient Name,Date Time,Physiological Data\n");
            }

            // Escribir los datos en una nueva línea
            writer.append(patientName).append(",")
                  .append(timestamp).append(",")
                  .append(physiologicalData).append("\n");

            System.out.println("Data saved to file: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void createDirectoryIfNotExists(String folderPath) {
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}

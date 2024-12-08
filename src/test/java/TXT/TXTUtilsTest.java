/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TXT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Andreoti
 */
public class TXTUtilsTest {
    
    public TXTUtilsTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of saveDataToTXT method, of class TXTUtils.
     */
    @Test
    public File testSaveDataToTXT() {
        System.out.println("saveDataToTXT");
        
        int report_id = 1;
        String patientName = "Andrea Martinez Palacios";
        String physiologicalData = "123/456/789";
        Date date = new Date();
        SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        SimpleDateFormat monitoringDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateForFile = fileDateFormat.format(date);
        String formattedDateForMonitoring = monitoringDateFormat.format(date);
        
        
        File file1=TXTUtils.saveDataToTXT(patientName, date, physiologicalData);
        
        //expected name
        String patientNoSpaces = patientName.replaceAll("\\s+", "");
        String expectedFileName = "TXT/" + report_id + "_" + patientNoSpaces + "_" + formattedDateForFile + "_monitoring.txt";
        System.out.println(expectedFileName);
        File file2 = new File(expectedFileName);
        System.out.println("Ruta: " + file2.getAbsolutePath());
        System.out.println("Existe: " + file2.exists());
        
        // verify that the file was created
        assertTrue(() -> file2.exists(), "El archivo no fue creado");

        // Verificar el contenido del archivo
        try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
            String line1 = reader.readLine();
            String line2 = reader.readLine();
            String line4 = reader.readLine();

            assertEquals("Patient Name: " + patientName, line1);
            assertEquals("Date and Time of Monitoring: " + formattedDateForMonitoring, line2);
            assertEquals("Physiological Data: " + physiologicalData, line4);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Error al leer el archivo");
        }

        //deleting file after test
        file2.delete();
        return file1;
    }

    
    
}

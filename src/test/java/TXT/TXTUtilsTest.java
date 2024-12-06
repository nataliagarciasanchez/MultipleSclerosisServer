/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TXT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Date;
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
    public void testSaveDataToTXT() {
        System.out.println("saveDataToTXT");
        String patientName = "Andrea Martinez Palacios";
        String physiologicalData = "123/456/789";
        
        Date date = java.sql.Date.valueOf("2024-12-06");
        TXTUtils.saveDataToTXT(patientName, date, physiologicalData);
        
        // Crear el nombre esperado del archivo
        String patientNoSpaces = patientName.replaceAll("\\s+", "");
        String expectedFileName = "TXT/" + patientNoSpaces + "_" + date.toString() + "_monitoring.txt";
        System.out.println(expectedFileName);
        File file = new File(expectedFileName);
        
        // Verificar que el archivo fue creado
        assertTrue(() -> file.exists(), "El archivo no fue creado");

        // Verificar el contenido del archivo
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line1 = reader.readLine();
            String line2 = reader.readLine();
            String line3 = reader.readLine();
            String line4 = reader.readLine();

            assertEquals("Patient Name: " + patientName, line1);
            assertEquals("Date and Time: " + date, line2);
            assertEquals(physiologicalData, line4);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Error al leer el archivo");
        }

        // Limpieza: eliminar el archivo después de la prueba (opcional)
        file.delete();
        
    }

    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Symptom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
public class JDBCSymptomManagerTest {
    
    private static JDBCSymptomManager symptomManager;
    private static JDBCManager jdbcManager;
    
    public JDBCSymptomManagerTest() {
        
    }
    
    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        symptomManager = new JDBCSymptomManager(jdbcManager);
        
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        assertNotNull(symptomManager);
    }
    
    
    @AfterAll
    public static void tearDownClass() {
         if (jdbcManager != null) {
        try {
            // Asegúrate de que la conexión esté cerrada correctamente
            jdbcManager.getConnection().setAutoCommit(true); // Restaura auto-commit
            jdbcManager.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }
    
    @BeforeEach
    public void setUp() throws SQLException {
         // Limpiar la base de datos antes de cada prueba
        jdbcManager.clearAllTables();
        try (Statement statement = jdbcManager.getConnection().createStatement()) {
        ResultSet countRs = statement.executeQuery("SELECT COUNT(*) FROM Symptoms");
        if (countRs.next()) {
            int count = countRs.getInt(1);
            System.out.println("Registros en Symptoms después de clearAllTables: " + count);
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        

    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        if (jdbcManager != null) {
            jdbcManager.clearAllTables();
        // Deshace todos los cambios realizados durante la prueba
        jdbcManager.getConnection().rollback();
        }
    }

    /**
     * Test of createSymptom method, of class JDBCSymptomManager.
     */
    @Test
    public void testCreateSymptom() {
        System.out.println("createSymptom");
        Symptom sys = new Symptom ("TempSymptom");
        System.out.println(sys.toString());
        
        symptomManager.createSymptom(sys);
        
        Symptom fetchedSymptom = symptomManager.getSymptomById(sys.getId());
        System.out.println(fetchedSymptom.toString());
        assertNotNull(fetchedSymptom);
        assertEquals(sys.getId(), fetchedSymptom.getId());
        assertEquals(sys.getName(), fetchedSymptom.getName());
    }

    /**
     * Test of removeSymptom method, of class JDBCSymptomManager.
     */
    @Test
    public void testRemoveSymptom() { //TODO revisar por que se regeneran los sintomas
        System.out.println("removeSymptom");
        Symptom sys = new Symptom ("TempSymptom");
        
        List<Symptom> symptomsInitial = symptomManager.getListOfSymptoms();
        int initialSymptoms = symptomsInitial.size();
        
        symptomManager.createSymptom(sys);
        System.out.println(sys.toString());
        List<Symptom> symptomsBefore = symptomManager.getListOfSymptoms();
        assertEquals(initialSymptoms + 1, symptomsBefore.size());
        
        symptomManager.removeSymptom(sys.getId());
        List<Symptom> symptomsAfter = symptomManager.getListOfSymptoms();
        assertEquals(initialSymptoms, symptomsAfter.size());   //Debe haber el mimsmo numero que initial
       
    }

    /**
     * Test of updateSymptom method, of class JDBCSymptomManager.
     */
    @Test
    public void testUpdateSymptom() {
        System.out.println("updateSymptom");
        Symptom symptom = new Symptom ("TempSymptom");
        symptomManager.createSymptom(symptom);
        System.out.println(symptom.toString());
        symptom.setName("UpdatedSymptom");
        symptomManager.updateSymptom(symptom);
        Symptom updatedSymptom = symptomManager.getSymptomById(symptom.getId());
        System.out.println(updatedSymptom.toString());
        assertNotNull(updatedSymptom);
        assertEquals(symptom.getId(), updatedSymptom.getId());
        assertEquals(symptom.getName(), updatedSymptom.getName());
    }

    /**
     * Test of getListOfSymptoms method, of class JDBCSymptomManager.
     */
    @Test
    public void testGetListOfSymptoms() {
        System.out.println("getListOfSymptoms");
        
        List<Symptom> symptomsInitial = symptomManager.getListOfSymptoms();
        int initialSymptoms = symptomsInitial.size();
        
        Symptom symptom1 = new Symptom ("TempSymptom1");
        Symptom symptom2 = new Symptom ("TempSymptom2");
        symptomManager.createSymptom(symptom1);
        symptomManager.createSymptom(symptom2);
        
        
        List<Symptom> sys = symptomManager.getListOfSymptoms();
        System.out.println(sys.toString());
        assertEquals(initialSymptoms + 2, sys.size());
        
        assertTrue(sys.stream().anyMatch(symptom -> symptom.getId().equals(symptom1.getId())));
        assertTrue(sys.stream().anyMatch(symptom -> symptom.getId().equals(symptom2.getId())));
        assertTrue(sys.stream().anyMatch(symptom -> symptom.getName().equals(symptom1.getName())));
        assertTrue(sys.stream().anyMatch(symptom -> symptom.getName().equals(symptom2.getName())));
    }

    /**
     * Test of getSymptomById method, of class JDBCSymptomManager.
     */
    @Test
    public void testGetSymptomById() {
        System.out.println("getSymptomById");
        Symptom symptom = new Symptom ("TempSymptom");
        symptomManager.createSymptom(symptom);
        System.out.println(symptom.toString());
        
        Symptom fetchedSymptom = symptomManager.getSymptomById(symptom.getId());
        assertNotNull(fetchedSymptom);
        assertEquals(symptom.getId(), fetchedSymptom.getId());
        assertEquals(symptom.getName(), fetchedSymptom.getName());
    }

    /**
     * Test of getSymptomByName method, of class JDBCSymptomManager.
     */
    @Test
    public void testGetSymptomByName() {
        System.out.println("getSymptomByName");
        Symptom sys = new Symptom ("TempSymptom");
        symptomManager.createSymptom(sys);
        System.out.println(sys.toString());
        
        List<Symptom> symptoms= symptomManager.getSymptomByName("TempSymptom");
        assertNotNull(symptoms);
        assertFalse(symptoms.isEmpty());
        assertTrue(symptoms.stream().anyMatch(symptom -> symptom.getName().equals(sys.getName())));
        assertTrue(symptoms.stream().anyMatch(symptom -> symptom.getId().equals(sys.getId())));
        
    }
    
}

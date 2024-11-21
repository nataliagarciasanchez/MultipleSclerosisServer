/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Symptom;
import java.sql.SQLException;
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
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        if (jdbcManager != null) {
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
    public void testRemoveSymptom() {
        System.out.println("deleteSymptom");
        Symptom sys = new Symptom ("TempSymptom");
        symptomManager.createSymptom(sys);
        List<Symptom> rolesBefore = symptomManager.getListOfSymptoms();
        assertEquals(1, rolesBefore.size());
        symptomManager.removeSymptom(sys.getId());
        List<Symptom> rolesAfter = symptomManager.getListOfSymptoms();
        assertEquals(0, rolesAfter.size());   //Debe haber 1
       
    }

    /**
     * Test of updateSymptom method, of class JDBCSymptomManager.
     */
    @Test
    public void testUpdateSymptom() {
        System.out.println("updateSymptom");
        Symptom symptom = new Symptom ("TempSymptom");
        symptomManager.createSymptom(symptom);
        symptom.setName("UpdatedSymptom");
        symptomManager.updateSymptom(symptom);
        Symptom updatedSymptom = symptomManager.getSymptomById(symptom.getId());
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
        Symptom symptom1 = new Symptom ("TempSymptom1");
        Symptom symptom2 = new Symptom ("TempSymptom2");
        symptomManager.createSymptom(symptom1);
        symptomManager.createSymptom(symptom2);
        List<Symptom> sys = symptomManager.getListOfSymptoms();
        System.out.println(sys.toString());
        assertEquals(2, sys.size());
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
        Integer id = null;
        JDBCSymptomManager instance = null;
        Symptom expResult = null;
        Symptom result = instance.getSymptomById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSymptomByName method, of class JDBCSymptomManager.
     */
    @Test
    public void testGetSymptomByName() {
        System.out.println("getSymptomByName");
        String name = "";
        JDBCSymptomManager instance = null;
        List<Symptom> expResult = null;
        List<Symptom> result = instance.getSymptomByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

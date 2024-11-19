/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Patient;
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
public class JDBCPatientManagerTest {
    
    private static JDBCPatientManager patientManager;
    private static JDBCManager jdbcManager;
    
    public JDBCPatientManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        patientManager = new JDBCPatientManager(jdbcManager);
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        assertNotNull(patientManager);
        
    }
    
    @AfterAll
    public static void tearDownClass() {
         if (jdbcManager != null) {
            jdbcManager.disconnect();
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
     * Test of createPatient method, of class JDBCPatientManager.
     */
    @Test
    public void testCreatePatient() {
        System.out.println("createPatient");
        Patient p = new Patient ("TempPatient");
        System.out.println(p.toString());
        patientManager.registerPatient(p);
        Patient fetchedPatient = patientManager.getPatientById(p.getId());
        System.out.println(fetchedPatient.toString());
        assertNotNull(fetchedPatient);
        assertEquals("TempPatient", fetchedPatient.getName());
        }

    /**
     * Test of removePatientById method, of class JDBCPatientManager.
     */
    @Test
    public void testRemovePatientById() {
        System.out.println("removePatientById");
        Integer id = null;
        JDBCPatientManager instance = null;
        instance.removePatientById(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updatePatient method, of class JDBCPatientManager.
     */
    @Test
    public void testUpdatePatient() {
        System.out.println("updatePatient");
        Patient p = null;
        JDBCPatientManager instance = null;
        instance.updatePatient(p);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfPatients method, of class JDBCPatientManager.
     */
    @Test
    public void testGetListOfPatients() {
        System.out.println("getListOfPatients");
        JDBCPatientManager instance = null;
        List<Patient> expResult = null;
        List<Patient> result = instance.getListOfPatients();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPatientById method, of class JDBCPatientManager.
     */
    @Test
    public void testGetPatientById() {
        System.out.println("getPatientById");
        Integer id = null;
        JDBCPatientManager instance = null;
        Patient expResult = null;
        Patient result = instance.getPatientById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPatientByName method, of class JDBCPatientManager.
     */
    @Test
    public void testGetPatientByName() {
        System.out.println("getPatientByName");
        String name = "";
        JDBCPatientManager instance = null;
        List<Patient> expResult = null;
        List<Patient> result = instance.getPatientByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPatientsFromDoctor method, of class JDBCPatientManager.
     */
    @Test
    public void testGetPatientsFromDoctor() {
        System.out.println("getPatientsFromDoctor");
        Integer doctorId = null;
        JDBCPatientManager instance = null;
        List<Patient> expResult = null;
        List<Patient> result = instance.getPatientsFromDoctor(doctorId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

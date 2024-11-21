/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Patient;
import POJOs.User;
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
        User user=new User("TempUser");
        Patient p = new Patient ("TempPatient");
        System.out.println(p.toString());
        patientManager.registerPatient(p, user);
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
        System.out.println("DeletePatient");
        Patient p = new Patient ("TempPatient");
        User u= new User ("TempUser");
        patientManager.registerPatient(p,u);
        List<Patient> PatientsBefore = patientManager.getListOfPatients();
        assertEquals(1, PatientsBefore.size());
        
       patientManager.removePatientById(p.getId());
        List<Patient> PatientsAfter = patientManager.getListOfPatients();
        assertEquals(0, PatientsAfter.size());
    }

    /**
     * Test of updatePatient method, of class JDBCPatientManager.
     */
    @Test
    public void testUpdatePatient() {      
        System.out.println("updatePatient");
        Patient p = new Patient ("Patient");
        User u= new User ("User");
        patientManager.registerPatient(p,u);
        p.setName("UpdatedPatient");
        patientManager.updatePatient(p);
        Patient updatedPatient = patientManager.getPatientById(p.getId());
        assertNotNull(updatedPatient);
        assertEquals("UpdatedPatient", updatedPatient.getName());
    }

    /**
     * Test of getListOfPatients method, of class JDBCPatientManager.
     */
    @Test
    public void testGetListOfPatients() {
        System.out.println("getListOfPatients");
        patientManager.registerPatient(new Patient("Patient1"), new User ("User1"));
        patientManager.registerPatient(new Patient("Patient2"), new User ("User2"));

        List<Patient> patients = patientManager.getListOfPatients();
        assertEquals(2, patients.size());
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals("Patient1")));
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals("Patient2")));
        
    }

    /**
     * Test of getPatientById method, of class JDBCPatientManager.
     */
    @Test
    public void testGetPatientById() {
        System.out.println("getPatientById");
        Patient p = new Patient("TempPatient");
        User u= new User ("TempUser");
        patientManager.registerPatient(p,u);
        Patient fetchedPatient = patientManager.getPatientById(p.getId());
        assertNotNull(fetchedPatient);
        assertEquals(p.getId(), fetchedPatient.getId());
    }

    /**
     * Test of getPatientByName method, of class JDBCPatientManager.
     */
    @Test
    public void testGetPatientByName() {
        System.out.println("getPatientByName");
        Patient p = new Patient("TempPatient");
        User u= new User ("TempUser");
        patientManager.registerPatient(p,u);
        List<Patient> patients= patientManager.getPatientByName("TempPatient");
        assertNotNull(patients);
        assertFalse(patients.isEmpty());
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals("TempPatient")));
        System.out.println("getPatientByName");
        
    }

    /**
     * Test of getPatientByUser method, of class JDBCPatientManager.
     */
    @Test
    public void  testGetPatientByUser(){
        System.out.println("testGetPatientByUser");
        Patient p = new Patient("TempPatient");
        User u= new User ("TempUser");
        patientManager.registerPatient(p,u);
        Patient fetchedPatient= patientManager.getPatientByUser(u);
        assertNotNull(fetchedPatient);
        assertEquals(p.getUser(), fetchedPatient.getUser());
        
    }
    
    /**
     * Test of getPatientsFromDoctor method, of class JDBCPatientManager.
     */
    @Test
    public void testGetPatientsFromDoctor(){
        System.out.println("testGetPatientsFromDoctor");
        Patient p = new Patient("TempPatient");
        User u= new User ("TempUser");
        patientManager.registerPatient(p,u);
        //Integer doctorId
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import Menu.Utilities.Utilities;
import POJOs.Doctor;
import POJOs.Gender;
import POJOs.Patient;
import POJOs.Role;
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
    private static JDBCDoctorManager doctorManager;
    private static JDBCUserManager userManager;
    private static JDBCRoleManager roleManager;
    private static Role r1;
    private static Role r2;
    private static Role r3;
    private static User u1;
    private static User u2;
    private static User u3;
    private static Doctor d;
    
    public JDBCPatientManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        patientManager = new JDBCPatientManager(jdbcManager);
        doctorManager = new JDBCDoctorManager(jdbcManager);
        userManager = new JDBCUserManager(jdbcManager);
        roleManager = new JDBCRoleManager(jdbcManager);
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
        r1 = new Role ("Doctor");
        roleManager.createRole(r1);
        r2 = new Role ("Patient");
        roleManager.createRole(r2);
        r3 = new Role ("Patient");
        roleManager.createRole(r3);
        u1 = new User ("email", "password", r1);
        userManager.registerUser(u1);
        u2 = new User ("email", "password", r2);
        userManager.registerUser(u2);
        u3 = new User ("email", "password", r3);
        userManager.registerUser(u3);
        d = new Doctor ("TempDoctor", "NEUROLOGY", u1);
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
    public void testCreatePatient() throws ParseException {
        System.out.println("createPatient");
        java.sql.Date dob=Utilities.convertString2SqlDate("14/10/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,u2);
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
    public void testRemovePatientById() throws ParseException {
        System.out.println("DeletePatient");
        java.sql.Date dob=Utilities.convertString2SqlDate("14/10/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,u2);
        System.out.println(p.toString());
        patientManager.registerPatient(p);
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
    public void testUpdatePatient() throws ParseException {      
        System.out.println("updatePatient");
        java.sql.Date dob=Utilities.convertString2SqlDate("14/10/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,u2);
        patientManager.registerPatient(p);
        p.setName("UpdatedPatient");
        patientManager.updatePatient(p);
        Patient updatedPatient = patientManager.getPatientById(p.getId());
        assertNotNull(updatedPatient);
        assertEquals("UpdatedPatient", updatedPatient.getName());
    }

    /**
     * Test of getListOfPatients method, of class JDBCPatientManager.
     */
   /* @Test
    public void testGetListOfPatients() {
        System.out.println("getListOfPatients");
        Doctor d1 = new Doctor ("Doctor1","NEUROLOGY",u1);
        System.out.println(d1.toString());
        doctorManager.createDoctor(d1);
        Patient p1 = new Patient ("TempPatient1","Auba","71045623A",dob, Gender.FEMALE,d1);
        Patient p2 = new Patient ("TempPatient2","Auba","71045623A",dob, Gender.MALE,d1);
        patientManager.registerPatient(p1,u2);
        patientManager.registerPatient(p2,u3);
        List<Patient> patients = patientManager.getListOfPatients();
        assertEquals(2, patients.size());
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals("Patient1")));
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals("Patient2")));
        
    }

    /**
     * Test of getPatientById method, of class JDBCPatientManager.
     */
   /* @Test
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
   /* @Test
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
   /* @Test
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
    /*@Test
    public void testGetPatientsFromDoctor(){
        System.out.println("testGetPatientsFromDoctor");
        Doctor d= new Doctor ("TempDoctor");
        Patient p = new Patient("TempPatient",d);
        User u= new User ("TempUserPatient");
        patientManager.registerPatient(p,u);
        //Integer doctorId
    }
    */
    
}

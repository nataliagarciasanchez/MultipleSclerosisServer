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
import java.text.ParseException;
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
        d = new Doctor ("TempDoctor", "TempDoctorSurname", u1);
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
        patientManager.registerPatient(p);
        System.out.println(p.toString());
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
        patientManager.registerPatient(p);
        System.out.println(p.toString());
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
        System.out.println(p.toString());
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
    public void testGetListOfPatients() throws ParseException {
        System.out.println("getListOfPatients");
        java.sql.Date dob1=Utilities.convertString2SqlDate("17/8/2003");
        java.sql.Date dob2=Utilities.convertString2SqlDate("14/10/2023");
        Patient p1 = new Patient ("TempPatient1","Auba","71045623A",dob1, Gender.FEMALE,"678954326",d,u2);
        Patient p2 = new Patient ("TempPatient2","Arribas","91145642F",dob2, Gender.MALE,"678934390",d,u3);
        patientManager.registerPatient(p1);
        patientManager.registerPatient(p2);
        System.out.println(p1.toString());
        System.out.println(p2.toString());
        List<Patient> patients = patientManager.getListOfPatients();
        assertEquals(2, patients.size());
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals("TempPatient1")));
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals("TempPatient2")));
        assertTrue(patients.stream().anyMatch(patient -> patient.getSurname().equals("Auba")));
        assertTrue(patients.stream().anyMatch(patient -> patient.getSurname().equals("Arribas")));
        assertTrue(patients.stream().anyMatch(patient -> patient.getNIF().equals("71045623A")));
        assertTrue(patients.stream().anyMatch(patient -> patient.getNIF().equals("91145642F")));
        assertTrue(patients.stream().anyMatch(patient -> patient.getDob().equals (dob1)));
        assertTrue(patients.stream().anyMatch(patient -> patient.getDob().equals (dob2)));
        assertTrue(patients.stream().anyMatch(patient -> patient.getGender().equals(Gender.FEMALE)));
        assertTrue(patients.stream().anyMatch(patient -> patient.getGender().equals(Gender.MALE)));
        assertTrue(patients.stream().anyMatch(patient -> patient.getPhone().equals(678954326)));
        assertTrue(patients.stream().anyMatch(patient -> patient.getPhone().equals(678934390)));
        
        
    }

    /**
     * Test of getPatientById method, of class JDBCPatientManager.
     */
   @Test
    public void testGetPatientById() throws ParseException {
        System.out.println("getPatientById");
        java.sql.Date dob=Utilities.convertString2SqlDate("14/10/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,u2);
        patientManager.registerPatient(p);
        System.out.println(p.toString());
        Patient fetchedPatient = patientManager.getPatientById(p.getId());
        assertNotNull(fetchedPatient);
        assertEquals(p.getId(), fetchedPatient.getId());
        assertEquals(p.getName(), fetchedPatient.getName());
        assertEquals(p.getSurname(), fetchedPatient.getSurname());
        assertEquals(p.getNIF(), fetchedPatient.getNIF());
        assertEquals(p.getDob(), fetchedPatient.getDob());
        assertEquals(p.getGender(), fetchedPatient.getGender());
        assertEquals(p.getPhone(), fetchedPatient.getPhone());
        
    }

    /**
     * Test of getPatientByName method, of class JDBCPatientManager.
     */
    @Test
    public void testGetPatientByName() throws ParseException {
        System.out.println("getPatientByName");
        java.sql.Date dob=Utilities.convertString2SqlDate("14/10/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,u2);
        patientManager.registerPatient(p);
        System.out.println(p.toString());
        List<Patient> patients= patientManager.getPatientByName("TempPatient");
        assertNotNull(patients);
        assertFalse(patients.isEmpty());
        assertTrue(patients.stream().anyMatch(patient -> patient.getId().equals(p.getId())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals(p.getName())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getSurname().equals(p.getSurname())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getNIF().equals(p.getNIF())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getDob().equals(p.getDob())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getGender().equals(p.getGender())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getPhone().equals(p.getPhone())));
        
    }

    /**
     * Test of getPatientByUser method, of class JDBCPatientManager.
     */
   @Test
    public void  testGetPatientByUser() throws ParseException{
        System.out.println("testGetPatientByUser");
        java.sql.Date dob=Utilities.convertString2SqlDate("14/10/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,u2);
        patientManager.registerPatient(p);
        System.out.println(p.toString());
        Patient fetchedPatient = patientManager.getPatientByUser(u2);
        assertNotNull(fetchedPatient);
        assertEquals(p.getId(), fetchedPatient.getId());
        assertEquals(p.getName(), fetchedPatient.getName());
        assertEquals(p.getSurname(), fetchedPatient.getSurname());
        assertEquals(p.getNIF(), fetchedPatient.getNIF());
        assertEquals(p.getDob(), fetchedPatient.getDob());
        assertEquals(p.getGender(), fetchedPatient.getGender());
        assertEquals(p.getPhone(), fetchedPatient.getPhone());
        
        
    }
    
    /**
     * Test of getPatientsFromDoctor method, of class JDBCPatientManager.
     */
    @Test
    public void testGetPatientsFromDoctor() throws ParseException{
        System.out.println("testGetPatientsFromDoctor");
        java.sql.Date dob1=Utilities.convertString2SqlDate("24/04/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob1, Gender.FEMALE,"678954326",d,u2);
        patientManager.registerPatient(p);
        System.out.println(p.toString());
        List<Patient> patients= patientManager.getPatientsFromDoctor(d.getId());
        assertNotNull(patients);
        assertFalse(patients.isEmpty());
        assertTrue(patients.stream().anyMatch(patient -> patient.getId().equals(p.getId())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals(p.getName())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getSurname().equals(p.getSurname())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getNIF().equals(p.getNIF())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getDob().equals(p.getDob())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getGender().equals(p.getGender())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getPhone().equals(p.getPhone())));
        
    }
    
    /**
     * Test of getDoctorIdFromPatient method, of class JDBCPatientManager.
     */
    @Test
    public void getDoctorIdFromPatient() throws ParseException{
        System.out.println("testgetDoctorIdFromPatient");
        java.sql.Date dob1=Utilities.convertString2SqlDate("24/04/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob1, Gender.FEMALE,"678954326",d,u2);
        patientManager.registerPatient(p);
        System.out.println(p.toString());
        int doctor_id= patientManager.getDoctorIdFromPatient(p);
        assertNotNull(doctor_id);
        assertEquals(p.getDoctor().getId(), doctor_id);
        
    }
   
        
    /**
     * Test of assignDoctor2Patient method, of class JDBCPatientManager.
     */
    @Test
    public void assignDoctor2Patient() throws ParseException{
        System.out.println("testAssignDoctor2Patient");
        java.sql.Date dob1=Utilities.convertString2SqlDate("24/04/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob1, Gender.FEMALE,"678954326",u2);
        patientManager.registerPatient(p);
        System.out.println(p.toString());
        int assigned_id= patientManager.assignDoctor2Patient();
        assertNotNull(assigned_id);
        assertEquals(p.getDoctor().getId(), assigned_id);
        
    }
    
    
}


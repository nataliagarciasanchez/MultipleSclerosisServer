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
import java.sql.Date;
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
    private static Role role_doctor;
    private static Role role_patient;
    private static User user_doctor;
    private static User user_patient1;
    private static User user_patient2;
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
        role_doctor = new Role ("Doctor");
        roleManager.createRole(role_doctor);
        role_patient = new Role ("Patient");
        roleManager.createRole(role_patient);
        
        user_doctor = new User ("doctorEmail", "doctorPassword", role_doctor);
        userManager.registerUser(user_doctor);
        d = new Doctor ("TempDoctor", "TempDoctorSurname", user_doctor);
        doctorManager.registerDoctor(d);
        
        user_patient1 = new User ("patientEmail1", "patienrPassword1", role_patient);
        userManager.registerUser(user_patient1);
        
        user_patient2 = new User ("patientEmail2", "patienrPassword2", role_patient);
        userManager.registerUser(user_patient2);
        
        
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        if (jdbcManager != null) {
        // Deshace todos los cambios realizados durante la prueba
        jdbcManager.getConnection().rollback();
    }
    }

   /**
     * Test of registerPatient method, of class JDBCPatientManager.
     */
    @Test
    public void testRegisterPatient() throws ParseException {
        System.out.println("createPatient");
        java.sql.Date dob=Utilities.convertString2SqlDate("14/10/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,user_patient1);
        patientManager.registerPatient(p);
        System.out.println(p.toString());
        Patient fetchedPatient = patientManager.getPatientById(p.getId());
        System.out.println(fetchedPatient.toString());
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
     * Test of removePatientById method, of class JDBCPatientManager.
     */
    @Test
    public void testRemovePatientById() throws ParseException {
        System.out.println("RemovePatient");
        java.sql.Date dob=Utilities.convertString2SqlDate("14/10/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,user_patient1);
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
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,user_patient1);
        
        patientManager.registerPatient(p);
        System.out.println(p.toString());
        
        p.setName("UpdatedPatient");
        patientManager.updatePatient(p);
        
        Patient updatedPatient = patientManager.getPatientById(p.getId());
        assertNotNull(updatedPatient);
        assertEquals(p.getName(), updatedPatient.getName());
    }

    /**
     * Test of getListOfPatients method, of class JDBCPatientManager.
     */
    @Test
    public void testGetListOfPatients() throws ParseException {
        System.out.println("getListOfPatients");
        java.sql.Date dob1=Utilities.convertString2SqlDate("17/8/2003");
        java.sql.Date dob2=Utilities.convertString2SqlDate("14/10/2023");
        Patient p1 = new Patient ("TempPatient1", "Auba", "71045623A", dob1, Gender.FEMALE, "678954326", d, user_patient1);
        Patient p2 = new Patient ("TempPatient2", "Arribas", "91145642F",dob2, Gender.MALE, "678934390", d, user_patient2);
        
        patientManager.registerPatient(p1);
        patientManager.registerPatient(p2);
        System.out.println(p1.toString());
        System.out.println(p2.toString());
        
        List<Patient> patients = patientManager.getListOfPatients();
        
        assertEquals(2, patients.size());
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals(p1.getName())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getName().equals(p2.getName())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getSurname().equals(p1.getSurname())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getSurname().equals(p2.getSurname())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getNIF().equals(p1.getNIF())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getNIF().equals(p2.getNIF())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getDob().equals (p1.getDob())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getDob().equals (p2.getDob())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getGender().equals(p1.getGender())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getGender().equals(p2.getGender())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getPhone().equals(p1.getPhone())));
        assertTrue(patients.stream().anyMatch(patient -> patient.getPhone().equals(p2.getPhone())));
        
        
    }

    /**
     * Test of getPatientById method, of class JDBCPatientManager.
     */
   @Test
    public void testGetPatientById() throws ParseException {
        System.out.println("getPatientById");
        java.sql.Date dob=Utilities.convertString2SqlDate("14/10/2003");
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,user_patient1);
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
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,user_patient1);
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
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob, Gender.FEMALE,"678954326",d,user_patient1);
        patientManager.registerPatient(p);
        System.out.println(p.toString());
        Patient fetchedPatient = patientManager.getPatientByUser(user_patient1);
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
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob1, Gender.FEMALE,"678954326",d,user_patient1);
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
        Patient p = new Patient ("TempPatient","Auba","71045623A",dob1, Gender.FEMALE,"678954326",d,user_patient1);
        patientManager.registerPatient(p);
        System.out.println(p.toString());
        int doctor_id= patientManager.getDoctorIdFromPatient(p);
        assertNotNull(doctor_id);
        assertEquals(p.getDoctor().getId(), doctor_id);
        
    }
   
}


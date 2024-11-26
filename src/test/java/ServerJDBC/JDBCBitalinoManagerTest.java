/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Bitalino;
import POJOs.Doctor;
import POJOs.Gender;
import POJOs.Patient;
import POJOs.Report;
import POJOs.Role;
import POJOs.SignalType;
import POJOs.User;
import java.sql.Date;
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
public class JDBCBitalinoManagerTest {
    
    private static JDBCBitalinoManager bitalinoManager;
    private static JDBCManager jdbcManager;
    private static JDBCReportManager reportManager;
    private static JDBCRoleManager roleManager;
    private static JDBCUserManager userManager;
    private static JDBCPatientManager patientManager;
    private static JDBCDoctorManager doctorManager;
    private static Role rp; // role patient
    private static Role rd; // role doctor
    private static User up; // user patient
    private static User ud; // user doctor
    private static Doctor d;
    private static Patient p;
    private static Report r;
    
    
    public JDBCBitalinoManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        bitalinoManager = new JDBCBitalinoManager(jdbcManager);
        patientManager = new JDBCPatientManager(jdbcManager);
        reportManager = new JDBCReportManager(jdbcManager);
        roleManager = new JDBCRoleManager(jdbcManager);
        userManager = new JDBCUserManager(jdbcManager);
        doctorManager = new JDBCDoctorManager(jdbcManager);
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        assertNotNull(bitalinoManager);
        assertNotNull(roleManager);
        assertNotNull(userManager);
        assertNotNull(doctorManager);
        assertNotNull(patientManager);
        assertNotNull(reportManager);
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
        rd = new Role("Doctor");
        roleManager.createRole(rd);
        ud = new User("emailD", "passD", rd);
        userManager.registerUser(ud);
        d = new Doctor("name", "surname", ud);
        doctorManager.registerDoctor(d);
        rp = new Role("Patient");
        roleManager.createRole(rp);
        up = new User("emailP", "passP", rp);
        userManager.registerUser(up);
        Date dob = java.sql.Date.valueOf("2003-09-30");
        p = new Patient("name", "Auba", "71523456U", dob, Gender.FEMALE, "615345689", d, up);
        patientManager.registerPatient(p);
        Date date = java.sql.Date.valueOf("2024-04-19");
        r = new Report(date, p);
        reportManager.createReport(r);
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        if (jdbcManager != null) {
        // Deshace todos los cambios realizados durante la prueba
        jdbcManager.getConnection().rollback();
    }
    }
    
    /**
     * Test of createBitalino method, of class JDBCBitalinoManager.
     */
    @Test
    public void testCreateBitalino() {
        System.out.println("createBitalino");
        Date recorded = java.sql.Date.valueOf("2024-11-21");
        Bitalino b = new Bitalino (recorded,SignalType.EMG,r);
        bitalinoManager.createBitalino(b);
        System.out.println(b.toString());
        // Verificar si fue creado correctamente
        Bitalino fetchedBitalino = bitalinoManager.getBitalinoById(b.getId());
        assertNotNull(fetchedBitalino);
        assertEquals(b.getDate(), fetchedBitalino.getDate());
        assertEquals(b.getSignal_type(), fetchedBitalino.getSignal_type());
        assertEquals(b.getDuration(), fetchedBitalino.getDuration());
        assertEquals(b.getId(), fetchedBitalino.getId());
    }

    /**
     * Test of removeBitalinoById method, of class JDBCBitalinoManager.
     */
    @Test
    public void testRemoveBitalinoById() {
        System.out.println("DeleteBitalino");
        Date recorded = java.sql.Date.valueOf("2024-11-21");
        Bitalino b = new Bitalino (recorded,SignalType.EMG,r);
        bitalinoManager.createBitalino(b);
        System.out.println(b.toString());
        List<Bitalino> BitalinosBefore = bitalinoManager.getListOfBitalinos();
        assertEquals(1, BitalinosBefore.size());
        bitalinoManager.removeBitalinoById(b.getId());
        List<Bitalino> BitalinosAfter = bitalinoManager.getListOfBitalinos();
        assertEquals(0, BitalinosAfter.size());
        
    }/*

    /**
     * Test of updateBitalino method, of class JDBCBitalinoManager.
     */
   // @Test
    public void testUpdateBitalino() {
        System.out.println("updateBitalino");
        Date recorded = java.sql.Date.valueOf("2024-11-21");
        Bitalino b = new Bitalino (recorded,SignalType.EMG,r);
        bitalinoManager.createBitalino(b);
        System.out.println(b.toString());
        b.setSignal_type(SignalType.ECG);
        bitalinoManager.updateBitalino(b);
        System.out.println(b.toString());
        Bitalino updatedBitalino = bitalinoManager.getBitalinoById(b.getId());
        assertNotNull(updatedBitalino);
        assertEquals(b.getSignal_type(), updatedBitalino.getSignal_type());
        
    }

    /**
     * Test of getListOfBitalinos method, of class JDBCBitalinoManager.
     */
    @Test
    public void testGetListOfBitalinos() {
        System.out.println("getListOfBitalinos");
        Date recorded1 = java.sql.Date.valueOf("2024-11-21");
        Bitalino b1 = new Bitalino (recorded1,SignalType.EMG,r);     
        Date recorded2 = java.sql.Date.valueOf("2024-11-21");
        Bitalino b2 = new Bitalino (recorded2,SignalType.EMG,r);
        bitalinoManager.createBitalino(b1);
        bitalinoManager.createBitalino(b2);
        System.out.println(b1.toString());
        System.out.println(b2.toString());
        
        List<Bitalino> bitalinos = bitalinoManager.getListOfBitalinos();
        assertEquals(2, bitalinos.size());
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getDate().equals(b1.getDate())));
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getDate().equals(b2.getDate())));
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getSignal_type().equals(b1.getSignal_type())));
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getSignal_type().equals(b2.getSignal_type())));
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getDuration().equals(b1.getDuration())));
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getDuration().equals(b2.getDuration())));
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getId().equals(b1.getId())));
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getId().equals(b2.getId())));
        
    }

    /**
     * Test of getBitalinoById method, of class JDBCBitalinoManager.
     */
    @Test
    public void testGetBitalinoById() {
        System.out.println("getBitalinoById");
        Date recorded = java.sql.Date.valueOf("2024-11-21");
        Bitalino b = new Bitalino (recorded,SignalType.EMG,r);
        bitalinoManager.createBitalino(b);
        System.out.println(b.toString());
        Bitalino fetchedBitalino = bitalinoManager.getBitalinoById(b.getId());
        assertNotNull(fetchedBitalino);
        assertEquals(b.getId(), fetchedBitalino.getId());
        assertEquals(b.getDate(), fetchedBitalino.getDate());
        assertEquals(b.getSignal_type(), fetchedBitalino.getSignal_type());
        assertEquals(b.getDuration(), fetchedBitalino.getDuration());
       
      }

    /**
     * Test of getBitalinosByDate method, of class JDBCBitalinoManager.
     */
    @Test
    public void testGetBitalinosByDate() {
        Date recorded = java.sql.Date.valueOf("2024-11-21");
        Bitalino b = new Bitalino (recorded,SignalType.EMG,r);
        bitalinoManager.createBitalino(b);
        System.out.println(b.toString());
        List<Bitalino> bitalinos= bitalinoManager.getBitalinosByDate(recorded);
        assertNotNull(bitalinos);
        assertFalse(bitalinos.isEmpty());
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getDate().equals(recorded)));
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getId().equals(b.getId())));
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getSignal_type().equals(b.getSignal_type())));
        assertTrue(bitalinos.stream().anyMatch(bitalino -> bitalino.getDuration().equals(b.getDuration())));
        
    }

    /**
     * Test of getBitalinosOfReport method, of class JDBCBitalinoManager.
     */
    @Test
    public void testGetBitalinosOfReport() {
        System.out.println("getBitalinosOfReport");
        Date recorded1 = java.sql.Date.valueOf("2024-11-21");
        Bitalino b1 = new Bitalino (recorded1,SignalType.EMG,r);
        bitalinoManager.createBitalino(b1);
        Date recorded2 = java.sql.Date.valueOf("2024-11-21");
        Bitalino b2 = new Bitalino (recorded2,SignalType.ECG,r);
        bitalinoManager.createBitalino(b2);
        System.out.println(b1.toString());
        System.out.println(b2.toString());
        List<Bitalino> bitalinos = bitalinoManager.getBitalinosOfReport(r.getId());
        assertNotNull(bitalinos, "La lista bitalino NO debería ser null.");
        assertEquals(2, bitalinos.size(), "Debería haber exactamente 2 ");
        assertEquals(b1.getDate(), bitalinos.get(0).getDate());
        assertEquals(b1.getSignal_type(), bitalinos.get(0).getSignal_type());
        assertEquals(b2.getDate(), bitalinos.get(1).getDate());
        assertEquals(b2.getSignal_type(), bitalinos.get(1).getSignal_type());

    }
    
}

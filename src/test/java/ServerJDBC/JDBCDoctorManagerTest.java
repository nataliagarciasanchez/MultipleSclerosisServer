/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Doctor;
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
public class JDBCDoctorManagerTest {
    
    private static JDBCDoctorManager doctorManager;
    private static JDBCManager jdbcManager;
    
    public JDBCDoctorManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        doctorManager = new JDBCDoctorManager(jdbcManager);
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        
        assertNotNull(doctorManager);
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
     * Test of createDoctor method, of class JDBCDoctorManager.
     */
    @Test
    public void testCreateDoctor() {
        System.out.println("createDoctor");
        Doctor d = new Doctor ("TempDoctor");
        System.out.println(d.toString());
        
        doctorManager.createDoctor(d);
        Doctor fetchedDoctor = doctorManager.getDoctorById(d.getId());
        System.out.println(fetchedDoctor.toString());
        assertNotNull(fetchedDoctor);
        assertEquals("TempDoctor", fetchedDoctor.getName());
        
    }

    /**
     * Test of removeDoctorById method, of class JDBCDoctorManager.
     */
    @Test
    public void testRemoveDoctorById() {      
        System.out.println("DeleteDoctor");
        Doctor d = new Doctor ("TempDoctor");
        doctorManager.createDoctor(d);
        List<Doctor> DoctorsBefore = doctorManager.getListOfDoctors();
        assertEquals(1, DoctorsBefore.size());
        
       doctorManager.removeDoctorById(d.getId());
        List<Doctor> DoctorsAfter = doctorManager.getListOfDoctors();
        assertEquals(0, DoctorsAfter.size());
       
    }
    
    /**
     * Test of updateDoctor method, of class JDBCDoctorManager.
     */
    
    public void testUpdateDoctor() {
        System.out.println("updateDoctor");
        Doctor d = new Doctor ("Doctor");
        doctorManager.createDoctor(d);
        d.setName("UpdatedDoctor");
        doctorManager.updateDoctor(d);
        Doctor updatedDoctor = doctorManager.getDoctorById(d.getId());
        assertNotNull(updatedDoctor);
        assertEquals("UpdatedDoctor", updatedDoctor.getName());
       
    }

    /**
     * Test of getListOfDoctors method, of class JDBCDoctorManager.
     */
    @Test
    public void testGetListOfDoctors() {
        System.out.println("getListOfDoctors");
        doctorManager.createDoctor(new Doctor("Doctor1"));
        doctorManager.createDoctor(new Doctor("Doctor2"));

        List<Doctor> doctors = doctorManager.getListOfDoctors();
        assertEquals(2, doctors.size());
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getName().equals("Doctor1")));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getName().equals("Doctor2")));
    }
    

    /**
     * Test of getDoctorById method, of class JDBCDoctorManager.
     */
    @Test
    public void testGetDoctorById() {
        System.out.println("getDoctorById");
        Doctor d = new Doctor("TempDoctor");
        doctorManager.createDoctor(d);
        Doctor fetchedDoctor = doctorManager.getDoctorById(d.getId());
        assertNotNull(fetchedDoctor);
        assertEquals(d.getId(), fetchedDoctor.getId());
       
    }
    
    /**
     * Test of getDoctorById method, of class JDBCDoctorManager.
     */
    @Test
    public void testgetDoctorByUser(){
        
       /* System.out.println("getDoctorByUser");
        Doctor d = new Doctor("TempDoctor");
        doctorManager.createDoctor(d);
        Doctor fetchedDoctor = doctorManager.getDoctorById(d.getId());
        assertNotNull(fetchedDoctor);
        assertEquals(d.getId(), fetchedDoctor.getId());
    */
        }
    

    /**
     * Test of getDoctorByName method, of class JDBCDoctorManager.
     */
    @Test
    public void testGetDoctorByName() {
        System.out.println("getDoctorByName");
        Doctor d = new Doctor("TempDoctor");
        doctorManager.createDoctor(d);
        List<Doctor> doctors= doctorManager.getDoctorByName("TempDoctor");
        assertNotNull(doctors);
        assertFalse(doctors.isEmpty());
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getName().equals("TempDoctor")));
    }
    
}

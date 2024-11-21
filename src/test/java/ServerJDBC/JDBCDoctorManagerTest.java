/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Doctor;
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
public class JDBCDoctorManagerTest {
    
    private static JDBCDoctorManager doctorManager;
    private static JDBCManager jdbcManager;
    private static JDBCRoleManager roleManager;
    private static JDBCUserManager userManager;
    private static Role r;
    private static User u;
    
    public JDBCDoctorManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        doctorManager = new JDBCDoctorManager(jdbcManager);
        roleManager = new JDBCRoleManager(jdbcManager);
        userManager = new JDBCUserManager(jdbcManager);
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
        r = new Role ("Doctor");
        roleManager.createRole(r);
        u = new User ("email", "password", r);
        userManager.registerUser(u);
        
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
        
        Doctor d = new Doctor("TempDoctor", "NEUROLOGY", u);
        System.out.println(d.toString());
        
        doctorManager.createDoctor(d);
        Doctor fetchedDoctor = doctorManager.getDoctorById(d.getId());
        System.out.println(fetchedDoctor.toString());
        assertNotNull(fetchedDoctor);
        assertEquals(d.getName(), fetchedDoctor.getName());
        assertEquals(d.getId(), fetchedDoctor.getId());
        assertEquals(d.getSpecialty(), fetchedDoctor.getSpecialty());
        
        
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
        Doctor d1 = new Doctor ("Doctor1");
        Doctor d2 = new Doctor ("Doctor2");
        doctorManager.createDoctor(d1);
        doctorManager.createDoctor(d2);

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
        User u= new User ("TempUser");
        Doctor d = new Doctor(1,"TempDoctor", "NEUROLOGY", u);
        doctorManager.createDoctor(d);
        Doctor fetchedDoctor = doctorManager.getDoctorById(d.getId());
        assertNotNull(fetchedDoctor);
        assertEquals(d.getId(), fetchedDoctor.getId());
        assertEquals(d.getName(), fetchedDoctor.getName());
        assertEquals(d.getSpecialty(), fetchedDoctor.getSpecialty());
        assertEquals(d.getUser(), fetchedDoctor.getUser());
        
       
    }
    
    /**
     * Test of getDoctorById method, of class JDBCDoctorManager.
     */
    @Test
    public void testgetDoctorByUser(){
        System.out.println("getDoctorByUser");
        User u= new User ("TempUser");
        Doctor d = new Doctor(1,"TempDoctor", "NEUROLOGY", u);
        doctorManager.createDoctor(d);
        Doctor fetchedDoctor = doctorManager.getDoctorByUser(u);
        assertNotNull(fetchedDoctor);
        assertEquals(d.getUser(), fetchedDoctor.getUser());
        assertEquals(d.getId(), fetchedDoctor.getId());
        assertEquals(d.getName(), fetchedDoctor.getName());
        assertEquals(d.getSpecialty(), fetchedDoctor.getSpecialty());
        
      }
    

    /**
     * Test of getDoctorByName method, of class JDBCDoctorManager.
     */
    @Test
    public void testGetDoctorByName() {
        System.out.println("getDoctorByName");
        User u= new User ("TempUser");
        Doctor d = new Doctor(1,"TempDoctor", "NEUROLOGY", u);
        doctorManager.createDoctor(d);
        List<Doctor> doctors= doctorManager.getDoctorByName("TempDoctor");
        assertNotNull(doctors);
        assertFalse(doctors.isEmpty());
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getName().equals("TempDoctor")));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getId().equals(1)));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getSpecialty().equals("NEUROLOGY")));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getUser().equals(u)));
    }
    
    //pero en create doctor no deberia haber register doctor
     /**
     * Test of getDoctorIds method, of class JDBCDoctorManager.
     */
    @Test
        public void testgetDoctorIds(){
        User u1= new User ("TempUser1");
        Doctor d1 = new Doctor(1,"TempDoctor1", "NEUROLOGY", u1);
        doctorManager.createDoctor(d1);
        User u2= new User ("TempUser2");
        Doctor d2 = new Doctor(2,"TempDoctor2", "NEUROLOGY", u2);
        doctorManager.createDoctor(d2);
        User u3= new User ("TempUser3");
        Doctor d3 = new Doctor(1,"TempDoctor3", "NEUROLOGY", u3);
        doctorManager.createDoctor(d3);
        List<Integer> id_doctors= doctorManager.getDoctorIds();
        assertNotNull(id_doctors);
        assertFalse(id_doctors.isEmpty());
        assertTrue(id_doctors.contains(1), "The list should contain the doctor ID 1");
        assertTrue(id_doctors.contains(2), "The list should contain the doctor ID 2");
        assertTrue(id_doctors.contains(3), "The list should contain the doctor ID 3");
        }    
    
}

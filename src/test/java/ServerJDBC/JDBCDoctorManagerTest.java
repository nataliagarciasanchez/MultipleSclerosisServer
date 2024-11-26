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
    private static User u1;
    private static User u2;
    
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
        u1 = new User ("email", "password", r);
        userManager.registerUser(u1);
        u2 = new User ("email", "password", r);
        userManager.registerUser(u2);
        
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
    public void testRegisterDoctor() {
        System.out.println("createDoctor");
        Doctor d = new Doctor("TempNameDoc", "TempSurnameDoc", u1);
        System.out.println(d.toString());
        doctorManager.registerDoctor(d);
        Doctor fetchedDoctor = doctorManager.getDoctorById(d.getId());
        System.out.println(fetchedDoctor.toString());
        assertNotNull(fetchedDoctor);
        assertEquals(d.getName(), fetchedDoctor.getName());
        assertEquals(d.getId(), fetchedDoctor.getId());
        assertEquals(d.getSurname(), fetchedDoctor.getSurname());
        
        
    }

    /**
     * Test of removeDoctorById method, of class JDBCDoctorManager.
     */
    @Test
    public void testRemoveDoctorById() {      
        System.out.println("DeleteDoctor");
        Doctor d = new Doctor ("TempDoctorName","TempDoctorSurname", u1);
        System.out.println(d.toString());
        doctorManager.registerDoctor(d);
        List<Doctor> DoctorsBefore = doctorManager.getListOfDoctors();
        assertEquals(1, DoctorsBefore.size());
        doctorManager.removeDoctorById(d.getId());
        List<Doctor> DoctorsAfter = doctorManager.getListOfDoctors();
        assertEquals(0, DoctorsAfter.size());
       
    }
    
    /**
     * Test of updateDoctor method, of class JDBCDoctorManager.
     */
    @Test
    public void testUpdateDoctor() {
        System.out.println("updateDoctor");
        Doctor d = new Doctor ("TempDoctorName","TempDoctorSurname", u1);
        System.out.println(d.toString());
        doctorManager.registerDoctor(d);
        d.setName("UpdatedDoctorName");
        d.setSurname("UpdatedDoctorSurname");
        doctorManager.updateDoctor(d);
        Doctor updatedDoctor = doctorManager.getDoctorById(d.getId());
        assertNotNull(updatedDoctor);
        assertEquals("UpdatedDoctorName", updatedDoctor.getName());
        assertEquals("UpdatedDoctorSurname", updatedDoctor.getSurname());
    }

    /**
     * Test of getListOfDoctors method, of class JDBCDoctorManager.
     */
    @Test
    public void testGetListOfDoctors() {
        System.out.println("getListOfDoctors");
        Doctor d1 = new Doctor ("Doctor1","Surname1",u1);
        Doctor d2 = new Doctor ("Doctor2","Surname2",u2);
        System.out.println(d1.toString());
        System.out.println(d2.toString());
        doctorManager.registerDoctor(d1);
        doctorManager.registerDoctor(d2);

        List<Doctor> doctors = doctorManager.getListOfDoctors();
        assertEquals(2, doctors.size());
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getId().equals(d1.getId())));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getId().equals(d2.getId())));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getName().equals(d1.getName())));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getName().equals(d2.getName())));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getSurname().equals(d1.getSurname())));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getSurname().equals(d2.getSurname())));
    }
    

    /**
     * Test of getDoctorById method, of class JDBCDoctorManager.
     */
    @Test
    public void testGetDoctorById() {
        System.out.println("getDoctorById");
        Doctor d = new Doctor ("Doctor1","NEUROLOGY",u1);
        System.out.println(d.toString());
        doctorManager.registerDoctor(d);
        Doctor fetchedDoctor = doctorManager.getDoctorById(d.getId());
        assertNotNull(fetchedDoctor);
        assertEquals(d.getId(), fetchedDoctor.getId());
        assertEquals(d.getName(), fetchedDoctor.getName());
        assertEquals(d.getSpecialty(), fetchedDoctor.getSpecialty());
        
        
       
    }
    
    /**
     * Test of getDoctorById method, of class JDBCDoctorManager.
     */
    @Test
    public void testgetDoctorByUser(){
        System.out.println("getDoctorByUser");
        Doctor d = new Doctor("TempDoctorName", "TempDoctorSurname", u1);
         System.out.println(d.toString());
        doctorManager.registerDoctor(d);
        Doctor fetchedDoctor = doctorManager.getDoctorByUser(u1);
        assertNotNull(fetchedDoctor);
        assertEquals(d.getId(), fetchedDoctor.getId());
        assertEquals(d.getName(), fetchedDoctor.getName());
        assertEquals(d.getSurname(), fetchedDoctor.getSurname());
        
      }
    

    /**
     * Test of getDoctorByName method, of class JDBCDoctorManager.
     */
    @Test
    public void testGetDoctorByName() {
        System.out.println("getDoctorByName");
        Doctor d = new Doctor("TempDoctor", "NEUROLOGY", u1);
        System.out.println(d.toString());
        doctorManager.registerDoctor(d);
        List<Doctor> doctors= doctorManager.getDoctorByName("TempDoctor");
        assertNotNull(doctors);
        assertFalse(doctors.isEmpty());
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getName().equals("TempDoctor")));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getId().equals(d.getId())));
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getSpecialty().equals("NEUROLOGY")));
    }
    
    //pero en create doctor no deberia haber register doctor
     /**
     * Test of getDoctorIds method, of class JDBCDoctorManager.
     */
    @Test
        public void testgetDoctorIds(){
        Doctor d1 = new Doctor("TempDoctor1", "NEUROLOGY", u1);
        doctorManager.registerDoctor(d1);
        System.out.println(d1.toString());
        Doctor d2 = new Doctor("TempDoctor2", "NEUROLOGY", u2);
        doctorManager.registerDoctor(d2);
        System.out.println(d2.toString());
        List<Integer> id_doctors= doctorManager.getDoctorIds();
        assertNotNull(id_doctors);
        assertFalse(id_doctors.isEmpty());
        assertTrue(id_doctors.contains(d1.getId()), "The list should contain the doctor ID");
        assertTrue(id_doctors.contains(d2.getId()), "The list should contain the doctor ID");
        
        }    
    

    
}

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
            jdbcManager.disconnect();
         }
    }
    
    @BeforeEach
    public void setUp() throws SQLException {
        // Limpiar la base de datos antes de cada prueba
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Doctor");
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        // Limpiar la base de datos después de cada prueba
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Doctor");
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
        System.out.println("RemoveDoctorById");
        Integer id = null;
        Doctor d = new Doctor ("TempDoctor");
        doctorManager.createDoctor(d);
      //TENGO QUE VERLO  d.removeDoctorById(d.getId());
        List<Doctor> DoctorsAfter = doctorManager.getListOfDoctors();
        assertEquals(0, DoctorsAfter.size());
       
    }
   /*  System.out.println("deleteRole");
        Role role = new Role("TempRole");
        roleManager.createRole(role);

        List<Role> rolesBefore = roleManager.getListOfRoles();
        assertEquals(1, rolesBefore.size());

        roleManager.removeRoleById(role.getId());

        List<Role> rolesAfter = roleManager.getListOfRoles();
        assertEquals(0, rolesAfter.size());*/
/*
    /**
     * Test of updateDoctor method, of class JDBCDoctorManager.
     */
    
    public void testUpdateDoctor() {
        System.out.println("updateDoctor");
        Doctor d = null;
        JDBCDoctorManager instance = null;
        instance.updateDoctor(d);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfDoctors method, of class JDBCDoctorManager.
     */
    @Test
    public void testGetListOfDoctors() {
        System.out.println("getListOfDoctors");
        JDBCDoctorManager instance = null;
        List<Doctor> expResult = null;
        List<Doctor> result = instance.getListOfDoctors();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDoctorById method, of class JDBCDoctorManager.
     */
    @Test
    public void testGetDoctorById() {
        System.out.println("getDoctorById");
        Integer id = null;
        JDBCDoctorManager instance = null;
        Doctor expResult = null;
        Doctor result = instance.getDoctorById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDoctorByName method, of class JDBCDoctorManager.
     */
    @Test
    public void testGetDoctorByName() {
        System.out.println("getDoctorByName");
        String name = "";
        JDBCDoctorManager instance = null;
        List<Doctor> expResult = null;
        List<Doctor> result = instance.getDoctorByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

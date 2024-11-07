/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Patient;
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
    
    public JDBCPatientManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createPatient method, of class JDBCPatientManager.
     */
    @Test
    public void testCreatePatient() {
        System.out.println("createPatient");
        Patient p = null;
        JDBCPatientManager instance = null;
        instance.createPatient(p);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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

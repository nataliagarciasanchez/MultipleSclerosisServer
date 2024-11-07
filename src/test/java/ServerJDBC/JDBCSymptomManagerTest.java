/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Symptom;
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
public class JDBCSymptomManagerTest {
    
    public JDBCSymptomManagerTest() {
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
     * Test of createSymptom method, of class JDBCSymptomManager.
     */
    @Test
    public void testCreateSymptom() {
        System.out.println("createSymptom");
        Symptom symptom = null;
        JDBCSymptomManager instance = null;
        instance.createSymptom(symptom);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeSymptom method, of class JDBCSymptomManager.
     */
    @Test
    public void testRemoveSymptom() {
        System.out.println("removeSymptom");
        Integer id = null;
        JDBCSymptomManager instance = null;
        instance.removeSymptom(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateSymptom method, of class JDBCSymptomManager.
     */
    @Test
    public void testUpdateSymptom() {
        System.out.println("updateSymptom");
        Symptom symptom = null;
        JDBCSymptomManager instance = null;
        instance.updateSymptom(symptom);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfSymptoms method, of class JDBCSymptomManager.
     */
    @Test
    public void testGetListOfSymptoms() {
        System.out.println("getListOfSymptoms");
        JDBCSymptomManager instance = null;
        List<Symptom> expResult = null;
        List<Symptom> result = instance.getListOfSymptoms();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSymptomById method, of class JDBCSymptomManager.
     */
    @Test
    public void testGetSymptomById() {
        System.out.println("getSymptomById");
        Integer id = null;
        JDBCSymptomManager instance = null;
        Symptom expResult = null;
        Symptom result = instance.getSymptomById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSymptomByName method, of class JDBCSymptomManager.
     */
    @Test
    public void testGetSymptomByName() {
        System.out.println("getSymptomByName");
        String name = "";
        JDBCSymptomManager instance = null;
        List<Symptom> expResult = null;
        List<Symptom> result = instance.getSymptomByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

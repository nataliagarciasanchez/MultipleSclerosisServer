/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Bitalino;
import java.sql.Date;
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
    
    public JDBCBitalinoManagerTest() {
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
     * Test of createBitalino method, of class JDBCBitalinoManager.
     */
    @Test
    public void testCreateBitalino() {
        System.out.println("createBitalino");
        Bitalino b = null;
        JDBCBitalinoManager instance = null;
        instance.createBitalino(b);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeBitalinoById method, of class JDBCBitalinoManager.
     */
    @Test
    public void testRemoveBitalinoById() {
        System.out.println("removeBitalinoById");
        Integer id = null;
        JDBCBitalinoManager instance = null;
        instance.removeBitalinoById(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateBitalino method, of class JDBCBitalinoManager.
     */
    @Test
    public void testUpdateBitalino() {
        System.out.println("updateBitalino");
        Bitalino b = null;
        JDBCBitalinoManager instance = null;
        instance.updateBitalino(b);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfBitalinos method, of class JDBCBitalinoManager.
     */
    @Test
    public void testGetListOfBitalinos() {
        System.out.println("getListOfBitalinos");
        JDBCBitalinoManager instance = null;
        List<Bitalino> expResult = null;
        List<Bitalino> result = instance.getListOfBitalinos();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBitalinoById method, of class JDBCBitalinoManager.
     */
    @Test
    public void testGetBitalinoById() {
        System.out.println("getBitalinoById");
        Integer id = null;
        JDBCBitalinoManager instance = null;
        Bitalino expResult = null;
        Bitalino result = instance.getBitalinoById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBitalinosByDate method, of class JDBCBitalinoManager.
     */
    @Test
    public void testGetBitalinosByDate() {
        System.out.println("getBitalinosByDate");
        Date d = null;
        JDBCBitalinoManager instance = null;
        List<Bitalino> expResult = null;
        List<Bitalino> result = instance.getBitalinosByDate(d);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBitalinosOfReport method, of class JDBCBitalinoManager.
     */
    @Test
    public void testGetBitalinosOfReport() {
        System.out.println("getBitalinosOfReport");
        Integer report_id = null;
        JDBCBitalinoManager instance = null;
        List<Bitalino> expResult = null;
        List<Bitalino> result = instance.getBitalinosOfReport(report_id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
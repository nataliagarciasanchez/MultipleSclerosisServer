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
public class JDBCReport_SymptomsManagerTest {
    
    public JDBCReport_SymptomsManagerTest() {
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
     * Test of addSymptomToReport method, of class JDBCReport_SymptomsManager.
     */
    @Test
    public void testAddSymptomToReport() {
        System.out.println("addSymptomToReport");
        Integer symptomId = null;
        Integer reportId = null;
        JDBCReport_SymptomsManager instance = null;
        instance.addSymptomToReport(symptomId, reportId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeSymptomFromReport method, of class JDBCReport_SymptomsManager.
     */
    @Test
    public void testRemoveSymptomFromReport() {
        System.out.println("removeSymptomFromReport");
        Integer symptomId = null;
        Integer reportId = null;
        JDBCReport_SymptomsManager instance = null;
        instance.removeSymptomFromReport(symptomId, reportId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of emptyReport method, of class JDBCReport_SymptomsManager.
     */
    @Test
    public void testEmptyReport() {
        System.out.println("emptyReport");
        Integer reportId = null;
        JDBCReport_SymptomsManager instance = null;
        instance.emptyReport(reportId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSymptomsFromReport method, of class JDBCReport_SymptomsManager.
     */
    @Test
    public void testGetSymptomsFromReport() {
        System.out.println("getSymptomsFromReport");
        Integer reportId = null;
        JDBCReport_SymptomsManager instance = null;
        List<Symptom> expResult = null;
        List<Symptom> result = instance.getSymptomsFromReport(reportId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

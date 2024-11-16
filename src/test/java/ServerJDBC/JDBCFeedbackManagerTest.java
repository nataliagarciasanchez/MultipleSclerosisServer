/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Feedback;
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
public class JDBCFeedbackManagerTest {
    
    private static JDBCFeedbackManager feedbackManager;
    private static JDBCManager jdbcManager;
    
    
    public JDBCFeedbackManagerTest() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        feedbackManager = new JDBCFeedbackManager(jdbcManager);
        assertNotNull(feedbackManager);
    }
    
    @BeforeAll
    public static void setUpClass() {
         if (jdbcManager != null) {
            jdbcManager.disconnect();
         }
    }
    
    @AfterAll
    public static void tearDownClass() throws SQLException {
        // Limpiar la base de datos antes de cada prueba
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Feedback");
    }
    
    @BeforeEach
    public void setUp() throws SQLException {
        // Limpiar la base de datos después de cada prueba
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Feedback");
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createFeedback method, of class JDBCFeedbackManager.
     */
    @Test
    public void testCreateFeedback() {
        System.out.println("createFeedback");
        Feedback f = null;
        JDBCFeedbackManager instance = null;
        instance.createFeedback(f);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeFeedbackById method, of class JDBCFeedbackManager.
     */
    @Test
    public void testRemoveFeedbackById() {
        System.out.println("removeFeedbackById");
        Integer id = null;
        JDBCFeedbackManager instance = null;
        instance.removeFeedbackById(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateFeedback method, of class JDBCFeedbackManager.
     */
    @Test
    public void testUpdateFeedback() {
        System.out.println("updateFeedback");
        Feedback f = null;
        JDBCFeedbackManager instance = null;
        instance.updateFeedback(f);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFeedBackById method, of class JDBCFeedbackManager.
     */
    @Test
    public void testGetFeedBackById() {
        System.out.println("getFeedBackById");
        Integer id = null;
        JDBCFeedbackManager instance = null;
        Feedback expResult = null;
        Feedback result = instance.getFeedBackById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFeedBackByDate method, of class JDBCFeedbackManager.
     */
    @Test
    public void testGetFeedBackByDate() {
        System.out.println("getFeedBackByDate");
        Date date = null;
        JDBCFeedbackManager instance = null;
        List<Feedback> expResult = null;
        List<Feedback> result = instance.getFeedBackByDate(date);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfFeedbacksOfPatient method, of class JDBCFeedbackManager.
     */
    @Test
    public void testGetListOfFeedbacksOfPatient() {
        System.out.println("getListOfFeedbacksOfPatient");
        Integer patient_id = null;
        JDBCFeedbackManager instance = null;
        List<Feedback> expResult = null;
        List<Feedback> result = instance.getListOfFeedbacksOfPatient(patient_id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfFeedbacksOfDoctor method, of class JDBCFeedbackManager.
     */
    @Test
    public void testGetListOfFeedbacksOfDoctor() {
        System.out.println("getListOfFeedbacksOfDoctor");
        Integer doctor_id = null;
        JDBCFeedbackManager instance = null;
        List<Feedback> expResult = null;
        List<Feedback> result = instance.getListOfFeedbacksOfDoctor(doctor_id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

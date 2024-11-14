/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Report;
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
public class JDBCReportManagerTest {
    
    private static JDBCReportManager reportManager;
    private static JDBCManager jdbcManager;
    
    public JDBCReportManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        reportManager = new JDBCReportManager(jdbcManager);
        assertNotNull(reportManager);
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
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Report");
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        // Limpiar la base de datos después de cada prueba
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Report");
    }

    /**
     * Test of createReport method, of class JDBCReportManager.
     */
    @Test
    public void testCreateReport() {
        System.out.println("createReport");
        Report r = null;
        JDBCReportManager instance = null;
        instance.createReport(r);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeReportById method, of class JDBCReportManager.
     */
    @Test
    public void testRemoveReportById() {
        System.out.println("removeReportById");
        Integer id = null;
        JDBCReportManager instance = null;
        instance.removeReportById(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateReport method, of class JDBCReportManager.
     */
    @Test
    public void testUpdateReport() {
        System.out.println("updateReport");
        Report r = null;
        JDBCReportManager instance = null;
        instance.updateReport(r);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfReports method, of class JDBCReportManager.
     */
    @Test
    public void testGetListOfReports() {
        System.out.println("getListOfReports");
        JDBCReportManager instance = null;
        List<Report> expResult = null;
        List<Report> result = instance.getListOfReports();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReportsFromPatient method, of class JDBCReportManager.
     */
    @Test
    public void testGetReportsFromPatient() {
        System.out.println("getReportsFromPatient");
        Integer patientId = null;
        JDBCReportManager instance = null;
        List<Report> expResult = null;
        List<Report> result = instance.getReportsFromPatient(patientId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReportById method, of class JDBCReportManager.
     */
    @Test
    public void testGetReportById() {
        System.out.println("getReportById");
        Integer id = null;
        JDBCReportManager instance = null;
        Report expResult = null;
        Report result = instance.getReportById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReportByDate method, of class JDBCReportManager.
     */
    @Test
    public void testGetReportByDate() {
        System.out.println("getReportByDate");
        Date date = null;
        JDBCReportManager instance = null;
        List<Report> expResult = null;
        List<Report> result = instance.getReportByDate(date);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Bitalino;
import POJOs.SignalType;
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
public class JDBCBitalinoManagerTest {
    
    private static JDBCBitalinoManager bitalinoManager;
    private static JDBCManager jdbcManager;
    
    public JDBCBitalinoManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        bitalinoManager = new JDBCBitalinoManager(jdbcManager);
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        assertNotNull(bitalinoManager);
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
     * Test of createBitalino method, of class JDBCBitalinoManager.
     */
    @Test
    public void testCreateBitalino() {
        System.out.println("createBitalino");
        //I dont know if i need one by default with establised parameters
       Bitalino b = new Bitalino (SignalType.EMG);
        System.out.println(b.toString());
        bitalinoManager.createBitalino(b);
        // Verificar si fue creado correctamente
        Bitalino fetchedBitalino = bitalinoManager.getBitalinoById(b.getId());
        assertNotNull(fetchedBitalino);
        assertEquals("EMG", fetchedBitalino.getSignal_type());
    }

    /**
     * Test of removeBitalinoById method, of class JDBCBitalinoManager.
     */
    @Test
    /*public void testRemoveBitalinoById() {
        System.out.println("RemoveBitallinoById");
        Integer id = null;
        Bitalino bitalino = new Bitalino ("TempBitalino");
        bitalinoManager.createDoctor(d);
      //TENGO QUE VERLO  d.removeDoctorById(d.getId());
        List<Doctor> DoctorsAfter = doctorManager.getListOfDoctors();
        assertEquals(0, DoctorsAfter.size());
        System.out.println("removeBitalinoById");
        Integer id = null;
        JDBCBitalinoManager instance = null;
        instance.removeBitalinoById(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }/*

    /**
     * Test of updateBitalino method, of class JDBCBitalinoManager.
     */
   // @Test
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Administrator;
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
public class JDBCAdministratorManagerTest {
    
    public JDBCAdministratorManagerTest() {
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
     * Test of createAdministrator method, of class JDBCAdministratorManager.
     */
    @Test
    public void testCreateAdministrator() {
        System.out.println("createAdministrator");
        Administrator a = null;
        JDBCAdministratorManager instance = null;
        instance.createAdministrator(a);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeAdministratorById method, of class JDBCAdministratorManager.
     */
    @Test
    public void testRemoveAdministratorById() {
        System.out.println("removeAdministratorById");
        Integer id = null;
        JDBCAdministratorManager instance = null;
        instance.removeAdministratorById(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateAdministrator method, of class JDBCAdministratorManager.
     */
    @Test
    public void testUpdateAdministrator() {
        System.out.println("updateAdministrator");
        Administrator a = null;
        JDBCAdministratorManager instance = null;
        instance.updateAdministrator(a);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfAdministrators method, of class JDBCAdministratorManager.
     */
    @Test
    public void testGetListOfAdministrators() {
        System.out.println("getListOfAdministrators");
        JDBCAdministratorManager instance = null;
        List<Administrator> expResult = null;
        List<Administrator> result = instance.getListOfAdministrators();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdministratorById method, of class JDBCAdministratorManager.
     */
    @Test
    public void testGetAdministratorById() {
        System.out.println("getAdministratorById");
        Integer id = null;
        JDBCAdministratorManager instance = null;
        Administrator expResult = null;
        Administrator result = instance.getAdministratorById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdministratorByName method, of class JDBCAdministratorManager.
     */
    @Test
    public void testGetAdministratorByName() {
        System.out.println("getAdministratorByName");
        String name = "";
        JDBCAdministratorManager instance = null;
        List<Administrator> expResult = null;
        List<Administrator> result = instance.getAdministratorByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

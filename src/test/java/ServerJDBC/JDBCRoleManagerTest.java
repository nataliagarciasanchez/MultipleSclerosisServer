/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Role;
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
public class JDBCRoleManagerTest {
    
    public JDBCRoleManagerTest() {
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
     * Test of createRole method, of class JDBCRoleManager.
     */
    @Test
    public void testCreateRole() {
        System.out.println("createRole");
        Role role = null;
        JDBCRoleManager instance = new JDBCRoleManager();
        instance.createRole(role);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeRoleById method, of class JDBCRoleManager.
     */
    @Test
    public void testRemoveRoleById() {
        System.out.println("removeRoleById");
        Integer id = null;
        JDBCRoleManager instance = new JDBCRoleManager();
        instance.removeRoleById(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateRole method, of class JDBCRoleManager.
     */
    @Test
    public void testUpdateRole() {
        System.out.println("updateRole");
        Role r = null;
        JDBCRoleManager instance = new JDBCRoleManager();
        instance.updateRole(r);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfRoles method, of class JDBCRoleManager.
     */
    @Test
    public void testGetListOfRoles() {
        System.out.println("getListOfRoles");
        JDBCRoleManager instance = new JDBCRoleManager();
        List<Role> expResult = null;
        List<Role> result = instance.getListOfRoles();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRoleById method, of class JDBCRoleManager.
     */
    @Test
    public void testGetRoleById() {
        System.out.println("getRoleById");
        Integer id = null;
        JDBCRoleManager instance = new JDBCRoleManager();
        Role expResult = null;
        Role result = instance.getRoleById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRoleByName method, of class JDBCRoleManager.
     */
    @Test
    public void testGetRoleByName() {
        System.out.println("getRoleByName");
        String roleName = "";
        JDBCRoleManager instance = new JDBCRoleManager();
        Role expResult = null;
        Role result = instance.getRoleByName(roleName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

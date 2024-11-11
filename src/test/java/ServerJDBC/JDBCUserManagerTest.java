/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Patient;
import POJOs.Role;
import POJOs.User;
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
public class JDBCUserManagerTest {
    
    public JDBCUserManagerTest() {
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
     * Test of register method, of class JDBCUserManager.
     */
    @Test
    public void testRegister() {
        System.out.println("register");
        User user = null;
        JDBCUserManager instance = new JDBCUserManager();
        instance.register(user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of login method, of class JDBCUserManager.
     */
    @Test
    public void testLogin() {
        System.out.println("login");
        String email = "";
        String password = "";
        JDBCUserManager instance = new JDBCUserManager();
        User expResult = null;
        User result = instance.login(email, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeUserById method, of class JDBCUserManager.
     */
    @Test
    public void testRemoveUserById() {
        System.out.println("removeUserById");
        Integer id = null;
        JDBCUserManager instance = new JDBCUserManager();
        instance.removeUserById(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateUser method, of class JDBCUserManager.
     */
    @Test
    public void testUpdateUser() {
        System.out.println("updateUser");
        User user = null;
        JDBCUserManager instance = new JDBCUserManager();
        instance.updateUser(user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListOfUsers method, of class JDBCUserManager.
     */
    @Test
    public void testGetListOfUsers() {
        System.out.println("getListOfUsers");
        JDBCUserManager instance = new JDBCUserManager();
        List<User> expResult = null;
        List<User> result = instance.getListOfUsers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserById method, of class JDBCUserManager.
     */
    @Test
    public void testGetUserById() {
        System.out.println("getUserById");
        Integer id = null;
        JDBCUserManager instance = new JDBCUserManager();
        User expResult = null;
        User result = instance.getUserById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserByEmail method, of class JDBCUserManager.
     */
    @Test
    public void testGetUserByEmail() {
        System.out.println("getUserByEmail");
        String email = "";
        JDBCUserManager instance = new JDBCUserManager();
        User expResult = null;
        User result = instance.getUserByEmail(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of assignRole2User method, of class JDBCUserManager.
     */
    @Test
    public void testAssignRole2User() {
        System.out.println("assignRole2User");
        User user = null;
        Role role = null;
        JDBCUserManager instance = new JDBCUserManager();
        instance.assignRole2User(user, role);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkPassword method, of class JDBCUserManager.
     */
    @Test
    public void testCheckPassword() {
        System.out.println("checkPassword");
        User u = null;
        JDBCUserManager instance = new JDBCUserManager();
        User expResult = null;
        User result = instance.checkPassword(u);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of changePassword method, of class JDBCUserManager.
     */
    @Test
    public void testChangePassword() {
        System.out.println("changePassword");
        User user = null;
        String new_password = "";
        JDBCUserManager instance = new JDBCUserManager();
        instance.changePassword(user, new_password);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsersByRole method, of class JDBCUserManager.
     */
    @Test
    public void testGetUsersByRole() {
        System.out.println("getUsersByRole");
        Integer role_id = null;
        JDBCUserManager instance = new JDBCUserManager();
        List<User> expResult = null;
        List<User> result = instance.getUsersByRole(role_id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPatientByUser method, of class JDBCUserManager.
     */
    @Test
    public void testGetPatientByUser() {
        System.out.println("getPatientByUser");
        User user = null;
        JDBCUserManager instance = new JDBCUserManager();
        Patient expResult = null;
        Patient result = instance.getPatientByUser(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

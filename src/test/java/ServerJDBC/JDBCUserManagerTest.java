/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Patient;
import POJOs.Role;
import POJOs.User;
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
public class JDBCUserManagerTest {
    private static JDBCUserManager userManager;
    private static JDBCManager jdbcManager;
    
    public JDBCUserManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        userManager = new JDBCUserManager(jdbcManager);
        assertNotNull(userManager);
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
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Users");
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        // Limpiar la base de datos después de cada prueba
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Users");
    }

    /**
     * Test of register method, of class JDBCUserManager.
     */
    @Test
    public void testRegister() {
        System.out.println("registerUser");
        Role role = new Role(1, "Doctor"); // Crear un rol ficticio
        User user = new User("test@example.com", "password123", role);
        userManager.registerUser(user);

        User fetchedUser = userManager.getUserById(user.getId());
        assertNotNull(fetchedUser, "El usuario debería haberse registrado correctamente.");
        assertEquals("test@example.com", fetchedUser.getEmail());
    }

    /**
     * Test of login method, of class JDBCUserManager.
     */
    @Test
    public void testLogin() {
        System.out.println("login");
        Role role = new Role(1, "Doctor");
        User user = new User("login@example.com", "password123", role);
        userManager.registerUser(user);

        User loggedInUser = userManager.login("login@example.com", "password123");
        assertNotNull(loggedInUser, "El usuario debería haber iniciado sesión correctamente.");
        assertEquals(user.getEmail(), loggedInUser.getEmail());
    }

    /**
     * Test of removeUserById method.
     */
    @Test
    public void testRemoveUserById() {
        System.out.println("removeUserById");
        Role role = new Role(1, "Doctor");
        User user = new User("delete@example.com", "password123", role);
        userManager.registerUser(user);

        userManager.removeUserById(user.getId());
        User deletedUser = userManager.getUserById(user.getId());
        assertNull(deletedUser, "El usuario debería haberse eliminado correctamente.");
    }

    /**
     * Test of updateUser method, of class JDBCUserManager.
     */
    @Test
    public void testUpdateUser() {
        System.out.println("updateUser");
        Role role = new Role(1, "Doctor");
        User user = new User("update@example.com", "password123", role);
        userManager.registerUser(user);

        user.setEmail("updated@example.com");
        user.setPassword("newpassword");
        userManager.updateUser(user);

        User updatedUser = userManager.getUserById(user.getId());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("newpassword", updatedUser.getPassword());
    }

    /**
     * Test of getListOfUsers method, of class JDBCUserManager.
     */
    @Test
    public void testGetListOfUsers() {
        System.out.println("getListOfUsers");
        Role role = new Role(1, "Doctor");
        userManager.registerUser(new User("user1@example.com", "password1", role));
        userManager.registerUser(new User("user2@example.com", "password2", role));

        List<User> users = userManager.getListOfUsers();
        assertEquals(2, users.size(), "Debería haber exactamente 2 usuarios en la base de datos.");
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

    
    
}

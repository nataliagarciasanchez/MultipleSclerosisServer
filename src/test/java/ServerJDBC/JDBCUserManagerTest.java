/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Patient;
import POJOs.Role;
import POJOs.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private static JDBCRoleManager roleManager;
    private static Role r;

    public JDBCUserManagerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        userManager = new JDBCUserManager(jdbcManager);
        roleManager = new JDBCRoleManager(jdbcManager);
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        assertNotNull(userManager);
        assertNotNull(roleManager);

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
        jdbcManager.clearAllTables();
        r = new Role("Doctor"); // role ficticio
        roleManager.createRole(r);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (jdbcManager != null) {
            // Deshace todos los cambios realizados durante la prueba
            jdbcManager.getConnection().rollback();
        }
    }

    /**
     * Test of register method, of class JDBCUserManager.
     */
    @Test
    public void testRegister() {
        System.out.println("registerUser");
        User user = new User("test@example.com", "password123", r);
        userManager.registerUser(user);

        User fetchedUser = userManager.getUserById(user.getId());
        assertNotNull(fetchedUser, "El usuario debería haberse registrado correctamente.");
        assertEquals(user.getId(), fetchedUser.getId());
        assertEquals(user.getEmail(), fetchedUser.getEmail());
        assertEquals(user.getPassword(), fetchedUser.getPassword()); // comprobamos los atributos
    }

    /**
     * Test of login method, of class JDBCUserManager.
     */
    @Test
    public void testLogin() {
        System.out.println("login");

        User user = new User("login@example.com", "password123", r);
        userManager.registerUser(user);

        User loggedInUser = userManager.login("login@example.com", "password123");
        assertNotNull(loggedInUser, "El usuario debería haber iniciado sesión correctamente.");
        assertEquals(user.getId(), loggedInUser.getId());
        assertEquals(user.getEmail(), loggedInUser.getEmail());
        assertEquals(user.getPassword(), loggedInUser.getPassword()); // comprobamos los atributos
    }

    /**
     * Test of removeUserById method.
     */
    @Test
    public void testRemoveUserById() {
        System.out.println("removeUserById");

        User user = new User("delete@example.com", "password123", r);
        userManager.registerUser(user);

        List<User> usersBefore = userManager.getListOfUsers();
        assertEquals(1, usersBefore.size(), "Debería haber exactamente 1 usuario antes de eliminar.");

        userManager.removeUserById(user.getId()); // Eliminar usuario

        List<User> usersAfter = userManager.getListOfUsers();
        assertEquals(0, usersAfter.size(), "Debería haber 0 usuarios después de eliminar.");
    }

    /**
     * Test of updateUser method, of class JDBCUserManager.
     */
    @Test
    public void testUpdateUser() {
        System.out.println("updateUser");
        User user = new User("update@example.com", "password123", r);
        userManager.registerUser(user);

        user.setEmail("updated@example.com");
        user.setPassword("newpassword");
        userManager.updateUser(user);

        User updatedUser = userManager.getUserById(user.getId());
        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(user.getEmail(), updatedUser.getEmail());
        assertEquals(user.getPassword(), updatedUser.getPassword());
    }

    /**
     * Test of getListOfUsers method, of class JDBCUserManager.
     */
    @Test
    public void testGetListOfUsers() {
        System.out.println("getListOfUsers");
        User u1 = new User("user1@example.com", "password1", r);
        User u2 = new User("user2@example.com", "password2", r);

        userManager.registerUser(u1);
        userManager.registerUser(u2);

        List<User> users = userManager.getListOfUsers();
        assertEquals(2, users.size(), "Debería haber exactamente 2 usuarios en la base de datos.");
        assertTrue(users.stream().anyMatch(user -> user.getId().equals(u1.getId())));
        assertTrue(users.stream().anyMatch(user -> user.getId().equals(u2.getId())));

    }

    /**
     * Test of getUserById method, of class JDBCUserManager.
     */
    @Test
    public void testGetUserById() {
        System.out.println("getUserById");
        Role role = new Role(1, "Doctor");
        User user = new User("getbyid@example.com", "password123", role);
        userManager.registerUser(user);

        User fetchedUser = userManager.getUserById(user.getId());
        assertNotNull(fetchedUser);
        assertEquals(user.getId(), fetchedUser.getId());
        assertEquals(user.getEmail(), fetchedUser.getEmail());
        assertEquals(user.getPassword(), fetchedUser.getPassword());
    }

    /**
     * Test of getUserByEmail method, of class JDBCUserManager.
     */
    @Test
    public void testGetUserByEmail() {
        System.out.println("getUserByEmail");

        User user = new User("getbyemail@example.com", "password123", r);
        userManager.registerUser(user);

        User fetchedUser = userManager.getUserByEmail("getbyemail@example.com");
        assertNotNull(fetchedUser);
        assertEquals(user.getId(), fetchedUser.getId());
        assertEquals(user.getEmail(), fetchedUser.getEmail());
        assertEquals(user.getPassword(), fetchedUser.getPassword());
    }

    /**
     * Test of getUsersByRole method, of class JDBCUserManager.
     */
    @Test
    public void testGetUsersByRole() {
        System.out.println("getUsersByRole");

        User u1 = new User("user1@example.com", "password1", r);
        User u2 = new User("user2@example.com", "password2", r);

        userManager.registerUser(u1);
        userManager.registerUser(u2);

        List<User> users = userManager.getUsersByRole(r.getId());
        assertEquals(2, users.size(), "Debería haber exactamente 2 usuarios con el rol especificado.");
        assertTrue(users.stream().anyMatch(user -> user.getId().equals(u1.getId())));
        assertTrue(users.stream().anyMatch(user -> user.getId().equals(u2.getId())));
    }

}

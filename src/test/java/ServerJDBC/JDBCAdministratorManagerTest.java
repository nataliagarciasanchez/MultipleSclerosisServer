/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Administrator;
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
public class JDBCAdministratorManagerTest {

    private static JDBCAdministratorManager adminManager;
    private static JDBCUserManager userManager;
    private static JDBCManager jdbcManager;

    public JDBCAdministratorManagerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Conectar la base de datos
        userManager = new JDBCUserManager(jdbcManager);
        adminManager = new JDBCAdministratorManager(jdbcManager);
        assertNotNull(adminManager);
    }

    @AfterAll
    public static void tearDownClass() {
        if (jdbcManager != null) {
            jdbcManager.disconnect();
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        // Limpiar tablas antes de cada prueba
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Administrators");
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Users");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Limpiar tablas después de cada prueba
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Administrators");
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Users");
    }

    /**
     * Test of createAdministrator method, of class JDBCAdministratorManager.
     */
    @Test
    public void testCreateAdministrator() {
        System.out.println("createAdministrator");

        // Crear un usuario asociado al administrador
        Role role = new Role(1, "Admin"); // Supongamos que el rol Admin ya existe en la base de datos
        User user = new User("admin@example.com", "password123", role);

        // Registrar el usuario en la base de datos
        userManager.registerUser(user);
        assertNotNull(user.getId(), "El ID del usuario no debería ser null después del registro.");

        // Crear el administrador asociado al usuario
        Administrator admin = new Administrator("AdminName", user);

        // Crear el administrador en la base de datos
        adminManager.createAdministrator(admin);

        // Verificar que el administrador fue creado
        assertNotNull(admin.getId(), "El ID del administrador no debería ser null después de la creación.");

        // Recuperar el administrador desde la base de datos
        Administrator fetchedAdmin = adminManager.getAdministratorById(admin.getId());
        assertNotNull(fetchedAdmin, "El administrador debería haberse creado y recuperado correctamente.");
        assertEquals("AdminName", fetchedAdmin.getName(), "El nombre del administrador debería coincidir.");
        assertEquals(user.getId(), fetchedAdmin.getUser().getId(), "El ID del usuario asociado debería coincidir.");
    }

    /**
     * Test of removeAdministratorById method, of class
     * JDBCAdministratorManager.
     */
    @Test
    public void testRemoveAdministratorById() {
        System.out.println("removeAdministratorById");

        // Crear usuario
        User user = new User("admin@example.com", "password123", new Role(1, "Admin"));
        userManager.registerUser(user);

        // Crear administrador
        Administrator admin = new Administrator("AdminToRemove", user);
        adminManager.createAdministrator(admin);

        // Eliminar administrador
        adminManager.removeAdministratorById(admin.getId());

        // Verificar que el administrador fue eliminado
        Administrator fetchedAdmin = adminManager.getAdministratorById(admin.getId());
        assertNull(fetchedAdmin, "El administrador debería haberse eliminado.");
    }

    /**
     * Test of updateAdministrator method, of class JDBCAdministratorManager.
     */
    @Test
    public void testUpdateAdministrator() {
        System.out.println("updateAdministrator");

        // Crear usuario
        User user = new User("admin@example.com", "password123", new Role(1, "Admin"));
        userManager.registerUser(user);

        // Crear administrador
        Administrator admin = new Administrator("OldName", user);
        adminManager.createAdministrator(admin);

        // Actualizar nombre del administrador
        admin.setName("NewName");
        adminManager.updateAdministrator(admin);

        // Verificar que el nombre fue actualizado
        Administrator updatedAdmin = adminManager.getAdministratorById(admin.getId());
        assertEquals("NewName", updatedAdmin.getName(), "El nombre del administrador debería haberse actualizado.");
    }

    /**
     * Test of getListOfAdministrators method, of class
     * JDBCAdministratorManager.
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

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

        // Crear usuarios y administradores
        User user1 = new User("admin1@example.com", "password1", new Role(1, "Admin"));
        User user2 = new User("admin2@example.com", "password2", new Role(1, "Admin"));
        userManager.registerUser(user1);
        userManager.registerUser(user2);

        adminManager.createAdministrator(new Administrator("Admin1", user1));
        adminManager.createAdministrator(new Administrator("Admin2", user2));

        // Obtener lista de administradores
        List<Administrator> admins = adminManager.getListOfAdministrators();
        assertEquals(2, admins.size(), "Debería haber exactamente 2 administradores.");
    }

    /**
     * Test of getAdministratorById method, of class JDBCAdministratorManager.
     */
    @Test
    public void testGetAdministratorById() {
         System.out.println("getAdministratorById");

        // Crear usuario
        User user = new User("admin@example.com", "password123", new Role(1, "Admin"));
        userManager.registerUser(user);

        // Crear administrador
        Administrator admin = new Administrator("AdminName", user);
        adminManager.createAdministrator(admin);

        // Obtener administrador por ID
        Administrator fetchedAdmin = adminManager.getAdministratorById(admin.getId());
        assertNotNull(fetchedAdmin, "El administrador debería existir.");
        assertEquals(admin.getName(), fetchedAdmin.getName(), "El nombre del administrador debería coincidir.");
    }

    /**
     * Test of getAdministratorByName method, of class JDBCAdministratorManager.
     */
    @Test
    public void testGetAdministratorByName() {
         System.out.println("getAdministratorByName");

        // Crear usuario
        User user = new User("admin@example.com", "password123", new Role(1, "Admin"));
        userManager.registerUser(user);

        // Crear administrador
        Administrator admin = new Administrator("AdminName", user);
        adminManager.createAdministrator(admin);

        // Buscar administrador por nombre
        List<Administrator> admins = adminManager.getAdministratorByName("AdminName");
        assertEquals(1, admins.size(), "Debería haber exactamente 1 administrador con ese nombre.");
        assertEquals(admin.getName(), admins.get(0).getName(), "El nombre del administrador debería coincidir.");
    }

}

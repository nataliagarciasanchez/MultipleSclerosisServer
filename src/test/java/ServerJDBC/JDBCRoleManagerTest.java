/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Role;
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
public class JDBCRoleManagerTest {
    
    private static JDBCRoleManager roleManager;
    private static JDBCManager jdbcManager;
    
    public JDBCRoleManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        // Inicializar JDBCManager y JDBCRoleManager antes de ejecutar las pruebas
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        roleManager = new JDBCRoleManager(jdbcManager);
        assertNotNull(roleManager);
        
    }
    
    @AfterAll
    public static void tearDownClass() {
        // Cerrar la conexión y limpiar los recursos después de cada prueba
        if (jdbcManager != null) {
            jdbcManager.disconnect();
        }
        
    }
    
    @BeforeEach
    public void setUp() throws SQLException {
        // Limpiar la base de datos antes de cada prueba
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Roles");
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        // Limpiar la base de datos después de cada prueba
        jdbcManager.getConnection().createStatement().execute("DELETE FROM Roles");
    }

    /**
     * Test of createRole method, of class JDBCRoleManager.
     */
    @Test
    public void testCreateRole() {
        System.out.println("createRole");
        Role role = new Role("Admin");
        System.out.println(role.toString());
        
        // Crear el rol
        roleManager.createRole(role);

        // Verificar si el rol fue creado correctamente
        Role fetchedRole = roleManager.getRoleById(role.getId());
        System.out.println(fetchedRole.toString());
        assertNotNull(fetchedRole);
        assertEquals("Admin", fetchedRole.getName());
        
    }

    /**
     * Test of removeRoleById method, of class JDBCRoleManager.
     */
    @Test
    public void testRemoveRoleById() {
        System.out.println("deleteRole");
        Role role = new Role("TempRole");
        roleManager.createRole(role);

        List<Role> rolesBefore = roleManager.getListOfRoles();
        assertEquals(1, rolesBefore.size());

        roleManager.removeRoleById(role.getId());

        List<Role> rolesAfter = roleManager.getListOfRoles();
        assertEquals(0, rolesAfter.size());
    }

    /**
     * Test of updateRole method, of class JDBCRoleManager.
     */
    @Test
    public void testUpdateRole() {
        System.out.println("updateRole");
        // Crear un rol para actualizar
        Role role = new Role("User");
        roleManager.createRole(role);

        // Actualizar el nombre del rol
        role.setName("UpdatedUser");
        roleManager.updateRole(role);

        // Verificar que el rol fue actualizado
        Role updatedRole = roleManager.getRoleById(role.getId());
        assertNotNull(updatedRole);
        assertEquals("UpdatedUser", updatedRole.getName());
    }

    /**
     * Test of getListOfRoles method, of class JDBCRoleManager.
     */
    @Test
    public void testGetListOfRoles() {
        System.out.println("getListOfRoles");
        roleManager.createRole(new Role("User"));
        roleManager.createRole(new Role("Moderator"));

        List<Role> roles = roleManager.getListOfRoles();
        assertEquals(2, roles.size());
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals("User")));
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals("Moderator")));
    }

    /**
     * Test of getRoleById method, of class JDBCRoleManager.
     */
    @Test
    public void testGetRoleById() {
        System.out.println("getRoleById");
        // Crear un rol
        Role role = new Role("Admin");
        roleManager.createRole(role);

        // Obtener el rol por su ID
        Role fetchedRole = roleManager.getRoleById(role.getId());

        // Verificar que el rol obtenido sea el mismo
        assertNotNull(fetchedRole);
        assertEquals("Admin", fetchedRole.getName());
        assertEquals(role.getId(), fetchedRole.getId());
    }

    /**
     * Test of getRoleByName method, of class JDBCRoleManager.
     */
    @Test
    public void testGetRoleByName() {
        System.out.println("getRoleByName");
        // Crear un rol
        Role role = new Role("Moderator");
        roleManager.createRole(role);
        System.out.println(role.toString());
        // Obtener el rol por su nombre
        Role fetchedRole = roleManager.getRoleByName("Moderator");
        System.out.println(fetchedRole.toString());
        // Verificar que el rol obtenido sea el correcto
        assertNotNull(fetchedRole);
        assertEquals("Moderator", fetchedRole.getName());
    }
    
}

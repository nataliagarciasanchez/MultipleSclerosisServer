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
        
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        
        assertNotNull(roleManager);
        
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
            jdbcManager.clearAllTables();
        // Deshace todos los cambios realizados durante la prueba
        jdbcManager.getConnection().rollback();
        }
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
        assertEquals(role.getId(), fetchedRole.getId());
        assertEquals(role.getName(), fetchedRole.getName());
        
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
        assertEquals(1, rolesBefore.size()); //comprobamos que hay 1 role

        roleManager.removeRoleById(role.getId());

        List<Role> rolesAfter = roleManager.getListOfRoles();
        assertEquals(0, rolesAfter.size()); //comprobamos que hay 0 roles
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
        assertEquals(role.getId(), updatedRole.getId());
        assertEquals(role.getName(), updatedRole.getName());
    }

    /**
     * Test of getListOfRoles method, of class JDBCRoleManager.
     */
    @Test
    public void testGetListOfRoles() {
        System.out.println("getListOfRoles");
        Role r1 = new Role ("Doctor");
        Role r2 = new Role ("Patient");
        roleManager.createRole(r1);
        roleManager.createRole(r2);

        List<Role> roles = roleManager.getListOfRoles();
        System.out.println(roles.toString());
        assertEquals(2, roles.size());
        assertTrue(roles.stream().anyMatch(role -> role.getId().equals(r1.getId())));
        assertTrue(roles.stream().anyMatch(role -> role.getId().equals(r2.getId())));
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals(r1.getName())));
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals(r2.getName())));
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
        assertEquals(role.getId(), fetchedRole.getId());
        assertEquals(role.getName(), fetchedRole.getName());
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
        assertEquals(role.getName(), fetchedRole.getName());
        assertEquals(role.getId(), fetchedRole.getId());
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class JDBCManagerTest {
    
    private static JDBCManager jdbcManager;
    
    public JDBCManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        // Inicializar JDBCManager
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Conectar a la base de datos
        assertNotNull(jdbcManager.getConnection(), "Conexión inicializada.");
        System.out.println("Base de datos conectada correctamente.");
    }
    
    @AfterAll
    public static void tearDownClass() {
        if (jdbcManager != null) {
            jdbcManager.disconnect(); // Cerrar la conexión
            System.out.println("Conexión cerrada correctamente.");
        }
    }
    
    @BeforeEach
    public void setUp() {
        // Limpiar todas las tablas antes de cada prueba
        jdbcManager.clearAllTables();
    }
    
    @AfterEach
    public void tearDown() {
        //No necesario rollback
    }

    /**
     * Test of getConnection method, of class JDBCManager.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        Connection connection = jdbcManager.getConnection();
        assertNotNull(connection, "La conexión debería estar inicializada.");
        try {
            assertFalse(connection.isClosed(), "La conexión debería estar abierta.");
        } catch (SQLException e) {
            fail("Error al verificar el estado de la conexión: " + e.getMessage());
        }
        
    }

    /**
     * Test of disconnect method, of class JDBCManager.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        JDBCManager instance = new JDBCManager();
        
        Connection connection = instance.getConnection();
        //Verify connected before disconnect
        try {
            
            assertNotNull(connection, "La conexión debería estar inicializada.");
            assertFalse(connection.isClosed(), "La conexión debería estar abierta antes de desconectar.");
        } catch (SQLException e) {
            fail("Error al verificar el estado de la conexión antes de desconectar: " + e.getMessage());
        }
        
        instance.disconnect();
        
        //Verify disconnected
        try {
            assertTrue(connection.isClosed(), "La conexión debería estar cerrada.");
        } catch (SQLException e) {
            fail("Error al verificar si la conexión está cerrada: " + e.getMessage());
        }
    }
    
     /**
     * Test del método createTables de la clase JDBCManager.
     */
    @Test
    public void testCreateTables() {
        System.out.println("createTables");

        try (Statement stmt = jdbcManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'")) {

            boolean hasRoles = false, hasUsers = false, hasDoctors = false;

            while (rs.next()) {
                String tableName = rs.getString("name");
                if ("Roles".equalsIgnoreCase(tableName)) hasRoles = true;
                if ("Users".equalsIgnoreCase(tableName)) hasUsers = true;
                if ("Doctors".equalsIgnoreCase(tableName)) hasDoctors = true;
            }

            assertTrue(hasRoles, "La tabla Roles debería existir.");
            assertTrue(hasUsers, "La tabla Users debería existir.");
            assertTrue(hasDoctors, "La tabla Doctors debería existir.");

        } catch (SQLException e) {
            fail("Error al verificar las tablas: " + e.getMessage());
        }
    }

    /**
     * Test del método insertRoles de la clase JDBCManager.
     */
    @Test
    public void testInsertRoles() {
        System.out.println("insertRoles");

        jdbcManager.insertRoles(); // Insertar roles predefinidos

        try (Statement stmt = jdbcManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Roles")) {

            int count = 0;
            while (rs.next()) {
                count++;
                String roleName = rs.getString("name");
                assertTrue(roleName.equals("patient") || roleName.equals("doctor"),
                           "El rol debería ser 'patient' o 'doctor'.");
            }

            assertEquals(2, count, "Debería haber exactamente 2 roles insertados.");

        } catch (SQLException e) {
            fail("Error al verificar los roles: " + e.getMessage());
        }
    }

    /**
     * Test del método clearAllTables de la clase JDBCManager.
     */
    @Test
    public void testClearAllTables() {
        System.out.println("clearAllTables");

        // Insertar roles y doctores para probar la limpieza
        jdbcManager.insertRoles();
        jdbcManager.insertDoctor();

        // Limpiar las tablas
        jdbcManager.clearAllTables();

        try (Statement stmt = jdbcManager.getConnection().createStatement();
             ResultSet rsRoles = stmt.executeQuery("SELECT COUNT(*) AS count FROM Roles");
             ResultSet rsDoctors = stmt.executeQuery("SELECT COUNT(*) AS count FROM Doctors")) {

            rsRoles.next();
            int rolesCount = rsRoles.getInt("count");
            assertEquals(0, rolesCount, "La tabla Roles debería estar vacía.");

            rsDoctors.next();
            int doctorsCount = rsDoctors.getInt("count");
            assertEquals(0, doctorsCount, "La tabla Doctors debería estar vacía.");

        } catch (SQLException e) {
            fail("Error al verificar el estado de las tablas: " + e.getMessage());
        }
    }
}

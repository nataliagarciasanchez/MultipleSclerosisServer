/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
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
            assertTrue(connection.isClosed(), "Connection should be closed.");
        } catch (SQLException e) {
            fail("Error while verifying if connection is closed: " + e.getMessage());
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

            boolean hasRoles = false, hasUsers = false, hasPatients = false,  hasDoctors = false;
            boolean hasAdministrators = false, hasReports = false, hasSymptoms = false;
            boolean hasBitalinos = false, hasReport_Bitalinos = false, hasFeedbacks = false;

            while (rs.next()) {
                String tableName = rs.getString("name");
                if ("Roles".equalsIgnoreCase(tableName)) hasRoles = true;
                if ("Users".equalsIgnoreCase(tableName)) hasUsers = true;
                if ("Patients".equalsIgnoreCase(tableName)) hasPatients = true;
                if ("Doctors".equalsIgnoreCase(tableName)) hasDoctors = true;
                if ("Administrators".equalsIgnoreCase(tableName)) hasAdministrators = true;
                if ("Reports".equalsIgnoreCase(tableName)) hasReports = true;
                if ("Symptoms".equalsIgnoreCase(tableName)) hasSymptoms = true;
                if ("Bitalinos".equalsIgnoreCase(tableName)) hasBitalinos = true;
                if ("Report_Symptoms".equalsIgnoreCase(tableName)) hasReport_Bitalinos = true;
                if ("Feedbacks".equalsIgnoreCase(tableName)) hasFeedbacks = true;
            }

            assertTrue(hasRoles, "The table Roles should exist.");
            assertTrue(hasUsers, "The table Users should exist.");
            assertTrue(hasPatients, "The table Patients should exist.");
            assertTrue(hasDoctors, "The table Doctors should exist.");
            assertTrue(hasAdministrators, "The table Administrators should exist.");
            assertTrue(hasReports, "The table Reports should exist.");
            assertTrue(hasSymptoms, "The table Symptoms should exist.");
            assertTrue(hasBitalinos, "The table Bitalinos should exist.");
            assertTrue(hasReport_Bitalinos, "The table Report_Symptoms should exist.");
            assertTrue(hasFeedbacks, "The table Feedbacks should exist.");

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
                assertTrue(roleName.equals("patient") || roleName.equals("doctor") || roleName.equals("administrator"),
                           "Role should be 'administor', 'doctor' or 'patient'," );
            }

            assertEquals(3, count, "Debería haber exactamente 2 roles insertados.");

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
        jdbcManager.insertAdministrator();
        jdbcManager.insertSymptoms();

        // Limpiar las tablas
        jdbcManager.clearAllTables();

        // Lista de tablas a verificar
        List<String> tables = Arrays.asList("Roles", "Users", "Doctors", "Patients", "Administrators", "Reports", "Symptoms", "Bitalinos", "Report_Symptoms", "Feedbacks");

        try ( Connection connection = jdbcManager.getConnection();  Statement stmt = connection.createStatement()) {

            // Iterar sobre cada tabla y verificar que esté vacía
            for (String table : tables) {
                String query = "SELECT COUNT(*) AS count FROM " + table;
                try ( ResultSet rs = stmt.executeQuery(query)) {
                    rs.next();
                    int count = rs.getInt("count");
                    assertEquals(0, count, "The " + table + " table should be empty after clearing.");
                }
            }
        } catch (SQLException e) {
            fail("Error while verifying the state of the tables: " + e.getMessage());
        }
    }

}

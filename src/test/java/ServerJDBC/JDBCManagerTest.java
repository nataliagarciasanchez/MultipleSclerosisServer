/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import java.sql.Connection;
import java.sql.SQLException;
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
}

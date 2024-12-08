/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author laura
 */
public class JDBCFilesManagerTest {

    private static JDBCFilesManager filesManager;
    private static JDBCManager jdbcManager;
    private static JDBCBitalinoManager bitalinoManager;
    private static JDBCPatientManager patientManager;
    private static JDBCReportManager reportManager;
    private static JDBCDoctorManager doctorManager;
    private static JDBCRoleManager roleManager;
    private static JDBCUserManager userManager;
    private static Patient testPatient;
    private static Report testReport;
    private static Bitalino bitalinoEMG;
    private static Bitalino bitalinoECG;

    

    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect();
        filesManager = new JDBCFilesManager(jdbcManager);
        bitalinoManager = new JDBCBitalinoManager(jdbcManager);
        patientManager = new JDBCPatientManager(jdbcManager);
        reportManager = new JDBCReportManager(jdbcManager);

        try {
            // Desactivar auto-commit para realizar rollback después de las pruebas
            jdbcManager.getConnection().setAutoCommit(false);

            // Crear datos de prueba
            testPatient = new Patient("TestPatient", "Lastname", "12345678A",
                    java.sql.Date.valueOf("2000-01-01"),
                    Gender.FEMALE, "123456789", null, null);
            patientManager.registerPatient(testPatient);

            testReport = new Report(java.sql.Date.valueOf("2024-12-08"), testPatient);
            reportManager.createReport(testReport);

            bitalinoEMG = new Bitalino(java.sql.Date.valueOf("2024-12-08"), SignalType.EMG, "EMG_Signal_Data", testReport);
            bitalinoECG = new Bitalino(java.sql.Date.valueOf("2024-12-08"), SignalType.ECG, "ECG_Signal_Data", testReport);

            bitalinoManager.saveBitalino(bitalinoEMG);
            bitalinoManager.saveBitalino(bitalinoECG);

        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar los datos de prueba.");
        }
    }

    @AfterAll
    public static void tearDownClass() {
        if (jdbcManager != null) {
            try {
                jdbcManager.getConnection().setAutoCommit(true);
                jdbcManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        jdbcManager.clearAllTables();

        // Crear roles
        Role patientRole = new Role("Patient");
        roleManager.createRole(patientRole);

        Role doctorRole = new Role("Doctor");
        roleManager.createRole(doctorRole);

        // Crear usuarios
        User doctorUser = new User("doctor@test.com", "password123", doctorRole);
        userManager.registerUser(doctorUser);

        User patientUser = new User("patient@test.com", "password123", patientRole);
        userManager.registerUser(patientUser);

        // Crear doctor
        Doctor doctor = new Doctor("Dr. Marcos", "Neurology", doctorUser);
        doctorManager.registerDoctor(doctor);

        // Crear paciente
        Date dob = java.sql.Date.valueOf("1990-01-01");
        testPatient = new Patient("Laura", "Gallego", "12345678A", dob, Gender.FEMALE, "123456789", doctor, patientUser);
        patientManager.registerPatient(testPatient);

        // Crear report
        testReport = new Report(java.sql.Date.valueOf("2024-12-08"), testPatient);
        reportManager.createReport(testReport);

        // Crear bitalinos
        bitalinoEMG = new Bitalino(java.sql.Date.valueOf("2024-12-08"), SignalType.EMG, "EMG data", testReport);
        bitalinoECG = new Bitalino(java.sql.Date.valueOf("2024-12-08"), SignalType.ECG, "ECG data", testReport);

        bitalinoManager.saveBitalino(bitalinoEMG);
        bitalinoManager.saveBitalino(bitalinoECG);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (jdbcManager != null) {
            jdbcManager.getConnection().rollback();
        }
    }

    @Test
    public void testCreateFile() {
        System.out.println("createFile");
        try {
            // Crear archivo temporal de prueba
            java.io.File tempFile = java.io.File.createTempFile("test_file", ".txt");
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write("This is a test file content.");
            }

            // Guardar el archivo en la base de datos
            filesManager.createFile(tempFile, bitalinoEMG.getId(), bitalinoECG.getId());

            // Verificar que el archivo se ha almacenado
            String query = "SELECT COUNT(*) FROM Files WHERE file_name = ?";
            PreparedStatement ps = jdbcManager.getConnection().prepareStatement(query);
            ps.setString(1, tempFile.getName());
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));

            rs.close();
            ps.close();

            // Eliminar el archivo temporal
            tempFile.deleteOnExit();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            fail("No se pudo ejecutar la prueba testCreateFile.");
        }
    }
}


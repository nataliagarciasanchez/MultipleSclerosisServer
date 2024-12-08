/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.sql.Blob;
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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;


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
    private static Bitalino bemg;
    private static Bitalino becg;
    private static Role rp; // role patient
    private static Role rd; // role doctor
    private static User up; // user patient
    private static User ud; // user doctor
    private static Doctor d;
    private static Patient p;
    private static Report r;
    private static int bemg_id;

    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect();
        filesManager = new JDBCFilesManager(jdbcManager);
        bitalinoManager = new JDBCBitalinoManager(jdbcManager);
        patientManager = new JDBCPatientManager(jdbcManager);
        reportManager = new JDBCReportManager(jdbcManager);
        roleManager = new JDBCRoleManager(jdbcManager);
        userManager = new JDBCUserManager(jdbcManager);
        doctorManager = new JDBCDoctorManager(jdbcManager);
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        assertNotNull(filesManager);
        assertNotNull(bitalinoManager);
        assertNotNull(patientManager);
        assertNotNull(reportManager);
        assertNotNull(roleManager);
        assertNotNull(userManager);
        assertNotNull(doctorManager);
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

        // Crear roles
        rp = new Role("Patient");
        roleManager.createRole(rp);
        rd = new Role("Doctor");
        roleManager.createRole(rd);

        // Crear usuarios
        up = new User("emailP", "passP", rp);
        userManager.registerUser(up);
        ud = new User("emailD", "passD", rd);
        userManager.registerUser(ud);

        // Crear doctor
        d = new Doctor("name", "surname", ud);
        doctorManager.registerDoctor(d);

        // Crear paciente
        Date dob = java.sql.Date.valueOf("2002-12-20");
        p = new Patient("Laura", "gallego", "39511471R", dob, Gender.FEMALE, "679719921", d, up);
        patientManager.registerPatient(p);

        // Crear report
        Date date = java.sql.Date.valueOf("2024-12-08");
        r = new Report(date, p);
        reportManager.createReport(r);

        // Crear bitalinos
        Date recorded = java.sql.Date.valueOf("2024-12-09");
        //String duration= "60.0";
        String signalValues = "123\n456\n789";
        bemg = new Bitalino(recorded,SignalType.EMG, signalValues, r);
        bitalinoManager.saveBitalino(bemg);
        bemg_id = bemg.getId();
        System.out.println(bemg.toString());

        becg = new Bitalino(recorded, SignalType.ECG, signalValues, r);
        bitalinoManager.saveBitalino(becg);
        System.out.println(becg.toString());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (jdbcManager != null) {
            // Deshace todos los cambios realizados durante la prueba
            jdbcManager.getConnection().rollback();
        }
    }

    @Test
    public void testCreateFile() {
        System.out.println("createFile");
        
        File testFile = new File("test_file.txt");
        try{
            String fileContent = "This is a test file.";
            Files.writeString(testFile.toPath(), fileContent);
            
            filesManager.createFile(testFile, bemg.getId(), becg.getId());
            String sql = "SELECT file_name, file_data FROM Files where file_name = ?";
            try(PreparedStatement p = jdbcManager.getConnection().prepareStatement(sql)){
                p.setString(1, testFile.getName());
                
                ResultSet rs = p.executeQuery();
                String fetchedFileName = rs.getString("file_name");
                byte [] fetchedData = rs.getBytes("file_data");
                assertEquals(testFile.getName(), fetchedFileName, "Names should match");
                assertArrayEquals(Files.readAllBytes(testFile.toPath()), fetchedData, "Content should match.");
                rs.close();
            }
        }catch(Exception e ){
            fail("Test failed: " + e.getMessage());
        }
        
        
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Doctor;
import POJOs.Gender;
import POJOs.Patient;
import POJOs.Report;
import POJOs.Role;
import POJOs.User;
import java.sql.Date;
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
public class JDBCReportManagerTest {
    
    private static JDBCReportManager reportManager;
    private static JDBCDoctorManager doctorManager;
    private static JDBCPatientManager patientManager;
    private static JDBCUserManager userManager;
    private static JDBCRoleManager roleManager;
    private static JDBCManager jdbcManager;
    private static Role rp; // role patient
    private static Role rd; // role doctor
    private static User up; // user patient
    private static User ud; // user doctor
    private static Doctor d;
    private static Patient p;
    
    ;
    
    public JDBCReportManagerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        reportManager = new JDBCReportManager(jdbcManager);
        roleManager = new JDBCRoleManager(jdbcManager); // Inicializar roleManager
        userManager = new JDBCUserManager(jdbcManager); // Inicializar userManager
        doctorManager = new JDBCDoctorManager(jdbcManager);
        patientManager = new JDBCPatientManager(jdbcManager);
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        assertNotNull(reportManager);
        assertNotNull(userManager);
        assertNotNull(roleManager);
        assertNotNull(patientManager);
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
         // para crear doctor
        rd = new Role("Doctor");
        roleManager.createRole(rd);
        ud = new User("emailD", "passD", rd);
        userManager.registerUser(ud);
        d = new Doctor("name", "NEUROLOGY", ud);
        doctorManager.createDoctor(d);
        
        //para crear patient
        rp = new Role("Patient");
        roleManager.createRole(rp);
        up = new User("emailP", "passP", rp);
        userManager.registerUser(up);
        Date dob = java.sql.Date.valueOf("2003-08-04");
        p = new Patient("Noelia", "Arribas", "1357968U", dob, Gender.FEMALE, "679003245", d, up);
        patientManager.registerPatient(p);
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
        if (jdbcManager != null) {
        // Deshace todos los cambios realizados durante la prueba
        jdbcManager.getConnection().rollback();
    }
    }
    
//HAY QUE VER COMO CREARLO PARA REPORT CON QUE ATRIBUTO
    /**
     * Test of createReport method, of class JDBCReportManager.
     */
    @Test
    public void testCreateReport() {
        System.out.println("createReport");
        
        Date date= java.sql.Date.valueOf("2024-09-14");
        Report r = new Report (date,p); //creamos report
        
        System.out.println(r.toString());
        reportManager.createReport(r);
        Report fetchedReport = reportManager.getReportById(r.getId());
        System.out.println(fetchedReport.toString());
        assertNotNull(fetchedReport);
        assertEquals(r.getDate(), fetchedReport.getDate());
        assertEquals(r.getId(), fetchedReport.getId());
        
    }

    /**
     * Test of removeReportById method, of class JDBCReportManager.
     */
    @Test
    public void testRemoveReportById() {
        System.out.println("DeleteReport");
        Date date= java.sql.Date.valueOf("2024-09-14");
        Report r = new Report (date,p);
        System.out.println(r.toString());
        reportManager.createReport(r);
        List<Report> ReportsBefore = reportManager.getListOfReports();
        assertEquals(1, ReportsBefore.size());
        reportManager.removeReportById(r.getId());
        List<Report> ReportsAfter = reportManager.getListOfReports();
        assertEquals(0, ReportsAfter.size());
    }

    /**
     * Test of updateReport method, of class JDBCReportManager.
     */
    @Test
    public void testUpdateReport() {
        System.out.println("updateReport");
        Date date= java.sql.Date.valueOf("2024-09-14");
        Report r = new Report (date,p);
        System.out.println(r.toString());
        reportManager.createReport(r);
        Date newDate= java.sql.Date.valueOf("2004-09-24");
        r.setDate(newDate);
        reportManager.createReport(r);
        Report updatedReport = reportManager.getReportById(r.getId());
        assertNotNull(updatedReport);
        assertEquals(newDate, updatedReport.getDate());
    }

    /**
     * Test of getListOfReports method, of class JDBCReportManager.
     */
    @Test
    public void testGetListOfReports() {
        System.out.println("getListOfReports");
        Date date1= java.sql.Date.valueOf("2024-09-14");
        Date date2= java.sql.Date.valueOf("2024-09-14");
        Report r1 = new Report (date1,p);
        Report r2 = new Report (date2,p);
        System.out.println(r1.toString());
        System.out.println(r2.toString());
        reportManager.createReport(r1);
        reportManager.createReport(r2);
        
        List<Report> reports = reportManager.getListOfReports();
        assertEquals(2, reports.size());
        assertTrue(reports.stream().anyMatch(report -> report.getId().equals(r1.getId())));
        assertTrue(reports.stream().anyMatch(report -> report.getId().equals(r1.getId())));
        assertTrue(reports.stream().anyMatch(report -> report.getDate().equals(date1)));
        assertTrue(reports.stream().anyMatch(report -> report.getDate().equals(date2)));
    }

    /**
     * Test of getReportsFromPatient method, of class JDBCReportManager.
     */
    @Test
    public void testGetReportsFromPatient() {
        System.out.println("getReportsFromPatient");
        Date date= java.sql.Date.valueOf("2024-09-14");
        Report r = new Report (date,p);
        System.out.println(r.toString());
        reportManager.createReport(r);
        List<Report> reports= reportManager.getReportsFromPatient(p.getId());
        assertNotNull(reports);
        assertFalse(reports.isEmpty());
        assertTrue(reports.stream().anyMatch(report -> report.getDate().equals(date)));
        //assertTrue(reports.stream().anyMatch(report -> report.getPatient().equals(r.getPatient())));
       
    }

    /**
     * Test of getReportById method, of class JDBCReportManager.
     */
    @Test
    public void testGetReportById() {
        System.out.println("getReportById");
        Date date= java.sql.Date.valueOf("2024-09-14");
        Report r = new Report (date,p);
        System.out.println(r.toString());
        reportManager.createReport(r);
        Report fetchedReport = reportManager.getReportById(r.getId());
        assertNotNull(fetchedReport);
        assertEquals(r.getId(), fetchedReport.getId());
        assertEquals(r.getDate(), fetchedReport.getDate());
        //assertEquals(r.getPatient(), fetchedReport.getPatient());
       
    }

    /**
     * Test of getReportByDate method, of class JDBCReportManager.
     */
    @Test
    public void testGetReportByDate() {
        System.out.println("getReportByDate");
        Date date= java.sql.Date.valueOf("2024-09-14");
        Report r = new Report (date,p);
        System.out.println(r.toString());
        reportManager.createReport(r);
        List<Report> reports= reportManager.getReportByDate(date);
        assertNotNull(reports);
        assertFalse(reports.isEmpty());
        assertTrue(reports.stream().anyMatch(report -> report.getDate().equals(date)));
        //assertTrue(reports.stream().anyMatch(report -> report.getPatient().equals(r.getPatient())));
       
    }
    
}

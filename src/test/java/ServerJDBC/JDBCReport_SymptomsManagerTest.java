/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Doctor;
import POJOs.Patient;
import POJOs.Report;
import POJOs.Role;
import POJOs.Symptom;
import POJOs.User;
import ServerInterfaces.ReportManager;
import POJOs.Gender;
import java.sql.Date;

import java.sql.SQLException;
import java.text.ParseException;
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
public class JDBCReport_SymptomsManagerTest {

    private static JDBCReport_SymptomsManager reportSymptomsManager;
    private static JDBCSymptomManager symptomManager;
    private static JDBCReportManager reportManager;
    private static JDBCManager jdbcManager;
    private static JDBCRoleManager roleManager;
    private static JDBCUserManager userManager;
    private static JDBCDoctorManager doctorManager;
    private static JDBCPatientManager patientManager;
    
    private static Role rp; // role patient
    private static Role rd; // role doctor
    private static User up; // user patient
    private static User ud; // user doctor
    private static Doctor d;
    private static Patient p;
    private static Report report;
    private static Symptom s1;
    private static Symptom s2;
    private static Symptom s3;

    public JDBCReport_SymptomsManagerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Conectar la base de datos
        symptomManager = new JDBCSymptomManager(jdbcManager);
        reportManager = new JDBCReportManager(jdbcManager);
        reportSymptomsManager = new JDBCReport_SymptomsManager(jdbcManager);
        roleManager = new JDBCRoleManager(jdbcManager);
        userManager = new JDBCUserManager(jdbcManager);
        doctorManager = new JDBCDoctorManager(jdbcManager);
        patientManager = new JDBCPatientManager(jdbcManager);


        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
            //Desactivar
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }

        assertNotNull(reportSymptomsManager);
        assertNotNull(symptomManager);
        assertNotNull(reportManager);
        assertNotNull(roleManager);
        assertNotNull(userManager);
        assertNotNull(doctorManager);
        assertNotNull(patientManager);
    }

    @AfterAll
    public static void tearDownClass() {
        if (jdbcManager != null) {
            try {
                jdbcManager.getConnection().setAutoCommit(true); // Restaura auto-commit
                jdbcManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @BeforeEach
    public void setUp() throws SQLException, ParseException {
        // Limpiar la base de datos antes de cada prueba
        jdbcManager.clearAllTables();
        // para crear doctor
        rd = new Role("Doctor");
        roleManager.createRole(rd);
        ud = new User("emailD", "passD", rd);
        userManager.registerUser(ud);
        d = new Doctor("name", "specialty", ud);
        doctorManager.createDoctor(d);
        
        //para crear patient
        rp = new Role("Patient");
        roleManager.createRole(rp);
        up = new User("emailP", "passP", rp);
        userManager.registerUser(up);
        Date dob = java.sql.Date.valueOf("2003-09-30");
        p = new Patient("name", "surname", "123456789", dob, Gender.FEMALE, "666666666", d, up);
        
        //para crear symptoms
        s1 = new Symptom("symptom1");
        symptomManager.createSymptom(s1);
        s2 = new Symptom("symptom1");
        symptomManager.createSymptom(s2);
        s3 = new Symptom("symptom3");
        symptomManager.createSymptom(s3);
        
        //para crear report
        Date date = java.sql.Date.valueOf("2024-01-01");;
        report = new Report(date, p);
        
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (jdbcManager != null) {
            // Deshace todos los cambios realizados durante la prueba
            jdbcManager.getConnection().rollback();
        }
    }

    /**
     * Test of addSymptomToReport method, of class JDBCReport_SymptomsManager.
     */
    @Test
    public void testAddSymptomToReport() {
        System.out.println("addSymptomToReport");

        // Agregar relación entre síntoma y reporte
        reportSymptomsManager.addSymptomToReport(s1.getId(), report.getId());

        // Validar que la relación se creó correctamente
        List<Symptom> symptoms = reportSymptomsManager.getSymptomsFromReport(report.getId());
        assertNotNull(symptoms, "La lista de síntomas no debería ser null.");
        assertEquals(1, symptoms.size(), "Debería haber exactamente 1 síntoma asociado al reporte.");
        
        assertEquals(s1.getName(), symptoms.get(0).getName(), "El nombre del síntoma debería coincidir.");
        assertEquals(s1.getId(), symptoms.get(0).getId());
    }

    /**
     * Test of removeSymptomFromReport method, of class
     * JDBCReport_SymptomsManager.
     */
    @Test
    public void testRemoveSymptomFromReport() {
        System.out.println("removeSymptomFromReport");

        // Asociar síntoma al reporte
        reportSymptomsManager.addSymptomToReport(s1.getId(), report.getId());
        
        List<Symptom> symptomsBefore = reportSymptomsManager.getSymptomsFromReport(report.getId());
        assertEquals(1, symptomsBefore.size()); //comprobamos que hay 1 role

        // Eliminar la asociación entre síntoma y reporte
        reportSymptomsManager.removeSymptomFromReport(s1.getId(), report.getId());

        // Validar que el síntoma fue eliminado del reporte
        List<Symptom> symptomsAfter = reportSymptomsManager.getSymptomsFromReport(report.getId());
        assertNotNull(symptomsAfter, "La lista de síntomas no debería ser null.");
        assertEquals(0, symptomsAfter.size(), "No debería haber síntomas asociados al reporte.");
    }

    /**
     * Test of emptyReport method, of class JDBCReport_SymptomsManager.
     */
    @Test
    public void testEmptyReport() {
        System.out.println("emptyReport");

        

        // Asociar síntomas al reporte
        reportSymptomsManager.addSymptomToReport(s1.getId(), report.getId());
        reportSymptomsManager.addSymptomToReport(s2.getId(), report.getId());
        reportSymptomsManager.addSymptomToReport(s3.getId(), report.getId());

        List<Symptom> symptomsBefore = reportSymptomsManager.getSymptomsFromReport(report.getId());
        assertEquals(3, symptomsBefore.size()); //comprobamos que hay 3 sintomas

        // Vaciar el reporte
        reportSymptomsManager.emptyReport(report.getId());

        // Validar que el reporte esté vacío
        List<Symptom> symptomsAfter = reportSymptomsManager.getSymptomsFromReport(report.getId());
        assertNotNull(symptomsAfter, "La lista de síntomas no debería ser null.");
        assertEquals(0, symptomsAfter.size(), "No debería haber síntomas asociados al reporte.");
    }

    /**
     * Test of getSymptomsFromReport method, of class
     * JDBCReport_SymptomsManager.
     */
    @Test
    public void testGetSymptomsFromReport() {
        System.out.println("getSymptomsFromReport");

        

        // Asociar síntoma al reporte
        reportSymptomsManager.addSymptomToReport(s1.getId(), report.getId());
        reportSymptomsManager.addSymptomToReport(s2.getId(), report.getId());
        reportSymptomsManager.addSymptomToReport(s3.getId(), report.getId());

        // Recuperar los síntomas asociados al reporte
        List<Symptom> symptoms = reportSymptomsManager.getSymptomsFromReport(report.getId());
        assertNotNull(symptoms, "La lista de síntomas no debería ser null.");
        assertEquals(3, symptoms.size(), "Debería haber exactamente 3 síntoma asociado al reporte.");
        assertEquals(s1.getName(), symptoms.get(0).getName(), "El nombre del síntoma debería coincidir.");
        assertEquals(s2.getName(), symptoms.get(1).getName(), "El nombre del síntoma debería coincidir.");
        assertEquals(s3.getName(), symptoms.get(2).getName(), "El nombre del síntoma debería coincidir.");
    }

}

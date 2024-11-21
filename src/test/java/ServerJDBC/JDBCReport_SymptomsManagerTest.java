/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.Report;
import POJOs.Symptom;
import ServerInterfaces.ReportManager;
import ServerJDBC.JDBCPatientManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class JDBCReport_SymptomsManagerTest {

    private static JDBCReport_SymptomsManager reportSymptomsManager;
    private static JDBCSymptomManager symptomManager;
    private static JDBCManager jdbcManager;

    public JDBCReport_SymptomsManagerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        /* jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Asegúrate de que la conexión esté establecida antes de usar roleManager
        Report_SymptomsManager = new JDBCReport_SymptomsManager(jdbcManager);
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        assertNotNull(Report_SymptomsManager);*/
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Conectar la base de datos
        symptomManager = new JDBCSymptomManager(jdbcManager);
        reportSymptomsManager = new JDBCReport_SymptomsManager(jdbcManager);

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
    public void setUp() throws SQLException {
        // Limpiar la base de datos antes de cada prueba
        jdbcManager.clearAllTables();
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

        // Crear un síntoma
        Symptom symptom = new Symptom(1, "Fever");
        symptomManager.createSymptom(symptom); // Guarda el síntoma en la base de datos

        // Crear un reporte
        Report report = new Report(java.sql.Date.valueOf("2024-01-01")); // Reporte sin paciente
        ReportManager reportManager = new JDBCReportManager(jdbcManager);
        reportManager.createReport(report); // Guarda el reporte en la base de datos

        // Agregar relación entre síntoma y reporte
        reportSymptomsManager.addSymptomToReport(symptom.getId(), report.getId());

        // Validar que la relación se creó correctamente
        List<Symptom> symptoms = reportSymptomsManager.getSymptomsFromReport(report.getId());
        assertNotNull(symptoms, "La lista de síntomas no debería ser null.");
        assertEquals(1, symptoms.size(), "Debería haber exactamente 1 síntoma asociado al reporte.");
        assertEquals(symptom.getName(), symptoms.get(0).getName(), "El nombre del síntoma debería coincidir.");
    }

    /**
     * Test of removeSymptomFromReport method, of class
     * JDBCReport_SymptomsManager.
     */
    @Test
    public void testRemoveSymptomFromReport() {
        System.out.println("removeSymptomFromReport");

        // Crear un síntoma
        Symptom symptom = new Symptom(1, "Headache");
        symptomManager.createSymptom(symptom);

        // Crear un reporte
        Report report = new Report(java.sql.Date.valueOf("2024-01-01"));
        ReportManager reportManager = new JDBCReportManager(jdbcManager);
        reportManager.createReport(report);

        // Asociar síntoma al reporte
        reportSymptomsManager.addSymptomToReport(symptom.getId(), report.getId());

        // Eliminar la asociación entre síntoma y reporte
        reportSymptomsManager.removeSymptomFromReport(symptom.getId(), report.getId());

        // Validar que el síntoma fue eliminado del reporte
        List<Symptom> symptoms = reportSymptomsManager.getSymptomsFromReport(report.getId());
        assertNotNull(symptoms, "La lista de síntomas no debería ser null.");
        assertEquals(0, symptoms.size(), "No debería haber síntomas asociados al reporte.");
    }

    /**
     * Test of emptyReport method, of class JDBCReport_SymptomsManager.
     */
    @Test
    public void testEmptyReport() {
        System.out.println("emptyReport");

        // Crear dos síntomas
        Symptom symptom1 = new Symptom(1, "Cough");
        Symptom symptom2 = new Symptom(2, "Nausea");
        symptomManager.createSymptom(symptom1);
        symptomManager.createSymptom(symptom2);

        // Crear un reporte
        Report report = new Report(java.sql.Date.valueOf("2024-01-01"));
        ReportManager reportManager = new JDBCReportManager(jdbcManager);
        reportManager.createReport(report);

        // Asociar síntomas al reporte
        reportSymptomsManager.addSymptomToReport(symptom1.getId(), report.getId());
        reportSymptomsManager.addSymptomToReport(symptom2.getId(), report.getId());

        // Vaciar el reporte
        reportSymptomsManager.emptyReport(report.getId());

        // Validar que el reporte esté vacío
        List<Symptom> symptoms = reportSymptomsManager.getSymptomsFromReport(report.getId());
        assertNotNull(symptoms, "La lista de síntomas no debería ser null.");
        assertEquals(0, symptoms.size(), "No debería haber síntomas asociados al reporte.");
    }

    /**
     * Test of getSymptomsFromReport method, of class
     * JDBCReport_SymptomsManager.
     */
    @Test
    public void testGetSymptomsFromReport() {
        System.out.println("getSymptomsFromReport");

    // Crear un síntoma
    Symptom symptom = new Symptom(1, "Fatigue");
    symptomManager.createSymptom(symptom);

    // Crear un reporte
    Report report = new Report(java.sql.Date.valueOf("2024-01-01"));
    ReportManager reportManager = new JDBCReportManager(jdbcManager);
    reportManager.createReport(report);

    // Asociar síntoma al reporte
    reportSymptomsManager.addSymptomToReport(symptom.getId(), report.getId());

    // Recuperar los síntomas asociados al reporte
    List<Symptom> symptoms = reportSymptomsManager.getSymptomsFromReport(report.getId());
    assertNotNull(symptoms, "La lista de síntomas no debería ser null.");
    assertEquals(1, symptoms.size(), "Debería haber exactamente 1 síntoma asociado al reporte.");
    assertEquals(symptom.getName(), symptoms.get(0).getName(), "El nombre del síntoma debería coincidir.");
    }

}

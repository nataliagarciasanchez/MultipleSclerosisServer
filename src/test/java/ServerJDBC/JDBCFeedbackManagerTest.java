/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerJDBC;

import POJOs.*;
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
public class JDBCFeedbackManagerTest {

    private static JDBCFeedbackManager feedbackManager;
    private static JDBCDoctorManager doctorManager;
    private static JDBCPatientManager patientManager;
    private static JDBCUserManager userManager;
    private static JDBCRoleManager roleManager;
    private static JDBCManager jdbcManager;

    private static Role patientRole;
    private static Role doctorRole;
    private static User patientUser;
    private static User doctorUser;
    private static Doctor doctor;
    private static Patient patient;

    public JDBCFeedbackManagerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        jdbcManager = new JDBCManager();
        jdbcManager.connect(); // Conectar a la base de datos

        roleManager = new JDBCRoleManager(jdbcManager);
        userManager = new JDBCUserManager(jdbcManager);
        doctorManager = new JDBCDoctorManager(jdbcManager);
        patientManager = new JDBCPatientManager(jdbcManager);

        feedbackManager = new JDBCFeedbackManager(jdbcManager, doctorManager, patientManager);
        try {
            // Desactiva auto-commit para manejar transacciones manualmente
            jdbcManager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("No se pudo configurar la conexión para transacciones.");
        }
        assertNotNull(roleManager);
        assertNotNull(userManager);
        assertNotNull(feedbackManager);
        assertNotNull(doctorManager);
        assertNotNull(patientManager);
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

        // Crear roles para doctor y paciente
        patientRole = new Role("Patient");
        roleManager.createRole(patientRole);
        doctorRole = new Role("Doctor");
        roleManager.createRole(doctorRole);

        // Crear usuarios para doctor y paciente
        patientUser = new User("patient@example.com", "password123", patientRole);
        userManager.registerUser(patientUser);

        doctorUser = new User("doctor@example.com", "password456", doctorRole);
        userManager.registerUser(doctorUser);

        // Crear doctor asociado al usuario
        doctor = new Doctor("Dr. Smith", "NEUROLOGY", doctorUser);
        doctorManager.createDoctor(doctor);

        // Crear paciente asociado al usuario y al doctor
        patient = new Patient("John", "Doe", "12345678A", java.sql.Date.valueOf("1990-01-01"), Gender.MALE, "123456789", doctor, patientUser);
        patientManager.registerPatient(patient);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (jdbcManager != null) {
            // Deshace todos los cambios realizados durante la prueba
            jdbcManager.getConnection().rollback();
        }
    }

    /**
     * Test of createFeedback method, of class JDBCFeedbackManager.
     */
    @Test
    public void testCreateFeedback() {
        System.out.println("createFeedback");
        // Crear un feedback
        Feedback feedback = new Feedback(java.sql.Date.valueOf("2024-01-01"), "Excellent treatment!", doctor, patient);
        feedbackManager.createFeedback(feedback);

        // Verificar que el feedback fue creado correctamente
        Feedback fetchedFeedback = feedbackManager.getFeedBackById(feedback.getId());
        assertNotNull(fetchedFeedback, "El feedback no debería ser null.");
        assertEquals("Excellent treatment!", fetchedFeedback.getMessage(), "El mensaje del feedback no coincide.");
        assertEquals(doctor.getId(), fetchedFeedback.getDoctor().getId(), "El doctor del feedback no coincide.");
        assertEquals(patient.getId(), fetchedFeedback.getPatient().getId(), "El paciente del feedback no coincide.");
    }

    /**
     * Test of removeFeedbackById method, of class JDBCFeedbackManager.
     */
    @Test
    public void testRemoveFeedbackById() {
        System.out.println("removeFeedbackById");

        // Crear un feedback
        Feedback feedback = new Feedback(java.sql.Date.valueOf("2024-01-01"), "Excellent treatment!", doctor, patient);
        feedbackManager.createFeedback(feedback);

        // Remover el feedback
        feedbackManager.removeFeedbackById(feedback.getId());

        // Verificar que el feedback fue eliminado correctamente
        Feedback fetchedFeedback = feedbackManager.getFeedBackById(feedback.getId());
        assertNull(fetchedFeedback, "El feedback debería ser null tras su eliminación.");
    }

    /**
     * Test of updateFeedback method, of class JDBCFeedbackManager.
     */
    @Test
    public void testUpdateFeedback() {
        System.out.println("updateFeedback");

        // Crear un feedback
        Feedback feedback = new Feedback(java.sql.Date.valueOf("2024-01-01"), "Good treatment", doctor, patient);
        feedbackManager.createFeedback(feedback);

        // Actualizar el mensaje del feedback
        feedback.setMessage("Excellent treatment!");
        feedbackManager.updateFeedback(feedback);

        // Verificar que el feedback fue actualizado correctamente
        Feedback fetchedFeedback = feedbackManager.getFeedBackById(feedback.getId());
        assertNotNull(fetchedFeedback);
        assertEquals("Excellent treatment!", fetchedFeedback.getMessage(), "El mensaje del feedback no coincide tras la actualización.");
    }

    /**
     * Test of getFeedBackById method, of class JDBCFeedbackManager.
     */
    @Test
    public void testGetFeedBackById() {
        System.out.println("getFeedBackById");
        Integer id = null;
        JDBCFeedbackManager instance = null;
        Feedback expResult = null;
        Feedback result = instance.getFeedBackById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFeedBackByDate method, of class JDBCFeedbackManager.
     */
    @Test
    public void testGetFeedBackByDate() {
        System.out.println("getFeedBackByDate");

        // Crear un feedback
        Feedback feedback = new Feedback(java.sql.Date.valueOf("2024-01-01"), "Excellent treatment!", doctor, patient);
        feedbackManager.createFeedback(feedback);

        // Recuperar feedbacks por fecha
        List<Feedback> feedbacks = feedbackManager.getFeedBackByDate(java.sql.Date.valueOf("2024-01-01"));
        assertNotNull(feedbacks);
        assertEquals(1, feedbacks.size());
        assertEquals(feedback.getId(), feedbacks.get(0).getId());
    }

    /**
     * Test of getListOfFeedbacksOfPatient method, of class JDBCFeedbackManager.
     */
    @Test
    public void testGetListOfFeedbacksOfPatient() {
        System.out.println("getListOfFeedbacksOfPatient");

        // Crear un feedback
        Feedback feedback = new Feedback(java.sql.Date.valueOf("2024-01-01"), "Great experience!", doctor, patient);
        feedbackManager.createFeedback(feedback);

        // Recuperar feedbacks del paciente
        List<Feedback> feedbacks = feedbackManager.getListOfFeedbacksOfPatient(patient.getId());
        assertNotNull(feedbacks);
        assertEquals(1, feedbacks.size());
        assertEquals(feedback.getId(), feedbacks.get(0).getId());
    }

    /**
     * Test of getListOfFeedbacksOfDoctor method, of class JDBCFeedbackManager.
     */
    @Test
    public void testGetListOfFeedbacksOfDoctor() {
        System.out.println("getListOfFeedbacksOfDoctor");

        // Crear un feedback
        Feedback feedback = new Feedback(java.sql.Date.valueOf("2024-01-01"), "Very professional!", doctor, patient);
        feedbackManager.createFeedback(feedback);

        // Recuperar feedbacks del doctor
        List<Feedback> feedbacks = feedbackManager.getListOfFeedbacksOfDoctor(doctor.getId());
        assertNotNull(feedbacks);
        assertEquals(1, feedbacks.size());
        assertEquals(feedback.getId(), feedbacks.get(0).getId());
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package IOCommunication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import POJOs.*;
import ServerJDBC.JDBCManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maipa
 */
public class ServerPatientCommunicationTest {
     private ServerPatientCommunication serverInstance;
    private Thread serverThread;
    private int port = 9000; // Example port
    
    public ServerPatientCommunicationTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() throws InterruptedException {
         if (serverInstance != null) {
            serverInstance.stopServer(); // Detiene el servidor
        }

        if (serverThread != null && serverThread.isAlive()) {
            serverThread.join(); // Espera a que el hilo termine
        }
    }

    /**
     * Test of startServer method, of class ServerPatientCommunication.
     */
    @Test
    public void testStartServer() {
        System.out.println("startServer");
        ServerPatientCommunication instance = null;
        instance.startServer();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testSaveDataToCSV() {
        System.out.println("Testing saving data to CSV...");

        // Simulate client sending a report to the server
        try (Socket clientSocket = new Socket("localhost", port);
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            // Simulate client sending "sendReport" action
            out.writeObject("sendReport");

            // Create and send a sample report
            Report report = new Report();
            report.setPatient(new Patient("Doe"));
            Bitalino emgData = new Bitalino(new Date(24,12,20), SignalType.EMG, "ECG: 120, EMG: 45", report);
            Bitalino ecgData = new Bitalino(new Date(24,12,20),SignalType.ECG, "Temp: 36.5, SpO2: 98", report);
            report.setBitalinos(new ArrayList<>(List.of(emgData, ecgData)));

            out.writeObject(report); // Send the report to the server

            // Wait briefly to ensure the server processes the report
            Thread.sleep(2000);

            // Check if the CSV file is created
            File csvFile = new File("CSV/TestPatient_monitoring.csv");
            assertTrue(csvFile.exists(), "The CSV file should have been created.");

            // Verify the content of the CSV file
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                String header = reader.readLine();
                assertEquals("Patient Name,Date Time,Physiological Data", header, "CSV header is incorrect.");

                String dataLine = reader.readLine();
                assertTrue(dataLine.contains("TestPatient"), "CSV should contain the patient's name.");
                assertTrue(dataLine.contains("ECG: 120, EMG: 45"), "CSV should contain the EMG data.");
                assertTrue(dataLine.contains("Temp: 36.5, SpO2: 98"), "CSV should contain the ECG data.");
            }

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
}

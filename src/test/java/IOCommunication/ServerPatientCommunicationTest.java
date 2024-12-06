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
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author maipa
 */
public class ServerPatientCommunicationTest {
     private ServerPatientCommunication serverInstance;
    private Thread serverThread;
    private int port = 9000; // Example port
    private final String testPatientName = "TestPatient";
    private final String testPhysiologicalData = "ECG: 120, EMG: 45";
    
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
         // Inicializa el JDBCManager si es necesario
        JDBCManager jdbcManager = new JDBCManager();
        
        // Inicializa la instancia del servidor
        serverInstance = new ServerPatientCommunication(port, jdbcManager);
        
        // Inicia el servidor en un hilo separado
        serverThread = new Thread(() -> serverInstance.startServer());
        serverThread.start();
    }
    
    @AfterEach
    public void tearDown() throws InterruptedException {
         if (serverInstance != null) {
            serverInstance.stopServer(); // Detiene el servidor
        }

        if (serverThread != null && serverThread.isAlive()) {
            serverThread.join(); // Espera a que el hilo termine
        }
         // Clean up the created TXT file
        File file = new File("TXT/" + testPatientName + "_monitoring.txt");
        if (file.exists()) {
            file.delete();
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
    public void testSaveDataToTXT() throws IOException{
        // Simulate a client sending data to the server
        try (Socket clientSocket = new Socket("localhost", port);
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            // Send mock report to the server
            out.writeObject("sendReport"); // Specify the action
            out.writeObject(testPatientName); // Send patient name
            out.writeObject(testPhysiologicalData); // Send physiological data

            // Wait for server confirmation
            String response = (String) in.readObject();
            Assertions.assertEquals("Report received and data saved.", response);

            // Verify that the TXT file is created and contains the correct data
            File txtFile = new File("TXT/" + testPatientName + "_monitoring.txt");
            assertTrue(txtFile.exists(), "TXT file should exist");

            // Verify the content of the file
            try (BufferedReader reader = new BufferedReader(new FileReader(txtFile))) {
                String line;
                boolean dataFound = false;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(testPatientName) && line.contains(testPhysiologicalData)) {
                        dataFound = true;
                        break;
                    }
                }
                assertTrue(dataFound, "File content should contain patient name and physiological data");
            }
        } catch (Exception e) {
            Assertions.fail("An error occurred during the test: " + e.getMessage());
        }
    }
}

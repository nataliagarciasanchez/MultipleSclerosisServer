/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import POJOs.Bitalino;
import POJOs.Doctor;
import POJOs.Frame;
import POJOs.Patient;
import POJOs.User;
import ServerJDBC.JDBCBitalinoManager;
import ServerJDBC.JDBCDoctorManager;
import ServerJDBC.JDBCManager;
import ServerJDBC.JDBCPatientManager;
import ServerJDBC.JDBCRoleManager;
import ServerJDBC.JDBCUserManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to test all the method in the communication
 * @author maipa
 */
public class ServerPatientCommunication {
    
    private ServerSocket serverSocket;
    private int port;
    private JDBCUserManager userManager;
    private JDBCRoleManager roleManager;
    private JDBCPatientManager patientManager;
    private JDBCDoctorManager doctorManager;
    private JDBCBitalinoManager bitalinoManager;
    

    public ServerPatientCommunication(int port, JDBCManager jdbcManager) {
        this.roleManager=new JDBCRoleManager(jdbcManager);
        this.userManager = new JDBCUserManager(jdbcManager, roleManager);
        this.patientManager=new JDBCPatientManager(jdbcManager);
        this.doctorManager=new JDBCDoctorManager(jdbcManager);
        this.bitalinoManager = new JDBCBitalinoManager(jdbcManager);
        this.port=port;   
    }
    
    
    /**
     * 
     * startServer is called from the menu when the Server is executed
     */
    public void startServer(){
         try {
            
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server started. ");

            while (true) {
                Socket patientSocket = serverSocket.accept();
                System.out.println("New client connected.");

                //we start a new thread for each connection made
                new Thread(new ServerPatientThread(patientSocket)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            releaseResourcesServer(serverSocket);
        }
    }
    
    
    class ServerPatientThread implements Runnable {
        
        
        private Socket patientSocket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ServerPatientThread(Socket patientSocket) {
            this.patientSocket = patientSocket;
            
        }
        
        @Override
        public void run() {
            try{
                in = new ObjectInputStream(patientSocket.getInputStream());
                out = new ObjectOutputStream(patientSocket.getOutputStream());
                out.flush();
                handlePatientsRequest();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                releaseResourcesPatient(in,patientSocket);

            }
        }
        
        /**
         * Handles all requests from patient
         */
        private void handlePatientsRequest(){
            boolean running=true;
            while(running){
                try {
                    String action = (String) in.readObject(); // Leer acción del cliente
                    
                    switch (action) {
                        case "register":
                            handleRegister();
                            break;
                        case "login":
                            handleLogin();
                            break;
                        case "logout":
                            running=false;
                            handleLogout();
                            break;
                        case "updateInformation":
                            handleUpdateInformation();
                            break;
                        case "sendECGSignals":
                            handleECGSignals();
                            break;
                        case "sendEMGSignals":
                            handleEMGSignals();
                            break;
                        default:
                            out.writeObject("Not recognized action");
                            break; 
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        
        /**
         * Registers into database the patient
         */
        private void handleRegister() {
            try {
                User user = (User) in.readObject();
                userManager.registerUser(user);
                Patient patient = (Patient) in.readObject();
                patientManager.registerPatient(patient);

                out.writeObject("Registered with success");
                out.flush();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception exc) {
                try {
                    out.writeObject("Error during registration: " + exc.getMessage());
                    out.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        /**
         * Logs in by retrieving patient info from the registered patient in the database
         */
        private void handleLogin() {
            try {
                String username = (String) in.readObject();
                String password = (String) in.readObject();
                User user = userManager.login(username, password);
                Patient patient = patientManager.getPatientByUser(user);
                Doctor doctor = doctorManager.getDoctorById(patientManager.getDoctorIdFromPatient(patient));
                patient.setDoctor(doctor);
                patient.setUser(user);
                out.writeObject(patient);
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Releases all the resources of the socket established with the patient
         */
        private void handleLogout(){
            try {
                releaseResourcesPatient(in,patientSocket);
                out.writeObject("Connection closed. ");
            } catch (IOException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Changes password in the database
         */
        private void handleUpdateInformation() {
            try {
               
                User user=(User) in.readObject();
                userManager.updateUser(user);
                out.writeObject("Information changed correclty");

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Receives all signals and sends them to a doctor
         * @return list of all frames recorded in the ECG
         */
        private Bitalino handleECGSignals() {
            Bitalino bitalino=null;
            
            try {
                // Receive ECG frames from the client
                bitalino = (Bitalino) in.readObject();
                bitalinoManager.createBitalino(bitalino);
                System.out.println("Received bitalino");

                // TODO Should send the list to the doctor and then from the ServerDoctor communication receive the diagnostic from the doctor

                // Send the acknowledgment back to the client
                out.writeObject("Signals sent to doctor for diagnostic. This might take a few minutes. Please wait.");
                out.flush();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    out.writeObject("Error processing ECG signals: " + ex.getMessage());
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitalino;
        }
        
        /**
         * Receives all signals and sends them to a doctor
         * @return list of all frames recorded in the ECG
         */
        private Bitalino handleEMGSignals() {
            
            Bitalino bitalino=null;
            
            try {
                // Receive ECG frames from the client
                bitalino = (Bitalino) in.readObject();
                System.out.println("Received bitalino");

                // Should send the list to the doctor and then return the diagnostic from the doctor
                // Send the acknowledgment back to the client
                out.writeObject("Signals sent to doctor for diagnostic. This might take a few minutes. Please wait.");
                out.flush();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    out.writeObject("Error processing EMG signals: " + ex.getMessage());
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitalino;
        }

        private static void releaseResourcesPatient(InputStream inputStream, Socket socket) {

            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void releaseResourcesServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

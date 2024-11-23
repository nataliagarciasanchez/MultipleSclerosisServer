/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import POJOs.Doctor;
import POJOs.Patient;
import POJOs.Role;
import POJOs.User;
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
    

    public ServerPatientCommunication(int port, JDBCManager jdbcManager) {
        this.roleManager=new JDBCRoleManager(jdbcManager);
        this.userManager = new JDBCUserManager(jdbcManager, roleManager);
        this.patientManager=new JDBCPatientManager(jdbcManager);
        this.doctorManager=new JDBCDoctorManager(jdbcManager);
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
        
        private void handleLogout(){
            try {
                releaseResourcesPatient(in,patientSocket);
                out.writeObject("Connection closed. ");
            } catch (IOException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void handleUpdateInformation() {
            try {
                String username = (String) in.readObject();
                String newPassword = (String) in.readObject();
                //Role role_patient = roleManager.getRoleByName("patient");
                User user=userManager.getUserByEmail(username);
                user.setPassword(newPassword);
                userManager.updateUser(user);
                out.writeObject("Information changed correclty");

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void handleECGSignals() {
            //TODO recibe las señales del bitalino y en base a eso y a los symptoms debe crear un diagnóstico 
        }

        private void handleEMGSignals() {
            //TODO recibe las señales del bitalino y en base a eso y a los symptoms debe crear un diagnóstico  
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

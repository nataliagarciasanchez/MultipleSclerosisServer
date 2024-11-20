/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import POJOs.Patient;
import POJOs.Role;
import POJOs.User;
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
 *
 * @author maipa
 */
public class ServerPatientCommunication {
    
    private ServerSocket serverSocket;
    private int port;
    private JDBCUserManager userManager;
    private JDBCRoleManager roleManager;
    private JDBCPatientManager patientManager;

    public ServerPatientCommunication(int port, JDBCManager jdbcManager) {
        this.roleManager=new JDBCRoleManager(jdbcManager);
        this.userManager = new JDBCUserManager(jdbcManager, roleManager);
        this.patientManager=new JDBCPatientManager(jdbcManager);
        this.port=port;   
    }
    
    
    /**
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
               
                handlePatientsRequest();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                releaseResourcesPatient(in,patientSocket);

            }
        }
        
        private void handlePatientsRequest(){
            
            while(true){
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
                            handleLogout();
                        case "changePassword":
                            handleChangePassword();
                            break;
                        case "findPatient":
                            handleFindPatient();
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
                Role role = new Role(1, "patient");
                userManager.assignRole2User(user, role);
                Patient patient = (Patient) in.readObject();
                patientManager.registerPatient(patient, user);
                
                out.writeObject("Registered with success");
                
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
                out.writeObject((user != null) ? "Successful login" : "Incorrect introduced data");
                
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

        private void handleChangePassword() {
            try {
                String username = (String) in.readObject();
                String oldPassword = (String) in.readObject();
                String newPassword = (String) in.readObject();
                
                User user = userManager.login(username, oldPassword);
                if (user != null) {
                    userManager.changePassword(user, newPassword);
                    out.writeObject("Password changed correclty");
                } else {
                    out.writeObject("Incorrect username or password");
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void handleFindPatient() {
            try {
                String username = (String) in.readObject();
                String password = (String) in.readObject();
                
                User user = new User(username, password, new Role("patient"));
                Patient patient = patientManager.getPatientByUser(user);
                
                if (patient != null) {
                    out.writeObject("Patient found: " + patient);
                } else {
                    out.writeObject("Patient not found or incorrect credentials");
                }
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

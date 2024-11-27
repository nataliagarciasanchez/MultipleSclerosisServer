/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import POJOs.Doctor;
import POJOs.Patient;
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
 *
 * @author maipa
 */
public class ServerDoctorCommunication{
    
    private ServerSocket serverSocket;
    private int port;
    private JDBCUserManager userManager;
    private JDBCRoleManager roleManager;
    private JDBCDoctorManager doctorManager;

    public ServerDoctorCommunication(int port, JDBCManager jdbcManager) {
        this.port=port;
        this.roleManager=new JDBCRoleManager(jdbcManager);
        this.userManager = new JDBCUserManager(jdbcManager, roleManager);
        this.doctorManager=new JDBCDoctorManager(jdbcManager);
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
                Socket doctorSocket = serverSocket.accept();
                System.out.println("New client connected.");

                //we start a new thread for each connection made
                new Thread(new ServerDoctorThread(doctorSocket)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            releaseResourcesServer(serverSocket);
        }
    }
    
    class ServerDoctorThread implements Runnable {
        
        private Socket doctorSocket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ServerDoctorThread(Socket doctorSocket) {
            this.doctorSocket = doctorSocket;
            
        }
        
        @Override
        public void run() {
            try{
                in = new ObjectInputStream(doctorSocket.getInputStream());
                out = new ObjectOutputStream(doctorSocket.getOutputStream());
                out.flush();
                handleDoctorsRequest();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                releaseResourcesDoctor(in,doctorSocket);

            }
        }
        
        /**
         * Handles all requests from patient
         */
        private void handleDoctorsRequest(){
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
                            //case para viewPatient
                        case "sendFeedback":
                            receiveFeedback();
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
         * Registers into database the doctor
         */
        private void handleRegister() {
            try {
                User user = (User) in.readObject();
                userManager.registerUser(user);
                Doctor doctor = (Doctor) in.readObject();
                doctorManager.registerDoctor(doctor);

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
                Doctor doctor= doctorManager.getDoctorByUser(user);
                doctor.setUser(user);
                out.writeObject(doctor);
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Releases all the resources of the socket established with the patient
         */
        private void handleLogout(){
            try {
                releaseResourcesDoctor(in,doctorSocket);
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
        
        private void receiveFeedback(){
            //TODO recives report and has to send it to the patient
        }
        
        
        
        
        private static void releaseResourcesDoctor(InputStream inputStream, Socket socket) {

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

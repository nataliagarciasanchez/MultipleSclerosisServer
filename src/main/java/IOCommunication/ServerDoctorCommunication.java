/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import IOCommunication.ServerPatientCommunication.ServerPatientThread;
import POJOs.Doctor;
import POJOs.Feedback;
import POJOs.Patient;
import POJOs.Report;
import POJOs.User;
import ServerJDBC.JDBCDoctorManager;
import ServerJDBC.JDBCFeedbackManager;
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
import java.util.List;
import java.util.ListIterator;

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
    private JDBCPatientManager patientManager;
    private JDBCFeedbackManager feedbackManager;
    private int connectedDoctors = 0;
    private boolean isRunning = true;

    public ServerDoctorCommunication(int port, JDBCManager jdbcManager) {
        this.port=port;
        this.roleManager=new JDBCRoleManager(jdbcManager);
        this.userManager = new JDBCUserManager(jdbcManager, roleManager);
        this.doctorManager=new JDBCDoctorManager(jdbcManager);
        this.patientManager = new JDBCPatientManager(jdbcManager);
        this.feedbackManager=new JDBCFeedbackManager(jdbcManager);
        
    }
    
    /**
     * 
     * startServer is called from the menu when the Server is executed
     */
    public void startServer(){
         try {
            
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server started. ");

            while (isRunning) {
                try{
                Socket doctorSocket = serverSocket.accept();
                System.out.println("New doctor connected.");

                //we start a new thread for each connection made
                new Thread(new ServerDoctorThread(doctorSocket)).start();
                } catch (IOException ex) {
                    if (isRunning) { // Solo registra el error si el servidor está activo
                        Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            releaseResourcesServer(serverSocket);
        }
    }
    
    public synchronized void doctorConnected() {
        connectedDoctors++;
    }

    public synchronized void doctorDisconnected() {
        if (connectedDoctors > 0) {
            connectedDoctors--;
        }
    }

    public synchronized int getConnectedDoctors() {
        return connectedDoctors;
    }

    public void stopServer() {
        System.out.println("Stopping server....");
        isRunning = false;
        
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); // Cierra el ServerSocket para liberar el puerto
            }
        } catch (IOException ex) {
        Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, "Error closing the server socket", ex);
        }
    }
    
    private static void releaseResourcesServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    class ServerDoctorThread implements Runnable {
        
        private final Socket doctorSocket;
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
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, "Error initializing streams", e);             
            } finally {
                try {
                    if (doctorSocket != null && !doctorSocket.isClosed()) {
                        doctorSocket.close();
                    }
                    System.out.println("Connection with doctor closed.");
                } catch (IOException e) {
                    Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, "Error closing socket", e);
                }
                releaseResourcesDoctor(in, out, doctorSocket);

            }
        }
        
        /**
         * Handles all requests from patient
         */
        private void handleDoctorsRequest(){
            boolean running = true;
            while(running){
                try {
                    String action = (String) in.readObject(); // Leer acción
                    
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
                        case "viewPatients":
                            handleViewPatients();
                            break;
                        case "sendFeedback":
                            receiveFeedbackFromDoctor();    
                        default:
                            out.writeObject("Not recognized action");
                            break; 
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
                    running = false;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
                    running = false;
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
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception exc) {
                try {
                    out.writeObject("Error during registration: " + exc.getMessage());
                    out.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        /**
         * Logs in by retrieving patient info from the registered doctor in the database
         */
        private void handleLogin() {
            try {
                String username = (String) in.readObject();
                String password = (String) in.readObject();
                User user = userManager.login(username, password);
                if(user == null){
                    System.out.println("user not found");
                }else{
                    System.out.println("Login of user successfull");}
                Doctor doctor= doctorManager.getDoctorByUser(user);
                if(doctor == null){
                    System.out.println("doctor not found");
                }else{
                    System.out.println("Login of doctor successfull");}
                doctor.setUser(user);
                out.writeObject(doctor);
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Releases all the resources of the socket established with the doctor
         */
        private void handleLogout(){
            try {
                releaseResourcesDoctor(in, out, doctorSocket);
                out.writeObject("Connection closed. ");
            } catch (IOException ex) {
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private void handleViewPatients (){
            try{
                System.out.println("Doctor is trying to viewPatients");
                Doctor doctor =  (Doctor) in.readObject();
                System.out.println("I have read the doctor.");
                List <Patient> patientsFromDoctor = patientManager.getPatientsFromDoctor(doctor.getId());
                
                System.out.println("Patients retrieved from manager: \n" + patientsFromDoctor);
                ListIterator it = patientsFromDoctor.listIterator();
                while(it.hasNext()){
                    System.out.println(it.next());
                }
                out.writeObject(patientsFromDoctor);
                out.flush();
            }catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        
        
        private void receiveFeedbackFromDoctor(){
            boolean sent=false;
            try {
                Feedback feedback=(Feedback) in.readObject();
                out.writeObject("Server has received feedback");
                out.flush();
                feedbackManager.createFeedback(feedback);
                if (sent) {
                    out.writeObject("Feedback enviado al paciente con éxito.");
                } else {
                out.writeObject("El paciente no está conectado.");
                }
                out.flush();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
        
        
        private static void releaseResourcesDoctor(ObjectInputStream in, ObjectOutputStream out,Socket socket) {

            try {
                in.close();
                out.close();
                
            } catch (IOException ex) {
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error while closing socket.");
            }
        }
}
    
    
    
    


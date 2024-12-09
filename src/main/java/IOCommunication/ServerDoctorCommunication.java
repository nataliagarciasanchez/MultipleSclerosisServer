/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import POJOs.Bitalino;
import POJOs.Doctor;
import POJOs.Feedback;
import POJOs.Patient;
import POJOs.Report;
import POJOs.Role;
import POJOs.Symptom;
import POJOs.User;
import ServerJDBC.JDBCBitalinoManager;
import ServerJDBC.JDBCDoctorManager;
import ServerJDBC.JDBCFeedbackManager;
import ServerJDBC.JDBCFilesManager;
import ServerJDBC.JDBCManager;
import ServerJDBC.JDBCPatientManager;
import ServerJDBC.JDBCReportManager;
import ServerJDBC.JDBCReport_SymptomsManager;
import ServerJDBC.JDBCRoleManager;
import ServerJDBC.JDBCSymptomManager;
import ServerJDBC.JDBCUserManager;
import java.io.File;
import java.io.IOException;
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
    private final int port;
    private final JDBCUserManager userManager;
    private final JDBCRoleManager roleManager;
    private final JDBCDoctorManager doctorManager;
    private final JDBCPatientManager patientManager;
    private final JDBCReportManager reportManager;
    private final JDBCSymptomManager symptomManager;
    private final JDBCReport_SymptomsManager rep_sympManager;
    private final JDBCBitalinoManager bitalinoManager;
    private final JDBCFeedbackManager feedbackManager;
    private final JDBCFilesManager fileManager;
    private final String confirmation = "DoctorServerCommunication";
    private int connectedDoctors = 0;
    private boolean isRunning = true;

    public ServerDoctorCommunication(int port, JDBCManager jdbcManager) {
        this.port=port;
        this.roleManager=new JDBCRoleManager(jdbcManager);
        this.userManager = new JDBCUserManager(jdbcManager, roleManager);
        this.doctorManager=new JDBCDoctorManager(jdbcManager);
        this.patientManager = new JDBCPatientManager(jdbcManager);
        this.reportManager = new JDBCReportManager(jdbcManager);
        this.symptomManager = new JDBCSymptomManager(jdbcManager);
        this.rep_sympManager = new JDBCReport_SymptomsManager(jdbcManager);
        this.bitalinoManager = new JDBCBitalinoManager(jdbcManager);
        this.feedbackManager=new JDBCFeedbackManager(jdbcManager);
        this.fileManager = new JDBCFilesManager(jdbcManager);
        
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
                doctorConnected();
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
                checkAuthorizedConnection();
                //handleDoctorsRequest();
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
                        case "getSignalsFile":
                            handleGetSignalsFile();
                        case "sendFeedback":
                            receiveFeedbackFromDoctor();    
                        default:
                            out.writeObject("Not recognized action");
                            break; 
                    }
                 } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, "Error with doctor communication", ex);
                        running = false;

                    }
            }
            } catch (IOException e) {
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, "Error initializing streams", e);             
            } finally {
                try {
                    if (doctorSocket != null && !doctorSocket.isClosed()) {
                        doctorSocket.close();
                        System.out.println("Connection with doctor closed.");
                        doctorDisconnected();
                    }
                    
                } catch (IOException e) {
                    Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, "Error closing socket", e);
                }
            }
        }
         
        private boolean checkAuthorizedConnection(){
             boolean authorization = false;
            try{
                String message = (String) in.readObject();
                if (!message.equals(confirmation)){ // confirmation message not valid - the one connected is not Doctor
                    System.out.println("Unauthorized connection.");
                    System.out.println("Closing connection...");
                    doctorSocket.close();
                }else{
                    System.out.println("Authorized connection.");
                    authorization = true;
                }
            
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
            return authorization;
        }
        /**
         * Registers into database the doctor
         */
        private void handleRegister() {
            try {
                User user = (User) in.readObject();
                boolean isValid = userManager.verifyValidUsername(user);
                if (isValid == true) { //the email is valid, there are no users using that email
                    userManager.registerUser(user);
                    Doctor doctor = (Doctor) in.readObject();
                    doctorManager.registerDoctor(doctor);

                    out.writeObject("Registered with success");
                    out.flush();
                } else {
                    out.writeObject("Username already in use. ");
                    out.flush();
                }
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
                
                if (user == null) {
                    out.writeObject("Invalid username or password.");
                    System.out.println("user not found");
                } else {
                    System.out.println("Login of user successfull");
                    Doctor doctor = doctorManager.getDoctorByUser(user);
                    if (doctor == null) {
                        out.writeObject("Invalid username or password.");
                        System.out.println("doctor not found");
                    } else {
                        System.out.println("Login of doctor successfull");
                        Role role = roleManager.getRoleByName("doctor");
                        user.setRole(role);
                        doctor.setUser(user);
                        out.writeObject(doctor);
                    }
                }
                
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

                User user = (User) in.readObject();
                Doctor doctor = (Doctor) in.readObject();
                
                System.out.println("Procession update request...");

                 // Validar los datos
                if (user == null || doctor == null) {
                    out.writeObject("Invalid data received.");
                    return;
                }

                // Validar que el paciente esté asociado con el usuario
                if (!doctor.getUser().getId().equals(user.getId())) {
                    out.writeObject("Patient and user mismatch.");
                    return;
                }
                
                userManager.updateUser(user);
                doctorManager.updateDoctor(doctor);
                
                out.writeObject("Information updated successfully.");


            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private void handleViewPatients (){
            try{
                System.out.println("Doctor is trying to viewPatients");
                Doctor doctor =  (Doctor) in.readObject();
                System.out.println("I have read the doctor.");
                List <Patient> patientsFromDoctor = patientManager.getPatientsFromDoctor(doctor.getId());
                
                System.out.println("Patients retrieved from manager: \n" + patientsFromDoctor);
                /*ListIterator it = patientsFromDoctor.listIterator();
                while(it.hasNext()){
                    System.out.println(it.next());
                }*/
                List <Report> reportsFromPatient = null;
                //List <Integer> symptomsIdFromReport = null;
                List <Symptom> symptomsFromReport = null;
                List <Bitalino> bitalinosFromReport = null;
                
                for (Patient patient : patientsFromDoctor){
                    //getting the list of reports from each patient of doctor
                    reportsFromPatient = reportManager.getReportsFromPatient(patient.getId());
                    patient.setReports(reportsFromPatient);
                    for (Report report : reportsFromPatient){
                        report.setPatient(patient);
                        //getting the list of symptoms from each report
                        symptomsFromReport = rep_sympManager.getSymptomsFromReport(report.getId());
                        report.setSymptom(symptomsFromReport);
                        //getting the list of bitalinos from each report
                        bitalinosFromReport = bitalinoManager.getBitalinosOfReport(report.getId());
                        report.setBitalinos(bitalinosFromReport);
                        
                    }
                }
                out.writeObject(patientsFromDoctor);
                out.flush();
            }catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerDoctorCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        
        private void handleGetSignalsFile(){
            
            try{
            System.out.println("Reaching for file...");
            Report report = (Report) in.readObject();
            
            Integer bitalinoEMG_id = report.getBitalinos().get(0).getId();
            Integer bitalinoECG_id = report.getBitalinos().get(1).getId();
            File signalsFile = fileManager.getFileFromBitalinosId(bitalinoEMG_id, bitalinoECG_id);
            
            out.writeObject(signalsFile);
            
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
    
    
    
    


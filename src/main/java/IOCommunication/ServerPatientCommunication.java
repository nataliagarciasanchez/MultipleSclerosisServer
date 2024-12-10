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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import TXT.TXTUtils;
import java.io.File;
import java.sql.Date;


/**
 * Class used to test all the method in the communication
 *
 * @author maipa
 */
public class ServerPatientCommunication {

    private ServerSocket serverSocket;
    private final int port;
    private final JDBCUserManager userManager;
    private final JDBCRoleManager roleManager;
    private final JDBCPatientManager patientManager;
    private final JDBCDoctorManager doctorManager;
    private final JDBCBitalinoManager bitalinoManager;
    private final JDBCReportManager reportManager;
    private final JDBCSymptomManager symptomManager;
    private JDBCFeedbackManager feedbackManager;
    private final JDBCReport_SymptomsManager reportSymptomsManager;
    private final JDBCFilesManager fileManager;
    private final String confirmation = "PatientServerCommunication";
    private int connectedPatients = 0;
    private boolean isRunning = true;
    private boolean authorization;

    public ServerPatientCommunication(int port, JDBCManager jdbcManager) {
        this.roleManager = new JDBCRoleManager(jdbcManager);
        this.userManager = new JDBCUserManager(jdbcManager, roleManager);
        this.patientManager = new JDBCPatientManager(jdbcManager);
        this.doctorManager = new JDBCDoctorManager(jdbcManager);
        this.bitalinoManager = new JDBCBitalinoManager(jdbcManager);
        this.reportManager = new JDBCReportManager(jdbcManager);
        this.symptomManager = new JDBCSymptomManager(jdbcManager);
        this.reportSymptomsManager=new JDBCReport_SymptomsManager(jdbcManager);
        this.feedbackManager = new JDBCFeedbackManager(jdbcManager);
        this.fileManager=new JDBCFilesManager(jdbcManager);
        this.port = port;
    }

    /**
     *
     * startServer is called from the menu when the Server is executed
     */
    public void startServer() {
        try {

            this.serverSocket = new ServerSocket(port);
            System.out.println("Server started. ");

            while (isRunning) {
                try {
                    Socket patientSocket = serverSocket.accept();

                    System.out.println("\nNew patient connected.");
                    
                    clientConnected();
                    

                    //we start a new thread for each connection made
                    new Thread(new ServerPatientThread(patientSocket)).start();

                } catch (IOException ex) {
                    if (isRunning) { // Solo registra el error si el servidor está activo
                        Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            releaseServerResources(serverSocket);
        }
    }

    public synchronized void clientConnected() {
        connectedPatients++;
    }

    public synchronized void clientDisconnected() {
        if (connectedPatients > 0) {
            connectedPatients--;
        }
    }

    public synchronized int getConnectedClients() {
        return connectedPatients;
    }

    public void stopServer() {
        System.out.println("Stopping server....");
        isRunning = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); // Cierra el ServerSocket para liberar el puerto
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, "Error closing the server socket", ex);
        }
    }

    private static void releaseServerResources(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class ServerPatientThread implements Runnable {

        private final Socket patientSocket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ServerPatientThread(Socket patientSocket) {
            this.patientSocket = patientSocket;

        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(patientSocket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(patientSocket.getInputStream());

                authorization = checkAuthorizedConnection();

                //handlePatientsRequest();
                boolean running = true;
                while (running && authorization) {
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
                                running = false;
                                handleLogout();
                                break;
                            case "updateInformation":
                                handleUpdateInformation();
                                break;
                            case "viewSymptoms":
                                handleViewSymptoms();
                                break;
                            case "sendReport":
                                handleReport();
                                break;
                            case "receiveFeedbacks":
                                sendFeedback2Patient();
                                break;

                            default:
                                out.writeObject("Not recognized action");
                                break;
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        //Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, "Error with patient communication", ex);
                        System.out.println("\nClient disconnected unexpectedly: " + ex.getMessage());
                        running = false;

                    }
                }
            } catch (IOException e) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, "Error initializing streams", e);
            } finally {
                try {
                    if (patientSocket != null && !patientSocket.isClosed()) {
                        patientSocket.close();
                    }
                    System.out.println("Connection with patient closed.");
                    clientDisconnected();
                } catch (IOException e) {
                    Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, "Error closing socket", e);
                }
            }
        }

        private boolean checkAuthorizedConnection() {
            boolean authorization = false;
            try {
                String message = (String) in.readObject();
                if (!message.equals(confirmation)) { // confirmation message not valid - the one connected is not Patient
                    out.writeObject(authorization);
                    System.out.println("Unauthorized connection.");
                    System.out.println("Closing connection...");
                    patientSocket.close();
                } else {
                    authorization = true;
                    out.writeObject(authorization);
                    System.out.println("Authorized connection.");
                   
                }

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }

            return authorization;
        }

        /**
         * Registers into database the patient
         */
        private void handleRegister() {
            try {
                System.out.println("Registering patient...");
                User user = (User) in.readObject();
                boolean isValid = userManager.verifyValidUsername(user);
                if (isValid == true) { //the email is valid, there are no users using that email
                    userManager.registerUser(user);
                    Patient patient = (Patient) in.readObject();
                    patientManager.registerPatient(patient);

                    out.writeObject("Registered with success");
                    out.flush();
                } else {
                    out.writeObject("Username already in use. ");
                    out.flush();
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Logs in by retrieving patient info from the registered patient in the
         * database
         */
        private void handleLogin() {
            try {
                System.out.println("Patient logging in...");
                String username = (String) in.readObject();
                String password = (String) in.readObject();
                User user = userManager.login(username, password);

                if (user == null) { //check that user exists
                    out.writeObject("Invalid username or password.");
                    System.out.println("Error while logging in.");
                } else {
                    Patient patient = patientManager.getPatientByUser(user); //check that user is a patient
                    if (patient == null) {
                        out.writeObject("Invalid username or password."); //user trying to log in without being a patient
                        System.out.println("Error while logging in.");
                    } else {
                        Doctor doctor = doctorManager.getDoctorById(patientManager.getDoctorIdFromPatient(patient));
                        Role role = roleManager.getRoleByName("patient");
                        user.setRole(role);
                        patient.setDoctor(doctor);
                        patient.setUser(user);
                        out.writeObject(patient);
                        System.out.println("Succesful log in!");
                    }
                }

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Releases all the resources of the socket established with the patient
         */
        private void handleLogout() {
            try {
                System.out.println("Patient logging out...");
                out.writeObject("Connection closed.");
                releaseResourcesPatient(in, out, patientSocket);
            } catch (IOException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Changes password in the database
         */
        private void handleUpdateInformation() {
            try {

                User user = (User) in.readObject();
                Patient patient = (Patient) in.readObject();
                
                System.out.println("Updating information...");

                 // Validar los datos
                if (user == null || patient == null) {
                    System.out.println("Error while updating");
                    out.writeObject("Invalid data received.");
                    return;
                }

                // Validar que el paciente esté asociado con el usuario
                if (!patient.getUser().getId().equals(user.getId())) {
                    out.writeObject("Patient and user mismatch.");
                    return;
                }
                
                userManager.updateUser(user);
                patientManager.updatePatient(patient);
                
                out.writeObject("Information updated successfully.");


            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Sends all the symptoms saved in the database
         */
        private void handleViewSymptoms() {

            try {
                System.out.println("Retrieving symptoms...");
                List<Symptom> listOfsymptoms = symptomManager.getListOfSymptoms();
                if(listOfsymptoms == null){
                    System.out.println("No symptoms where retrieved.");
                }

                out.writeObject(listOfsymptoms);
            } catch (IOException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Receives the report sent from the patient and creates to save it in
         * the database
         *
         * @return report
         */
        private void handleReport(){

            try {
                System.out.println("Handling report...");
                Report report = (Report) in.readObject();
                //System.out.println("Report: "+report);
                reportManager.createReport(report);
                out.writeObject("Report received correctly.");
                saveBitalinos(report);
                List<Symptom> symptoms=report.getSymptoms();

                for(Symptom symptom: symptoms){
                    int symptom_id=symptom.getId();
                    reportSymptomsManager.addSymptomToReport(symptom_id, report.getId());
                }
                String patientName = report.getPatient().getName();
                Date date = report.getDate();
                List <Bitalino> bitalinos = report.getBitalinos();
                
                saveBitalinos2Txt(patientName,bitalinos,date);


            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        /**
         * Saves the bitalinos with the signal_values in the database
         *
         * @param report
         */
        private void saveBitalinos(Report report) {
            System.out.println("Saving Bitalinos...");
            ArrayList<Bitalino> bitalinos = (ArrayList<Bitalino>) report.getBitalinos();
            for (Bitalino bitalino : bitalinos) {
                if (bitalino.getReport() == null) {
                    bitalino.setReport(report); // Link the report to the Bitalino
                }
                bitalinoManager.saveBitalino(bitalino);
            }
            
        }
        
        /**
         * Saves the bitalinos recorded physiological parameters (EMG, ECG) in a txt file 
         * @param patientName
         * @param bitalinos
         * @param date 
         */
        private void saveBitalinos2Txt(String patientName,List<Bitalino> bitalinos, Date date){
            System.out.println("Saving bitalinos to txt file...");
            StringBuilder allSignalValues = new StringBuilder();
            allSignalValues.append("Bitalino Signal Values:\n");
                Bitalino bitalinoEMG = bitalinos.get(0);
                Bitalino bitalinoECG = bitalinos.get(1);
                
                for (Bitalino bitalino : bitalinos) {
                    String signalValues = bitalino.getSignalValues();
                    allSignalValues.append("Signal ").append(bitalino.getSignal_type().toString()).append(": ").append(signalValues).append("\n");
                    allSignalValues.append("-------------------------------");
                }
                
                 // Insertar el archivo TXT en la base de datos como BLOB
                File file=TXTUtils.saveDataToTXT(patientName, date, allSignalValues.toString());
                fileManager.createFile(file, bitalinoEMG.getId(),bitalinoECG.getId());      
        }

        /**
         * Retrieves all feedbacks of that patient from the database 
         */
        public void sendFeedback2Patient() {
            System.out.println("Sending feedbacks to patient...");
            try {
                int patient_id = (int) in.readObject();
                List<Feedback> feedbacks = feedbackManager.getListOfFeedbacksOfPatient(patient_id);
                for (Feedback feedback : feedbacks){
                    int doctor_id = feedbackManager.getDoctorIdFromFeedback(feedback.getId());
                    feedback.setDoctor(doctorManager.getDoctorById(doctor_id));
                }
                out.writeObject(feedbacks);
                System.out.println(in.readObject());//confirmation from patient that the feedback has been sent
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private static void releaseResourcesPatient(ObjectInputStream in, ObjectOutputStream out, Socket socket) {

            try {
                out.close();
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerPatientCommunication.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error while closing socket.");
            }
        }
    }

}

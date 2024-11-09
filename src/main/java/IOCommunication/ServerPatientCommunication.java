/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import POJOs.Patient;
import POJOs.Role;
import POJOs.User;
import ServerJDBC.JDBCUserManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author maipa
 */
public class ServerPatientCommunication implements Runnable{

    private Socket patientSocket;
    private JDBCUserManager userManager;

    public ServerPatientCommunication(Socket clientSocket) {
        this.patientSocket = clientSocket;
        this.userManager = new JDBCUserManager(); // Iniciar el gestor de usuarios
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(patientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(patientSocket.getOutputStream())) {
             
            String action = (String) in.readObject(); // Leer acción del cliente

            switch (action) {
                case "register":
                    handleRegister(in, out);
                    break;
                case "login":
                    handleLogin(in, out);
                    break;
                case "changePassword":
                    handleChangePassword(in, out);
                    break;  
                case "findPatient":
                    handleFindPatient(in, out);
                    break;
                case "sendECGSignals":
                    handleECGSignals(in,out);
                    break;
                case "sendEMGSignals":
                    handleEMGSignals(in,out);
                    break;
                default:
                    out.writeObject("Not recognized action");
                    break;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                patientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            userManager.disconnect(); // Desconectar del EntityManager
        }
    }

    private void handleRegister(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        User user = (User) in.readObject();
        userManager.register(user);
        userManager.assignRole(user, userManager.getRoleByName("patient"));
        out.writeObject("Registered with success");
    }

    private void handleLogin(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        String username = (String) in.readObject();
        String password = (String) in.readObject();
        User user = userManager.login(username,password);
        out.writeObject((user != null) ? "Successful login" : "Incorrect introduced data");
    }

    private void handleChangePassword(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
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
    }
    
    
     private void handleFindPatient(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        String username = (String) in.readObject();
        String password = (String) in.readObject();
        
        User user=new User(username, password, new Role("patient"));
        Patient patient = userManager.getPatientByUser(user);
        
        if (patient != null) {
            out.writeObject("Patient found: " + patient);
        } else {
            out.writeObject("Patient not found or incorrect credentials");
        }
    }
     
    private void handleECGSignals(ObjectInputStream in, ObjectOutputStream out){
        //TODO recibe las señales del bitalino y en base a eso y a los symptoms debe crear un diagnóstico 
    }
    
    private void handleEMGSignals(ObjectInputStream in, ObjectOutputStream out){
        //TODO recibe las señales del bitalino y en base a eso y a los symptoms debe crear un diagnóstico  
    }
    
}

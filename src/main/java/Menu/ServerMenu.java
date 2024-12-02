/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menu;


import IOCommunication.ServerPatientCommunication;
import ServerJDBC.JDBCManager;
import javax.swing.SwingUtilities;


/**
 *
 * @author maipa
 */
public class ServerMenu {
    
    public static void main(String[] args) {
        
        JDBCManager jdbcManager = new JDBCManager();
        jdbcManager.connect();
        ServerPatientCommunication comPatient=new ServerPatientCommunication(9000,jdbcManager);
        //ServerDoctorCommunication comDoctor= new ServerDoctorCommunication (9001, jdbcManager);

        //Runs the admin graphic interface in a separate thread
        Thread adminThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                new ServerAdminGUI(comPatient, jdbcManager); 
            });
        });
        
        adminThread.start();
        
        //calls the main thread for handling connection of patients
        comPatient.startServer();
        //calls the main thread for handling connection of doctors
        //comDoctor.startServer ();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import ServerJDBC.JDBCManager;


/**
 *
 * @author maipa
 */
public class ComServerTemporalMenu {
    public static void main(String[] args) {
        
        JDBCManager jdbcManager = new JDBCManager();
        jdbcManager.connect();
        ServerPatientCommunication comPatient=new ServerPatientCommunication(9000,jdbcManager);
        comPatient.startServer();
        ServerDoctorCommunication comDoctor= new ServerDoctorCommunication (9000, jdbcManager);
        comDoctor.startServer ();
    }
}

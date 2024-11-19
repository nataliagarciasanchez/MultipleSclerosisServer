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
        ServerPatientCommunication com=new ServerPatientCommunication(1027,jdbcManager);
        com.startServer();
    }
}

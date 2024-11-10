/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import ServerJDBC.JDBCRoleManager;
import ServerJDBC.JDBCUserManager;
import java.net.Socket;

/**
 *
 * @author maipa
 */
public class ServerDoctorCommunication implements Runnable{

    private Socket doctorSocket;
    private JDBCUserManager userManager;
    private JDBCRoleManager roleManager;

    public ServerDoctorCommunication(JDBCRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}

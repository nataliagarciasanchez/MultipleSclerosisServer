/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menu;
import ServerInterfaces.UserManager;
import ServerJDBC.JDBCManager;
import javax.swing.*;

/**
 *
 * @author nataliagarciasanchez
 */
public class ServerMain extends JFrame{
    
        private static JDBCManager jdbcManager; 
        
        
        
        
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		jdbcManager = new JDBCManager();
                
                //jdbcManager.disconnect();
	}
        
        
}

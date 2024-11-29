/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import POJOs.Administrator;
import POJOs.User;
import ServerJDBC.JDBCManager;
import ServerJDBC.JDBCUserManager;
import java.util.Scanner;

/**
 *
 * @author Andreoti
 */
public class ServerAdminInterface {
    private static final Administrator admin = new Administrator();
    private static final String admin_password = "stop"; // futura contraseña IwAnTtOsToPtHeSeRvEr
    private boolean isRunning = true;
    private ServerPatientCommunication serverPatientCommunication;
    private JDBCUserManager userMan;
    
    
    
    public ServerAdminInterface(ServerPatientCommunication server, JDBCManager jdbcManager) {
        this.serverPatientCommunication = server;
        this.userMan = new JDBCUserManager(jdbcManager);
    }
    
    public void startAdminConsole() {
        System.out.println("Welcome to the administration console.");
        try (Scanner scanner = new Scanner(System.in)) {
            
            
            // Solicitar inicio de sesión antes de mostrar el menú
            if (!adminLogin(scanner)) {
                System.out.println("Acceso denegado. Cerrando consola de administración.");
                return; // Salir si las credenciales no son correctas
            }
            
            while (isRunning) {
                System.out.println("\nOpciones:");
                System.out.println("1. Verificar clientes conectados");
                System.out.println("2. Detener el servidor");
                System.out.println("3. Salir de la consola");
                System.out.print("Seleccione una opción: ");
                
                int option = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                
                switch (option) {
                    case 1:
                        checkConnectedClients();
                        break;
                    case 2:
                        stopServer(scanner);
                        break;
                    case 3:
                        System.out.println("Saliendo de la consola de administración.");
                        isRunning = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            }
        }
    }
    
    
    private void checkConnectedClients() {
        int connectedClients = serverPatientCommunication.getConnectedClients();
        System.out.println("Clientes conectados actualmente: " + connectedClients);
    }

    private void stopServer(Scanner scanner) {
        System.out.print("Password to stop the server: ");
        String password = scanner.nextLine();

        if (admin_password.equals(password)) {
            int patientsConnected = serverPatientCommunication.getConnectedClients();
            if (patientsConnected == 0) {
                System.out.println("There are no clients connected.\n Stopping the server...");
                serverPatientCommunication.stopServer();
                isRunning = false;
            } else {
                System.out.println("Cannot stop the server.\nThere are " + patientsConnected + " patients connected. " );
            }
        } else {
            System.out.println("Contraseña incorrecta.");
        }
    }
    
    
    private boolean adminLogin(Scanner scanner) {
    
    System.out.println("You need to log in.");
    
    // Se permiten 3 intentos para el inicio de sesión
    for (int attempts = 3; attempts > 0; attempts--) {
        //System.out.print("Email: ");
        //String username = scanner.nextLine();
        String username = "administrator@multipleSclerosis.com";
        //System.out.print("Password: ");
        //String password = scanner.nextLine();
        String password = "Password123";
        
        
        User user = userMan.login(username, password);
        if (user != null) {
            System.out.println("Log in succesfull. ");
            return true; 
        } else {
            System.out.println("Incorrect credentials. Remaining attempts: " + (attempts - 1));
        }
    }

    // Si se agotan los intentos, el acceso es denegado
    return false;
    }
    
}

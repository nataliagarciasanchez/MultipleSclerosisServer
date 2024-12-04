/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menu;


import IOCommunication.ServerDoctorCommunication;
import IOCommunication.ServerPatientCommunication;
import POJOs.Administrator;
import POJOs.User;
import ServerJDBC.JDBCAdministratorManager;
import ServerJDBC.JDBCManager;
import ServerJDBC.JDBCUserManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author maipa
 */
public class ServerAdminGUI {
    private final ServerPatientCommunication serverPatientCommunication;
    private final ServerDoctorCommunication serverDoctorCommunication;
    private JDBCUserManager userMan;
    private JDBCAdministratorManager adminMan;
    private boolean isRunning = true; 
    private final String admin_password = "stop";
    private final int MAX_ATTEMPS=3; //max attemps for the admin to login

    public ServerAdminGUI(ServerPatientCommunication serverPatientCommunication, ServerDoctorCommunication serverDoctorCommunication, JDBCManager jdbcManager) {
        this.serverPatientCommunication = serverPatientCommunication;
        this.serverDoctorCommunication = serverDoctorCommunication;
        this.userMan = new JDBCUserManager(jdbcManager);
        this.adminMan = new JDBCAdministratorManager(jdbcManager);
        
        // Perform login before showing admin options
        if (showLoginPanel()) {
            createGUI(); // If login is successful-> shows the admin interface
        } else {
            JOptionPane.showMessageDialog(null, "Access denied. App closing.",
                    "Autification Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0); // Exit the program if login fails
        }
        
    }
    
    private boolean showLoginPanel() {
        int attemps = 0;
        
        // Panel for login form
        while (attemps < MAX_ATTEMPS) {
            JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));

            // Username field
            JLabel userLabel = new JLabel("Username:");
            JTextField userField = new JTextField();
            loginPanel.add(userLabel);
            loginPanel.add(userField);

            // Password field
            JLabel passLabel = new JLabel("Password:");
            JPasswordField passField = new JPasswordField();
            loginPanel.add(passLabel);
            loginPanel.add(passField);

            // Adds an empty label for spacing
            loginPanel.add(new JLabel());

            // Displays the login panel in a dialog box
            int result = JOptionPane.showConfirmDialog(null, loginPanel, "Log in",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // If the user clicks OK, validate the credentials
            if (result == JOptionPane.OK_OPTION) {
                
                String username = userField.getText();
                String password = new String(passField.getPassword());
                User user = userMan.login(username, password);
                Administrator admin = adminMan.getAdministratorByUser(user);
                if (admin != null) {
                    return true;
                    
                } else {
                    attemps++;
                    int left_attemps=3-attemps;
                    JOptionPane.showMessageDialog(null, "Incorrect credentials." ,
                            "Autentification Error", JOptionPane.WARNING_MESSAGE);
                }

            }
        }
        return false;
    }

    private void createGUI() {
        // Crear la ventana principal
        JFrame frame = new JFrame("Administrator Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Panel para opciones
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        // Botón para verificar clientes conectados
        JButton checkClientsButton = new JButton("Verify connected clients");
        checkClientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkConnectedClients();
            }
        });
        panel.add(checkClientsButton);

        // Botón para detener el servidor
        JButton stopServerButton = new JButton("Stop server");
        stopServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer(frame);
            }
        });
        panel.add(stopServerButton);

        // Botón para salir de la consola
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isRunning = false;
                frame.dispose(); // Cierra la ventana
            }
        });
        panel.add(exitButton);

        // Añadir un mensaje de bienvenida
        JLabel welcomeLabel = new JLabel("Welcome to the administrator menu!", JLabel.CENTER);

        // Añadir componentes al frame
        frame.add(welcomeLabel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);

        // Mostrar la ventana
        frame.setVisible(true);
    }

    private void checkConnectedClients() {
        
        int connectedClients = serverPatientCommunication.getConnectedClients() + serverDoctorCommunication.getConnectedDoctors(); // Supongamos que este método existe
        JOptionPane.showMessageDialog(null, "Connected clients: " + connectedClients,
                "Connected clients", JOptionPane.INFORMATION_MESSAGE);
    }

    private void stopServer(JFrame frame) {
        // Solicitar la contraseña al usuario
        String password = JOptionPane.showInputDialog(frame, 
                "Please enter the password to stop the server:", 
                "Authentication", 
                JOptionPane.PLAIN_MESSAGE);

        // Verificar si la contraseña es correcta
        if (password != null && password.equals(admin_password)) { // Cambia "yourPasswordHere" por la contraseña real
            // Mostrar el cuadro de diálogo de confirmación
            int confirm = JOptionPane.showConfirmDialog(frame, 
                    "Are you sure you want to stop the server?", 
                    "Confirmation", 
                    JOptionPane.YES_NO_OPTION);

            // Si el usuario confirma, detener el servidor
            if (confirm == JOptionPane.YES_OPTION) {
                serverPatientCommunication.stopServer();
                JOptionPane.showMessageDialog(frame, 
                        "Server stopped with success.", 
                        "Stopped server", 
                        JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); 
            }
        } else if (password != null) {
            // Si la contraseña es incorrecta
            JOptionPane.showMessageDialog(frame, 
                    "Incorrect password. The server will not be stopped.", 
                    "Authentication Failed", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

import POJOs.User;
import ServerJPA.JPAUserManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author maipa
 */
public class ServerPatientCommunication implements Runnable{

    private Socket clientSocket;
    private JPAUserManager userManager;

    public ServerPatientCommunication(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.userManager = new JPAUserManager(); // Iniciar el gestor de usuarios
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
             
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
                default:
                    out.writeObject("Acción no reconocida");
                    break;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            userManager.disconnect(); // Desconectar del EntityManager
        }
    }

    private void handleRegister(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        User user = (User) in.readObject();
        userManager.register(user);
        userManager.assignRole(user, userManager.getRole("patient"));
        out.writeObject("Registro exitoso");
    }

    private void handleLogin(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        String username = (String) in.readObject();
        String password = (String) in.readObject();
        User user = userManager.login(username, password);
        out.writeObject((user != null) ? "Login exitoso" : "Credenciales incorrectas");
    }

    private void handleChangePassword(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        String username = (String) in.readObject();
        String oldPassword = (String) in.readObject();
        String newPassword = (String) in.readObject();

        User user = userManager.login(username, oldPassword);
        if (user != null) {
            userManager.updatePassword(user, newPassword);
            out.writeObject("Contraseña actualizada con éxito");
        } else {
            out.writeObject("Usuario o contraseña incorrectos");
        }
    }
    
}

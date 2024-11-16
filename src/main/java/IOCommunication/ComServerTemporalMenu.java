/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package IOCommunication;

/**
 *
 * @author maipa
 */
public class ComServerTemporalMenu {
    public static void main(String[] args) {
        ServerPatientCommunication com=new ServerPatientCommunication(9000);
        com.startServer();
    }
}

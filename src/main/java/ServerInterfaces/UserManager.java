/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;

import POJOs.Doctor;
import POJOs.Patient;
import POJOs.Role;
import POJOs.User;
import java.util.List;

/**
 *
 * @author maipa
 */
public interface UserManager {
    public void connect();
    public void disconnect();
    public void register(User user);
    public User login(String username, String password);
    public List<Role> getRoles();
    public void newRole(Role role);
    public void assignRole(User user, Role role);
    public Role getRoleById(Integer id);
    public Role getRoleByName(String roleName);
    public User getUser(String email);
    public User checkPassword(User user);
    public void changePassword(User user, String new_password);   
    public Patient getPatientByUser(User user); 
    public Doctor getDoctorByUser(User user);
    public User getUserById(Integer id);
    
}

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
    
    public void registerUser(User user);
    public User login(String email, String password);
    public void removeUserById(Integer id);
    public void updateUser(User u);
    public List <User> getListOfUsers();
    public User getUserById(Integer id);
    public User getUserByEmail(String email); // not a list because it must be unique
         
    public List <User> getUsersByRole(Integer role_id);
    public Patient getPatientByUser(User u); 
    //public Doctor getDoctorByUser(User u);
    
    public void assignRole2User(User user, Role role); // se hace en la tabla User
    public User checkPassword(User u);
    public void changePassword(User u, String new_password);  
    
    
}

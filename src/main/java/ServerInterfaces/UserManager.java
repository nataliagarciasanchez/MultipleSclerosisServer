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
    public boolean verifyValidUsername(User user);
    public void registerUser(User user);
    public User login(String email, String password);
    public void removeUserById(Integer id);
    public void updateUser(User user);
    public List <User> getListOfUsers();
    public User getUserById(Integer id);
    public User getUserByEmail(String email); // not a list because it must be unique
    public List <User> getUsersByRole(Integer role_id);
    public Role getRoleByUser(User user); 
}

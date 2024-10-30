/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;

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
    public void newUser(User user);
    public List<Role> getRoles();
    public void newRole(Role role);
    public Role getRole(Integer id);
    public User getUser(String email);
    public User checkPassword(String email, String password);
    public void changePassword(User user, String new_password);

}

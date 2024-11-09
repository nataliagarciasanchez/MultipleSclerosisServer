/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;

import POJOs.Role;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public interface RoleManager {
    public void createRole(Role r);
    public void removeRoleById(Integer id);
    public void updateRole(Role r);
    public List<Role> getListOfRoles();
    public Role getRoleById(Integer id);
    public Role getRoleByName(String roleName);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Role;
import POJOs.User;
import ServerInterfaces.RoleManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public class JDBCRoleManager implements RoleManager{
    
     private JDBCManager manager;
     private JDBCUserManager userMan;
    
    
    @Override
    public void createRole(Role role) {
        String sql = "INSERT INTO Roles (name) VALUES (?)";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, role.getName());
            p.executeUpdate();
            
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void removeRoleById(Integer id){
        String sql = "DELETE FROM Roles WHERE id = ?";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setInt(1, id);
            p.executeUpdate();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
    }

    @Override
    public void updateRole(Role r) {
        String sql = "UPDATE Roles SET name = ? WHERE id = ?";

        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, r.getName());
            p.executeUpdate();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Role> getListOfRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Roles";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            ResultSet rs = p.executeQuery(sql);
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                
                List <User> users = userMan.getUsersByRole(id);
                Role role = new Role(id, name, users);
                role.setId(rs.getInt("id"));
                roles.add(role);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }


    @Override
    public Role getRoleById(Integer id) {
        Role role = null;
        String sql = "SELECT * FROM Roles WHERE id = ?";
        try{
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");  
                List <User> users = userMan.getUsersByRole(id);
                role = new Role(id, name, users);
             
            }
            
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    
    @Override
    public Role getRoleByName(String roleName) {
        Role role = null;
        try {
            String sql = "SELECT * FROM Roles WHERE name LIKE ?";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, roleName);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                List <User> users = userMan.getUsersByRole(id);
                role = new Role(id, name, users);
                
            }
            p.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }
    
}

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
     

    public JDBCRoleManager() {
        
    }

    public JDBCRoleManager(JDBCManager manager) {
        this.manager = manager;
        
    }

    public void setManager(JDBCManager manager) {
        this.manager = manager;
    }

    

    
    @Override
    public void createRole(Role role) {
        String sql = "INSERT INTO Roles (name) VALUES (?)";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, role.getName());
            p.executeUpdate();
            // Obtener el ID generado por la base de datos
            ResultSet generatedKeys = p.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                role.setId(generatedId);  // Asigna el ID generado al objeto Role
            }
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
            p.setInt(2, r.getId());
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
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                
                
                Role role = new Role(id, name); // no le pasamos al lista de users
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
                
                role = new Role(id, name); // no le pasamos la lista de users
             
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
        System.out.println("Fetching role by name: " + roleName);
        Role role = null;
        try {
            String sql = "SELECT * FROM Roles WHERE name = ?";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, roleName.trim());
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
               
                role = new Role(id, name); // no le pasamos la lista de users
                
            }
            p.close();
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error in getRoleByName: " + e.getMessage());
            e.printStackTrace();
        }
        return role;
    }
    
}

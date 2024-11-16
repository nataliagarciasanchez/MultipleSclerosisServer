/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Administrator;
import POJOs.User;
import ServerInterfaces.AdministratorManager;
import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Andreoti
 */
public class JDBCAdministratorManager implements AdministratorManager {
    private JDBCManager manager;
    private JDBCUserManager userMan;

   

    public JDBCAdministratorManager(JDBCManager manager) {
        this.manager = manager;
        }
    
    @Override
    public void createAdministrator(Administrator a) {
        try{
            String sql = "INSERT INTO Administrators (name, user_id)"
                    +"values (?,?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1,a.getName());
            p.setInt(2,a.getUser().getId());
            p.executeUpdate();
            // Obtener el ID generado por la base de datos
            ResultSet generatedKeys = p.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                a.setId(generatedId);  // Asigna el ID generado al objeto Role
            }
            p.close();
            
            
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void removeAdministratorById(Integer id) {
        try {
            String sql = "DELETE FROM Administrators WHERE id = ?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
            e.printStackTrace();
        }
        
    }

    @Override
    public void updateAdministrator(Administrator a) {
        String sql = "UPDATE Administrators SET name = ? WHERE id = ?";
	try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	   
	    stmt.setString(1, a.getName());
	    stmt.setInt(2, a.getId());

	    stmt.executeUpdate();
	} catch (SQLException e) {
	     e.printStackTrace();
	}    
    }

    

    @Override
    public List<Administrator> getListOfAdministrators() {
        List<Administrator> administrators = new ArrayList<>();
	try {
	    String sql = "SELECT * FROM Administrators";
	    PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");

                Integer user_id = rs.getInt("user_id");
                User u = userMan.getUserById(user_id);
                    
                administrators.add(new Administrator (id,name, u));
            }
	            
            rs.close();
	    stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return administrators;
    }

   
    @Override
    public Administrator getAdministratorById(Integer id) {
        Administrator administrator=null;
	
	    try {
	        String sql = "SELECT * FROM Administrators WHERE id = ?";
	        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            Integer a_id = rs.getInt("id");
	            String name = rs.getString("name");
                    
                    Integer user_id = rs.getInt("user_id");
                    User u = userMan.getUserById(user_id);
                    
                    administrator = new Administrator (a_id,name, u);
	        }else {
	            System.out.println("Administrator with ID " + id + " not found.");
	        }

	        rs.close();
	        stmt.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return administrator;
	}
    

    @Override
    public List<Administrator> getAdministratorByName(String name) {
        List<Administrator> administrators = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Administrators WHERE name LIKE ?";
	    PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setString(1, "%" + name + "%");
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
	      	Integer a_id = rs.getInt("id");
	        String n = rs.getString("name");
                
	        Integer user_id = rs.getInt("user_id");
                User u = userMan.getUserById(user_id);
                    
                administrators.add(new Administrator(a_id,n,u));
		        
	    }if(administrators.isEmpty()){
	            System.out.println("Administrator with name " + name + " not found.");
	        }
	
            rs.close();
	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return administrators;
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Administrator;
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
   

    public JDBCAdministratorManager(JDBCManager manager) {
        this.manager = manager;
        }
    
    @Override
    public void createAdministrator(Administrator a) {
        try{
            String sql = "INSERT INTO administrators (name)"
                    +"values (?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1,a.getName());
            p.executeUpdate();
            p.close();
            
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    

    @Override
    public Administrator viewMyInfo(Integer administratorId) {
        Administrator administrator = null;
        try{
            String sql = "SELECT administrators.*"+
	                     "WHERE administrators.id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setInt(1, administratorId);
	        ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
	            Integer id = rs.getInt("id");
	            String name = rs.getString("name");
	            administrator = new Administrator(name, id);
	        } else {
	            System.out.println("Administrator with ID " + administratorId + " not found.");
	        }

	        rs.close();
	        stmt.close();
            
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return administrator;
    }

    @Override
    public List<Administrator> getListOfAdministrators() {
        List<Administrator> administrators = new ArrayList<>();
	        try {
	            String sql = "SELECT * FROM administrators";
	            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                Integer id = rs.getInt("ID");
	                String name = rs.getString("name");

	                Administrator administrator = new Administrator(name, id);
	                administrators.add(administrator);
	            }

	            rs.close();
	            stmt.close();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return administrators;
    }

    @Override
    public void removeAdministratorById(Integer id) {
        try {
            String sql = "DELETE FROM administrators WHERE id=?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void modifyAdministratorInfo(Integer id, String name) {
        String sql = "UPDATE administrators SET name = ? WHERE id = ?";
	try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	   
	    stmt.setString(1, name);
	    stmt.setInt(2, id);

	    stmt.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }    }

    @Override
    public Administrator searchAdministratorById(Integer id) {
        Administrator administrator=null;
	
	    try {
	        String sql = "SELECT * FROM administrators WHERE ID=?";
	        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            Integer a_id = rs.getInt("ID");
	            String name = rs.getString("name");
                    administrator = new Administrator (name,a_id);
	        }

	        rs.close();
	        stmt.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return administrator;
	}
    

    @Override
    public List<Administrator> searchAdministratorByName(String name) {
        List<Administrator> administrator = new ArrayList<>();
        try {
            String sql = "SELECT * FROM administrators WHERE name LIKE ?";
	    PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setString(1, "%" + name + "%");
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
	      	Integer a_id = rs.getInt("ID");
	        String n = rs.getString("name");
	        	           
	        administrator.add( new Administrator (n, a_id));
		        
	    }
	
            rs.close();
	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return administrator;
    }
    
}

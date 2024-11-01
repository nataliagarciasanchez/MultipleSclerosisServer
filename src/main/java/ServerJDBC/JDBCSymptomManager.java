/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Administrator;
import POJOs.Symptom;
import ServerInterfaces.SymptomManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public class JDBCSymptomManager implements SymptomManager{
    
    private JDBCManager manager;
    
    public JDBCSymptomManager(JDBCManager manager) {
        this.manager = manager;
        }

    @Override
    public void createSymptom(Symptom symptom) {
        try{
            String sql = "INSERT INTO symptoms (name)"
                    +"values (?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1,symptom.getName());
            p.executeUpdate();
            p.close();
            
        }catch(SQLException e) {
            e.printStackTrace();
        }}

    @Override
    public Symptom searchSymptomById(Integer id) {
        Symptom symptom=null;
	
	    try {
	        String sql = "SELECT * FROM symptoms WHERE ID=?";
	        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            Integer s_id = rs.getInt("ID");
	            String name = rs.getString("name");
                    symptom = new Symptom (s_id,name);
	        }else {
	            System.out.println("Symptom with ID " + id + " not found.");
	        }

	        rs.close();
	        stmt.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return symptom;
    }
    
    @Override
    public List<Symptom> searchSymptomByName(String name) {
        List<Symptom> symptoms = new ArrayList();
	
	    try {
	        String sql = "SELECT * FROM symptoms WHERE name LIKE ?";
	        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setString(1, name);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            Integer s_id = rs.getInt("ID");
	            String n = rs.getString("name");
                    
                    symptoms.add(new Symptom (s_id,n));
	        }else {
	            System.out.println("Symptom " + name + " not found.");
	        }

	        rs.close();
	        stmt.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return symptoms;
    }

    @Override
    public void updateSymptom(Symptom symptom) {
        String sql = "UPDATE symptoms SET name = ? WHERE id = ?";
	try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	   
	    stmt.setString(1, symptom.getName());
	    stmt.setInt(2, symptom.getId());

	    stmt.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    } }

    @Override
    public void deleteSymptom(Integer id) {
        try {
            String sql = "DELETE FROM symptoms WHERE id=?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
            e.printStackTrace();
        }}

    @Override
    public List<Symptom> getAllSymptoms() {
        List<Symptom> symptoms = new ArrayList<>();
	try {
	    String sql = "SELECT * FROM symptoms";
	    PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("ID");
                String name = rs.getString("name");

                Symptom symptom = new Symptom(id, name);
                symptoms.add(symptom);
            }
	            
            rs.close();
	    stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return symptoms;}
    
}

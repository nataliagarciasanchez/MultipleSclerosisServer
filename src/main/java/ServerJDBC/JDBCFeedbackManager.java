/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Feedback;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//FALTA EL PATIENT_ID DOCTOR_ID
/**
 *
 * @author noeli
 */
public class JDBCFeedbackManager {
      private JDBCManager manager;
   

    public JDBCFeedbackManager(JDBCManager manager) {
        this.manager = manager;
        }
  
    public void addFeedback(Feedback f){
    try{
            String sql = "INSERT INTO Feddbacks (date, message, doctor_id, patient_id)"
                    +"values (?,?,?,?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setDate(1,f.getDate());
            p.setString(2,f.getMessage());
            p.setInt(3,f.getDoctor().getId());
            p.setInt(4,f.getPatient().getId());
            p.executeUpdate();
            p.close();
            
        }catch(SQLException e) {
            e.printStackTrace();
        }}
    
    public Feedback viewFeedbackInfo(Integer id){
    Feedback feedback = null;
    
        try{
            String sql = "SELECT date, message FROM Feedbacks " +
	                     "WHERE feedbacks.id = ?";
	        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            Date date = rs.getDate("date");
                    String message = rs.getString("message");
                    feedback = new Feedback(date, message);
	        } else {
	            System.out.println("Feedback with ID " + id + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return feedback;
    }
    
    
    public void removeFeedbackById(Integer id){
    try {
            String sql = "DELETE FROM Feedbacks WHERE id=?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
            e.printStackTrace();
        }}
    
    public List<Feedback> getListOfFeedbacksByPatient(Integer patient_id){
        List<Feedback> feedbacks = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Feedbacks WHERE patient_id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1,  patient_id);
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
	        Date date = rs.getDate("date");
                String message= rs.getString("message");
                        
                feedbacks.add(new Feedback(date, message));
            }
            if(feedbacks.isEmpty()) {
	            System.out.println("Patient ID " + patient_id + " has no feedback saved.");
	        }

	    rs.close();
	    stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return feedbacks;
    }

    public List<Feedback> getListOfFeedbacksByDoctor(Integer doctor_id){
        List<Feedback> feedbacks = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Feedbacks WHERE doctor_id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1,  doctor_id);
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
	        Date date = rs.getDate("date");
                String message= rs.getString("message");
                        
                feedbacks.add(new Feedback(date, message));
            }
            if(feedbacks.isEmpty()) {
	            System.out.println("Doctor ID " + doctor_id + " has no feedback saved.");
	        }

	    rs.close();
	    stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return feedbacks;
    }

    public Feedback searchFeedbackById(Integer id){
    Feedback feedback = null;
        try{
            String sql = "SELECT * FROM Feedbacks WHERE id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
	        Date date = rs.getDate("date");
                String message = rs.getString("message");
                                
                feedback = new Feedback(date, message);
	        }else {
	            System.out.println("Feedback with ID " + id + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return feedback;
}
    public void modifyFeedback(Feedback f){
        String sql = "UPDATE Feedbacks SET date = ?, message = ?, doctor_id= ?, patient_id= WHERE id = ?";
	try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setDate(1, f.getDate());
            stmt.setString (2, f.getMessage());
            stmt.setInt(3, f.getPatient().getId());
	    stmt.setInt(4, f.getId());

	    stmt.executeUpdate();
	} catch (SQLException e) {
	     e.printStackTrace();
	} 
    }

}
    


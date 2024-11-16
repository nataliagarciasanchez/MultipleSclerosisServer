/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Doctor;
import POJOs.Feedback;
import POJOs.Patient;
import ServerInterfaces.FeedbackManager;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author noeli
 */
public class JDBCFeedbackManager implements FeedbackManager {
    
    private JDBCManager manager;
    private JDBCDoctorManager doctorman;
    private JDBCPatientManager patientman;
   

    public JDBCFeedbackManager(JDBCManager manager) {
        this.manager = manager;
        }
  
    
    @Override
    public void createFeedback(Feedback f) {
        try{
            String sql = "INSERT INTO Feedbacks (date, message, doctor_id, patient_id)"
                    +"values (?,?,?,?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setDate(1,f.getDate());
            p.setString(2,f.getMessage());
            p.setInt(3,f.getDoctor().getId());
            p.setInt(4,f.getPatient().getId());
            p.executeUpdate();
            // Obtener el ID generado por la base de datos
            ResultSet generatedKeys = p.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                f.setId(generatedId);  // Asigna el ID generado al objeto Role
            }
            p.close();
            
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeFeedbackById(Integer id) {
        try {
            String sql = "DELETE FROM Feedbacks WHERE id = ?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
            e.printStackTrace();
        }}

    @Override
    public void updateFeedback(Feedback f) {
        String sql = "UPDATE Feedbacks SET date = ?, message = ?, doctor_id= ?, patient_id= ? WHERE id = ?";
	try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setDate(1, f.getDate());
            stmt.setString (2, f.getMessage());
            stmt.setInt(3, f.getDoctor().getId());
            stmt.setInt(4, f.getPatient().getId());
	    stmt.setInt(5, f.getId());

	    stmt.executeUpdate();
	} catch (SQLException e) {
	     e.printStackTrace();
	} }

    @Override
    public Feedback getFeedBackById(Integer id) {
        Feedback feedback = null;
        try{
            String sql = "SELECT * FROM Feedbacks WHERE id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
	        Date date = rs.getDate("date");
                String message = rs.getString("message");
                Integer doctor_id= rs.getInt("doctor_id");
                Doctor d= doctorman.getDoctorById(doctor_id);
                Integer patient_id= rs.getInt("patient_id");
                Patient p= patientman.getPatientById(patient_id);                
                feedback = new Feedback(id, message, date, d, p);
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

    @Override
    public List<Feedback> getFeedBackByDate(Date date) {
        List<Feedback> feedbacks = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Feedbacks WHERE date = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setDate(1,  date);
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Integer id = rs.getInt("id");
	        String message = rs.getString("message");
                Integer doctor_id = rs.getInt("doctor_id");
                Doctor d = doctorman.getDoctorById(doctor_id);
                Integer patient_id = rs.getInt("patient_id");
                Patient p = patientman.getPatientById(patient_id); 
                
                feedbacks.add(new Feedback(id, message, date, d, p));
            }
            if(feedbacks.isEmpty()) {
	            System.out.println("No feedbacks found for date " + date);
	        }

	    rs.close();
	    stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return feedbacks;}

    @Override
    public List<Feedback> getListOfFeedbacksOfPatient(Integer patient_id) {
        List<Feedback> feedbacks = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Feedbacks WHERE patient_id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1,  patient_id);
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Integer id= rs.getInt("id");
	        Date date = rs.getDate("date");
                String message = rs.getString("message");
                Integer doctor_id= rs.getInt("doctor_id");
                Doctor d = doctorman.getDoctorById(doctor_id);
                Patient p = patientman.getPatientById(patient_id); 
                
                feedbacks.add(new Feedback(id, message, date, d, p));
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

    @Override
    public List<Feedback> getListOfFeedbacksOfDoctor(Integer doctor_id) {
        List<Feedback> feedbacks = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Feedbacks WHERE doctor_id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1,  doctor_id);
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Integer id= rs.getInt("id");
	        Date date = rs.getDate("date");
                String message = rs.getString("message");
                Doctor d = doctorman.getDoctorById(doctor_id);
                Integer patient_id= rs.getInt("patient_id");
                Patient p = patientman.getPatientById(patient_id); 
                
                feedbacks.add(new Feedback(id, message, date, d, p));
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

}
    


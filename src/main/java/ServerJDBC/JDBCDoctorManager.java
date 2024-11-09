/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;
import POJOs.Doctor;
import POJOs.Feedback;
import POJOs.Patient;
import POJOs.Specialty;
import POJOs.User;
import ServerInterfaces.DoctorManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public class JDBCDoctorManager implements DoctorManager {
    
    private JDBCManager manager;
   // private JPAUserManager userMan;
    private JDBCPatientManager patientMan;
    private JDBCFeedbackManager feedbackMan;
   

    public JDBCDoctorManager(JDBCManager manager) {
        this.manager = manager;
        }

    @Override
    public void createDoctor(Doctor d) {
        try{
            String sql = "INSERT INTO Doctors (name,specialty,user_id)"
                          +"values (?,?,?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1,d.getName());
            p.setString(2,d.getSpecialty().toString());
            p.setInt(1,d.getUser().getId());
            p.executeUpdate();
            p.close();

                }catch(SQLException e) {
                    e.printStackTrace();
                }    
    }

    @Override
    public void removeDoctorById(Integer id) {
        try {
            String sql = "DELETE FROM Doctors WHERE id = ?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
	e.printStackTrace();
	}
    }

    @Override
    public void updateDoctor(Doctor d) {
        String sql = "UPDATE Doctors SET name = ?, specialty = ? WHERE id = ?";
	try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setString(1, d.getName());
                stmt.setString(2,d.getSpecialty().toString());
	        stmt.setInt(3, d.getId());

	        stmt.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
    }

    @Override
    public List<Doctor> getListOfDoctors() {
        List<Doctor> doctors = new ArrayList<>();
	try {
	    String sql = "SELECT * FROM Doctors";
	    PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	        Integer id = rs.getInt("id");
	        String name = rs.getString("name");
                String specialtyString = rs.getString("specialty");
                Specialty specialty = Specialty.valueOf(specialtyString);
                
                Integer user_id = rs.getInt("user_id");
	        User u = userMan.getUserById(user_id);
                
                List <Patient> patients = patientMan.getPatientsFromDoctor(id);
                
                List <Feedback> feedbacks = feedbackMan.getListOfFeedbacksOfDoctor(id);
                
                Doctor doctor = new Doctor(id, name, specialty, u, patients, feedbacks);
	        doctors.add(doctor);
	        }

	        rs.close();
	        stmt.close();

	    }catch (SQLException e) {
	    e.printStackTrace();
	    }
	return doctors;
    }

    

    @Override
    public Doctor getDoctorById(Integer id) {
        Doctor doctor = null;
        try{
            String sql = "SELECT * FROM Doctors WHERE id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
	        Integer d_id = rs.getInt("id");
	        String name = rs.getString("name");
                String specialtyString = rs.getString("specialty");
                Specialty specialty = Specialty.valueOf(specialtyString);
                
	        Integer user_id = rs.getInt("user_id");
	        User u = userMan.getUserById(user_id);
                
                List <Patient> patients = patientMan.getPatientsFromDoctor(id);
                
                List <Feedback> feedbacks = feedbackMan.getListOfFeedbacksOfDoctor(id);
                
                doctor = new Doctor(id, name, specialty, u, patients, feedbacks);
	        }else {
	            System.out.println("Doctor with ID " + id + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return doctor;
    }

    @Override
    public List<Doctor> getDoctorByName(String name) {
        List<Doctor> doctors = new ArrayList<>();
	try {
            String sql = "SELECT * FROM Doctors WHERE name LIKE ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer d_id = rs.getInt("id");
                String n = rs.getString("name");
                String specialtyString = rs.getString("specialty");
                Specialty specialty = Specialty.valueOf(specialtyString);
                
                Integer user_id = rs.getInt("user_id");
	        User u = userMan.getUserById(user_id);
                
                List <Patient> patients = patientMan.getPatientsFromDoctor(d_id);
                
                List <Feedback> feedbacks = feedbackMan.getListOfFeedbacksOfDoctor(d_id);
                
                doctors.add(new Doctor(d_id, name, specialty, u, patients, feedbacks));

            }
            if(doctors.isEmpty()){
	            System.out.println("Doctor with name " + name + " not found.");
	        }
            rs.close();
            stmt.close();
            } catch (SQLException e) {
	e.printStackTrace();
	}
	return doctors;
    }

    
}

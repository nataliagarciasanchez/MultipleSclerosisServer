/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;
import POJOs.Doctor;
import POJOs.Gender;
import POJOs.Patient;
import POJOs.User;
import ServerInterfaces.PatientManager;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public class JDBCPatientManager implements PatientManager{
    
    private JDBCManager manager;
   
    public JDBCPatientManager(JDBCManager manager) {
        this.manager = manager;
        }

    @Override
    public void createPatient(Patient p) {
        try{
            String sql = "INSERT INTO patients (name, dob, gender, phone, doctor_id, user_id)"
                          +"values (?,?,?,?,?,?)";
            PreparedStatement ps = manager.getConnection().prepareStatement(sql);
            ps.setString(1,p.getName());
            ps.setString(2,p.getDob().toString());
            ps.setString(3,p.getGender().toString());
            ps.setInt(4,p.getPhone());
            ps.setInt(6,p.getDoctor().getId());
            ps.setInt(6,p.getUser().getId());
            ps.executeUpdate();
            ps.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }   
    }

    @Override
    public Patient viewMyInfo(Integer id) {
        Patient patient = null;
        try{
            String sql = "SELECT * FROM patients " +
	                     "WHERE patients.id = ?";
	        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            Integer p_id = rs.getInt("id");
	            String name = rs.getString("name");
                    Date dob = rs.getDate("dob");
                    String genderString = rs.getString("gender");
                    Gender gender = Gender.valueOf(genderString);
                    Integer phone = rs.getInt("phone");
	            patient = new Patient(p_id, name, dob, gender, phone);
	        } else {
	            System.out.println("Patient with ID " + id + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return patient;
    }

    @Override
    public List<Patient> getListOfPatients() {
        List<Patient> patients = new ArrayList<>();
	try {
	    String sql = "SELECT * FROM patients";
	    PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	        Integer id = rs.getInt("ID");
	        String name = rs.getString("name");
                Date dob = rs.getDate("dob");
                String genderString = rs.getString("gender");
                Gender gender = Gender.valueOf(genderString);
                Integer phone = rs.getInt("phone");
	        
                Patient patient = new Patient(id, name, dob, gender, phone);
	        patients.add(patient);
	        }

	        rs.close();
	        stmt.close();

	    }catch (SQLException e) {
	    e.printStackTrace();
	    }
	return patients;}

    @Override
    public void removePatientById(Integer id) {
        try {
            String sql = "DELETE FROM patients WHERE id=?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
	e.printStackTrace();
	}
    }

    @Override
    public Patient searchPatientById(Integer id) {
        Patient patient = null;
        try{
            String sql = "SELECT * FROM patients WHERE ID = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
	        Integer p_id = rs.getInt("ID");
	        String name = rs.getString("name");
                Date dob = rs.getDate("dob");
                String genderString = rs.getString("gender");
                Gender gender = Gender.valueOf(genderString);
                Integer phone = rs.getInt("phone");
	        
                patient = new Patient(p_id, name, dob, gender, phone);
	        }else {
	            System.out.println("Patient with ID " + id + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return patient;
    }

    @Override
    public List<Patient> searchPatientByName(String name) {
        List<Patient> patients = new ArrayList<>();
        try{
            String sql = "SELECT * FROM patients WHERE name LIKE ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setString(1,  "%" + name + "%");
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
	        Integer id = rs.getInt("ID");
	        String n = rs.getString("name");
                Date dob = rs.getDate("dob");
                String genderString = rs.getString("gender");
                Gender gender = Gender.valueOf(genderString);
                Integer phone = rs.getInt("phone");
	        
                patients.add(new Patient(id, n, dob, gender, phone));
                
	    }
            if(patients.isEmpty()) {
	            System.out.println("Patient with name " + name + " not found.");
	        }

	    rs.close();
	    stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return patients;
    }

    @Override
    public List<Patient> getPatientsFromDoctor(Integer doctorId) {
        List<Patient> patients = new ArrayList<>();
        try{
            String sql = "SELECT * FROM patients WHERE doctor_id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1,  doctorId);
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
	        Integer id = rs.getInt("ID");
	        String n = rs.getString("name");
                Date dob = rs.getDate("dob");
                String genderString = rs.getString("gender");
                Gender gender = Gender.valueOf(genderString);
                Integer phone = rs.getInt("phone");
	        
                patients.add(new Patient(id, n, dob, gender, phone));
                
	    }
            if(patients.isEmpty()) {
	            System.out.println("Doctor with ID " + doctorId + " has no patients.");
	        }

	    rs.close();
	    stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return patients;}

    @Override
    public void modifyPatientInfo(Integer id, String name, Date dob, Gender gender,Integer phone, Integer doctorId, Integer userId){
        String sql = "UPDATE patients SET name = ?, dob = ?, gender = ?, phone = ?, doctor_id = ?, user_id = ? WHERE id = ?";
	try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setString(1, name);
                stmt.setDate(2,dob);
                stmt.setString(3, gender.toString());
                stmt.setInt(4, phone);
	        stmt.setInt(5, doctorId);
                stmt.setInt(6, userId);

	        stmt.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
    
    };
    
}

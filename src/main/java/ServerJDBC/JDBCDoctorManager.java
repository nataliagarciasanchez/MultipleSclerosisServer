/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;
import POJOs.Doctor;
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
   

    public JDBCDoctorManager(JDBCManager manager) {
        this.manager = manager;
        }

    @Override
    public void createDoctor(Doctor d) {
        try{
            String sql = "INSERT INTO doctors (name,specialty,user_id)"
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
            String sql = "DELETE FROM doctors WHERE id=?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
	e.printStackTrace();
	}
    }

    @Override
    public void updateDoctor(Doctor d) {
        String sql = "UPDATE doctors SET name = ?, specialty = ? WHERE id = ?";
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
	    String sql = "SELECT * FROM doctors";
	    PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	        Integer id = rs.getInt("ID");
	        String name = rs.getString("name");
                String specialtyString = rs.getString("specialty");
                Specialty specialty = Specialty.valueOf(specialtyString);
	        
                Doctor doctor = new Doctor(name, id, specialty);
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
            String sql = "SELECT * FROM doctors WHERE ID = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
	        Integer d_id = rs.getInt("ID");
	        String name = rs.getString("name");
                String specialtyString = rs.getString("specialty");
                Specialty specialty = Specialty.valueOf(specialtyString);
	           
	        doctor = new Doctor (name, d_id, specialty);
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
            String sql = "SELECT * FROM doctors WHERE name LIKE ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer d_id = rs.getInt("ID");
                String n = rs.getString("name");
                String specialtyString = rs.getString("specialty");
                Specialty specialty = Specialty.valueOf(specialtyString);

                doctors.add( new Doctor (n, d_id,specialty));
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

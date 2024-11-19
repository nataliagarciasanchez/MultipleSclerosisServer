/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;
import POJOs.Doctor;
import POJOs.Feedback;
import POJOs.Gender;
import POJOs.Patient;
import POJOs.Report;
import POJOs.User;
import ServerInterfaces.PatientManager;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Andreoti
 */
public class JDBCPatientManager implements PatientManager{
    
    private JDBCManager manager;
    private JDBCDoctorManager doctorMan;
    private JDBCUserManager userMan;
    private JDBCReportManager reportMan;
    private JDBCFeedbackManager feedbackMan;

    
   
    public JDBCPatientManager(JDBCManager manager) {
        this.manager = manager;
        this.doctorMan=new JDBCDoctorManager(manager);
    }

    @Override
    public void registerPatient(Patient p, User user) {
        //before registering the patient, the server assigns a random doc
        int doc_id=assignDoctor2Patient();
        //Doctor chosen_doc=doctorMan.getDoctorById(doc_id);
        
        try{
            String sql = "INSERT INTO Patients (name, surname, NIF, dob, gender, phone, doctor_id, user_id)"
                          +"values (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = manager.getConnection().prepareStatement(sql);
            ps.setString(1,p.getName());
            ps.setString(2,p.getSurname());
            ps.setString(3,p.getNIF());
            ps.setDate(4,p.getDob());
            ps.setString(5,p.getGender().toString());
            ps.setString(6,p.getPhone());
            ps.setInt(7,doc_id);
            ps.setInt(8,user.getId());
            ps.executeUpdate();
            // Obtener el ID generado por la base de datos
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                p.setId(generatedId);  // Asigna el ID generado al objeto Role
            }
            ps.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }   
    }
    
    @Override
    
    public int assignDoctor2Patient(){
        List<Integer> docs_ids=doctorMan.getDoctorIds();
        if (docs_ids == null || docs_ids.isEmpty()) {
            throw new IllegalArgumentException("The list must not be null or empty.");
        }
        Random random = new Random();
        int randomIndex = random.nextInt(docs_ids.size());
        return docs_ids.get(randomIndex);
    }
    
    @Override
    public void removePatientById(Integer id) {
        try {
            String sql = "DELETE FROM Patients WHERE id = ?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
	e.printStackTrace();
	}
    }
    
    @Override
    public void updatePatient(Patient p) {
        
        String sql = "UPDATE Patients SET name = ?, surname= ?, NIF= ?, dob = ?, gender = ?, phone = ?, doctor_id = ?, user_id = ? WHERE id = ?";
	try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setString(1, p.getName());
                stmt.setString(2, p.getSurname());
                stmt.setString(3, p.getNIF());
                stmt.setDate(4, p.getDob());
                stmt.setString(5, p.getGender().toString());
                stmt.setString(6, p.getPhone());
	        stmt.setInt(7, p.getDoctor().getId());
                stmt.setInt(8, p.getUser().getId());
                stmt.setInt(8, p.getId());

	        stmt.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
    }
  
    

    @Override
    public List<Patient> getListOfPatients() {
        List<Patient> patients = new ArrayList<>();
	try {
	    String sql = "SELECT * FROM Patients";
	    PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	        Integer p_id = rs.getInt("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String NIF = rs.getString("NIF");
                Date dob = rs.getDate("dob");
                String genderString = rs.getString("gender");
                Gender gender = Gender.valueOf(genderString);
                String phone = rs.getString("phone");
                
                                                            
                Patient patient = new Patient(p_id, name, surname, NIF, dob, gender, phone);
	        patients.add(patient);
	        }

	        rs.close();
	        stmt.close();

	    }catch (SQLException e) {
	    e.printStackTrace();
	    }
	return patients;}

   

    @Override
    public Patient getPatientById(Integer id) {
        Patient patient = null;
        try{
            String sql = "SELECT * FROM Patients WHERE id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
	        
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String NIF = rs.getString("NIF");
                Date dob = rs.getDate("dob");
                String genderString = rs.getString("gender");
                Gender gender = Gender.valueOf(genderString);
                String phone = rs.getString("phone");
                                	        
                                                             
                patient = new Patient(id, name, surname, NIF, dob, gender, phone);
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
    public List<Patient> getPatientByName(String name) {
        List<Patient> patients = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Patients WHERE name LIKE ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setString(1,  "%" + name + "%");
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
	        Integer p_id = rs.getInt("id");
	        String n = rs.getString("name");
                String surname = rs.getString("surname");
                String NIF = rs.getString("NIF");
                Date dob = rs.getDate("dob");
                String genderString = rs.getString("gender");
                Gender gender = Gender.valueOf(genderString);
                String phone = rs.getString("phone");
                
                Patient pat = new Patient(p_id, name, surname, NIF, dob, gender, phone);                          
                patients.add(pat);  
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
            String sql = "SELECT * FROM Patients WHERE doctor_id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1,  doctorId);
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
	        Integer id = rs.getInt("id");
	        String n = rs.getString("name");
                String surname = rs.getString("surname");
                String NIF = rs.getString("NIF");
                Date dob = rs.getDate("dob");
                String genderString = rs.getString("gender");
                Gender gender = Gender.valueOf(genderString);
                String phone = rs.getString("phone");
	        
                Patient pat = new Patient(id, n, surname, NIF, dob, gender, phone);                          
                patients.add(pat);  
	    }
            if(patients.isEmpty()) {
	            System.out.println("Doctor with ID " + doctorId + " has no patients.");
	        }

	    rs.close();
	    stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return patients;
    }

    /**
     * Retrieves a patient associated with a specific user.
     * 
     * @param user 
     * @return patient
     */
    @Override
    public Patient getPatientByUser(User user) { 
        Patient patient = null;
        String sql = "SELECT * FROM Patients WHERE user_id = ?";

        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setInt(1, user.getId());
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                // Extracting the data from the ResultSet into variables
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String nif = rs.getString("NIF");
                java.sql.Date dob = rs.getDate("dob");
                Gender gender = Gender.valueOf(rs.getString("gender"));
                String phone = rs.getString("phone");            
               

                patient = new Patient(id, name, surname, nif, dob, gender, phone);

                p.close();
                rs.close();
            } else {
                System.out.println("Patient with user_id " + user.getId() + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }
    
    @Override
    public int getDoctorIdFromPatient(Patient p){
        Integer doctorId = null;
        String sql = "SELECT * FROM Patients WHERE id = ?";

        try {
            PreparedStatement pre = manager.getConnection().prepareStatement(sql);
            pre.setInt(1, p.getId());
            ResultSet rs = pre.executeQuery();

            if (rs.next()) {
                // Extracting the data from the ResultSet into variables
                
                doctorId = rs.getInt("doctor_id");
                
                pre.close();
                rs.close();
            } else {
                System.out.println("Patient with id " + p.getId() + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorId;
    
    }

    
    
}

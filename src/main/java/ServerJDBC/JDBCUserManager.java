/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.*;
import ServerInterfaces.UserManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author laura
 */
public class JDBCUserManager implements UserManager {

    private JDBCManager manager;
    private JDBCDoctorManager doctorMan;
    private JDBCPatientManager patientMan;
    private JDBCFeedbackManager feedbackMan;
    private JDBCReportManager reportMan;
    private JDBCRoleManager roleMan;

    public JDBCUserManager() {
        
    }



    @Override
    public void register(User user) {
        try {
        String sql = "INSERT INTO Users (email, password, role_id) VALUES (?, ?, ?)";
        PreparedStatement p = manager.getConnection().prepareStatement(sql);
        p.setString(1, user.getEmail());
        p.setString(2, user.getPassword()); //  aplicar el hash aquí si es necesario
        p.setInt(3, user.getRole().getId());
        p.executeUpdate();
        p.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    @Override
    public User login(String email, String password) {
        User user = null;  // Inicializamos user antes del bloque try
        String sql = "SELECT * FROM Users WHERE email LIKE ? AND password LIKE ?";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, email);
            p.setString(2, password);  // Encriptar la contraseña para la comparación
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                Integer id = rs.getInt("id");
                Integer role_id = rs.getInt("role_id");
                Role role = roleMan.getRoleById(role_id);
                
                // Creamos el objeto User
                user = new User(id, email, password, role);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; 
    }
    
    @Override
    public void removeUserById(Integer id) {
        String sql = "DELETE FROM Users WHERE id = ?";
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
    public void updateUser(User user) {
        String sql = "UPDATE Users SET email = ?, password = ?, role_id = ? WHERE id = ?";

        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);

            p.setString(1, user.getEmail());
            p.setString(2, user.getPassword()); // Puedes aplicar el hash aquí si es necesario
            p.setInt(3, user.getRole().getId());
            p.setInt(4, user.getId());

            p.executeUpdate();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<User> getListOfUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";

        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                // Leemos toda la información en una línea
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Role role =  roleMan.getRoleById(rs.getInt("role_id"));

                // Creamos el objeto User usando las variables leídas
                User user = new User(id, email, password, role);
                
                users.add(user);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User getUserById(Integer id) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE id = ?";

        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setInt(1, id);  
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("password");
                Role role =  roleMan.getRoleById(rs.getInt("role_id"));

                user = new User(id, email, password, role);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
    
    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, email);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                Role role =  roleMan.getRoleById(rs.getInt("role_id"));
                return new User(rs.getString("email"), rs.getString("password"), role);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public void assignRole2User(User user, Role role) {
        String sql = "UPDATE Users SET role_id = ? WHERE id = ?";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setInt(1, role.getId());  // Asigna el ID del rol al usuario
            p.setInt(2, user.getId());  // Especifica el usuario por su ID
            p.executeUpdate();

            // Actualizar la relación en el objeto en memoria
            user.setRole(role);
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User checkPassword(User u) {
        User user = null;
        
        try {
            String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, u.getEmail());
            p.setString(2, u.getPassword());
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Role role =  roleMan.getRoleById(rs.getInt("role_id"));
                user = new User(id, email, password, role);
            }
            
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    @Override
    public void changePassword(User user, String new_password) {
        String sql = "UPDATE Users SET password = ? WHERE id = ?";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            // Asigna la nueva contraseña directamente sin encriptación
            p.setString(1, new_password);
            p.setInt(2, user.getId());

            // Ejecuta la actualización en la base de datos
            p.executeUpdate();
            System.out.println("Password changed successfully for user ID: " + user.getId());
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List <User> getUsersByRole(Integer role_id){
        List <User> users = new ArrayList();
        String sql = "SELECT * FROM Users WHERE role_id = ?";

        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setInt(1, role_id);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                // Extracting the data from the ResultSet into variables
                Integer id = rs.getInt("id");
                String email = rs.getString("name");
                String password = rs.getString("password");
                Role r =  roleMan.getRoleById(role_id);
                User user = new User (id, email, password, r);
                users.add(user);
                p.close();
                rs.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    
    }
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
                Integer doctorId = rs.getInt("doctor_id");

                // Using the constructor to create the Patient object
                Doctor doctor = doctorMan.getDoctorById(doctorId);
                List<Report> reports = reportMan.getReportsFromPatient(id);
                List<Feedback> feedbacks = feedbackMan.getListOfFeedbacksOfPatient(id);

                patient = new Patient(id, name, surname, nif, dob, gender, phone, doctor, reports, feedbacks, user);

                p.close();
                rs.close();
            }else{
                System.out.println("Patient with user_id " + user.getId() + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }


    /* private Doctor getDoctorById(int doctorId) {}*/
    /*@Override
    public Doctor getDoctorByUser(User user) {
        Doctor doctor = null;
        String sql = "SELECT * FROM Doctors WHERE user_id = ?";

        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setInt(1, user.getId());
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                // Extracting the data from the ResultSet into variables
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String specialtyString = rs.getString("specialty");
                Specialty specialty = Specialty.valueOf(specialtyString);

                // Retrieve the list of patients associated with the doctor
                List<Patient> patients = patientMan.getPatientsFromDoctor(id);

                // Retrieve the list of feedback associated with the doctor
                List<Feedback> feedbacks = feedbackMan.getListOfFeedbacksOfDoctor(id);

                // Using the constructor to create the Doctor object
                doctor = new Doctor(id, name, specialty, user, patients, feedbacks);

                p.close();
                rs.close();
            }else{
            System.out.println("Doctor with user_id " + user.getId() + " not found.");
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }*/
    
    
    


    

}

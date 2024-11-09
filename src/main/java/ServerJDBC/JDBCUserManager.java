/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.*;
import ServerInterfaces.UserManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author laura
 */
public class JDBCUserManager implements UserManager {

    private Connection connection;
    private JDBCDoctorManager doctorMan;
    private JDBCPatientManager patientMan;
    private JDBCFeedbackManager feedbackMan;

    public JDBCUserManager() {
        this.connect();
    }

//Conecction to the database
    @Override
    public void connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:db/MultipleSclerosisServer.db");
            Statement stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");  // Habilitar claves foráneas en SQLite
            stmt.close();

            if (getRoles().isEmpty()) {
                // Si no hay roles, crearlos
                newRole(new Role("administrator"));
                newRole(new Role("doctor"));
                newRole(new Role("patient"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(User user) {
        String sql = "INSERT INTO Users (email, password, role_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());//add hashPassword
            stmt.setInt(3, user.getRole().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User login(String email, String password) {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);  // Encriptar la contraseña para la comparación
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Obtenemos el rol del usuario
                Role role = getRole(rs.getInt("role_id"));
                // Creamos y retornamos el objeto User
                return new User(rs.getString("email"), rs.getString("password"), role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Devuelve null si no encuentra al usuario
    }

    @Override
    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Roles";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Role role = new Role(rs.getString("name"));
                role.setId(rs.getInt("id"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public void newRole(Role role) {
        String sql = "INSERT INTO Roles (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void assignRole(User user, Role role) {
        String sql = "UPDATE Users SET role_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, role.getId());  // Asigna el ID del rol al usuario
            stmt.setInt(2, user.getId());  // Especifica el usuario por su ID
            stmt.executeUpdate();

            // Actualizar la relación en el objeto en memoria
            user.setRole(role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Role getRole(Integer id) {
        String sql = "SELECT * FROM Roles WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = new Role(rs.getString("name"));
                role.setId(rs.getInt("id"));
                return role;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Role getRoleFromType(String roleName) {
        String sql = "SELECT * FROM Roles WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = new Role(rs.getString("name"));
                role.setId(rs.getInt("id"));
                return role;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUser(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = getRole(rs.getInt("role_id"));
                return new User(rs.getString("email"), rs.getString("password"), role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User checkPassword(String email, String password) {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = getRole(rs.getInt("role_id"));
                return new User(rs.getString("email"), rs.getString("password"), role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void changePassword(User user, String new_password) {
        String sql = "UPDATE Users SET password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Asigna la nueva contraseña directamente sin encriptación
            stmt.setString(1, new_password);
            stmt.setInt(2, user.getId());

            // Ejecuta la actualización en la base de datos
            stmt.executeUpdate();
            System.out.println("Password changed successfully for user ID: " + user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Patient getPatientByUser(User user) {
        Patient patient = null;
        String sql = "SELECT * FROM Patients WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setUser(user);  // Asignamos el objeto User ya conocido
                patient.setName(rs.getString("name"));
                patient.setSurname(rs.getString("surname"));
                patient.setNIF(rs.getString("NIF"));
                patient.setDob(rs.getDate("dob"));
                patient.setGender(Gender.valueOf(rs.getString("gender")));  // Suponiendo que Gender es un Enum
                patient.setPhone(rs.getString("phone"));

                Integer doctorId = rs.getInt("doctor_id");
                Doctor doctor = doctorMan.getDoctorById(doctorId);  // Método auxiliar para obtener el doctor
                patient.setDoctor(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    /* private Doctor getDoctorById(int doctorId) {}*/
    @Override
    public Doctor getDoctorByUser(User user) {
        Doctor doctor = null;
        String sql = "SELECT * FROM Doctors WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setName(rs.getString("name"));
                doctor.setUser(user);  // Asigna el usuario ya conocido

               
            // Recuperar la especialidad
              String specialtyString = rs.getString("specialty");
              Specialty specialty = Specialty.valueOf(specialtyString);
              doctor.setSpecialty(specialty);

            // Recuperar la lista de pacientes asociados
            List<Patient> patients = patientMan.getPatientsFromDoctor(doctor.getId());
            doctor.setPatients(patients);

            // Recuperar la lista de feedback asociados
            List<Feedback> feedbacks = feedbackMan.getListOfFeedbacksOfDoctor(doctor.getId());
            doctor.setFeedback(feedbacks);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    @Override
    public User getUserById(Integer id) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = getRole(rs.getInt("role_id"));
                return new User(rs.getString("email"), rs.getString("password"), role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

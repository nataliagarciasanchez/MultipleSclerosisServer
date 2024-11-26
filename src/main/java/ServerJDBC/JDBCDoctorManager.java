/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Doctor;
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
    

    /**
     * Constructor for JDBCDoctorManager.
     *
     * @param manager the JDBCManager instance used for database connections.
     */
    public JDBCDoctorManager(JDBCManager manager) {
        this.manager = manager;

    }

    /**
     * Creates a new Doctor entry in the database.
     *
     * @param d the Doctor object containing the data to be inserted.
     */
    @Override
    public void registerDoctor(Doctor d) {
        try {
            String sql = "INSERT INTO Doctors (name, surname, specialty, user_id)"
                    + "values (?,?,?,?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, d.getName());
            p.setString(2,d.getSurname());
            p.setString(3, d.getSpecialty());
            p.setInt(4, d.getUser().getId());
            p.executeUpdate();
            // Obtener el ID generado por la base de datos
            ResultSet generatedKeys = p.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                d.setId(generatedId);  // Asigna el ID generado al objeto Role
            }
            p.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a Doctor entry from the database by its ID.
     *
     * @param id the ID of the Doctor to be removed.
     */
    @Override
    public void removeDoctorById(Integer id) {
        try {
            String sql = "DELETE FROM Doctors WHERE id = ?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing Doctor entry in the database.
     *
     * @param d the Doctor object containing updated data.
     */
    @Override
    public void updateDoctor(Doctor d) {
        String sql = "UPDATE Doctors SET name = ?, surname = ? WHERE id = ?";
        try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
            stmt.setString(1, d.getName());
            //stmt.setString(2,d.getSpecialty().toString());
            stmt.setString(2, d.getSurname());
            stmt.setInt(3, d.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retrieves a list of all Doctors from the database.
     *
     * @return a list of Doctor objects.
     */
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
                String surname = rs.getString("surname");

                Doctor doctor = new Doctor(id, name, surname);
                doctors.add(doctor);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    /**
     * Retrieves a Doctor from the database by its ID.
     *
     * @param id the ID of the Doctor to retrieve.
     * @return the Doctor object if found, or null otherwise.
     */
    @Override
    public Doctor getDoctorById(Integer id) {
        Doctor doctor = null;
        try {
            String sql = "SELECT * FROM Doctors WHERE id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String name = rs.getString("name");
                String surname = rs.getString("surname");

                doctor = new Doctor(id, name, surname);

            } else {
                System.out.println("Doctor with ID " + id + " not found.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    /**
     * Retrieves a list of Doctors by their name.
     *
     * @param name the name of the Doctor(s) to search for.
     * @return a list of Doctor objects matching the given name.
     */
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
                String surname = rs.getString("surname");
                Doctor d = new Doctor(d_id, name, surname);
                doctors.add(d);

            }
            if (doctors.isEmpty()) {
                System.out.println("Doctor with name " + name + " not found.");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    /**
     * Retrieves a doctor associated to the specified user
     *
     * @param user
     * @return
     */
    @Override
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
                String surname = rs.getString("surname");

                // Using the constructor to create the Doctor object
                doctor = new Doctor(id, name, surname);

                p.close();
                rs.close();
            } else {
                System.out.println("Doctor with user_id " + user.getId() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    /**
     * Method that returns a list of all the ids of the doctors registered to
     * assign a random doc to a patient
     *
     * @return list of ids of doctors
     */
    @Override
    public List<Integer> getDoctorIds() {
        List<Integer> doctorIds = new ArrayList<>();
        String query = "SELECT id FROM Doctors";

        try (PreparedStatement pstmt = manager.getConnection().prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                doctorIds.add(rs.getInt("id"));
            }

        } catch (SQLException e) {
            System.err.println("Error while fetching doctor IDs: " + e.getMessage());
            e.printStackTrace();
        }
        return doctorIds;
    }
}

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

    /**
     * Default constructor.
     */
    public JDBCUserManager() {

    }

    /**
     * Constructor with {@link JDBCManager}.
     *
     * @param manager the {@link JDBCManager} instance for database connection
     * management.
     *
     */
    public JDBCUserManager(JDBCManager manager) {
        this.manager = manager;
        this.roleMan=new JDBCRoleManager(manager);
    }

    /**
     * Constructor with {@link JDBCManager} and {@link JDBCRoleManager}.
     *
     * @param manager the {@link JDBCManager} instance for database connection
     * management.
     * @param roleManager the {@link JDBCRoleManager} instance for managing
     * roles.
     */
    public JDBCUserManager(JDBCManager manager, JDBCRoleManager roleManager) {
        this.manager = manager;
        this.roleMan = roleManager;
    }

    /**
     * Registers a new user in the database.
     *
     * @param user the {@link User} object containing user information.
     */
    @Override
    public void registerUser(User user) {
        try {
            String sql = "INSERT INTO Users (email, password, role_id) VALUES (?, ?, ?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, user.getEmail());
            p.setString(2, user.getPassword()); //  aplicar el hash aquí si es necesario
            p.setInt(3, user.getRole().getId());
            p.executeUpdate();
            // Obtener el ID generado por la base de datos
            ResultSet generatedKeys = p.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                user.setId(generatedId);  // Asigna el ID generado al objeto Role
            }
            p.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs in a user by verifying their email and password.
     *
     * @param email the email of the user.
     * @param password the password of the user.
     * @return the {@link User} object if login is successful, or null if
     * unsuccessful.
     */
    @Override
    public User login(String email, String password) {
        User user = null;  // Inicializamos user antes del bloque try
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?"; // mejor = en vez de LIKE, para buscar solo coincidencias exactas
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, email);
            p.setString(2, password);  // Encriptar la contraseña para la comparación
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                Integer id = rs.getInt("id");
                //Integer role_id = rs.getInt("role_id");
                //Role role = roleMan.getRoleById(role_id);

                // Creamos el objeto User
                user = new User(id, email, password);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Removes a user from the database by their ID.
     * 
     * @param id the ID of the user to be removed.
     */
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

    /**
     * Updates user information in the database.
     * 
     * @param user the {@link User} object with updated information.
     */
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

    /**
     * Retrieves a list of all users in the database.
     * 
     * @return a {@link List} of {@link User} objects.
     */
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
                //Role role = roleMan.getRoleById(rs.getInt("role_id"));

                // Creamos el objeto User usando las variables leídas
                User user = new User(id, email, password);

                users.add(user);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Retrieves a user from the database by their ID.
     * 
     * @param id the ID of the user.
     * @return the {@link User} object if found, or null otherwise.
     */
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
                //Role role = roleMan.getRoleById(rs.getInt("role_id"));

                user = new User(id, email, password);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Retrieves a user from the database by their email.
     * 
     * @param email the email of the user.
     * @return the {@link User} object if found, or null otherwise.
     */
    @Override
    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE email = ?";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, email);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt("id");
                String password = rs.getString("password");
                //Role role = roleMan.getRoleById(rs.getInt("role_id"));
                user = new User(id, email, password);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    

    /**
     * Retrieves a list of users by their role.
     * 
     * @param role_id the ID of the role.
     * @return a {@link List} of {@link User} objects with the specified role.
     */
    @Override
    public List<User> getUsersByRole(Integer role_id) {
        List<User> users = new ArrayList();
        String sql = "SELECT * FROM Users WHERE role_id = ?";

        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setInt(1, role_id);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                // Extracting the data from the ResultSet into variables
                Integer id = rs.getInt("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                //Role r = roleMan.getRoleById(role_id);

                User user = new User(id, email, password);
                users.add(user);

            }
            rs.close();
            p.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;

    }
}

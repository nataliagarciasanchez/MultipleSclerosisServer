/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Bitalino;
import POJOs.Report;
import POJOs.SignalType;
import ServerInterfaces.BitalinoManager;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//DUARTION IS FLOAT NOT INTEGER

/**
 *
 * @author noeli
 */
public class JDBCBitalinoManager implements BitalinoManager {

    private JDBCManager manager;

    /**
     * Constructor for the JDBCBitalinoManager.
     *
     * @param manager the {@link JDBCManager} instance used to manage database
     * connections.
     */
    public JDBCBitalinoManager(JDBCManager manager) {
        this.manager = manager;
    }

    /**
     * Creates a new Bitalino entry in the database.
     *
     * @param b the {@link Bitalino} object containing the data to be inserted.
     */
    @Override
    public void saveBitalino(Bitalino b) {
        String sql = "INSERT INTO Bitalinos (date, signal_type, duration, signal_values, report_id)"
                + "values (?,?,?,?,?)";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setDate(1, b.getDate());
            p.setString(2, b.getSignal_type().toString());
            p.setString(3, b.getDuration().toString());
            p.setString(4, b.getSignalValues());
            p.setInt(5, b.getReport().getId());

            p.executeUpdate();
            // Obtener el ID generado por la base de datos
            ResultSet generatedKeys = p.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                b.setId(generatedId);  // Asigna el ID generado al objeto Role
            }
            p.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a Bitalino entry from the database by its ID.
     *
     * @param id the ID of the Bitalino to be removed.
     */
    @Override
    public void removeBitalinoById(Integer id) {
        String sql = "DELETE FROM Bitalinos WHERE id = ?";
        try {
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing Bitalino entry in the database.
     *
     * @param b the {@link Bitalino} object containing updated data.
     */
    @Override
    public void updateBitalino(Bitalino b) {
        String sql = "UPDATE Bitalinos SET date = ?, signal_type = ?,"
                + " duration = ?, signal_values = ?, report_id=? WHERE id = ?";
        try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
            stmt.setDate(1, b.getDate());
            stmt.setString(2, b.getSignal_type().toString());
            stmt.setFloat(3, b.getDuration());
            stmt.setInt(4, b.getReport().getId());
            stmt.setString(5, b.getSignalValues());
            stmt.setInt(6, b.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retrieves a list of all Bitalinos from the database.
     *
     * @return a {@link List} of {@link Bitalino} objects.
     */
    @Override
    public List<Bitalino> getListOfBitalinos() {
        List<Bitalino> bitalinos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Bitalinos";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                Date date = rs.getDate("date");
                String signalTypeString = rs.getString("signal_type");
                SignalType signal_type = SignalType.valueOf(signalTypeString);
                String signalValues = rs.getString("signal_values");
                Bitalino b = new Bitalino(id, date, signal_type, signalValues);
                bitalinos.add(b);
            }
            if (bitalinos.isEmpty()){
                System.out.println("No bitalinos found in the db");
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bitalinos;
    }

    /**
     * Retrieves a Bitalino from the database by its ID.
     *
     * @param id the ID of the Bitalino to retrieve.
     * @return the {@link Bitalino} object if found, or null otherwise.
     */
    @Override
    public Bitalino getBitalinoById(Integer id) {
        Bitalino bitalino = null;
        try {
            String sql = "SELECT * FROM Bitalinos WHERE id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date b_date = rs.getDate("date");
                String signalTypeString = rs.getString("signal_type");
                SignalType signal_type = SignalType.valueOf(signalTypeString);
                String signalValues = rs.getString("signal_values");
                bitalino = new Bitalino(id, b_date, signal_type, signalValues);
            } else {
                System.out.println("Bitalino with ID " + id + " not found.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bitalino;
    }

    /**
     * Retrieves a list of Bitalinos from the database by a specific date.
     *
     * @param d the date to search for.
     * @return a {@link List} of {@link Bitalino} objects matching the given date.
     */
    @Override
    public List<Bitalino> getBitalinosByDate(Date d) {
        List<Bitalino> bitalinos = new ArrayList();
        try {
            String sql = "SELECT * FROM Bitalinos WHERE date = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
            stmt.setDate(1, d);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                Date b_date = rs.getDate("date");
                String signalTypeString = rs.getString("signal_type");
                SignalType signal_type = SignalType.valueOf(signalTypeString);
                String signalValues = rs.getString("signal_values");
                
                Bitalino b = new Bitalino(id, b_date, signal_type, signalValues);
                bitalinos.add(b);
            }
            
            if (bitalinos.isEmpty()){
                System.out.println("Bitalino with date " + d + " not found.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bitalinos;
    }

    /**
     * Retrieves a list of Bitalinos associated with a specific report ID.
     *
     * @param report_id the ID of the report to retrieve Bitalinos for.
     * @return a {@link List} of {@link Bitalino} objects associated with the given report ID.
     */
    @Override
    public List<Bitalino> getBitalinosOfReport(Integer report_id) {
        List<Bitalino> bitalinos = new ArrayList();
        try {
            String sql = "SELECT * FROM Bitalinos WHERE report_id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
            stmt.setInt(1, report_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                Date b_date = rs.getDate("date");
                String signalTypeString = rs.getString("signal_type");
                SignalType signal_type = SignalType.valueOf(signalTypeString);
                String signalValues = rs.getString("signal_values");
                
                Bitalino b = new Bitalino(id, b_date, signal_type, signalValues);
                bitalinos.add(b);
            }
            if (bitalinos.isEmpty()) {
                System.out.println("Bitalino of report " + report_id + " not found.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bitalinos;
    }
}

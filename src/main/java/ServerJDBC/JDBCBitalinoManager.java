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
public class JDBCBitalinoManager implements BitalinoManager{
     private JDBCManager manager;
     private JDBCReportManager reportman;
   
    public JDBCBitalinoManager(JDBCManager manager) {
        this.manager = manager;
        }
   
 
    @Override
    public void createBitalino(Bitalino b) {
        String sql = "INSERT INTO Bitalinos (date, signal_type, file_path, duration, report_id)"
                          +"values (?,?,?,?,?)";
        try{
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setDate(1,b.getDate());
            p.setString(2,b.getSignal_type().toString());
            p.setString(3,b.getFile_path());
            p.setString(4,b.getDuration().toString());
            p.setInt(5,b.getReport().getId());
      
            p.executeUpdate();
            // Obtener el ID generado por la base de datos
            ResultSet generatedKeys = p.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                b.setId(generatedId);  // Asigna el ID generado al objeto Role
            }
            p.close();

                }catch(SQLException e) {
                    e.printStackTrace();
                }   }

    @Override
    public void removeBitalinoById(Integer id) {
        String sql = "DELETE FROM Bitalinos WHERE id = ?";
        try {
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
	e.printStackTrace();
	}
    }

    @Override
    public void updateBitalino(Bitalino b) {
        String sql = "UPDATE Bitalinos SET date = ?, signal_type = ?, file_path = ?,"
                + " duration = ?, report_id=? WHERE id = ?";
        try {
                        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setDate(1, b.getDate());
            stmt.setString(2, b.getSignal_type().toString());
            stmt.setString(3, b.getFile_path());
            stmt.setFloat(4, b.getDuration());
            stmt.setInt(5, b.getReport().getId());
            stmt.setInt(6, b.getId());
            
            stmt.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }}

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
                String file_path = rs.getString("file_path");
                Float duration = rs.getFloat("duration");
                String signalTypeString = rs.getString("signal_type");
                SignalType ST = SignalType.valueOf(signalTypeString);
                Integer report_id = rs.getInt("report_id");
                Report r = reportman.getReportById(report_id);
	        Bitalino b = new Bitalino(id,date,ST,file_path, duration,r);
	        bitalinos.add(b);
	    }
	    rs.close();
	    stmt.close();

	    }catch (SQLException e) {
	    e.printStackTrace();
	    }
	return bitalinos;
    }

    @Override
    public Bitalino getBitalinoById(Integer id) {
        Bitalino bitalino = null;
        try{
            String sql = "SELECT * FROM Bitalinos WHERE id = ?";
	        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            Date b_date = rs.getDate("date");
	            String signalTypeString = rs.getString("signal_type");
                    String b_file_path = rs.getNString("file_path");
                    Float b_duration = rs.getFloat("duration");
                    Integer report_id = rs.getInt("report_id");
                    Report r= reportman.getReportById(report_id);
                    SignalType signal_type = SignalType.valueOf(signalTypeString);
                    bitalino = new Bitalino(id, b_date, signal_type,b_file_path, b_duration, r);
	        } else {
	            System.out.println("Bitalino with ID " + id + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return bitalino;
    }

    @Override
    public List<Bitalino> getBitalinosByDate(Date d) {
        List <Bitalino> bitalinos = new ArrayList();
        try{
            String sql = "SELECT * FROM Bitalinos WHERE date = ?";
	        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setDate(1, d);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
                    Integer id = rs.getInt("id");
	            Date b_date = rs.getDate("date");
	            String signalTypeString = rs.getString("signal_type");
                    String b_file_path= rs.getString("file_path");
                    Float b_duration = rs.getFloat("duration");
                    Integer report_id = rs.getInt("report_id");
                    Report r = reportman.getReportById(report_id);
                    SignalType signal_type = SignalType.valueOf(signalTypeString);
                    
                    bitalinos.add(new Bitalino(id, b_date, signal_type,b_file_path, b_duration, r));
	        } else {
	            System.out.println("Bitalino with date " + d + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return bitalinos;
    }

    @Override
    public List<Bitalino> getBitalinosOfReport(Integer report_id) {
        List <Bitalino> bitalinos = new ArrayList();
        try{
            String sql = "SELECT * FROM Bitalinos WHERE report_id = ?";
	        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setInt(1, report_id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
                    Integer id = rs.getInt("id");
	            Date b_date = rs.getDate("date");
	            String signalTypeString = rs.getString("signal_type");
                    String b_file_path = rs.getString("file_path");
                    Float b_duration = rs.getFloat("duration");
                    Integer r_id = rs.getInt("report_id");
                    Report r = reportman.getReportById(r_id);
                    SignalType signal_type = SignalType.valueOf(signalTypeString);
                    
                    bitalinos.add(new Bitalino(id, b_date, signal_type,b_file_path, b_duration, r));
	        } else {
	            System.out.println("Bitalino with date " + report_id + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return bitalinos;
    }
    }
    
    
    
    
    
   

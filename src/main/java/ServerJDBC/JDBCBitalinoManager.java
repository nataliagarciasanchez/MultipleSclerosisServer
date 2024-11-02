/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Bitalino;
import POJOs.Doctor;
import POJOs.Report;
import POJOs.SignalType;
import POJOs.Specialty;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author noeli
 */
public class JDBCBitalinoManager {
     private JDBCManager manager;
   
    public JDBCBitalinoManager(JDBCManager manager) {
        this.manager = manager;
        }
   
  public void createBitalino(Bitalino b){
        try{
            String sql = "INSERT INTO Bitalinos (date,signal_type,file_path,duration,report_id)"
                          +"values (?,?,?,?,?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1,b.getDate().toString());
            p.setString(2,b.getSignal_type().toString());
            p.setString(3,b.getFile_path());
            p.setString(4,b.getDuration().toString());
            p.setInt(5,b.getReport().getId());
      
            p.executeUpdate();
            p.close();

                }catch(SQLException e) {
                    e.printStackTrace();
                }    
    }

    public Bitalino viewBitalino(Integer id) {
        Bitalino bitalino = null;
        try{
            String sql = "SELECT date,signal_type, duration FROM Bitalinos " +
	                     "WHERE bitalinos.id = ?";
	        PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            Date b_date = rs.getDate("date");
	            String signalTypeString = rs.getString("signal_type");
                    Integer b_duration= rs.getInt ("duration");
                    String b_file_path= rs.getNString("file_path");
                    SignalType signal_type = SignalType.valueOf(signalTypeString);
                    bitalino = new Bitalino(b_date, signal_type,b_file_path, b_duration);
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
    
    public List<Bitalino> getListOfBitalinos(){
    List<Bitalino> bitalinos = new ArrayList<>();
	try {
	    String sql = "SELECT * FROM Bitalinos";
	    PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();

                    
	    while (rs.next()) {
	        Integer id = rs.getInt("id");
	        Date date = rs.getDate("date");
                String file_path= rs.getString("file_path");
                Integer duration= rs.getInt("duration");
                String signalTypeString = rs.getString("signal_type");
                SignalType ST = SignalType.valueOf(signalTypeString);
                
	        Bitalino b= new Bitalino(id,date,ST,file_path, duration);
	        bitalinos.add(b);
	        }
	        rs.close();
	        stmt.close();

	    }catch (SQLException e) {
	    e.printStackTrace();
	    }
	return bitalinos;
    }
    
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
    
    
    
    
    
    
}

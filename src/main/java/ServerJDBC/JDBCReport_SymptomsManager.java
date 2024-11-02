/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Bitalino;
import POJOs.SignalType;
import POJOs.Symptom;
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
public class JDBCReport_SymptomsManager {
    
    private JDBCManager manager;
   
    public JDBCReport_SymptomsManager(JDBCManager manager) {
        this.manager = manager;
        }
    Report_Symptoms ("
                + "      symptom_id INTEGER NOT NULL,"
                + "      report_id INTEGER NOT NULL,"
                + "      PRIMARY KEY(report_id, symptom_id)"
                + "     FOREIGN KEY (patient_id) REFERENCES Patients(id)"
                + "     FOREIGN KEY (report_id) REFERENCES Reports(id)"
                + ");";
     public void addSymptomToReport(Integer symptomId, Integer reportId){
         try{
            String sql = "INSERT INTO Report_Symptoms (symptom_id, report_id)"
                    +"values (?,?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setInt(1,symptomId);
            p.setInt(2,reportId);
            p.executeUpdate();
            p.close();
            
        }catch(SQLException e) {
            e.printStackTrace();
        }
     }
    public void removeSymptomFromReport(Integer symptomId, Integer reportId){
        try {
            String sql = "DELETE FROM Report_Symptoms WHERE symptom_id, report_id";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, symptomId);
            prep.setInt(1, reportId);

            prep.executeUpdate();			
	}catch(Exception e){
	e.printStackTrace();
	}
    }
    public void emptyReport(Integer reportId){
   //delete all the symptoms associated with a report 
    } 
    public List <Symptom> getSymptomsFromReport(Integer reportId){
        List<Symptom> symptoms = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Report_Symptoms WHERE report_id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1, reportId);
	    ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
	        Integer b_id = rs.getInt("id");
                Date b_date = rs.getDate("date");
                String SignalTypeString = rs.getString("signal_type");
                SignalType ST = SignalType.valueOf(SignalTypeString);
                String file_path = rs.getString("file_path");
	        Integer b_duration= rs.getInt("duration");
                bitalinos.add(new Bitalino(b_id, b_date, ST, file_path, b_duration));
	   
	        }else {
	            System.out.println("Bitalinos with ReportID " + report_id + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return bitalinos;
    }
    }
    
}

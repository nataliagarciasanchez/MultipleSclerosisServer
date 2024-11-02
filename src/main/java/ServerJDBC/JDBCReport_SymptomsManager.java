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
    private JDBCSymptom symptomman;
   
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
                Integer symptom_id= rs.getInt("symptom_id");
	        Symptom s= symptomman.searchSymptomById(symptom_id);
                symptoms.add(s);
	   
	        }else {
	            System.out.println("Symptoms with ReportID " + reportId + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return symptoms;
    }
    }
    
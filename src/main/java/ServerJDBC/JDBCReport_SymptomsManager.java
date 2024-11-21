/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;


import POJOs.Symptom;
import ServerInterfaces.Report_SymptomsManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author noeli
 */
public class JDBCReport_SymptomsManager implements Report_SymptomsManager{
    
    private JDBCManager manager;
    private JDBCSymptomManager symptomman;
   
    public JDBCReport_SymptomsManager(JDBCManager manager) {
        this.manager = manager;
        this.symptomman = new JDBCSymptomManager(manager);
        }
    
    @Override
    public void addSymptomToReport(Integer symptomId, Integer reportId) {
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

    @Override
    public void removeSymptomFromReport(Integer symptomId, Integer reportId) {
        try {
            String sql = "DELETE FROM Report_Symptoms WHERE symptom_id = ? AND report_id = ?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, symptomId);
            prep.setInt(2, reportId);

            prep.executeUpdate();
            prep.close();
	}catch(Exception e){
	e.printStackTrace();
	}
    }

    @Override
    public void emptyReport(Integer reportId) {
        try {
            String sql = "DELETE FROM Report_Symptoms WHERE report_id = ?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, reportId);

            prep.executeUpdate();
            prep.close();
	}catch(Exception e){
	e.printStackTrace();
	}
    }

    @Override
    public List<Symptom> getSymptomsFromReport(Integer reportId) {
        List<Symptom> symptoms = new ArrayList<>();
        
        try{
            String sql = "SELECT * FROM Report_Symptoms WHERE report_id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1, reportId);
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Integer symptom_id= rs.getInt("symptom_id");
	        Symptom s= symptomman.getSymptomById(symptom_id);
                symptoms.add(s);
	   
	        }
            if (symptoms.isEmpty()) {
                System.out.println("Report with id " + reportId + " has no symptoms associated.");
            }
	    rs.close();
	    stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return symptoms;
    }
    }
    
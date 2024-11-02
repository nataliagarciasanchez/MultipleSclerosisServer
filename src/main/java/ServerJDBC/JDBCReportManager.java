/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import POJOs.Bitalino;
import POJOs.Patient;
import POJOs.Report;
import POJOs.Symptom;
import ServerInterfaces.ReportManager;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Andreoti
 */
public class JDBCReportManager implements ReportManager{
    private JDBCManager manager;
    private JDBCReport_SymptomsManager rep_symMan;
    private JDBCBitalinoManager bitalinoMan;
    private JDBCPatientManager patientMan;
      
    public JDBCReportManager(JDBCManager manager) {
        this.manager = manager;
        }
    @Override
    public void createReport(Report r) {
        try{
            String sql = "INSERT INTO Reports (date, patient_id)"
                    +"values (?,?)";
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setDate(1,r.getDate());
            p.setInt(2,r.getPatient().getId());
            p.executeUpdate();
            p.close();
            
        }catch(SQLException e) {
            e.printStackTrace();
        }}

    @Override
    public void removeReportById(Integer id) {
        try {
            String sql = "DELETE FROM Reports WHERE id = ?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();			
	}catch(Exception e){
            e.printStackTrace();
        }}

    @Override
    public void updateReport(Report r) {
        String sql = "UPDATE Reports SET date = ?, patient_id = ? WHERE id = ?";
	try {
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	   
	    stmt.setDate(1, r.getDate());
            stmt.setInt(2, r.getPatient().getId());
	    stmt.setInt(3, r.getId());

	    stmt.executeUpdate();
	} catch (SQLException e) {
	     e.printStackTrace();
	}  
    }

   
    @Override
    public List<Report> getListOfReports() {
        List<Report> reports = new ArrayList<>();
	try {
	    String sql = "SELECT * FROM Reports";
	    PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                Date date = rs.getDate("date");
                
                Integer patient_id = rs.getInt("patient_id");
                Patient p = patientMan.getPatientById(patient_id);
                
                List <Bitalino> bitalinos = bitalinoMan.getBitalinosOfReport(id);
                
                List <Symptom> symptoms = rep_symMan.getSymptomsFromReport(id);
                
                Report report = new Report(id, date, p, bitalinos, symptoms);
                reports.add(report);
            }
	            
            rs.close();
	    stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    @Override
    public List<Report> getReportsFromPatient(Integer patientId) {
        List<Report> reports = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Reports WHERE patient_id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1,  patientId);
	    ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
	        Integer id = rs.getInt("id");
	        Date date = rs.getDate("date");
                Integer patient_id = rs.getInt("patient_id");
                Patient p = patientMan.getPatientById(patient_id);
                
                List <Bitalino> bitalinos = bitalinoMan.getBitalinosOfReport(id);
                
                List <Symptom> symptoms = rep_symMan.getSymptomsFromReport(id);
                
                Report report = new Report(id, date, p, bitalinos, symptoms);       
                reports.add(report);
                
	    }
            if(reports.isEmpty()) {
	            System.out.println("Patient with ID " + patientId + " has no reports.");
	        }

	    rs.close();
	    stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return reports;
    }

    @Override
    public Report getReportById(Integer id) {
        Report report = null;
        try{
            String sql = "SELECT * FROM Reports WHERE id = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setInt(1, id);
	    ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
	        
                Date date = rs.getDate("date");
                Integer patient_id = rs.getInt("patient_id");
                Patient p = patientMan.getPatientById(patient_id);
                
                List <Bitalino> bitalinos = bitalinoMan.getBitalinosOfReport(id);
                
                List <Symptom> symptoms = rep_symMan.getSymptomsFromReport(id);
                
                report = new Report(id, date, p, bitalinos, symptoms);
	        }else {
	            System.out.println("Report with ID " + id + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return report;
    }

    @Override
    public List<Report> getReportByDate(Date date) {
        List<Report> reports = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Reports WHERE date = ?";
            PreparedStatement stmt = manager.getConnection().prepareStatement(sql);
	    stmt.setDate(1, date);
	    ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
	        
                Integer id = rs.getInt("id");
                
                Integer patient_id = rs.getInt("patient_id");
                Patient p = patientMan.getPatientById(patient_id);
                
                List <Bitalino> bitalinos = bitalinoMan.getBitalinosOfReport(id);
                
                List <Symptom> symptoms = rep_symMan.getSymptomsFromReport(id);
                
                Report report = new Report(id, date, p, bitalinos, symptoms);
                                
                reports.add(report);
	        }else {
	            System.out.println("Report with date " + date + " not found.");
	        }

	        rs.close();
	        stmt.close();
        }catch(SQLException e){
        e.printStackTrace();
        }
        return reports;
    }
    
}

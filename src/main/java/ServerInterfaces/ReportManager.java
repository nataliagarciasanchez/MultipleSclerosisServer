/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;

import POJOs.Report;
import java.sql.Date;
import java.util.List;


/**
 *
 * @author Andreoti
 */
public interface ReportManager {
    public void createReport(Report r);
    public void removeReportById(Integer id);
    public void updateReport(Report r);
    public List<Report> getListOfReports();
    public List<Report> getReportsFromPatient(Integer patientId);   
    public Report getReportById(Integer id);
    public List<Report> getReportByDate(Date date);
    
}

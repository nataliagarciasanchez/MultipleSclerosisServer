/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;

import POJOs.Report;
import java.util.List;


/**
 *
 * @author Andreoti
 */
public interface ReportManager {
    public void createReport(Report r);
    public void removeReportById(int id);
    public Report viewReport(int id);
    public List<Report> getListOfReports();
    public Report searchReportById(int id);
    public List<Report> getListOfReportsOfPatient(int patientId);   
    
}

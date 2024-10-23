/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package MultipleSclerosisInterfaces;

import MultipleSclerosisPOJOs.Report;
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
    public void searchReportById(int id);
    public List<Report> getListOfReportsOfPatient(int patientId);   
    
}

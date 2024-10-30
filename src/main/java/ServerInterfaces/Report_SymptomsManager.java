/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;

import POJOs.Symptom;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public interface Report_SymptomsManager {
    public void addSymptomToReport(Integer symptomId, Integer reportId);
    public void removeSymptomFromReport(Integer symptomId, Integer reportId);
    public void emptyReport(Integer reportId); //delete all the symptoms associated with a report
    public List <Symptom> getSymptomsFromReport(Integer reportId);
}

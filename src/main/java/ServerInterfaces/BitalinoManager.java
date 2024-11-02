/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;

import POJOs.Bitalino;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public interface BitalinoManager {
    public void createBitalino(Bitalino b);
    public void removeBitalinoById(Integer id);
    public void updateBitalino(Bitalino b);
    public List<Bitalino> getListOfBitalinos();
    public Bitalino getBitalinoById(Integer id);
    public List<Bitalino> getBitalinosByDate(Date d);
    public List<Bitalino> getBitalinosOfReport(Integer report_id);
    
}

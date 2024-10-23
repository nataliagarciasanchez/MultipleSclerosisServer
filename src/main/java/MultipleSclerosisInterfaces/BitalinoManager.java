/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package MultipleSclerosisInterfaces;

import MultipleSclerosisPOJOs.Bitalino;
import MultipleSclerosisPOJOs.SignalType;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public interface BitalinoManager {
    public void createBitalino(Bitalino b);
    public Bitalino viewBitalino(Integer id);
    public List<Bitalino> getListOfBitalinos();
    public void removeBitalinoById(Integer id);
    public void modifyBitalinoInfo(Integer id, Date date, SignalType signal, String filepath, Float duration, Integer report_id);
    public List<Bitalino> getBitalinosOfReport(Integer report_id);
    
}

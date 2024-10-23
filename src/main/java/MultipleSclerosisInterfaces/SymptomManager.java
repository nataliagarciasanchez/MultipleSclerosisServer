/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package MultipleSclerosisInterfaces;


import MultipleSclerosisPOJOs.Symptom;
import java.util.List;

/**
 *
 * @author maipa
 */
public interface SymptomManager {
    
    public void createSymptom(Symptom symptom);
    public Symptom searchSymptomById(Integer id);
    public void updateSymptom(Symptom symptom);
    public void deleteSymptom(Integer id);
    public List<Symptom> getAllSymptoms();
}

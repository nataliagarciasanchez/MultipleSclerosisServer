/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package MultipleSclerosisInterfaces;

import MultipleSclerosisPOJOs.Specialty;
import java.util.List;

/**
 *
 * @author maipa
 */
public interface SpecialtyManager {
    
    public void createSpecialty(Specialty speciality);
    public Specialty searchSpecialityById(Integer id);
    public void updateSpeciality(Specialty speciality);
    public void deleteSpeciality(Integer id);
    public List<Specialty> getAllSpecialities();
}

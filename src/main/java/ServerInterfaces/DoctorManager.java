/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerInterfaces;

import POJOs.Doctor;
import POJOs.User;
import java.util.List;

/**
 *
 * @author nataliagarciasanchez
 */
public interface DoctorManager {
    
    public void registerDoctor(Doctor d);
    public void removeDoctorById(Integer id);
    public void updateDoctor(Doctor d);
    public List<Doctor> getListOfDoctors();
    public Doctor getDoctorById(Integer id);
    public Doctor getDoctorByUser(User user);   //FALTA
    public List<Doctor> getDoctorByName(String name);//es una lista porque puede haber doctores con el mismo nombre
    public List<Integer> getDoctorIds();    //FALTA
}

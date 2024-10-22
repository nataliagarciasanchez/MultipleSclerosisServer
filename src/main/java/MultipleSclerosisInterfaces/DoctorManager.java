/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisInterfaces;

import MultipleSclerosisPOJOs.Doctor;
import MultipleSclerosisPOJOs.User;
import java.util.List;

/**
 *
 * @author nataliagarciasanchez
 */
public interface DoctorManager {
    
    public void createDoctor(Doctor d);
    public Doctor viewMyInfo(int id);
    public List<Doctor> getListOfDoctors();
    public void removeDoctorById(int id);
    public void modifyDoctorInfo(int id, String name, String specialty, User user);
    public void searchDoctorById(int id);
    public List<Doctor> searchDoctorByName(String name);//es una lista porque puede haber doctores con el mismo nombre
    public void assignPatient2Doctor(int doctorId, int patientId);
    public void removePatientFromDoctor(int doctorId, int patientId);
   
}

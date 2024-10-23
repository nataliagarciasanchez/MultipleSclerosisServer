/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package MultipleSclerosisInterfaces;


import MultipleSclerosisPOJOs.Patient;
import MultipleSclerosisPOJOs.User;
import java.util.List;

/**
 *
 * @author maipa
 */
public interface PatientManager {
    public void createPatient(Patient p);
    public Patient viewMyInfo(Integer id);
    public List<Patient> getListOfPatients();
    public void removePatientById(Integer id);
    public void modifyPatientInfo(Integer id, String name, String specialty, User user);
    public Patient searchPatientById(Integer id);
    public List<Patient> searchPatientByName(String name);
    public List<Patient> searchPatientByDoctor(Integer doctorId);
    public void assignSymptom2Patient(Integer symptomId, Integer patientId);
    public void removePatientFromDoctor(Integer doctorId, Integer patientId);
    
}

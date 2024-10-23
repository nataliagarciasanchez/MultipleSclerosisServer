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
    public Patient viewMyInfo(int id);
    public List<Patient> getListOfPatients();
    public void removePatientById(int id);
    public void modifyPatientInfo(int id, String name, String specialty, User user);
    public void searchPatientById(int id);
    public List<Patient> searchPatientByName(String name);
    public List<Patient> searchPatientByDoctor(int doctorId);
    public void assignSymptom2Patient(int symptomId, int patientId);
    public void removePatientFromDoctor(int doctorId, int patientId);
    
}

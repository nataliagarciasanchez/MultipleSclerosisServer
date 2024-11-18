/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;


import POJOs.Patient;
//import POJOs.User;
import java.util.List;

/**
 *
 * @author maipa
 */
public interface PatientManager {
    public void registerPatient(Patient p);
    public void removePatientById(Integer id);
    public void updatePatient(Patient p);
    public List<Patient> getListOfPatients();
    public Patient getPatientById(Integer id);
    public List<Patient> getPatientByName(String name);
    public List<Patient> getPatientsFromDoctor(Integer doctorId);
    public int assignDoctor2Patient();   
}

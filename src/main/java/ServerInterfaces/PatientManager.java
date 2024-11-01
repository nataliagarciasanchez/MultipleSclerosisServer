/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;


import POJOs.Gender;
import POJOs.Patient;
import java.sql.Date;
//import POJOs.User;
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
    public void modifyPatientInfo(Integer id, String name, Date dob, Gender gender,Integer phone, Integer doctorId, Integer userId);
    public Patient searchPatientById(Integer id);
    public List<Patient> searchPatientByName(String name);
    public List<Patient> getPatientsFromDoctor(Integer doctorId);
       
}

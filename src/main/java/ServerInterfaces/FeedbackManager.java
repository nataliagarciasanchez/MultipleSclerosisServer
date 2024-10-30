/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;

import POJOs.Feedback;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public interface FeedbackManager {
    public void addFeedback(Feedback f);
    public Feedback viewFeedbackInfo(Integer id);
    public void removeFeedbackById(Integer id);
    public List<Feedback> getListOfFeedbacksOfPatient(Integer patient_id);
    public List<Feedback> getListOfFeedbacksOfDoctor(Integer doctor_id);
    public Feedback searchFeedbackById(Integer id);
    public void modifyFeedback(Integer id, String message, Date date, Integer doctor_id, Integer patient_id);
    
}

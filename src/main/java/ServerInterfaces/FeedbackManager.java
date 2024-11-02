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
    public void createFeedback(Feedback f);
    public void removeFeedbackById(Integer id);
    public void updateFeedback(Feedback f);
    public Feedback getFeedBackById(Integer id);
    public List<Feedback> getFeedBackByDate(Date date);
    public List<Feedback> getListOfFeedbacksOfPatient(Integer patient_id);
    public List<Feedback> getListOfFeedbacksOfDoctor(Integer doctor_id);
    
    
}

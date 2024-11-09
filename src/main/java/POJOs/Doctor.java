/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJOs;

import POJOs.Feedback;
import POJOs.Patient;
import POJOs.User;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author nataliagarciasanchez
 */

public class Doctor implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
    private Specialty specialty;
    private User user;
    
    private List<Patient> patients;
    
    private List<Feedback> feedbacks; 
    
    public Doctor(){
    super();
    }

    public Doctor(Integer id, String name, Specialty specialty, User user, List<Patient> patients, List<Feedback> feedbacks) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.user = user;
        this.patients = patients;
        this.feedbacks = feedbacks;
    }

    public Doctor(String name, Integer id, Specialty specialty, User user) {
        this.name = name;
        this.id = id;
        this.specialty = specialty;
        this.user = user;
    }
    
    public Doctor(String name, Integer id, Specialty specialty) {
        this.name = name;
        this.id = id;
        this.specialty = specialty;
    }
    
    

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public User getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public void setFeedback(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Doctor{" + "id=" + id + ", name=" + name + ", specialty=" + specialty + ", user=" + user + ", patients=" + patients + ", feedbacks=" + feedbacks + '}';
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.specialty);
        hash = 53 * hash + Objects.hashCode(this.user);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Doctor other = (Doctor) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.specialty, other.specialty)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.user, other.user);
    }
    

}

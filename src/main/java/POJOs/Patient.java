/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJOs;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author laura
 */
@Entity
@Table (name= "Patients")
public class Patient implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue (generator = "Patients")
    @TableGenerator(name = "Patients", table = "sqlite_sequence",  pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "patients")
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String name;
    private String surname;
    private String NIF;
    private Date dob;
    private Gender gender;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Report> reports;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks; 
   
    
    public Patient() {
        super();
    }

    public Patient(Integer id, String name, String surname, String NIF, Date dob, Gender gender, String phone, Doctor doctor, List<Report> reports, List <Feedback> feedbacks, User user) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.doctor = doctor;
        this.reports = reports;
        this.feedbacks = feedbacks;
        this.user = user;
    }
    
    public Patient(Integer id, String name,String surname,String NIF, Date dob, Gender gender, String phone) {
        this.id = id;
        this.name = name;
        this.surname=surname;
        this.NIF=NIF;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDob() {
        return dob;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getSurname() {
        return surname;
    }

    public String getNIF() {
        return NIF;
    }
    
    
    public User getUser(){
        return user;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "Patient{" + "id=" + id + ", name=" + name + ", dob=" + dob + ", gender=" + gender + ", phone=" + phone + ", doctor=" + doctor + ", reports=" + reports + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + Objects.hashCode(this.dob);
        hash = 53 * hash + Objects.hashCode(this.gender);
        hash = 53 * hash + Objects.hashCode(this.phone);
        hash = 53 * hash + Objects.hashCode(this.doctor);
        hash = 53 * hash + Objects.hashCode(this.reports);
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
        final Patient other = (Patient) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dob, other.dob)) {
            return false;
        }
        if (this.gender != other.gender) {
            return false;
        }
        if (!Objects.equals(this.phone, other.phone)) {
            return false;
        }
        if (!Objects.equals(this.doctor, other.doctor)) {
            return false;
        }
        return Objects.equals(this.reports, other.reports);
    }
    
    

    
    
}

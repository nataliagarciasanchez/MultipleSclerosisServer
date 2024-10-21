/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisPOJOs;

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
@Table (name= "reports")
public class Report implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (generator = "reports")
    @TableGenerator(name = "reports", table = "sqlite_sequence",  pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "reports")
    private Integer id;
    private Date date;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @OneToMany(mappedBy = "reports", cascade = CascadeType.ALL)
    private List<Bitalino> bitalinos;
    private List<Symptom> symptom;
    
    public Report(){
    super();
    }

    public Report(Integer id, Date date, Patient patient, List<Bitalino> bitalinos, List<Symptom> symptom) {
        this.id = id;
        this.date = date;
        this.patient = patient;
        this.bitalinos = bitalinos;
        this.symptom = symptom;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Patient getPatient() {
        return patient;
    }

    public List<Bitalino> getBitalinos() {
        return bitalinos;
    }

    public List<Symptom> getSymptom() {
        return symptom;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setBitalinos(List<Bitalino> bitalinos) {
        this.bitalinos = bitalinos;
    }

    public void setSymptom(List<Symptom> symptom) {
        this.symptom = symptom;
    }

    @Override
    public String toString() {
        return "Report{" + "id=" + id + ", date=" + date + ", patient=" + patient + ", bitalinos=" + bitalinos + ", symptom=" + symptom + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.date);
        hash = 59 * hash + Objects.hashCode(this.patient);
        hash = 59 * hash + Objects.hashCode(this.bitalinos);
        hash = 59 * hash + Objects.hashCode(this.symptom);
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
        final Report other = (Report) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.patient, other.patient)) {
            return false;
        }
        if (!Objects.equals(this.bitalinos, other.bitalinos)) {
            return false;
        }
        return Objects.equals(this.symptom, other.symptom);
    }
    
    
}

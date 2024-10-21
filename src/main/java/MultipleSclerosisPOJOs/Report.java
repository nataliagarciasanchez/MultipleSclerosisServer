/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisPOJOs;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
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
    private Patient patient;
    
    @OneToMany(mappedBy = "reports", cascade = CascadeType.ALL)
    private List<Bitalino> bitalinos;
    private List<Symptom> symptom;
}

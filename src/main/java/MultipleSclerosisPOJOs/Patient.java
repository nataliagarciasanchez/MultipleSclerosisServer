/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisPOJOs;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author laura
 */
public class Patient implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
     private Date dob;
    private Gender gender;
    private Integer phone;
    private Doctor doctor;
    private List<Report> reports;

    
    
}

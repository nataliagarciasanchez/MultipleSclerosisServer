/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisPOJOs;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author laura
 */
public class Report implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Date date;
    private Patient patient;
    private List<Bitalino> bitalinos;
    private List<Report_Symptoms> report_Symptoms;
}

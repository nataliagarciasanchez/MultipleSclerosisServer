/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisPOJOs;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author noeli
 */
public class Bitalino implements Serializable{
     private static final long serialVersionUID = 123456789L;
     private Date date;
     private String signal_type;
     private String file_path;
     private Float duration;
     private Integer clinical_historyID;
     
     public Bitalino(){
     super();
     }
     
     public Bitalino (Date date,String signal_type,String file_path,Float duration){
         this.date=date;
         this.signal_type=signal_type;
         this.file_path=file_path;
         this.duration=duration;
     }
     
     public Bitalino (Date date,String signal_type,String file_path,Float duration,Integer clinical_historyID){
         this.date=date;
         this.signal_type=signal_type;
         this.file_path=file_path;
         this.duration=duration;
         this.clinical_historyID=clinical_historyID;
     }
     
     public Date getDate() {
	return date;
    }
     public void setDate(Date date) {
	this.date = date;
    }
     public String getSignal_type(String signal_type) {
	return signal_type;
    } 
     public void setSignal_type(String signal_type) {
	this.signal_type = signal_type;
    }
     public String getFile_path(String file_path) {
	return file_path;
     }
     public void setFile_path(String file_path) {
	this.file_path = file_path;
     }
     public Float getDuration(Float duration) {
	return duration;
    }
      public void setDuration(Float duration) {
	this.duration = duration;
      }
     public Integer getClinicalHistoryID(Integer clinicalHistoryID) {
	return clinicalHistoryID;
     }
     public void SetClinicalHistoryID(Integer clinicalHistoryID) {
	this.clinical_historyID= clinicalHistoryID;  
     }
       public String toString() {
        return "Bitalino {" +"Date=" + date +" Signal Type=" + signal_type + " File path=" + file_path +  "Duration=" + duration + " Clinical History ID=" + clinical_historyID + '}';
       }
        }


     

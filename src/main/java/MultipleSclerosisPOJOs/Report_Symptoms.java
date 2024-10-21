/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisPOJOs;

import java.util.Objects;

/**
 *
 * @author Andreoti
 */
public class Report_Symptoms {
     private static final long serialVersionUID = 1L;
     
     private Integer symptomID;
     private Integer reportID;
     
     public Report_Symptoms(){
     super();
     }

    public Report_Symptoms(Integer symptomID, Integer reportID) {
        this.symptomID = symptomID;
        this.reportID = reportID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getSymptomID() {
        return symptomID;
    }

    public Integer getReportID() {
        return reportID;
    }

    public void setSymptomID(Integer symptomID) {
        this.symptomID = symptomID;
    }

    public void setReportID(Integer reportID) {
        this.reportID = reportID;
    }

    @Override
    public String toString() {
        return "Report_Symptoms{" + "symptomID=" + symptomID + ", reportID=" + reportID + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.symptomID);
        hash = 53 * hash + Objects.hashCode(this.reportID);
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
        final Report_Symptoms other = (Report_Symptoms) obj;
        if (!Objects.equals(this.symptomID, other.symptomID)) {
            return false;
        }
        return Objects.equals(this.reportID, other.reportID);
    }
     
     
    
}

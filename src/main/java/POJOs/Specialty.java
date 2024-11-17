/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJOs;

/**
 *
 * @author maipa
 */
public enum Specialty {
    NEUROLOGY;
    
    public static String getEnumFromSQL(){
        StringBuilder enumValuesSpecialty = new StringBuilder();
        for (Specialty specialty : Specialty.values()) {
            
            enumValuesSpecialty.append("'").append(specialty.name()).append("'");
   
        }
        if (enumValuesSpecialty.length() > 0) {
            enumValuesSpecialty.setLength(enumValuesSpecialty.length() - 1); 
        }

        return enumValuesSpecialty.substring(0,enumValuesSpecialty.length()-1);
    }
    
}

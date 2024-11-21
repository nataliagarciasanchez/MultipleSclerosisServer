/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menu.Utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 *
 * @author noeli
 */
public class Utilities {
     private static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

    public static boolean validateDate(LocalDate doaLocalDate) {
        boolean ok = true;
        if (doaLocalDate.isAfter(LocalDate.now())) {
            ok = false;
            System.out.println("Invalid email, try again");
        }
        return ok;
    }
    public static java.sql.Date convertString2SqlDate(String dateStr) throws ParseException {
        // Formato de la fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Parsear el String a java.util.Date
        java.util.Date utilDate = dateFormat.parse(dateStr);
        
        // Convertir java.util.Date a java.sql.Date
        return new java.sql.Date(utilDate.getTime());
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisJDBC;
import MultipleSclerosisPOJOs.Gender;
import MultipleSclerosisPOJOs.SignalType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author nataliagarciasanchez
 */
public class JDBCManager {
    private Connection c = null;
	
    public JDBCManager() {
		
	try {
			
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:./db/MultipleSclerosis.db");
            c.createStatement().execute("PRAGMA foreign_keys=ON");
			
            System.out.print("Database Connection opened.");
            this.createTables();
			
        }
	catch(SQLException e) {
            e.printStackTrace();
	}
            catch(ClassNotFoundException e) {
		System.out.print("Libraries not loaded");
	}
    }
	
	
    private void createTables() {
	try {
			
            Statement stmt = c.createStatement();
			
	    String create_table_doctors = "CREATE TABLE IF NOT EXIST Doctors ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "    email TEXT NOT NULL, "
                + "    name TEXT NOT NULL, "
		+ "    specialty_id INTEGER NOT NULL,"
		+ "    licensePDF BLOB,"
		+ "    FOREIGN KEY (speciality_id) REFERENCES Specialty(id)"
		+ ");";
		
            stmt.executeUpdate(create_table_doctors);
            
            //HAY QUE HACER ESTO PARA LOS ENUM 
            StringBuilder enumValues=new StringBuilder();
            for (Gender gender : Gender.values()) {
                if (enumValues.length() > 0) {
                    enumValues.append(", ");
                }
                enumValues.append("'").append(gender.name()).append("'");
            }
		
            String create_table_patients="CREATE TABLE IF NOT EXIST Patients ("
                +"      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     name TEXT NOT NULL, "
                + "     date of birth DATE NOT NULL, "
                + "     gender ENUM("+enumValues.toString()+") NOT NULL, "
                + "     phone INTEGER NOT NULL, "
                + "     doctor_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (doctor_id) REFERENCES Doctor(id)"
                + ");";
       
            stmt.executeUpdate(create_table_patients);
            
            String create_table_report="CREATE TABLE IF NOT EXIST Report ("
                +"      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "
                + "     patient_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (patient_id) REFERENCES Patient(id)"
                + ");";
       
            stmt.executeUpdate(create_table_report);
            
            String create_table_symptoms="CREATE TABLE IF NOT EXIST Symptom ("
                +"      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     name TEXT NOT NULL, "
                + "     report_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (report_id) REFERENCES Report(id)"
                + ");";
       
            stmt.executeUpdate(create_table_symptoms);
            
            StringBuilder bitValues=new StringBuilder();
            for (SignalType signal : SignalType.values()) {
                if (bitValues.length() > 0) {
                    bitValues.append(", ");
                }
                bitValues.append("'").append(signal.name()).append("'");
            }
            
            String create_table_bitalinos="CREATE TABLE IF NOT EXIST Bitalino ("
                +"      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "
                + "     signal_type ENUM("+bitValues.toString()+") NOT NULL, "
                + "     file_path VARCHAR(255) NOT NULL, "
                + "     duration FLOAT NOT NULL, "
                + "     report_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (report_id) REFERENCES Report(id)"
                + ");";
       
            stmt.executeUpdate(create_table_bitalinos);
			
	}catch(SQLException e) {
            if(!e.getMessage().contains("already exists")){
		e.printStackTrace();
            }			
	}
    }
	
	
    public Connection getConnection(){
	return c; 
    }
	
    public void disconnect() {
	try {
            c.close();
	}catch(SQLException e) {
            e.printStackTrace();
	}	
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisJDBC;
import MultipleSclerosisPOJOs.Gender;
import MultipleSclerosisPOJOs.Specialty;
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
            
            String create_table_administrators = "CREATE TABLE IF NOT EXIST Administrators ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    name TEXT NOT NULL, "
		+ "    user_id,"
		+ "    FOREIGN KEY (user_id) REFERENCES Users(id)"
		+ ");";
		
            stmt.executeUpdate(create_table_administrators);
            
            StringBuilder enumValuesSpecialty=new StringBuilder();
            for (Specialty specialty : Specialty.values()) {
                if (enumValuesSpecialty.length() > 0) {
                    enumValuesSpecialty.append(", ");
                }
                enumValuesSpecialty.append("'").append(specialty.name()).append("'");
            }
			
	    String create_table_doctors = "CREATE TABLE IF NOT EXIST Doctors ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    name TEXT NOT NULL, "
		+ "    specialty TEXT("+enumValuesSpecialty.toString()+") NOT NULL,"
		+ "    user_id,"
		+ "    FOREIGN KEY (user_id) REFERENCES Users(id)"
		+ ");";
		
            stmt.executeUpdate(create_table_doctors);
            
            //HAY QUE HACER ESTO PARA LOS ENUM 
            StringBuilder enumValuesGender=new StringBuilder();
            for (Gender gender : Gender.values()) {
                if (enumValuesGender.length() > 0) {
                    enumValuesGender.append(", ");
                }
                enumValuesGender.append("'").append(gender.name()).append("'");
            }
		
            String create_table_patients="CREATE TABLE IF NOT EXIST Patients ("
                +"      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     name TEXT NOT NULL, "
                + "     dob DATE NOT NULL, "
                + "     gender TEXT("+enumValuesGender.toString()+") NOT NULL, " //los enums se representarán como TEXT
                + "     phone INTEGER NOT NULL, "
                + "     doctor_id INTEGER NOT NULL, "
                + "     user_id INTEGER NOT NULL"
                + "     FOREIGN KEY (doctor_id) REFERENCES Doctors(id)"
                + "     FOREIGN KEY (user_id) REFERENCES Users(id)"
                + ");";
       
            stmt.executeUpdate(create_table_patients);
            
            String create_table_reports="CREATE TABLE IF NOT EXIST Reports ("
                + "      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "
                + "     patient_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (patient_id) REFERENCES Patients(id)"
                + ");";
       
            stmt.executeUpdate(create_table_reports);
            
            String create_table_symptoms="CREATE TABLE IF NOT EXIST Symptoms ("
                + "      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     name TEXT NOT NULL, "
                + ");";
       
            stmt.executeUpdate(create_table_symptoms);
            
            String create_table_report_symptoms="CREATE TABLE IF NOT EXIST Report_ymptoms ("
                + "      symptom_id INTEGER NOT NULL,"
                + "      report_id INTEGER NOT NULL,"
                + "      PRIMARY KEY(report_id, symptom_id)"
                + "     FOREIGN KEY (patient_id) REFERENCES Patients(id)"
                + "     FOREIGN KEY (report_id) REFERENCES Reports(id)"
                + ");";
       
            stmt.executeUpdate(create_table_report_symptoms);
            
            StringBuilder bitValues=new StringBuilder();
            for (SignalType signal : SignalType.values()) {
                if (bitValues.length() > 0) {
                    bitValues.append(", ");
                }
                bitValues.append("'").append(signal.name()).append("'");
            }
            
            String create_table_bitalinos="CREATE TABLE IF NOT EXIST Bitalinos ("
                + "     id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "
                + "     signal_type ENUM("+bitValues.toString()+") NOT NULL, "
                + "     file_path TEXT NOT NULL, "
                + "     duration FLOAT NOT NULL, "
                + "     report_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (report_id) REFERENCES Reports(id)"
                + ");";
       
            stmt.executeUpdate(create_table_bitalinos);
            
            
            String create_table_feedbacks = "CREATE TABLE IF NOT EXIST Feedbacks ("
                + "     id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "
                + "     message TEXT NOT NULL"
                + "     doctor_id INTEGER NOT NULL, "
                + "     patient_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (doctor_id) REFERENCES Doctors(id)"
                + "     FOREIGN KEY (patient_id) REFERENCES Patients(id)"
                + ");";
            
            stmt.executeUpdate(create_table_feedbacks);
            
			
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

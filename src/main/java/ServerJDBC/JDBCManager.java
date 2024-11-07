/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;
import POJOs.Gender;
import POJOs.Specialty;
import POJOs.SignalType;
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
            // Asegurándote de que la base de datos se cree en la carpeta "db" dentro del proyecto
            String dbPath = "db/MultipleSclerosisServer.db"; 
            c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

           // c = DriverManager.getConnection("jdbc:sqlite:./db/MultipleSclerosisServer.db");
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
            
            String create_table_administrators = "CREATE TABLE IF NOT EXISTS Administrators ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    name TEXT NOT NULL, "
		+ "    user_id INTEGER NOT NULL,"
		+ "    FOREIGN KEY (user_id) REFERENCES Users(id)"
		+ ");";
		
            stmt.executeUpdate(create_table_administrators);
                        System.out.println("\nAdministrators created");

            
            StringBuilder enumValuesSpecialty=new StringBuilder();
            for (Specialty specialty : Specialty.values()) {
                if (enumValuesSpecialty.length() > 0) {
                    enumValuesSpecialty.append(", ");
                }
                enumValuesSpecialty.append("'").append(specialty.name()).append("'");
            }
			
	    String create_table_doctors = "CREATE TABLE IF NOT EXISTS Doctors ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    name TEXT NOT NULL, "
		+ "    specialty TEXT CHECK(specialty IN ("+enumValuesSpecialty.toString()+")) NOT NULL,"
		+ "    user_id INTEGER NOT NULL,"
		+ "    FOREIGN KEY (user_id) REFERENCES Users(id)"
		+ ");";
		
            stmt.executeUpdate(create_table_doctors);
                        System.out.println("\nDoctors created");

            
            //HAY QUE HACER ESTO PARA LOS ENUM 
            StringBuilder enumValuesGender=new StringBuilder();
            for (Gender gender : Gender.values()) {
                if (enumValuesGender.length() > 0) {
                    enumValuesGender.append(", ");
                }
                enumValuesGender.append("'").append(gender.name()).append("'");
            }
		
            String create_table_patients="CREATE TABLE IF NOT EXISTS Patients ("
                +"      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     name TEXT NOT NULL, "
                + "     surname TEXT NOT NULL, "
                + "     NIF TEXT NOT NULL, "
                + "     dob DATE NOT NULL, "
                + "     gender TEXT CHECK(gender IN ("+enumValuesGender.toString()+")) NOT NULL, " //los enums se representarán como TEXT
                + "     phone TEXT NOT NULL, "
                + "     doctor_id INTEGER NOT NULL, "
                + "     user_id INTEGER NOT NULL,"
                + "     FOREIGN KEY (doctor_id) REFERENCES Doctors(id),"
                + "     FOREIGN KEY (user_id) REFERENCES Users(id)"
                + ");";
       
            stmt.executeUpdate(create_table_patients);
            System.out.println("\nPatients created");
            
            String create_table_reports="CREATE TABLE IF NOT EXISTS Reports ("
                + "      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "
                + "     patient_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (patient_id) REFERENCES Patients(id)"
                + ");";
       
            stmt.executeUpdate(create_table_reports);
            System.out.println("\nReports table created");
            
            String create_table_symptoms="CREATE TABLE IF NOT EXISTS Symptoms ("
                + "      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     name TEXT NOT NULL "
                + ");";
       
            stmt.executeUpdate(create_table_symptoms);
                        System.out.println("\nSymptoms table created");

            
            String create_table_report_symptoms="CREATE TABLE IF NOT EXISTS Report_Symptoms ("
                + "      symptom_id INTEGER NOT NULL,"
                + "      report_id INTEGER NOT NULL,"
                + "      PRIMARY KEY(report_id, symptom_id),"
                + "     FOREIGN KEY (symptom_id) REFERENCES Symptoms(id),"
                + "     FOREIGN KEY (report_id) REFERENCES Reports(id)"
                + ");";
       
            stmt.executeUpdate(create_table_report_symptoms);
                                    System.out.println("\nR_S table created");

            
            StringBuilder bitValues=new StringBuilder();
            for (SignalType signal : SignalType.values()) {
                if (bitValues.length() > 0) {
                    bitValues.append(", ");
                }
                bitValues.append("'").append(signal.name()).append("'");
            }
            
            String create_table_bitalinos="CREATE TABLE IF NOT EXISTS Bitalinos ("
                + "     id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "
                + "     signal_type TEXT CHECK (signal_type IN ("+bitValues.toString()+")) NOT NULL, "
                + "     file_path TEXT NOT NULL, "
                + "     duration FLOAT NOT NULL, "
                + "     report_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (report_id) REFERENCES Reports(id)"
                + ");";
       
            stmt.executeUpdate(create_table_bitalinos);
                                    System.out.println("\nBitalinos table created");

            
            String create_table_feedbacks = "CREATE TABLE IF NOT EXISTS Feedbacks ("
                + "     id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "
                + "     message TEXT NOT NULL,"
                + "     doctor_id INTEGER NOT NULL, "
                + "     patient_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (doctor_id) REFERENCES Doctors(id),"
                + "     FOREIGN KEY (patient_id) REFERENCES Patients(id)"
                + ");";
            
            stmt.executeUpdate(create_table_feedbacks);
                                    System.out.println("\nFeedbacks table created");

            System.out.println("\nALL TABLES ARE NOW CREATED!!");
            
			
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

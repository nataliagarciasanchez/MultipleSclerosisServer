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
            //c.setAutoCommit(true);// activamos autocommit
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
            
            String create_table_roles = "CREATE TABLE IF NOT EXISTS Roles ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    name TEXT NOT NULL "
		+ ");";
		
            stmt.executeUpdate(create_table_roles);
            //System.out.println("\nRoles created");
            
            String create_table_users = "CREATE TABLE IF NOT EXISTS Users ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    email TEXT NOT NULL, "
                + "    password TEXT NOT NULL, "
		+ "    role_id INTEGER NOT NULL,"
		+ "    FOREIGN KEY (role_id) REFERENCES Roles(id)"
		+ ");";
		
            stmt.executeUpdate(create_table_users);
            //System.out.println("\nUsers created");
                        
            String create_table_administrators = "CREATE TABLE IF NOT EXISTS Administrators ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    name TEXT NOT NULL, "
		+ "    user_id INTEGER NOT NULL,"
		+ "    FOREIGN KEY (user_id) REFERENCES Users(id)"
		+ ");";
		
            stmt.executeUpdate(create_table_administrators);
            //System.out.println("\nAdministrators created");

            
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
            //System.out.println("\nDoctors created");

            
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
            //System.out.println("\nPatients created");
            
            String create_table_reports="CREATE TABLE IF NOT EXISTS Reports ("
                + "      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "
                + "     patient_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (patient_id) REFERENCES Patients(id)"
                + ");";
       
            stmt.executeUpdate(create_table_reports);
            //System.out.println("\nReports table created");
            
            String create_table_symptoms="CREATE TABLE IF NOT EXISTS Symptoms ("
                + "      id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     name TEXT NOT NULL "
                + ");";
       
            stmt.executeUpdate(create_table_symptoms);
            //System.out.println("\nSymptoms table created");
             
            String insert_symptom1 = "INSERT INTO Symptoms (id, name) VALUES (1,'Loss of balance')";
		stmt.executeUpdate(insert_symptom1);
            String insert_symptom2 = "INSERT INTO Symptoms (id, name) VALUES (2,'Muscle spasm')";
		stmt.executeUpdate(insert_symptom2);
            String insert_symptom3 = "INSERT INTO Symptoms (id, name) VALUES (3,'Numbness or abnormal sensation in any area')";
		stmt.executeUpdate(insert_symptom3);
            String insert_symptom4 = "INSERT INTO Symptoms (id, name) VALUES (4,'Trouble moving arms and legs')";
		stmt.executeUpdate(insert_symptom4);
            String insert_symptom5 = "INSERT INTO Symptoms (id, name) VALUES (5,'Difficulty walking')";
		stmt.executeUpdate(insert_symptom5);
            String insert_symptom6 = "INSERT INTO Symptoms (id, name) VALUES (6,'Problems with coordination and making small movements')";
		stmt.executeUpdate(insert_symptom6);
            String insert_symptom7 = "INSERT INTO Symptoms (id, name) VALUES (7,'Tremor in one or both arms or legs')";
		stmt.executeUpdate(insert_symptom7);
            String insert_symptom8 = "INSERT INTO Symptoms (id, name) VALUES (8,'Weakness in one or both arms or legs')";
		stmt.executeUpdate(insert_symptom8);
            String insert_symptom9 = "INSERT INTO Symptoms (id, name) VALUES (9,'Constipation and stool leakage')";
		stmt.executeUpdate(insert_symptom9);
            String insert_symptom10 = "INSERT INTO Symptoms (id, name) VALUES (10,'Difficulty starting to urinate')";
		stmt.executeUpdate(insert_symptom10);
            String insert_symptom11 = "INSERT INTO Symptoms (id, name) VALUES (11,'Frequent need to urinate')";
		stmt.executeUpdate(insert_symptom11);
            String insert_symptom12 = "INSERT INTO Symptoms (id, name) VALUES (12,'Intense urgency to urinate')";
		stmt.executeUpdate(insert_symptom12);
            String insert_symptom13 = "INSERT INTO Symptoms (id, name) VALUES (13,'Urine leakage (incontinence)')";
		stmt.executeUpdate(insert_symptom13);
            String insert_symptom14 = "INSERT INTO Symptoms (id, name) VALUES (14,'Facial pain')";
		stmt.executeUpdate(insert_symptom14);
            String insert_symptom15 = "INSERT INTO Symptoms (id, name) VALUES (15,'Painful muscle spasms')";
		stmt.executeUpdate(insert_symptom15);
            String insert_symptom16 = "INSERT INTO Symptoms (id, name) VALUES (16,'Tingling, itching, or burning sensation in arms and legs')";
		stmt.executeUpdate(insert_symptom16);
            String insert_symptom17 = "INSERT INTO Symptoms (id, name) VALUES (17,'Reduced attention span, ability to discern, and memory loss')";
		stmt.executeUpdate(insert_symptom17);
            String insert_symptom18 = "INSERT INTO Symptoms (id, name) VALUES (18,'Difficulty with reasoning and problem-solving')";
		stmt.executeUpdate(insert_symptom18);
            String insert_symptom19 = "INSERT INTO Symptoms (id, name) VALUES (19,'Depression or feelings of sadness')";
		stmt.executeUpdate(insert_symptom19);
            String insert_symptom20 = "INSERT INTO Symptoms (id, name) VALUES (20,'Dizziness or loss of balance')";
		stmt.executeUpdate(insert_symptom20);
            String insert_symptom21 = "INSERT INTO Symptoms (id, name) VALUES (21,'Hearing loss')";
		stmt.executeUpdate(insert_symptom21);
            String insert_symptom22 = "INSERT INTO Symptoms (id, name) VALUES (22,'Erectile problems')";
		stmt.executeUpdate(insert_symptom22);
            String insert_symptom23 = "INSERT INTO Symptoms (id, name) VALUES (23,'Problems with vaginal lubrication')";
		stmt.executeUpdate(insert_symptom23);
            String insert_symptom24 = "INSERT INTO Symptoms (id, name) VALUES (24,'Poorly articulated or difficult-to-understand speech')";
		stmt.executeUpdate(insert_symptom24);
            String insert_symptom25 = "INSERT INTO Symptoms (id, name) VALUES (25,'Trouble chewing and swallowing')";
		stmt.executeUpdate(insert_symptom25);
            
                

            
            String create_table_report_symptoms="CREATE TABLE IF NOT EXISTS Report_Symptoms ("
                + "      symptom_id INTEGER NOT NULL,"
                + "      report_id INTEGER NOT NULL,"
                + "      PRIMARY KEY(report_id, symptom_id),"
                + "     FOREIGN KEY (symptom_id) REFERENCES Symptoms(id),"
                + "     FOREIGN KEY (report_id) REFERENCES Reports(id)"
                + ");";
       
            stmt.executeUpdate(create_table_report_symptoms);
            //System.out.println("\nR_S table created");

            
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
            //System.out.println("\nBitalinos table created");

            
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
            //System.out.println("\nFeedbacks table created");

            System.out.println("\nTables created!");
            
			
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

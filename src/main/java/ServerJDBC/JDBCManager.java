/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;
import POJOs.Gender;
import POJOs.SignalType;
import Security.PasswordEncryption;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nataliagarciasanchez
 */
public class JDBCManager {
    private Connection c = null;
	
    public JDBCManager() {
		
    }
    
   public void connect() {
        try {
            if (c == null || c.isClosed()) {
                Class.forName("org.sqlite.JDBC");

                String dbPath = "db/MultipleSclerosisServer.db";
                File persistentDbFile = new File(dbPath);
                
                if(!persistentDbFile.exists()){
                    InputStream dbResource = getClass().getClassLoader().getResourceAsStream("MultipleSclerosisServer.db");
                    if (dbResource == null) {
                        throw new RuntimeException("Database file not found inside JAR: MultipleSclerosisServer.db");
                    }

                    persistentDbFile.getParentFile().mkdirs(); // Crea directorios si no existen
                    try (FileOutputStream out = new FileOutputStream(persistentDbFile)) {
                        dbResource.transferTo(out);
                    }
                }

                
                c = DriverManager.getConnection("jdbc:sqlite:" + persistentDbFile.getAbsolutePath());
                c.createStatement().execute("PRAGMA foreign_keys=ON");
                System.out.println("Database connection opened at: " + persistentDbFile.getAbsolutePath());

                this.createTables();
                this.insertSymptoms();
                this.insertRoles();
                this.insertDoctor(); 
                this.insertAdministrator();
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }
	
    private void createTables() {
	try {
			
            Statement stmt = c.createStatement();
            
            String create_table_roles = "CREATE TABLE IF NOT EXISTS Roles ("
		+ "    id INTEGER PRIMARY KEY,"
                + "    name TEXT NOT NULL "
		+ ");";
		
            stmt.executeUpdate(create_table_roles);
            //System.out.println("\nRoles created");
            
            String create_table_users = "CREATE TABLE IF NOT EXISTS Users ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    email TEXT NOT NULL, "
                + "    password TEXT NOT NULL, "
		+ "    role_id INTEGER NOT NULL,"
		+ "    FOREIGN KEY (role_id) REFERENCES Roles(id) ON DELETE CASCADE"
		+ ");";
		
            stmt.executeUpdate(create_table_users);
            //System.out.println("\nUsers created");
                        
            String create_table_administrators = "CREATE TABLE IF NOT EXISTS Administrators ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    name TEXT NOT NULL, "
		+ "    user_id INTEGER NOT NULL,"
		+ "    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE"
		+ ");";
		
            stmt.executeUpdate(create_table_administrators);
            //System.out.println("\nAdministrators created");

            
            //String enumValuesSpecialty=Specialty.getEnumFromSQL();
            
			
	    String create_table_doctors = "CREATE TABLE IF NOT EXISTS Doctors ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    name TEXT NOT NULL, "
                + "    surname TEXT NOT NULL, "    
		+ "    specialty TEXT NOT NULL,"
		+ "    user_id INTEGER NOT NULL,"
		+ "    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE"
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
                + "     FOREIGN KEY (doctor_id) REFERENCES Doctors(id) ON DELETE CASCADE,"
                + "     FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE"
                + ");";
       
            stmt.executeUpdate(create_table_patients);
            //System.out.println("\nPatients created");
            
            String create_table_reports="CREATE TABLE IF NOT EXISTS Reports ("
                + "     id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "   
                + "     patient_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (patient_id) REFERENCES Patients(id) ON DELETE CASCADE"
                + ");";
       
            stmt.executeUpdate(create_table_reports);
            
            
            String create_table_symptoms="CREATE TABLE IF NOT EXISTS Symptoms ("
                + "      id INTEGER PRIMARY KEY,"
                + "     name TEXT NOT NULL "
                + ");";
       
            stmt.executeUpdate(create_table_symptoms);
            //System.out.println("\nSymptoms table created");
     
            String create_table_report_symptoms="CREATE TABLE IF NOT EXISTS Report_Symptoms ("
                + "      symptom_id INTEGER NOT NULL,"
                + "      report_id INTEGER NOT NULL,"
                + "      PRIMARY KEY(report_id, symptom_id),"
                + "     FOREIGN KEY (symptom_id) REFERENCES Symptoms(id) ON DELETE CASCADE,"
                + "     FOREIGN KEY (report_id) REFERENCES Reports(id) ON DELETE CASCADE"
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
                + "     duration FLOAT NOT NULL, "
                + "     signal_values TEXT NOT NULL, "
                + "     report_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (report_id) REFERENCES Reports(id) ON DELETE CASCADE"
                + ");";
       
            stmt.executeUpdate(create_table_bitalinos);
            //System.out.println("\nBitalinos table created");

            
            String create_table_feedbacks = "CREATE TABLE IF NOT EXISTS Feedbacks ("
                + "     id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     date DATE NOT NULL, "
                + "     message TEXT NOT NULL,"
                + "     doctor_id INTEGER NOT NULL, "
                + "     patient_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (doctor_id) REFERENCES Doctors(id) ON DELETE CASCADE,"
                + "     FOREIGN KEY (patient_id) REFERENCES Patients(id) ON DELETE CASCADE"
                + ");";
            
            stmt.executeUpdate(create_table_feedbacks);
            
            String create_table_files = "CREATE TABLE IF NOT EXISTS Files ("
                + "     id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "     file_name TEXT NOT NULL, "
                + "     file_data BLOB NOT NULL, "
                + "     date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "     bitalinoEMG_id INTEGER NOT NULL, "
                + "     bitalinoECG_id INTEGER NOT NULL, "
                + "     FOREIGN KEY (bitalinoEMG_id) REFERENCES Bitalinos(id) ON DELETE CASCADE,"
                + "     FOREIGN KEY (bitalinoECG_id) REFERENCES Bitalinos(id) ON DELETE CASCADE"
                + ");";
            
            stmt.executeUpdate(create_table_files);
            
            //System.out.println("\nFeedbacks table created");
            stmt.close();
            System.out.println("\nTables created!");
            
			
	}catch(SQLException e) {
            if(!e.getMessage().contains("already exists")){
		e.printStackTrace();
            }			
	}
    }
    
    public void insertSymptoms(){
        
        try {
            Statement s1 = c.createStatement();
            ResultSet rs = s1.executeQuery("SELECT COUNT(*) FROM Symptoms");
            rs.next();
            int rowCount = rs.getInt(1);
            rs.close();
            s1.close();

            String[] symptoms = {
                "Loss of balance",
                "Muscle spasm",
                "Numbness or abnormal sensation in any area",
                "Trouble moving arms and legs",
                "Difficulty walking",
                "Problems with coordination and making small movements",
                "Tremor in one or both arms or legs",
                "Weakness in one or both arms or legs",
                "Constipation and stool leakage",
                "Difficulty starting to urinate",
                "Frequent need to urinate",
                "Intense urgency to urinate",
                "Urine leakage (incontinence)",
                "Facial pain",
                "Painful muscle spasms",
                "Tingling, itching, or burning sensation in arms and legs",
                "Reduced attention span, ability to discern, and memory loss",
                "Difficulty with reasoning and problem-solving",
                "Depression or feelings of sadness",
                "Dizziness or loss of balance",
                "Hearing loss",
                "Erectile problems",
                "Problems with vaginal lubrication",
                "Poorly articulated or difficult-to-understand speech",
                "Trouble chewing and swallowing"
            };
        
            // Insert data when tables are empty
            if(rowCount==0){
                Statement stmt=c.createStatement();
                for (String symptom : symptoms) {
                    String insertQuery = "INSERT INTO Symptoms (name) VALUES ('" + symptom + "')";
                    stmt.executeUpdate(insertQuery);
                } 
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        }

            
    }
    
    public void insertRoles(){
        
        try {
            Statement s1 = c.createStatement();
            ResultSet rs = s1.executeQuery("SELECT COUNT(*) FROM Roles");
            rs.next();
            int rowCount = rs.getInt(1);
            rs.close();
            s1.close();
            
            if(rowCount==0){
                Statement stmt=c.createStatement();
                String insertRole1 = "INSERT INTO Roles (id, name) VALUES (1, 'patient')";
                String insertRole2 = "INSERT INTO Roles (id, name) VALUES (2, 'doctor')";
                String insertRole3 = "INSERT INTO Roles (id, name) VALUES (3, 'administrator')";
                stmt.executeUpdate(insertRole1);
                stmt.executeUpdate(insertRole2);
                stmt.executeUpdate(insertRole3);
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void insertAdministrator(){
        try{
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM Administrators");
            rs.next();
            int rowCount = rs.getInt(1);
            System.out.println("Number of administrators: " + rowCount);
            rs.close();
            s.close();
            
            if (rowCount == 0) {
                // Inserción del primer usuario
                String insertUserAdmin = "INSERT INTO Users (email, password, role_id) VALUES (?, ?, ?)";
                PreparedStatement p = c.prepareStatement(insertUserAdmin, Statement.RETURN_GENERATED_KEYS);
                p.setString(1, "administrator@multipleSclerosis.com");
                String hashedPassword = PasswordEncryption.hashPassword("Password123");
                p.setString(2, hashedPassword);
                p.setInt(3, 3);
                p.executeUpdate();

                // Obtener el ID generado para el primer usuario
                ResultSet generatedKeys1 = p.getGeneratedKeys();
                int userId1 = -1;
                    if (generatedKeys1.next()) {
                        userId1 = generatedKeys1.getInt(1);
                    }
                p.close();
                
                
                String insertAdmin = "INSERT INTO Administrators (name, user_id) VALUES (?, ?)";
                PreparedStatement pstmt = c.prepareStatement(insertAdmin);
                pstmt.setString(1, "Andrea");
                pstmt.setInt(2, userId1); // Usar el ID generado
                pstmt.executeUpdate();
                pstmt.close();
            }
        }catch (SQLException ex) {
        Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
    public void insertDoctor() {
        try {
            Statement s1 = c.createStatement();
            ResultSet rs = s1.executeQuery("SELECT COUNT(*) FROM Doctors");
            rs.next();
            int rowCount = rs.getInt(1);
            rs.close();
            s1.close();

            if (rowCount == 0) {
                // Inserción del primer usuario
                String insertUserDoc1 = "INSERT INTO Users (email, password, role_id) VALUES (?, ?, ?)";
                PreparedStatement p1 = c.prepareStatement(insertUserDoc1, Statement.RETURN_GENERATED_KEYS);
                p1.setString(1, "doctor.garcia@multipleSclerosis.com");
                String hashedPassword1 = PasswordEncryption.hashPassword("Password456");
                p1.setString(2, hashedPassword1);
                p1.setInt(3, 2);
                p1.executeUpdate();

                // Obtener el ID generado para el primer usuario
                ResultSet generatedKeys1 = p1.getGeneratedKeys();
                int userId1 = -1;
                    if (generatedKeys1.next()) {
                        userId1 = generatedKeys1.getInt(1);
                    }
                p1.close();

                // Inserción del segundo usuario
                String insertUserDoc2 = "INSERT INTO Users (email, password, role_id) VALUES (?, ?, ?)";
                PreparedStatement p2 = c.prepareStatement(insertUserDoc2, Statement.RETURN_GENERATED_KEYS);
                p2.setString(1, "doctor.perales@multipleSclerosis.com");
                String hashedPassword2 = PasswordEncryption.hashPassword("Password678");
                p2.setString(2, hashedPassword2);
                p2.setInt(3, 2);
                p2.executeUpdate();

                // Obtener el ID generado para el segundo usuario
                ResultSet generatedKeys2 = p2.getGeneratedKeys();
                int userId2 = -1;
                    if (generatedKeys2.next()) {
                        userId2 = generatedKeys2.getInt(1);
                    }
                p2.close();

                // Inserción del primer doctor
                String insertDoc1 = "INSERT INTO Doctors (name, surname, specialty, user_id) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = c.prepareStatement(insertDoc1);
                pstmt.setString(1, "DR.Maria");
                pstmt.setString(2, "Garcia");
                pstmt.setString(3, "NEUROLOGY");
                pstmt.setInt(4, userId1); // Usar el ID generado
                pstmt.executeUpdate();
                pstmt.close();

                // Inserción del segundo doctor
                String insertDoc2 = "INSERT INTO Doctors (name, surname, specialty, user_id) VALUES (?,?, ?, ?)";
                PreparedStatement pstmt2 = c.prepareStatement(insertDoc2);
                pstmt2.setString(1, "DR.Marcos");
                pstmt2.setString(2, "Perales");
                pstmt2.setString(3, "NEUROLOGY");
                pstmt2.setInt(4, userId2); // Usar el ID generado
                pstmt2.executeUpdate();
                pstmt2.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    public Connection getConnection() {
        try {
            if (c == null || c.isClosed()) {
                connect();
            }
            // Habilita las restricciones de claves foráneas
            c.createStatement().execute("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

	
    public void disconnect() {
	if (c != null) {
            try {
                c.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }	
        }
    }
    
    public void clearAllTables() {
        try {
            
            try (Statement statement = c.createStatement();
                 Statement deleteStatement = c.createStatement()) {
                 statement.execute("PRAGMA foreign_keys = OFF");

                // Obtiene todas las tablas excepto las internas
                ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name != 'sqlite_sequence';");
                while (rs.next()) {
                    String tableName = rs.getString("name");
                    
                    System.out.println("Clearing table: " + tableName);
                    deleteStatement.executeUpdate("DELETE FROM \"" + tableName + "\"");
                }
                
                statement.executeUpdate("DELETE FROM sqlite_sequence");
  
                statement.execute("PRAGMA foreign_keys = ON");
            }
            System.out.println("\nAll tables cleared!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

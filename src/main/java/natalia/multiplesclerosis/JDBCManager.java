/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisJDBC;
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
            c = DriverManager.getConnection("jdbc:sqlite:./db/FertilityClinic2.db");
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
			
	    String sql = "CREATE TABLE doctors ("
		+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "    email TEXT NOT NULL,"
		+ "    phone INTEGER NOT NULL,"
                + "    name TEXT NOT NULL,\n"
		+ "    speciality_id INTEGER NOT NULL,"
		+ "    licensePDF BLOB,"
		+ "    FOREIGN KEY (speciality_id) REFERENCES Specialities(id)"
		+ ");"
		+ "";
            stmt.executeUpdate(sql);
			
	
			
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

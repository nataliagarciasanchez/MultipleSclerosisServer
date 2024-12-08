/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerJDBC;

import ServerInterfaces.FileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maipa
 */
public class JDBCFilesManager implements FileManager{
    private JDBCManager manager;


    /**
     * Constructor for JDBCFilesManager.
     *
     * @param manager the JDBCManager instance for database connection
     * management.
     */
    public JDBCFilesManager(JDBCManager manager){
        this.manager = manager;
        
    }

    /**
     * Creates a new file entry in the database.
     *
     * @param file the file object containing the data to be inserted.
     * @param bitalinoEMG_id
     * @param bitalinoECG_id
     */
    @Override
    public void createFile(File file, int bitalinoEMG_id, int bitalinoECG_id) {
        String sql = "INSERT INTO Files (file_name, file_data, date_time, bitalinoEMG_id, bitalinoECG_id) VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?)";
       try (PreparedStatement p = manager.getConnection().prepareStatement(sql);
        FileInputStream fis = new FileInputStream(file)){
           
        //String sql = "INSERT INTO Files (file_name, file_data, date_time, bitalinoEMG_id, bitalinoECG_id) VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?)";
        //PreparedStatement p = manager.getConnection().prepareStatement(sql);
        //FileInputStream fis = new FileInputStream(file);
        p.setString(1,file.getName());
        p.setBinaryStream(2, fis, (int) file.length());
        p.setInt(3, bitalinoEMG_id);
        p.setInt(4,bitalinoECG_id);
       
        p.executeUpdate();
        p.close();

        } catch (SQLException e) {
            e.printStackTrace(); // O utiliza un logger para registrar la excepción
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JDBCFilesManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JDBCFilesManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}

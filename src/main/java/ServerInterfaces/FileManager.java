/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;

import java.io.File;

/**
 *
 * @author maipa
 */
public interface FileManager {
    public void createFile(File file, Integer bitalinoEMG_id, Integer bitalinoECG_id);
    public File getFileFromBitalinosId (Integer bitalinoEMG_id, Integer bitalinoECG_id);
}

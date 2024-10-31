/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ServerInterfaces;

import POJOs.Administrator;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public interface AdministratorManager {
    public void createAdministrator(Administrator a);
    public Administrator viewMyInfo(Integer administratorId);
    public List<Administrator> getListOfAdministrators();
    public void removeAdministratorById(Integer id);
    public void modifyAdministratorInfo(Integer id, String name);
    public Administrator searchAdministratorById(Integer id);
    public List<Administrator> searchAdministratorByName(String name);//es una lista porque puede haber doctores con el mismo nombre
    
    
}

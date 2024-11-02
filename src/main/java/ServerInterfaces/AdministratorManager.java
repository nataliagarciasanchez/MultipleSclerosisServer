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
    public void removeAdministratorById(Integer id);
    public void updateAdministrator(Administrator a);
    public List<Administrator> getListOfAdministrators();
    public Administrator getAdministratorById(Integer id);
    public List<Administrator> getAdministratorByName(String name);//es una lista porque puede haber doctores con el mismo nombre
       
}

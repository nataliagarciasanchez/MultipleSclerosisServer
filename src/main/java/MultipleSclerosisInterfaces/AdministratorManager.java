/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package MultipleSclerosisInterfaces;

import MultipleSclerosisPOJOs.Administrator;
import MultipleSclerosisPOJOs.User;
import java.util.List;

/**
 *
 * @author Andreoti
 */
public interface AdministratorManager {
    public void createDoctor(Administrator a);
    public Administrator viewMyInfo(int id);
    public List<Administrator> getListOfAdministrators();
    public void removeAdministratorById(int id);
    public void modifyAdministratorInfo(int id, String name, User user);
    public void searchAdministratorById(int id);
    public List<Administrator> searchAdministratorByName(String name);//es una lista porque puede haber doctores con el mismo nombre
}

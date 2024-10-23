/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisJPA;

import MultipleSclerosisInterfaces.UserManager;
import MultipleSclerosisPOJOs.Role;
import MultipleSclerosisPOJOs.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author nataliagarciasanchez
 */
public class JPAUserManager implements UserManager {
    
    private EntityManager em;
	
	public JPAUserManager() {
		super();
		this.connect();
	}
    @Override
    public void newRole(Role role) {
	em.getTransaction().begin();
	em.persist(role);
	em.getTransaction().commit();	
    }
        
        
    @Override
    public void connect() {
        //TODO TIENE QUE COINCIDIR EL NOMBRE CON EL DEL PERSISTENCE.XML FILE DE META-INF
        em = Persistence.createEntityManagerFactory("multipleSclerosis-provider").createEntityManager();

	em.getTransaction().begin();
	em.createNativeQuery("PRAGMA foreign_keys = ON").executeUpdate();
	em.getTransaction().commit();
	
	if(this.getRoles().isEmpty())
	{
		Role admin = new Role("administrator");
		Role doctor = new Role("doctor");
		Role patient=new Role ("patient");
		this.newRole(admin);
		this.newRole(doctor);
		this.newRole(patient);
	
	}
    }

    @Override
    public void disconnect() {
        if (em != null && em.isOpen()) {
            em.close();  // Cierra el EntityManager
        }
    }

    @Override
    public void newUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        try {
            em.getTransaction().begin();  // Inicia una transacción
            em.persist(user);             // Persiste el objeto User en la base de datos
            em.getTransaction().commit(); // Hace commit a la transacción
        } catch (Exception e) {
            em.getTransaction().rollback(); // Si hay un error, realiza rollback
            throw new RuntimeException("Error al guardar el usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Role> getRoles() {
        
    }

    @Override
    public Role getRole(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public User getUser(String email) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public User checkPassword(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void changePassword(User user, String new_password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
   
    
}

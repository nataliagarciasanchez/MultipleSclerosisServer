/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJOs;
import java.io.Serializable;
import javax.persistence.*;


/**
 *
 * @author nataliagarciasanchez
 */
public class User implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String email;//username
    private String password;
    private Role role;
	
    public User() {
	super();
    }
    public User(Integer id, String email, String password, Role role) {
	this.id = id;
	this.email = email;
	this.password = password;
	this.role = role;
    }
    
    public User(String email, String password, Role role) {
	this.email = email;
	this.password = password;
	this.role = role;
    }

    public Integer getId() {
	return id;
    }
    public void setId(Integer id) {
	this.id = id;
    }
    public String getEmail() {
	return email;
    }
    public void setEmail(String email) {
	this.email = email;
    }
    public String getPassword() {
	return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", password=" + password + ", role=" + role
		+ "]";
    }
}

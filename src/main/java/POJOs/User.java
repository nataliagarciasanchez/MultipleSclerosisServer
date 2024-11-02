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
@Entity
@Table (name= "Users")
public class User implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (generator = "Users")
    @TableGenerator(name = "Users", table = "sqlite_sequence",  pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Users")
    private Integer id;
    private String email;//username
    @Lob
    private String password;
    @ManyToOne
    @JoinColumn (name="role_id")
    private Role role;
	
    public User() {
	super();
    }
	
    public User(String email, String password, Role role) {
	super();
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

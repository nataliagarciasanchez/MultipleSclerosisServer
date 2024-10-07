/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisPOJOs;
import java.io.Serializable;


/**
 *
 * @author nataliagarciasanchez
 */
@Entity
@Table (name= "users")
public class User implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (generator = "users")
    @TableGenerator(name = "users", table = "sqlite_sequence",  pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "users")
    private Integer id;
    private String email;
    @Lob
    private byte[] password;
    @ManyToOne
    @JoinColumn (name="role_id")
    private Role role;
	
    public User() {
	super();
    }
	
    public User(String email, byte[] password, Role role) {
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
    public byte[] getPassword() {
	return password;
    }
    public void setPassword(byte[] password) {
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
        return "User [id=" + id + ", email=" + email + ", password=" + Arrays.toString(password) + ", role=" + role
		+ "]";
    }
}

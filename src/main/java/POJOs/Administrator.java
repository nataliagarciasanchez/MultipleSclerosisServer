/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJOs;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author Andreoti
 */

public class Administrator implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private Integer id;
    

    private User user;
    
    public Administrator(){
    super();
    }

    public Administrator(Integer id,String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
    
    public void setUser(User user){
       this.user = user;
    }
    
    

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Administrator{" + "name=" + name + ", id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.name);
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Administrator other = (Administrator) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }
    
    
   
    
}

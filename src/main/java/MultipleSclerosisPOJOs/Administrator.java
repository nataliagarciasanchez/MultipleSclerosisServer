/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MultipleSclerosisPOJOs;

import java.io.Serializable;

/**
 *
 * @author Andreoti
 */
public class Administrator implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String name;
    private Integer id;

    public Administrator(String name, Integer id) {
        this.name = name;
        this.id = id;
    }
   
    
}

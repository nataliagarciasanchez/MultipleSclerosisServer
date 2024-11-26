/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Security;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 *
 * @author Andreoti
 */
public class PasswordEncryption {
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    //method for hashing password
    public static String hashPassword(String plainPassword){
        return encoder.encode(plainPassword);
    }
    
    //method to verify that password introduced matches the hashedpassword
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}

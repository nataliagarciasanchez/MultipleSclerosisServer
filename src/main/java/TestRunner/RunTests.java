/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestRunner;

import org.junit.platform.console.ConsoleLauncher;

public class RunTests {
    public static void main(String[] args) {
        ConsoleLauncher.main(new String[]{"--scan-classpath"});
    }
}


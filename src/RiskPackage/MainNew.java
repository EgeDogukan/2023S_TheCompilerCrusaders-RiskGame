package RiskPackage;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

import uipackage.login;

public class MainNew {
    public static void main(String[] args) {
        
    	GameControllerNew instance = GameControllerNew.getInstance();
    	try {
			instance.main(args);
		} catch (InterruptedException e) {
			e.printStackTrace();
		
		}   
    }
}

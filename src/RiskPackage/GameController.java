package RiskPackage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JFrame;

//import javax.management.StringValueExp;

import uipackage.*;;

public class GameController {
	
	private static int turnID = 0;
	private static ArrayList<Player> playerList = new ArrayList<Player>();
	public static boolean isBuildingMode;
	
	private GameController() {
		
	}
	
	public static GameController getInstance() {
		
		GameController instance = new GameController();
		return instance;
		
	}
	
	
	
    public static void main(String[] args) throws Exception {
        
    	MainMenu menu = new MainMenu();
    	menu.setVisible(true);
        
    	login loginPage = new login();
    	
    	while(true) {
    	if (menu.isLoginClicked==false) {
    		
            loginPage.frame.setVisible(false);
    	}
    	
    	else if (menu.isLoginClicked==true) {
    		menu.dispose();
    		loginPage.frame.setVisible(true);
    		


        do {                                                        //waiting until login phase completed
            System.out.println(loginPage.getLoginStatus());
            
        } while (loginPage.getLoginStatus() == false);
		loginPage.frame.dispose();
		
		whichMode modeSelection = new whichMode();
		modeSelection.setVisible(true);
		
		do {                                                        //waiting until login phase completed
            System.out.println(modeSelection.status);
            
        } while (modeSelection.status == -1);
		modeSelection.dispose();
		
		if (modeSelection.status==1) {
			
			BuildingMode RiskGameFrame = new BuildingMode();
		    RiskGameFrame.setLayout(null);
		    RiskGameFrame.setVisible(true);
	
		    // Declare a variable to store the number of players
		    AtomicInteger numberOfPlayers = new AtomicInteger(0);
		    AtomicInteger numberOfAIPlayers = new AtomicInteger(0);
		    
		    
		    do {                                                        //waiting until login phase completed
	            System.out.println(loginPage.getLoginStatus());
	            
	        } while (loginPage.getLoginStatus() == false);
			loginPage.frame.dispose();
		    
			
			do {                                                        //waiting until login phase completed
	            System.out.println(RiskGameFrame.getNumberOfPlayer());
	            
	        } while (RiskGameFrame.getNumberOfPlayer() < 0);
			
			numberOfPlayers.set(RiskGameFrame.getNumberOfPlayer());
			
			
			do {                                                        //waiting until login phase completed
	            System.out.println(RiskGameFrame.getNumberOfComp());
	            
	        } while (RiskGameFrame.getNumberOfComp() < 0);
	
			numberOfAIPlayers.set(RiskGameFrame.getNumberOfComp());
		        
	
		    // Use the variable after the frame is closed
		    System.out.println("Number of players: "+numberOfPlayers.get());
		    System.out.println("Number of AI players: "+numberOfAIPlayers.get());
		    
		    for (Continents continent : RiskGameFrame.getContinent()) {
		    	System.out.println("Name of the continent: "+continent.getName());
		    	for (Territory territory : continent.getTerritories()) {
		    		System.out.println("    Name of the territory: "+territory.getName());
		    	}
		    }
		    
		    isBuildingMode=true;
		    
		    GameController.playerList = initGame(numberOfPlayers.get(), numberOfAIPlayers.get(), RiskGameFrame.getContinent());
	        ArrayList<Continents> c = RiskGameFrame.initalSharing(playerList);
			RunningMode g = new RunningMode(c, playerList, numberOfAIPlayers.get(), numberOfPlayers.get());
	        
	        g.setLayout(new BorderLayout());
	        g.setVisible(true);
			turnID = g.getTurn() - 1;
			System.out.println("Currently, building mode!");
			RiskGameFrame.dispose();
			RiskGameFrame.dispose();
			break;
		}
		
		else {
			
			isBuildingMode=false;
			LoadMode RiskGameFrame = new LoadMode();
		    RiskGameFrame.setLayout(null);
		    RiskGameFrame.setVisible(true);
		    
		    GameController.playerList = initGameLoadMode(4, 0, RiskGameFrame.getContinent());
	        ArrayList<Continents> c = RiskGameFrame.initalSharing(playerList);
			RunningMode g = new RunningMode(c, playerList, 0, 4);
			
	        g.setLayout(new BorderLayout());
	        g.setVisible(true);
			turnID = g.getTurn();
			RiskGameFrame.dispose();
			break;
			
		}
	
		}
	}

	}
    
    public static ArrayList<Player> getPlayers(){
    	return playerList;
    }
    

    public static Color randomColorGenerator() {
    	Random random = new Random(); // Create a new Random object
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
    	return new Color(r,g,b);
    }
    
    static private ArrayList<Player> initGameLoadMode(int numberofPlayers, int numberofComp, ArrayList<Continents> continents) {
    	ArrayList<Territory> territories = new ArrayList<Territory>();
    	ArrayList<Player> playerList = new ArrayList<Player>();
    	
    	for (Continents continent : continents) {
    		if (continent.isIncluded){
				for (Territory territory : continent.getTerritories()){
					territories.add(territory);
				}
    		}
		}
    	
    	for (int i=0;i<numberofPlayers;i++) {
    		ArrayList<Territory> currentTerritories = new ArrayList<Territory>();
    		for (Territory territory : territories) {
        		if (territory.getOwnerID()==i){
        			currentTerritories.add(territory);
        		}
        	}
    		playerList.add(new Player(i, randomColorGenerator(), currentTerritories));
    	}
    	
    	return playerList;
    }
    

	static public ArrayList<Player> initGame(int numberofPlayers, int numberofComp, ArrayList<Continents> continents) throws Exception {
		
		
		/*
		Specifications for initGame() Method:
		initGame initialises the game by assigning territories to players based on the specified parameters.
		
		Requires:
		The number of players and computer players should not be negative.
		The continent list should not be null.
		
		Modifies:
		playerList
		territories
		
		Effects:
		Returns an ArrayList of Player objects representing the initialized players with assigned territories.
		Throws an exception if any of the following conditions are violated:
		- The number of players is negative.
		- The number of computer players is negative.
		- The continent list is null.
		- There are insufficient territories to assign to players.
		
		*/
		
		
		if (numberofPlayers<0) {
			throw (new Exception("number of player cannot be negative"));
		}
		else if (numberofComp<0) {
			throw (new Exception("number of computer player cannot be negative"));
		}
		else if (continents==null) {
			throw (new Exception("continent list cannot be null"));
		}
		
		
		ArrayList<Territory> territories = new ArrayList<Territory>();
		for (Continents continent : continents) {
			if (continent.isIncluded){
				for (Territory territory : continent.getTerritories()){
					territories.add(territory);
				}
			}
			
		}
		
		if(continents.size()<numberofComp+numberofComp) {
			throw (new Exception("insufficient territories to assign to players"));
		}
		
		
		
		int territoryPerPlayer = Math.floorDiv(territories.size(), (numberofPlayers+numberofComp));
		System.out.println("Number of territory: "+territories.size());
		System.out.println("Territory per player is: "+territoryPerPlayer);
		
		
		ArrayList<Player> playerList = new ArrayList<Player>();
		
		Collections.shuffle(territories);
		for(int j = 0; j < numberofComp + numberofPlayers; j++) {
			
			ArrayList<Territory> currentTerritories = new ArrayList<Territory>();
			
			
			for (int i=0; i<territoryPerPlayer;i++) {
				currentTerritories.add(territories.get(i+j*territoryPerPlayer));
			}
			
			playerList.add(new Player(j, randomColorGenerator(), currentTerritories));

		}
		
		
		return playerList;
	}
	


	public static int getCurrentTurnPlayerID() {
		return turnID;
	}

	public static void setCurrentTurnPlayerID(int ID) {
		turnID = ID - 1;
	}
	
	public boolean repOk() {
        if (playerList==null) {
            return false;
        }
        return true; 
    } 
	
	
}
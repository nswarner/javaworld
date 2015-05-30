/*	Purpose
 *
 *		The Player class is a model which simulates a fictional person in JavaWorld. This
 *		Player-Character (PC) interacts with Items, Mob-AI, Weather patterns, and moves
 *		throughout the virtual world through cardinal directions of north, east, south,
 *		west, as well as simulating a third dimension of up and down. The PC has health,
 *		ability points (such as magic, mana, rage, ... as in other games), an inventory,
 *		physical characteristics, and several other fields.
 *
 *	Algorithm
 *
 *		1. Declare the Player class
 *			- The class will extend Thread
 *			- The class will implement Serializable
 *		2. Declare an Inventory Object for the PC
 *		3. Declare a PhysicalCharacteristics for the PC
 *		4. Declare a Room Object
 *			- This will reference the PC's current Room location
 *		5. Declare a Zone Object
 *			- This will reference the PC's current Zone
 *		6. Declare necessary Unique / Fixed PC Data
 *			- Player Name
 *			- Player Title (Player set description of themselves)
 *			- Player Rank (Related to level / creator status)
 *			- Player Short Description (used for interactions, "Pete", "Fred", ...)
 *			- Player Long Description (used for descriptions, "Pete stands here")
 *		7. Declare the necessary state related PC Data
 *			- Player Level
 *			- Player Max Health
 *			- Player Max Mana
 *			- Player Current Health
 *			- Player Current Mana
 *		8. Declare necessary default and parameterized constructors
 *		9. Declare a method to check if a player file exists
 *		10. Declare a method to check if a given username password combination is valid
 *		11. Declare a method to save a given Player to file
 *		12. Declare a method to load a given Player from file
 *
 *	Structure / Process
 *
 *		The Player is initialized from the accept() thread and becomes a part of the static
 *		Players ArrayList which is a collection of all active Players (and by extension, their
 *		sockets). Once the Player has been added to the Players ArrayList, the GameServer's
 *		gameLoop will then begin interpreting input from the client each iteration until the
 *		client sends the "quit" or "exit" command.
 *
 *	Author			- Nicholas Warner
 *	Created 		- 4/24/2015
 *	Last Updated	- 5/1/2015
 */

// Import necessary packages, classes, and interfaces
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class Player {

	public static final int PLAYER_NONE			= 0;
	public static final int PLAYER_HEALTH		= 1;
	public static final int PLAYER_MANA 		= 2;
	public static final int PLAYER_STRENGTH		= 3;
	public static final int PLAYER_DEXTERITY	= 4;
	public static final int PLAYER_INTELLIGENCE	= 5;
	public static final int PLAYER_WISDOM		= 6;
	public static final int PLAYER_FORTITUDE	= 7;

	// The list of all active players in the game; Vector for synchronization
	private static Vector<Player> playerList = new Vector<Player>();

	// The PC's Inventory of Items
	private Inventory playerInventory;
	
	// The PC's Equipped Items
	private Equipment playerEquipment;

	// A reference to the PC's current Room inside the given Zone
	private Room currentRoom;
	
	// A reference to the PC's current World inside JavaWorld
	private World currentWorld;

	// Unique / Fixed PC Characteristics
	private String playerName;
	private String playerTitle;
	private String playerRank;
	private String shortDescription;
	private String longDescription;
	private String password;
	private boolean playerQuit;
	private boolean frozen;
	
	// State related PC Data
	private int playerLevel;
	private int maxHealth;
	private int maxMana;
	private int currentHealth;
	private int currentMana;
	private boolean[][] discoveredRooms;
	private boolean[] pScore;
	private long waiting;
	private long chatWaiting;

	// Connection related data
	Socket playerConnection;
	OutputStream playerOut;
	InputStream playerIn;

	// Default Constructor
    public Player() {
    	
    	playerInventory		= null;
    	currentRoom			= World.getRoom(100, 100);
    	currentWorld		= currentRoom.getWorld();
    	
    	playerName			= "JohnDoe";
    	playerTitle			= ", adventurer of JavaWorld";
    	playerRank			= "Unknown";
    	shortDescription	= "";
    	longDescription		= "";
		password			= "";
		playerQuit			= false;
    	frozen				= false;
    	
    	playerLevel			= 1;
    	maxHealth			= 100;
    	maxMana				= 100;
    	currentHealth		= 100;
    	currentMana			= 100;
		discoveredRooms		= new boolean[World.MAXROOMS][World.MAXROOMS];
		waiting				= System.currentTimeMillis();
		chatWaiting			= System.currentTimeMillis();     	

    	playerConnection	= null;
		playerOut			= null;
		playerIn			= null;
				
		playerInventory		= new Inventory();
		playerEquipment		= new Equipment();
    }

	// Accessor Methods
	public boolean getConnected() {
		
		if (playerOut == null) {
			
			return true;
		}
		
		return false;
	}

	public String getName() {
		
		return playerName;
	}
	
	public int getX() {

		return this.currentRoom.getX();
	}
	
	public int getY() {
		
		return this.currentRoom.getY();
	}
	
	public int getMaxHealth() {
		
		return maxHealth;
	}

	public int getHealth() {
		
		return currentHealth;
	}
	
	public World getWorld() {
		
		return currentWorld;
	}
	
	public int getLevel() {
		
		return playerLevel;
	}
	
    public Room getRoom() {
		
		return currentRoom;
	}

	public long getWaiting() {
		
		return waiting;
	}

	public long getChatWaiting() {
		
		return chatWaiting;
	}

	public String getAllEquipment() {
		
		return playerEquipment.displayEquipment();
	}
	
	public String getTitle() {
		
		return playerTitle;
	}
	
	public String getRank() {
		
		return playerRank;
	}
	
	public String getShortDescription() {
		
		return shortDescription;
	}
	
	public String getLongDescription() {
		
		return longDescription;
	}
	
	public int getMaxMana() {
		
		return maxMana;
	}
	
	public int getCurrentHealth() {
		
		return currentHealth;
	}
	
	public int getCurrentMana() {
		
		return currentMana;
	}

	public boolean getFrozen() {
		
		return frozen;
	}

	public static String getWhoStatus() {

		String output = "";
		String status = "";
		
		for(Player onePlayer: playerList) {
			
			if (onePlayer.isAdmin()) {
				
				status = "\t#rAdmin#n\t";
			}
			
			else {
				
				status = "\t" + onePlayer.getLevel() + "\t";
			}
			
			output += status + "#c" + onePlayer.getName() + "#n" + onePlayer.playerTitle + "#n\n\r";
		}
		
		return output;
	}
	
	public static String getPlayersInRoom(Player player) {

		String output = "";
		
		for(Player onePlayer: playerList) {
			
			if (!onePlayer.equals(player) && player.getRoom().equals(onePlayer.getRoom())) {
				
				output += "#Y" + onePlayer.getName() + "#n is standing here, chilling.\n";
			}
		}
		
		return output;
	}
	
	public static int getNumberOnline() {
		
		return playerList.size();
	}
	
	// Mutator Methods	
	public void clearDiscoveredRooms() {
		
		for(int i = 0; i < World.MAXROOMS; i++) {
			
			discoveredRooms[i][i] = false;
		}
	}
	public static void addPlayer(Player newPlayer) {
		
		playerList.add(newPlayer);
		infoAll(newPlayer.getName() + " has logged into Java World.");
	}
	
	public void setTitle(String title) {
		
		playerTitle = title;
	}
	
	public void setRank(String rank) {
		
		playerRank = rank;
	}
	
	public void setShortDescription(String shortDescription) {
		
		this.shortDescription = shortDescription;
	}
	
	public void setLongDescription(String longDescription) {
		
		this.longDescription = longDescription;
	}

	public void setMaxHealth(int maxHealth) {
		
		maxHealth = maxHealth;
	}
	
	public void setMaxMana(int maxMana) {
		
		this.maxMana = maxMana;
	}
	
	public void setCurrentHealth(int currentHealth) {
		
		this.currentHealth = currentHealth;
	}
	
	public void setCurrentMana(int currentMana) {
		
		this.currentMana = currentMana;
	}
		
	public void setLevel(int level) {
		
		playerLevel = level;
	}

	public void setCurrentRoom(Room room) {
		
		currentRoom = room;
	}
	
	public void setCurrentWorld(World world) {
		
		currentWorld = world;
	}
	
	public void setQuit(boolean quit) {
		
		playerQuit = quit;
	}
		
	// If the player exists AND the password is correct, return true
	public static boolean checkPlayerLogin(Player onePlayer) {

		// For the player's name
		String playerName = "";

		// We store the read-in file password here		
		String filePassword = "";
		
		// Our Scanner Object for File Input
		Scanner fileIn = null;
		
		// Prepare our String
		playerName = onePlayer.getName().toLowerCase();

		// So long as the player exists, there should be a password file		
		if (checkPlayerExists(playerName)) {

			Password onePassword = loadPWordFile(playerName);
						
			// If we have a match, or we're manually resetting the password
			if (onePassword.comparePassword(onePlayer.password)) {
				
				// Correct password entered
				return true;
			}
		}

		// Incorrect password entered		
		return false;
	}
	
	// Test whether the given player exists
	public static boolean checkPlayerExists(String playerName) {

		// Ensure we're comparing against lowercase
		if ((new File("../player/" + playerName.toLowerCase())).exists()) {
			
			return true;
		}
		
		return false;
	}

	// A method to save a single player file
	public static void savePlayer(Player onePlayer) {
		
		// Declare a PrintWriter object for writing
		PrintWriter fileOut = null;
		
		// Let's try to write the entirety of the player to file
		try {
			
			// Instantiate our PrintWriter object
			fileOut = new PrintWriter(new File("../player/" + onePlayer.getName().toLowerCase()));
			
			// Write the entirety of the player to file
			fileOut.println(onePlayer.getName());
			fileOut.println(onePlayer.getTitle());
			fileOut.println(onePlayer.getRank());
			fileOut.println(onePlayer.getShortDescription());
			fileOut.println(onePlayer.getLongDescription());
			fileOut.println(onePlayer.getLevel());
			fileOut.println(onePlayer.getMaxHealth());
			fileOut.println(onePlayer.getMaxMana());
			fileOut.println(onePlayer.getCurrentHealth());
			fileOut.println(onePlayer.getCurrentMana());
			
			// Always close the file!
			fileOut.close();
		}
		
		// Catch any exceptions
		catch (IOException e) {
			
			System.out.println("IOException in savePlayer: " + e.getMessage());
			System.exit(0);
		}
		
		catch (Exception e) {
			
			System.out.println("Generic Exception in savePlayer: " + e.getMessage());
			System.exit(0);
		}
		
		// Try to save all the player's discovered rooms
		try {

			// Test whether the user has discovered a single room or not
			boolean oneRoom = false;
			
			// Instantiate the PrintWriter to a discoveredRooms file
			fileOut = new PrintWriter(new File("../player/" + onePlayer.getName().toLowerCase() +
											   ".discoveredRooms"));
			
			// ... this is a little rough in a save function
			for(int i = 0; i < World.MAXROOMS; i++) {
				
				// ... possibly convert to a linked list
				for(int c = 0; c < World.MAXROOMS; c++) {
					
					// Check MAXROOMS * MAXROOMS to see if it's discovered
					if (onePlayer.discoveredRooms[i][c]) {
						
						// Print out the (x, y) into the discoveredRooms file
						fileOut.print(i + " " + c + " ");
						oneRoom = true;
					}
				}
			}
			
			// If there were literally no rooms discovered, they know of the home location
			if (!oneRoom) {
				
				fileOut.print(World.HOMELOCATION + " " + World.HOMELOCATION + " ");
			}
			
			// -1 indicates the end of the section
			fileOut.println("-1");
			
			// Always close the file!
			fileOut.close();
		}

		// Catch any Exceptions
		catch (IOException e) {
			
			System.out.println("IOException in savePlayer(discRooms): " + e.getMessage());
			System.exit(0);
		}
		
		catch (Exception e) {
			
			System.out.println("Generic Exception in savePlayer(discRooms): " + e.getMessage());
			System.exit(0);
		}
		
		// Save the player's inventory
		onePlayer.playerInventory.saveInventory(onePlayer.getName());
	}

	// A method to load a player given the player's name
	public static Player loadPlayer(String playerName) {
		
		// Declare a Scanner Object to read in from a file
		Scanner fileIn = null;
		// Declare temporary Strings for read in data
		String playerTitle = "";
		String playerRank = "";
		String playerShortDescription = "";
		String playerLongDescription = "";
		// Declare temporary ints for read in data
		int playerLevel = 0;
		int maxHealth = 0;
		int maxMana = 0;
		int currentHealth = 0;
		int currentMana = 0;
		
		// Ensure we're working with a lower case String
		playerName = playerName.toLowerCase();

		// Make sure we're doing this safely
		try {

			// Instantiate our Scanner object
			fileIn = new Scanner(new File("../player/" + playerName));

			// Read in the following temporary data
			playerName = fileIn.nextLine();
			playerTitle = fileIn.nextLine();
			playerRank = fileIn.nextLine();
			playerShortDescription = fileIn.nextLine();
			playerLongDescription = fileIn.nextLine();
			playerLevel = Integer.parseInt(fileIn.nextLine());
			maxHealth = Integer.parseInt(fileIn.nextLine());
			maxMana = Integer.parseInt(fileIn.nextLine());
			currentHealth = Integer.parseInt(fileIn.nextLine());
			currentMana = Integer.parseInt(fileIn.nextLine());
			
			// Always close the file!
			fileIn.close();
		}		
		
		// Catch any Exceptions
		catch (IOException e) {
			
			System.out.println("IOException in loadPlayer: " + e.getMessage());
			System.exit(0);
		}
		
		catch (Exception e) {
			
			System.out.println("General Exception in LoadPlayer: " + e.getMessage());
			System.exit(0);
		}
		
		// Create a new Player and set its values
		Player onePlayer = new Player();
		onePlayer.setName(playerName);
		onePlayer.setTitle(playerTitle);
		onePlayer.setRank(playerRank);
		onePlayer.setShortDescription(playerShortDescription);
		onePlayer.setLongDescription(playerLongDescription);
		onePlayer.setLevel(playerLevel);
		onePlayer.setMaxHealth(maxHealth);
		onePlayer.setMaxMana(maxMana);
		onePlayer.setCurrentHealth(currentHealth);
		onePlayer.setCurrentMana(currentMana);
		
		// Load the player's inventory
		onePlayer.playerInventory.loadInventory(onePlayer.getName());

		// If we're using the same seed
		if (!Config.getNewSeedFile()) {

			// Let's try to load the given file
			try {
	
				int roomX = 0;
				int roomY = 0;
				boolean keepGoing = true;
	
				fileIn = new Scanner(new File("../player/" + playerName + ".discoveredRooms"));
	
				while(keepGoing) {
					
					roomX = fileIn.nextInt();
					
					if (roomX != -1) {
	
						roomY = fileIn.nextInt();
						
						onePlayer.discoveredRooms[roomX][roomY] = true;
					}
					
					else {
						
						keepGoing = false;
					}
				}
	
				fileIn.close();
			}
	
			// Catch any Exceptions
			catch (IOException e) {
				
				System.out.println("IOException in loadPlayer: " + e.getMessage());
				System.exit(0);
			}
			
			catch (Exception e) {
				
				System.out.println("General Exception in LoadPlayer: " + e.getMessage());
				System.exit(0);
			}
		}
		
		// New seed file, they only have HOME LOCATION discovered
		else {
			
			onePlayer.discoveredRooms[World.HOMELOCATION][World.HOMELOCATION] = true;
		}
		
		// Return the given player
		return onePlayer;
	}
	
	// Save the player's inventory
	public void saveInventory() {
		
		playerInventory.saveInventory(playerName);
	}

	// Messages all currently connected Players
	public static void messageAll(String msg) {
		
		for(Player onePlayer: playerList) {
			
			onePlayer.message(msg);
			onePlayer.message(onePlayer.buildPrompt());
		}
	}
	
	// Messages all currently connected Players except given Player
	public static void messageAll(String msg, Player dontMsg) {
		
		for(Player onePlayer: playerList) {
			
			if (!onePlayer.equals(dontMsg)) {
				
				onePlayer.message(msg);
				onePlayer.message(onePlayer.buildPrompt());
			}
		}
	}

	// If we don't have a Player object but do have their name, message them by name
	public static void sendMessageToPlayerByName(String playerName, String msg) {
		
		// Get the player's name
		playerName = TextManipulator.oneArgument(playerName).toLowerCase();
		
		// Find the player in the player list
		for(Player onePlayer: playerList) {
			
			if (onePlayer.getName().toLowerCase().equals(playerName)) {
				
				// Message the given player
				onePlayer.message(msg + "\n\r");
				onePlayer.message(onePlayer.buildPrompt());
			}
		}
	}

	// A method to freeze a player's input (player can no longer act in game	
	public static boolean freezePlayer(String playerName) {
		
		// Get the player's name
		playerName = TextManipulator.oneArgument(playerName).toLowerCase();
		
		// Find the player
		for(Player onePlayer: playerList) {
			
			if (onePlayer.getName().toLowerCase().equals(playerName)) {
				
				// Freeze the given player
				onePlayer.frozen = true;
				return true;
			}
		}
		
		// Player wasn't found
		return false;
	}
	
	// If a player was frozen, they are now unfrozen
	public static boolean unFreezePlayer(String playerName) {
		
		// Get the player's name
		playerName = TextManipulator.oneArgument(playerName).toLowerCase();
		
		// Find the player
		for(Player onePlayer: playerList) {
			
			if (onePlayer.getName().toLowerCase().equals(playerName)) {
			
				// Unfreeze the player	
				onePlayer.frozen = false;
				return true;
			}
		}
		
		// Player wasn't found
		return false;		
	}
	
	// Create the player's password file (Serialized)
	public static void createPWordFile(Player newPlayer) {

		// Create a Password object from the player's password
		Password pWord = new Password(newPlayer.password);
		// Get the player's name
		String charName = newPlayer.getName().toLowerCase();
		
		// Save the serialized player password file		
		try {

			ObjectOutputStream objWriter = new ObjectOutputStream(
											new FileOutputStream("../player/pwdfiles/" + charName));
			
			objWriter.writeObject(pWord);
			
			objWriter.close();
		}

		// Catch thrown exceptions		
		catch (IOException e) {			
			System.out.println("Unable to create pWord file, " + charName + "!");
		}
		
		catch (Exception e) {
			System.out.println("Generic Exception thrown. See message: " + e.getMessage());
		}
	}
	
	// Load the given password file from the player's name
	public static Password loadPWordFile(String charName) {
		
		// Declare a Password object
		Password pWord = null;
		// Ensure we have an accurate player name
		charName = charName.toLowerCase();
		
		// Try to open the PWD File
		try {
			
			ObjectInputStream objReader = new ObjectInputStream(
											new FileInputStream("../player/pwdfiles/" + charName));
			
			// Read in a Serialized password (typecasted appropriately)
			pWord = (Password)objReader.readObject();
			
			// Close the ObjectInputStream ... Always close it!
			objReader.close();
		}

		// Catch any exceptions thrown
		catch (FileNotFoundException e) {
			System.out.println("Password File " + charName.toLowerCase() + 
							   " wasn't found.");
		}
		
		catch (EOFException e) {
			System.out.println("Password File " + charName.toLowerCase() + 
							   " had EOF read issues.");
		}
		
		catch (ClassNotFoundException e) {
			System.out.println("Issue with the Password class. Unable to load.");
		}
		
		catch (IOException e) {
			System.out.println("Issue reading Password File: " + charName.toLowerCase() + ".");
			System.out.println("Exception Message: " + e.getMessage());
		}
		
		catch (Exception e) {
			System.out.println("Generic Exception thrown. See message: " + e.getMessage());
		}

		// Return either a proper Player object or null		
		return pWord;
	}

	// In the event that something requires messaging all players, let's ensure they're not
	// left without a prompt.
    public static void rebuildAllPrompts() {
		
		for(Player onePlayer: playerList) {

			onePlayer.message(onePlayer.buildPrompt());
		}
	}

	// Mutator Methods
	public void setPlayerConnection(Socket playerConnection) {
		
		this.playerConnection = playerConnection;
	}
	
	public void setOutput(OutputStream playerOut) {
		
		this.playerOut = playerOut;
	}
	
	public void setInput(InputStream playerIn) {
		
		this.playerIn = playerIn;
	}

	public void setName(String playerName) {
		
		this.playerName = playerName;
	}

	public void setPassword(String password) {
		
		this.password = password;
	}
		
	public void setHealth(int currentHealth) {
		
		this.currentHealth = currentHealth;
	}
	
	/*********************************************
	 *
	 *		Player Class Methods
	 *
	 *********************************************/

	// Read input from a given Player
	public String readInput() {
		
		// For user input
		int c = 0;
		String temp = "";
		
		// Working with sockets
		try {

			// If there's something to be read from the socket
			if (this.playerIn.available() > 0) {
				
				// Let's read it in character by character
				while((c = this.playerIn.read()) >= 0) {
					
					// If this constitutes one command, we return the input
					if (c == '\n') {
						
						return temp;
					}
					
					// Otherwise, so long as it's a valid character, we build the String
					else if (c >= 32 && c <= 126) {
						
						temp += (char)c;
					}
				}
			}		
		}
		
		// Catch any Exceptions
		catch (IOException e) {
			
			System.out.println("IOException in readInput: " + e.getMessage());
			System.out.println("Exiting on error.");
			System.exit(0);
		}
		
		catch (Exception e) {
			
			System.out.println("Caught a generic exception. Exiting on error.");
			System.exit(0);
		}
		
		// There was nothing in the socket to read
		return null;
	}
	
	// Message one Player connected to JavaWorld
	public void message(String msg) {
		
		msg = TextManipulator.addColor(msg);
		
		// Working with sockets
		try {
			
			// Write one character at a time to the socket
			for(int i = 0; i < msg.length(); i++) {
				
				playerOut.write(msg.charAt(i));
			}
		}
		
		// Catch any Exceptions
		catch (IOException e) {
			
			if (!playerQuit) {

				System.out.println("Caught IOException in method message: " + e.getMessage());
				System.out.print("Expecting client has disconnected. Removing player from ");
				System.out.println("the player list.");
				
				playerQuit = true;
			}
		}
	}

	// Ensure the password field is empty
	public void clearPassword() {
		
		this.password = "";
	}
	
	// Check whether two passwords are equivalent
	public boolean comparePassword(String password) {
		
		if (this.password.equals(password)) {
			
			return true;
		}
		
		return false;
	}
	
	// Damage all players within a given room
	public static void damagePlayersInRoom(int x, int y, String shortDescription) {

		int damage = 0;
		
		for(Player onePlayer: playerList) {
			
			if (!onePlayer.isAdmin()) {
				
				if (onePlayer.getX() == x && onePlayer.getY() == y && x != 100 && y != 100) {
					
                    // Do 2-5 damage to a player
                    damage = ToolKit.rand(2, 5);
                    onePlayer.damagePlayer(damage);
                    onePlayer.message(shortDescription + " lashes out at you! Ouch! [#R" + 
                    									 damage + "#n]");
                    onePlayer.message(onePlayer.buildPrompt());					
				}
			}
		}		
	}
	
	// Damage an individual player a given amount
	public void damagePlayer(int damageAmount) {
		
		currentHealth -= damageAmount;
	}

	// Build an individual player's prompt
	public String buildPrompt() {
		
		String temp;
		String hColor;
		String nColor = "#y";
		
		switch(this.getHealth() / 10) {

			// Let color be a general indicator of health			
			case 10: hColor = "#C"; break;
			case 9:
			case 8: hColor = "#g"; break;
			case 7: 
			case 6: 
			case 5:
			case 4: hColor = "#y"; break;
			case 3: 
			case 2:
			case 1:
			case 0: hColor = "#r"; break;
			default: hColor = "#r"; break;
		}
		
		// If it's an admin, they're special
		if (playerName.equalsIgnoreCase(Config.getAdmin())) {
			
			nColor = "#g";
		}
		
		// Build the prompt
		temp = "\r\n<" + nColor + this.getName() + "#n: " + hColor + this.getHealth() + 
			   "#n/#C" + this.getMaxHealth() + "#n> ";
		
		// Return the prompt
		return temp;
	}
	
	// Remove the discovery of a given (x, y)
	public void undiscoverRoom(int x, int y) {
		
        if (x >= 0 && y >= 0 && x < World.MAXROOMS && y < World.MAXROOMS) {

            discoveredRooms[x][y] = false;
        }
	}
	
	// Set the location of a Player to (x, y)
	public void setLocation(int x, int y) {
	
		if (x >= 0 && x < World.MAXROOMS && y >= 0 && y < World.MAXROOMS) {
		
			this.currentRoom = World.getRoom(x, y);
		}
	}
    
    // Move a player East
    public boolean moveEast() {
		
		int x = this.currentRoom.getX();
		int y = this.currentRoom.getY();
		
		if (World.checkRoomExists(x + 1, y)) {
			
			this.currentRoom = World.getRoom(x + 1, y);
			return true;
		}
		
		return false;
	}

	// Move a player West
    public boolean moveWest() {
		
		int x = this.currentRoom.getX();
		int y = this.currentRoom.getY();
		
		if (World.checkRoomExists(x - 1, y)) {
			
			this.currentRoom = World.getRoom(x - 1, y);
			return true;
		}
		
		return false;
	}

	// Move a player north
	public boolean moveNorth() {
		
		int x = this.currentRoom.getX();
		int y = this.currentRoom.getY();
		
		if (World.checkRoomExists(x, y + 1)) {
			
			this.currentRoom = World.getRoom(x, y + 1);
			return true;
		}
		
		return false;
	}

	// Move a player south
    public boolean moveSouth() {
		
		int x = this.currentRoom.getX();
		int y = this.currentRoom.getY();
		
		if (World.checkRoomExists(x, y - 1)) {
			
			this.currentRoom = World.getRoom(x, y - 1);
			return true;
		}
		
		return false;
	}
	
	// Discover a given (x, y) room
    public void discoverRoom() {

        int x = getX();
        int y = getY();

        if (x >= 0 && x < World.MAXROOMS && y >= 0 && y < World.MAXROOMS) {

            discoveredRooms[x][y] = true;
        }
	}

	// Test whether a room has been discovered	
    public boolean discoveredRoom(int x, int y) {
	
        if (x >= 0 && y >= 0 && x < World.MAXROOMS && y < World.MAXROOMS) {

            return discoveredRooms[x][y];
        }
        
        return false;
	}
	
	// Update the state of the Player
	public static void updateState() {
		
		// This will relate to future updates, specifically objectives
	}
	
	// Read input from the player's socket and Interpret it via Interpreter class
	public static void interpretInput() {
		
		String playerInput = "";
		Player quitPlayer = null;
		
		// Iterate through the list of Players to interpret their input
		for(Player onePlayer : playerList) {

			if (onePlayer.getWaiting() > System.currentTimeMillis()) {
				
				continue;
			}
			
			playerInput = onePlayer.readInput();
			
			if (playerInput != null && playerInput != "") {

				Interpreter.checkCommand(onePlayer, playerInput);					
			}
			
			if (onePlayer.playerQuit) {
				
				quitPlayer = onePlayer;
			}

		} // for

		if (quitPlayer != null) {
			
			String tempName = quitPlayer.getName();

			playerList.remove(quitPlayer);
			
			System.out.println(tempName + " has quit.");
			infoAll(tempName + " has logged out of Java World.");
		}
	}

	// Test whether the given player is an admin or not
	public static boolean isAdmin(Player onePlayer) {
		
		if (onePlayer.isAdmin()) {
			
			return true;
		}
		
		return false;
	}
	
	// Send an information message to everyone in the game
	public static void infoAll(String msg) {
		
		msg = "#YInfo -> #n" + msg;
		messageAll(msg);		
	}
	
	// Pick up a give item
	public void pickupItem(Item oneItem) {
		
		playerInventory.addToInventory(oneItem);
		currentRoom.removeItem(oneItem);
	}

	// Drop a given item	
	public void dropItem(Item oneItem) {
		
		if (!playerEquipment.isEquipped(oneItem)) {

			message(playerInventory.removeFromInventory(oneItem));
			currentRoom.addItem(oneItem);
		}
		
		else {
			
			message("You cannot drop an equipped item.");
		}
	}
	
	// Display the player's inventory
	public String displayInventory() {
		
		return playerInventory.displayInventory(false);
	}
	
	// Check whether the player has a given Item
	public Item hasItem(String oneItem) {
		
		return playerInventory.contains(oneItem);
	}

	// Check whether the given Item is equipped on the player
	public Item hasItemInEquipment(String oneItem) {
		
		return playerInventory.containsInEquipment(oneItem);
	}

	// Check whether the Item is in the Player's inventory
	public Item hasItemInInventory(String oneItem) {
		
		return playerInventory.containsInInventory(oneItem);
	}
	
	// Equip a given item on a player
	public String equipItem(Item oneItem) {
		
		return playerEquipment.equipItem(oneItem);
	}
	
	// Unequip a given item
	public String unequipItem(Item oneItem) {
		
		return playerEquipment.unequipItem(oneItem);
	}
	
	// Save every player, then close their connection
	public static void saveAndCloseConnections() {
		
		for(Player onePlayer: Player.playerList) {

			Interpreter.commandSavePlayer(onePlayer);
			
			try {

				onePlayer.playerConnection.close();
			}
			
			catch (IOException e) {
				
				System.out.println("IOException with comm_shutdown: " + e.getMessage());
			}
		}
	}

	// Test whether the player is an administrator	
	public boolean isAdmin() {
		
		if (playerName.equalsIgnoreCase(Config.getAdmin())) {
			
			return true;
		}
		
		return false;
	}
		
	// Test whether there are any players within a given (x, y) room
	public static boolean anyPlayersInRoom(int roomX, int roomY) {
		
		for(Player onePlayer: playerList) {
			
			if (onePlayer.getX() == roomX && onePlayer.getY() == roomY) {
				
				return true;
			}
		}
                
        return false;		
	}

	// Send a message to all players to a given (x, y) room	
	public static void messageTheRoom(int x, int y, String message) {
		
		for(Player onePlayer: playerList) {
			
			if (onePlayer.getX() == x && onePlayer.getY() == y) {
				
            	onePlayer.message(message);
                onePlayer.message(onePlayer.buildPrompt());
			}
		}        		
	}

	// Send a message to all players to a given (x, y) room except a dontMsg player
	public static void messageTheRoom(int x, int y, String message, Player dontMsg) {
		
		for(Player onePlayer: playerList) {
			
			if (onePlayer.getX() == x && onePlayer.getY() == y && 
				onePlayer != dontMsg) {
				
            	onePlayer.message(message);
                onePlayer.message(onePlayer.buildPrompt());
			}
		}        		
	}
	
	// A toString method
	public String toString() {
		
		return ("Player: " + playerName + "\nTitle: " + playerTitle +
				"\nRank: " + playerRank + "\nLevel: " + playerLevel +
				"\nShort Description: " + shortDescription +
				"\nLong Description: " + longDescription +
				"\nMax Health: " + maxHealth + "\nMax Mana: " + maxMana + "\n");
	}
	
	// An equals method
	public boolean equals(Player onePlayer) {
		
		if (onePlayer.toString().equals(toString())) {
			
			return true;
		}
		
		return false;
	}
	
} // End of class Player
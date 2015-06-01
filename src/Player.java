// Import necessary packages, classes, and interfaces
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

/**
 *<pre>
 *	Purpose
 *
 *		The Player class is a model which simulates a fictional person in JavaWorld. This
 *		Player-Character (PC) interacts with Items, Mob-AI, Weather patterns, and moves
 *		throughout the virtual world through cardinal directions of north, east, south,
 *		west, as well as simulating a third dimension of up and down. The PC has health,
 *		ability points (such as magic, mana, rage, ... as in other games), an inventory,
 *		physical characteristics, and several other fields.
 *
 *	Structure / Process
 *
 *		The Player is initialized from the accept() thread and becomes a part of the static
 *		Players ArrayList which is a collection of all active Players (and by extension, their
 *		sockets). Once the Player has been added to the Players ArrayList, the GameServer's
 *		gameLoop will then begin interpreting input from the client each iteration until the
 *		client sends the "quit" or "exit" command.
 *</pre>
 * @author Nicholas Warner
 * @version 5.1, May 2015
 * @see Inventory
 * @see Room
 * @see Item
 * @see Equipment
 */
public class Player {

	/** A constant int referencing the Player's (null) attribute. */
	public static final int PLAYER_NONE			= 0;
	/** A constant int referencing the Player's Health attribute. */
	public static final int PLAYER_HEALTH		= 1;
	/** A constant int referencing the Player's Mana attribute. */
	public static final int PLAYER_MANA 		= 2;
	/** A constant int referencing the Player's Strength attribute. */
	public static final int PLAYER_STRENGTH		= 3;
	/** A constant int referencing the Player's Dexterity attribute. */
	public static final int PLAYER_DEXTERITY	= 4;
	/** A constant int referencing the Player's Intelligence attribute. */
	public static final int PLAYER_INTELLIGENCE	= 5;
	/** A constant int referencing the Player's Wisdom attribute. */
	public static final int PLAYER_WISDOM		= 6;
	/** A constant int referencing the Player's Fortitude attribute. */
	public static final int PLAYER_FORTITUDE	= 7;

	/** The list of all active players in the game; Vector for synchronization. */
	private static Vector<Player> playerList = new Vector<Player>();

	/** The Player's Inventory of Items. */
	private Inventory playerInventory;
	
	/** The Player's Equipped Items. */
	private Equipment playerEquipment;

	/** A reference to the Player's current Room inside the given Zone. */
	private Room currentRoom;
	
	/** A reference to the PC's current World inside JavaWorld. */
	private World currentWorld;

	/** This player's name. */
	private String playerName;
	/** This player's tite. */
	private String playerTitle;
	/** This player's rank. */
	private String playerRank;
	/** This player's short description, used in regular player interactions. */
	private String shortDescription;
	/** This player's long description, used when looked at or in a room. */
	private String longDescription;
	/** This player's (temporary) password. */
	private String password;
	/** 
	 * A boolean indicating whether a player has quit or not. This is mostly
	 * used to indicate there is not (or is) an active Socket associated with
	 * this player.
	 */
	private boolean playerQuit;
	/** A boolean indicating whether a Player is or isn't active in JavaWorld. */
	private boolean frozen;
	
	/** An int indicating the level of this Player. */
	private int playerLevel;
	/** An int indicating the maximum health of this Player. */
	private int maxHealth;
	/** An int indicating the maximum mana of this Player. */
	private int maxMana;
	/** An int indicating the current health of this Player. */
	private int currentHealth;
	/** An int indicating the current mana of this Player. */
	private int currentMana;
	/**
	 * A two-dimensional boolean which represents the (x, y) coordinate grid
	 * of JavaWorld. When a player enters a given room (x, y), this sets the
	 * discoveredRooms[x][y] to true.
	 */
	private boolean[][] discoveredRooms;
	/** 
	 * This holds the unix timestamp when a Player may act again. If the
	 * current unix time is less than this timestamp, a Player's action
	 * is held in queue in their msgIn buffer.
	 */
	private long waiting;
	/**
	 * Not yet implemented. Same general concept as waiting.
	 *
	 * @see #waiting
	 */
	private long chatWaiting;

	/** A reference to the Player's Socket. */
	Socket playerConnection;
	/** A reference to the Player's output stream. */
	OutputStream playerOut;
	/** A reference to the Player's input stream. */
	InputStream playerIn;

	/** 
	 * A default constructor. All values are initialized to null, 0, empty String,
	 * or a default setting such as World.HOMELOCATION. Some are also initialized
	 * to a reasonable value, such as level being 1 to start, or starting health
	 * being 100.
	 *
	 * @see World#HOMELOCATION
	 * @see Room#getWorld()
	 * @see Inventory
	 * @see Equipment
	 */
    public Player() {
    	
    	playerInventory		= null;
    	currentRoom			= World.getRoom(World.HOMELOCATION, World.HOMELOCATION);
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

	/**
	 * A method to test whether a Player has an active Socket connection. This
	 * method needs to be reworked.
	 *
	 * @return Returns true if the Player's playerOut is null and false
	 *			otherwise.
	 */
	public boolean getConnected() {
		
		if (playerOut == null) {
			
			return true;
		}
		
		return false;
	}

	/**
	 * A method to get the name of a Player.
	 *
	 * @return Returns the name of this Player.
	 */
	public String getName() {
		
		return playerName;
	}

	/**
	 * A method to get the Player's current X coordinate.
	 *
	 * @return Returns the Player's current X coordinate.
	 */
	public int getX() {

		return this.currentRoom.getX();
	}

	/**
	 * A method to get the Player's current Y coordinate.
	 *
	 * @return Returns the Player's current y coordinate.
	 */
	public int getY() {
		
		return this.currentRoom.getY();
	}

	/**
	 * A method to get the Player's maximum health.
	 *
	 * @return Returns the Player's maximum health.
	 */
	public int getMaxHealth() {
		
		return maxHealth;
	}

	/**
	 * A method to get the Player's current health.
	 *
	 * @return Returns the Player's current health.
	 */
	public int getHealth() {
		
		return currentHealth;
	}

	/**
	 * A method to get the World this Player is a part of.
	 *
	 * @return Returns the World Object this Player is in.
	 */
	public World getWorld() {
		
		return currentWorld;
	}

	/**
	 * A method to get this Player's current level.
	 *
	 * @return Returns this Player's current level.
	 */
	public int getLevel() {
		
		return playerLevel;
	}

	/**
	 * A method to get the Room Object the Player is currently in.
	 *
	 * @return Returns the current Room Object that the Player is in.
	 */
    public Room getRoom() {
		
		return currentRoom;
	}

	/**
	 * A method which gets this Player's current waiting time.
	 *
	 * @return Returns the Player's unix timestamp which is compared against
	 *			the current unix time.
	 * @see #waiting
	 */
	public long getWaiting() {
		
		return waiting;
	}

	/**
	 * A method which gets this Player's current chat waiting time.
	 *
	 * @return Returns the Player's current chat waiting value.
	 * @see #chatWaiting
	 */
	public long getChatWaiting() {
		
		return chatWaiting;
	}

	/**
	 * A method which gets a String representing this Player's current
	 * equipment.
	 *
	 * @return Returns a String displaying this Player's equipment.
	 */
	public String getAllEquipment() {
		
		return playerEquipment.displayEquipment();
	}

	/**
	 * A method which gets the Player's title.
	 *
	 * @return Returns this Player's title.
	 */
	public String getTitle() {
		
		return playerTitle;
	}

	/**
	 * A method which gets the Player's rank.
	 *
	 * @return Returns this Player's rank.
	 */
	public String getRank() {
		
		return playerRank;
	}

	/**
	 * A method which gets the Player's short description.
	 *
	 * @return Returns the Player's short description.
	 * @see #shortDescription
	 */
	public String getShortDescription() {
		
		return shortDescription;
	}

	/**
	 * A method which gets the Player's long description.
	 *
	 * @return Returns the Player's long description.
	 */
	public String getLongDescription() {
		
		return longDescription;
	}

	/**
	 * A method which gets the Player's maximum mana.
	 *
	 * @return Returns the Player's maxmimum mana.
	 */
	public int getMaxMana() {
		
		return maxMana;
	}

	/**
	 * A method which gets the Player's current health.
	 *
	 * @return Returns the Player's current health.
	 */
	public int getCurrentHealth() {
		
		return currentHealth;
	}

	/**
	 * A method which gets the Player's current mana.
	 *
	 * @return Returns the Player's current mana.
	 */
	public int getCurrentMana() {
		
		return currentMana;
	}

	/**
	 * A method which returns true if a player is frozen and false if not.
	 *
	 * @return Returns true if this Player is frozen and false if not.
	 */
	public boolean getFrozen() {
		
		return frozen;
	}

	/**
	 * A method which returns the Player's who status.
	 *
	 * @return Returns a String stating the Player's level, name, and title. If
	 *			the player is an Administrator, then the level is replaced with
	 *			the String, "Admin".
	 */
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

	/**
	 * A method which gets a list of all Players within a given room.
	 *
	 * @param player The player who's in the Room to begin with.
	 * @return Returns a String with a description of every player within a Player's room.
	 */
	public static String getPlayersInRoom(Player player) {

		String output = "";
		
		for(Player onePlayer: playerList) {
			
			if (!onePlayer.equals(player) && player.getRoom().equals(onePlayer.getRoom())) {
				
				output += "#Y" + onePlayer.getName() + "#n is standing here, chilling.\n";
			}
		}
		
		return output;
	}

	/**
	 * A method which gets the number of Players currently online in JavaWorld.
	 *
	 * @return Returns an int representing the numbers of players online.
	 */
	public static int getNumberOnline() {
		
		return playerList.size();
	}
	
	/**
	 * A method which resets a Player's list of discovered rooms as shown on
	 * their map.
	 */
	public void clearDiscoveredRooms() {
		
		for(int i = 0; i < World.MAXROOMS; i++) {
			
			discoveredRooms[i][i] = false;
		}
	}

	/**
	 * A method which adds a given player into the main playerList, then announces
	 * the player's login to the rest of the players on JavaWorld.
	 *
	 * @param newPlayer The given Player to be added to the playerList.
	 * @see #playerList
	 */
	public static void addPlayer(Player newPlayer) {
		
		playerList.add(newPlayer);
		infoAll(newPlayer.getName() + " has logged into Java World.");
	}

	/**
	 * A method which sets or changes the title of a given Player.
	 *
	 * @param title The new title of this Player.
	 */
	public void setTitle(String title) {
		
		playerTitle = title;
	}

	/**
	 * A method which sets the rank this Player.
	 *
	 * @param rank The new rank for this Player.
	 */
	public void setRank(String rank) {
		
		playerRank = rank;
	}

	/**
	 * A method to set the short description of this Player.
	 *
	 * @param shortDescription The new Short Description of this Player.
	 */
	public void setShortDescription(String shortDescription) {
		
		this.shortDescription = shortDescription;
	}

	/**
	 * A method to set the Long Description of this Player.
	 *
	 * @param longDescription The new Long Description of this Player.
	 */
	public void setLongDescription(String longDescription) {
		
		this.longDescription = longDescription;
	}

	/**
	 * A method to set the maximum health of this Player.
	 *
	 * @param maxHealth The new maximum health of this Player.
	 */
	public void setMaxHealth(int maxHealth) {
		
		maxHealth = maxHealth;
	}

	/**
	 * A method to set the maximum mana of this Player.
	 *
	 * @param maxMana The new maximum mana of this Player.
	 */
	public void setMaxMana(int maxMana) {
		
		this.maxMana = maxMana;
	}

	/**
	 * A method to set the current health of this Player.
	 *
	 * @param currentHealth The new current health of this Player.
	 */
	public void setCurrentHealth(int currentHealth) {
		
		this.currentHealth = currentHealth;
	}

	/**
	 * A method to set the current mana of this Player.
	 *
	 * @param currentMana The new current mana of this Player.
	 */
	public void setCurrentMana(int currentMana) {
		
		this.currentMana = currentMana;
	}

	/**
	 * A method to set the level of this Player.
	 *
	 * @param level The new level of this Player.
	 */	
	public void setLevel(int level) {
		
		playerLevel = level;
	}

	/**
	 * A method to set the current Room of this Player.
	 *
	 * @param room The new Room this Player occupies.
	 */
	public void setCurrentRoom(Room room) {
		
		currentRoom = room;
	}

	/**
	 * A method to set the current World of this Player.
	 *
	 * @param world The new World this Player is a part of.
	 */
	public void setCurrentWorld(World world) {
		
		currentWorld = world;
	}

	/**
	 * A method to set the quit boolean of this Player.
	 *
	 * @param quit A boolean value which represents whether the player has
	 *				quit(true) or not quit(false).
	 */
	public void setQuit(boolean quit) {
		
		playerQuit = quit;
	}
		
	/**
	 * A method which checks if a given Player exists and if their password is 
	 * correct.
	 *
	 * @param onePlayer The given Player to check
	 * @return If the player exists AND the password is correct, the method
	 *			returns true.
	 */
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
	
	/**
	 * A method to test whether the given player exists.
	 *
	 * @param playerName The Player's name to see if they exist.
	 * @return Returns true if the player exists, false if the Player doesn't.
	 */
	public static boolean checkPlayerExists(String playerName) {

		// Ensure we're comparing against lowercase
		if ((new File("../player/" + playerName.toLowerCase())).exists()) {
			
			return true;
		}
		
		return false;
	}

	/**
	 * A method to save a single player file and its accompanying discoveredRooms.
	 *
	 * @param onePlayer The player meant to be saved.
	 */
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

	/**
	 * A method to load a player given the player's name.
	 *
	 * @param playerName The name of the player to load.
	 * @return Returns the loaded Player Object.
	 */
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
	
				playerName = playerName.toLowerCase();

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
	
	/** A method to save this player's inventory. */
	public void saveInventory() {
		
		playerInventory.saveInventory(playerName);
	}

	/** 
	 * A method to message all currently connected Players.
	 * 
	 * @param msg The message to send to all the Players.
	 */
	public static void messageAll(String msg) {
		
		for(Player onePlayer: playerList) {
			
			onePlayer.message(msg);
			onePlayer.message(onePlayer.buildPrompt());
		}
	}
	
	/**
	 * Messages all currently connected Players except given Player.
	 *
	 * @param msg The message to send to all the Players.
	 * @param dontMsg The Player who should not receive the message.
	 */
	public static void messageAll(String msg, Player dontMsg) {
		
		for(Player onePlayer: playerList) {
			
			if (!onePlayer.equals(dontMsg)) {
				
				onePlayer.message(msg);
				onePlayer.message(onePlayer.buildPrompt());
			}
		}
	}

	/**
	 * A method to message a Player by name if we don't have a reference to the
	 * Player Object.
	 *
	 * @param playerName The name of the Player meant to be messaged
	 * @param msg The message to send to the given Player.
	 */
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

	/**
	 * A method to freeze a player's input (player can no longer act in game).
	 *
	 * @param playerName The name of the Player to be frozen.
	 * @return Returns true if the the Player was frozen successfully and false
	 *			if the Player was not frozen.
	 */
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
	
	/**
	 * A method to unfreeze a given Player.
	 *
	 * @param playerName The name of the Player to unfreeze.
	 * @return Returns true if the Player was successfully unfrozen, or false
	 *			if the Player was not.
	 */
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
	
	/**
	 * A method to create the player's password file (Serialized).
	 *
	 * @param newPlayer The given Player to create a Password File for.
	 */
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
	
	/** 
	 * A method to load the given password file from the player's name.
	 *
	 * @param charName The name of the Player meant to be loaded
	 * @return Returns a Password Object of the given Player.
	 */
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

	/** A method meant to rebuild the Prompts of all Players online in JavaWorld. */
    public static void rebuildAllPrompts() {
		
		for(Player onePlayer: playerList) {

			onePlayer.message(onePlayer.buildPrompt());
		}
	}

	/**
	 * A method to set the Player's Socket connection.
	 *
	 * @param playerConnection The Socket connection to be set on the Player Object.
	 */
	public void setPlayerConnection(Socket playerConnection) {
		
		this.playerConnection = playerConnection;
	}

	/**
	 * A method to set the Player's output stream.
	 *
	 * @param playerOut The new output stream for the Player.
	 */
	public void setOutput(OutputStream playerOut) {
		
		this.playerOut = playerOut;
	}

	/**
	 * A method to set the Player's input stream.
	 *
	 * @param playerIn The new input stream for the Player.
	 */
	public void setInput(InputStream playerIn) {
		
		this.playerIn = playerIn;
	}

	/**
	 * A method to set the Player's name.
	 *
	 * @param playerName The new name of the Player.
	 */
	public void setName(String playerName) {
		
		this.playerName = playerName;
	}

	/**
	 * A method to set the Password of the Player.
	 *
	 * @param password The new password for the Player.
	 */
	public void setPassword(String password) {
		
		this.password = password;
	}

	/**
	 * A method to set the current health of the Player.
	 *
	 * @param currentHealth The new currentHealth of the Player.
	 */	
	public void setHealth(int currentHealth) {
		
		this.currentHealth = currentHealth;
	}
	
	/*********************************************
	 *
	 *		Player Class Methods
	 *
	 *********************************************/

	/**
	 * A method to read input from this Player.
	 *
	 * @return Returns a String with the Player's input up to the next newline.
	 */
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
	
	/**
	 * A method to message one Player connected to JavaWorld.
	 *
	 * @param msg The message to send to this Player.
	 */
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

	/** A method to ensure the password field is empty. */
	public void clearPassword() {
		
		this.password = "";
	}
	
	/**
	 * A method to test whether two passwords are equivalent.
	 *
	 * @param password The password to be tested.
	 * @return Returns true if the passwords are equal and false if the
	 *			passwords are not equal.
	 */
	public boolean comparePassword(String password) {
		
		if (this.password.equals(password)) {
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * A method to damage all players within a given room.
	 *
	 * @param x The X coordinate of the Room.
	 * @param y The Y coordinate of the Room.
	 * @param shortDescription The short description of Player damaging the room.
	 */
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
	
	/**
	 * A method to damage an individual player a given amount.
	 *
	 * @param damageAmount The amount of damage done to this Player.
	 */
	public void damagePlayer(int damageAmount) {
		
		currentHealth -= damageAmount;
	}

	/**
	 * A method to build an individual player's prompt.	
	 *
	 * @return Returns the constructed Prompt as a String.
	 */
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
	
	/**
	 * A method to remove the discovery of a given (x, y).
	 *
	 * @param x The X coordinate of the Room
	 * @param y The Y coordinate of the Room
	 */
	public void undiscoverRoom(int x, int y) {
		
        if (x >= 0 && y >= 0 && x < World.MAXROOMS && y < World.MAXROOMS) {

            discoveredRooms[x][y] = false;
        }
	}
	
	/**
	 * A method to set the location of a Player to (x, y).
	 *
	 * @param x The X coordinate of the Room.
	 * @param y The Y coordinate of the Room.
	 */
	public void setLocation(int x, int y) {
	
		if (x >= 0 && x < World.MAXROOMS && y >= 0 && y < World.MAXROOMS) {
		
			this.currentRoom = World.getRoom(x, y);
		}
	}
    
    /**
	 * A method to move a player East.
	 *
	 * @return Returns true if the player moved and false if not.
	 */
    public boolean moveEast() {
		
		int x = this.currentRoom.getX();
		int y = this.currentRoom.getY();
		
		if (World.checkRoomExists(x + 1, y)) {
			
			this.currentRoom = World.getRoom(x + 1, y);
			return true;
		}
		
		return false;
	}

	/**
	 * A method to move a player West.	
	 *
	 * @return Returns true if the player moved and false if not.
	 */
    public boolean moveWest() {
		
		int x = this.currentRoom.getX();
		int y = this.currentRoom.getY();
		
		if (World.checkRoomExists(x - 1, y)) {
			
			this.currentRoom = World.getRoom(x - 1, y);
			return true;
		}
		
		return false;
	}

	/**
	 * A method to move a player north.
	 *
	 * @return Returns true if the player moved and false if not.
	 */
	public boolean moveNorth() {
		
		int x = this.currentRoom.getX();
		int y = this.currentRoom.getY();
		
		if (World.checkRoomExists(x, y + 1)) {
			
			this.currentRoom = World.getRoom(x, y + 1);
			return true;
		}
		
		return false;
	}

	/**
	 * A method to move a player south.
	 *
	 * @return Returns true if the Player moves and false if not.
	 */
    public boolean moveSouth() {
		
		int x = this.currentRoom.getX();
		int y = this.currentRoom.getY();
		
		if (World.checkRoomExists(x, y - 1)) {
			
			this.currentRoom = World.getRoom(x, y - 1);
			return true;
		}
		
		return false;
	}
	
	/** A method to discover a given (x, y) room for a Player. */
    public void discoverRoom() {

        int x = getX();
        int y = getY();

        if (x >= 0 && x < World.MAXROOMS && y >= 0 && y < World.MAXROOMS) {

            discoveredRooms[x][y] = true;
        }
	}

	/**
	 * A method to test whether a room has been discovered.
	 *
	 * @param x The X coordinate of the Room to test.
	 * @param y The Y coordinate of the Room to test.
	 * @return Returns true if the room is now discovered, false if there
	 *			is an issue with discovering the Room.
	 */
    public boolean discoveredRoom(int x, int y) {
	
        if (x >= 0 && y >= 0 && x < World.MAXROOMS && y < World.MAXROOMS) {

            return discoveredRooms[x][y];
        }
        
        return false;
	}
	
	/** A method to update the state of the Player. */
	public static void updateState() {
		
		// This will relate to future updates, specifically objectives
	}
	
	/** 
	 * A method to read input from the player's socket and Interpret it via 
	 * the Interpreter class.
	 */
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

	/**
	 * A method to test whether the given player is an admin or not.
	 *
	 * @param onePlayer The Player to test.
	 * @return Returns true if the Player is an Admin and false if not.
	 */
	public static boolean isAdmin(Player onePlayer) {
		
		if (onePlayer.isAdmin()) {
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * A method to send an information message to everyone in the game.
	 *
	 * @param msg The message to send to all the Players in the game.
	 */
	public static void infoAll(String msg) {
		
		msg = "#YInfo -> #n" + msg;
		messageAll(msg);		
	}
	
	/**
	 * A method to pick up a give item.
	 *
	 * @param oneItem The given Item to be picked up.
	 */
	public void pickupItem(Item oneItem) {
		
		playerInventory.addToInventory(oneItem);
		currentRoom.removeItem(oneItem);
	}

	/**
	 * A method to drop a given item.
	 *
	 * @param oneItem The Item meant to be dropped.
	 */
	public void dropItem(Item oneItem) {
		
		if (!playerEquipment.isEquipped(oneItem)) {

			message(playerInventory.removeFromInventory(oneItem));
			currentRoom.addItem(oneItem);
		}
		
		else {
			
			message("You cannot drop an equipped item.");
		}
	}
	
	/**
	 * A method to display the player's inventory.
	 *
	 * @return Returns a String listing the Player's Inventory.
	 */
	public String displayInventory() {
		
		return playerInventory.displayInventory(false);
	}
	
	/**
	 * A method to check whether the player has a given Item.
	 *
	 * @param oneItem The name of the Item to be checked for.
	 * @return Returns true if the item is within the Player's Inventory
	 *			and false if the item isn't.
	 */
	public Item hasItem(String oneItem) {
		
		return playerInventory.contains(oneItem);
	}

	/**
	 * A method to check whether the given Item is equipped on the player.
	 *
	 * @param oneItem The name of the Item to be checked for.
	 * @return Returns the requested Item if available, null if not.
	 */
	public Item hasItemInEquipment(String oneItem) {
		
		return playerInventory.containsInEquipment(oneItem);
	}

	/**
	 * A method to check whether the Item is in the Player's inventory.
	 *
	 * @param oneItem The name of the Item to be checked for.
	 * @return Returns a true if the Item is in Inventory and false if the
	 *			Item is not in Inventory.	
	 */
	public Item hasItemInInventory(String oneItem) {
		
		return playerInventory.containsInInventory(oneItem);
	}
	
	/**
	 * A method to equip a given item on a player.
	 *
	 * @param oneItem The name of the Item to be equipped.
	 * @return Returns a String with the text of the Item being equipped.
	 */
	public String equipItem(Item oneItem) {
		
		return playerEquipment.equipItem(oneItem);
	}
	
	/** 
	 * A method to unequip a given item.
	 *
	 * @param oneItem The name of the Item to be unequipped.
	 * @return Returns a String with the text of the Item being removed.
	 */
	public String unequipItem(Item oneItem) {
		
		return playerEquipment.unequipItem(oneItem);
	}
	
	/** A method to save every player, then close their connection. */
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

	/**
	 * A method to test whether the player is an administrator or not.
	 *
	 * @return A true value if the Player is an Administrator and a false value
	 *			if the Player is not an Administrator.
	 */
	public boolean isAdmin() {
		
		if (playerName.equalsIgnoreCase(Config.getAdmin())) {
			
			return true;
		}
		
		return false;
	}
		
	/**
	 * A method to test whether there are any players within a given (x, y) room.
	 *
	 * @param roomX The X coordinate for the Room.
	 * @param roomY The Y coordinate for the Room.
	 * @return Returns a true value if the Player is in the given (x, y) room and
	 *			a false value if the Player is not.
	 */
	public static boolean anyPlayersInRoom(int roomX, int roomY) {
		
		for(Player onePlayer: playerList) {
			
			if (onePlayer.getX() == roomX && onePlayer.getY() == roomY) {
				
				return true;
			}
		}
                
        return false;		
	}

	/**
	 * A method to send a message to all players to a given (x, y) room.
	 *
	 * @param x The X coordinate for the Room.
	 * @param y The Y coordinate for the Room.
	 * @param message The message to be sent to the Players in the room.
	 */
	public static void messageTheRoom(int x, int y, String message) {
		
		for(Player onePlayer: playerList) {
			
			if (onePlayer.getX() == x && onePlayer.getY() == y) {
				
            	onePlayer.message(message);
                onePlayer.message(onePlayer.buildPrompt());
			}
		}        		
	}

	/**
	 * A method to send a message to all players to a given (x, y) room 
	 * except a dontMsg player.
	 *
	 * @param x The Room's x coordinate.
	 * @param y The Room's y coordinate.
	 * @param message The message to be sent to the Players in the Room
	 * @param dontMsg The Player who does not receive the message.
	 */
	public static void messageTheRoom(int x, int y, String message, Player dontMsg) {
		
		for(Player onePlayer: playerList) {
			
			if (onePlayer.getX() == x && onePlayer.getY() == y && 
				onePlayer != dontMsg) {
				
            	onePlayer.message(message);
                onePlayer.message(onePlayer.buildPrompt());
			}
		}        		
	}
	
	/**
	 * A method to describe the Player and return the value as a String.
	 *
	 * @return Returns a unique String indicating information about the Player.
	 */
	public String toString() {
		
		return ("Player: " + playerName + "\nTitle: " + playerTitle +
				"\nRank: " + playerRank + "\nLevel: " + playerLevel +
				"\nShort Description: " + shortDescription +
				"\nLong Description: " + longDescription +
				"\nMax Health: " + maxHealth + "\nMax Mana: " + maxMana + "\n");
	}
	
	/**
	 * A method to test whether two Players are equal, or two Player Objects
	 * are equal.
	 *
	 * @param onePlayer The Player to be compared to this Player.
	 * @return Returns true if the Players are the same, or false if not.
	 */
	public boolean equals(Player onePlayer) {
		
		if (onePlayer.toString().equals(toString())) {
			
			return true;
		}
		
		return false;
	}
	
} // End of class Player

/*	Purpose
 *
 *		This class inteprets a player's command input and assigns that input to a given method. All
 *		of the commands which a player can use are listed in this class and some may be called
 *		externally (such as saving) but most are private.
 *
 *	Algorithm
 *
 *		1. Declare the Interpreter class
 *		2. Declare a String array to hold all of the commands that are available
 *		3. Declare a second String array to hold Administrative commands
 *		4. Declare a primary method which inteprets player input and assigns it a method
 *		5. Declare the following command methods:
 *			- commandNorth
 *			- commandEast
 *			- commandSouth
 *			- commandWest
 *			- commandSavePlayer
 *			- commandChat
 *			- commandName
 *			- commandLook
 *			- commandRecall
 *			- commandGet
 *			- commandDrop
 *			- commandInventory
 *			- commandRemove
 *			- commandShowEquipped
 *			- commandEquip
 *			- commandCommands
 *			- commandScore
 *			- commandWho
 *			- commandMap
 *			- commandCredits
 *			- commandTitle
 *			- commandLaugh
 *			- commandDance
 *			- commandQuit
 *			- commandItemsList
 *		6. Create a minimap Method to build a minimap of surrounding rooms
 *		7. Create a toString method and an equals method
 *
 *	Structure / Process
 *
 *		In the main game loop, after any updates, the game polls through every player's socket and
 *		looks for new input. If any new input is found, one command, described as a String ending in
 *		a newline, is pulled. The player may have more information buffered in their socket, but
 *		they will need to wait for the next loop before it will be executed.
 *
 *		This information is then tested in the checkCommand method which is what actually pairs
 *		the input String to a command (or notifies the user they entered gibberish). Then the
 *		method is executed upon the Player and any additional arguments they may have sent.
 *
 *	Author			- Nicholas Warner
 *	Created 		- 4/24/2015
 *	Last Updated	- 5/17/2015
 */

// Import the necessary classes, packages, interfaces, ...
import java.io.IOException;

public class Interpreter {

	// A list of all available commands
	public static final String[] commands = {
							"chat",
							"laugh",
							"dance",
							"name",
							"look",
							"get",
							"inventory",
							"commands",
							"east",
							"west",
							"north",
							"south",
							"score",
							"quit",
							"who",
							"map",
							"remove",
							"equipment",
							"save",
							"wear",
							"credits",
							"title",
							"hangman"
							};

	// A list of all available admin commands
	public static final String[] adminCommands = {
		
							"shutdown",
							"ilist",
							"freeze",
							"unfreeze",
							"info"
                            };

	// Run the command and it's associated method
	public static boolean checkCommand(Player player, String playerInput) {
		
		// To hold the name of the command given
		String commandName = "";
		
		// Assume that we've found a command
        boolean foundCommand = true;
        
		// In case they want to quit
		boolean quit = false;
		
		// Prepare our Input
		playerInput = playerInput.trim();
		
		// Verify we have something
		if (playerInput != null && playerInput != "") {
			
			commandName = TextManipulator.oneArgument(playerInput).toLowerCase();
		}
		
		// If there's also an argument, separate it
		if (playerInput.contains(" ")) {
			
			playerInput = playerInput.substring(playerInput.indexOf(" ") + 1);
		}
		
		// If no argument, let it be
		else {
			
			playerInput = "";
		}
		
		if ((player.getHealth() > 0 || "quit".startsWith(commandName) || 
			 "recall".startsWith(commandName)) && !commandName.equals("") &&
			 !player.getFrozen()) {

			// Movement	
			if		("east".startsWith(commandName))	{	commandEast(player);	}
			else if ("north".startsWith(commandName))	{	commandNorth(player);	}
			else if ("south".startsWith(commandName))	{	commandSouth(player);	}
			else if ("west".startsWith(commandName))	{	commandWest(player);	}		
	
			// Important / Interactional Commands
			else if ("save".startsWith(commandName))	{	commandSavePlayer(player);			}
			else if ("chat".startsWith(commandName))	{	commandChat(player, playerInput);	}
			else if ("name".startsWith(commandName))	{	commandName(player, playerInput);	}
			else if ("look".startsWith(commandName))	{	commandLook(player, playerInput, false);	}
			else if ("recall".startsWith(commandName))	{	commandRecall(player);				}
			else if ("get".startsWith(commandName))		{	commandGet(player, playerInput);	}
			else if ("grab".startsWith(commandName))	{	commandGet(player, playerInput);	}
			else if ("drop".startsWith(commandName))	{	commandDrop(player, playerInput);	}
			else if ("inventory".startsWith(commandName)) 
					/* inventory */						{	commandInventory(player);			}
			else if ("remove".startsWith(commandName))	{	commandRemove(player, playerInput);	}
			else if ("equipment".startsWith(commandName))	
					/* equipment */						{	commandShowEquipped(player);		}
			else if ("wear".startsWith(commandName))	{	commandEquip(player, playerInput);	}

			// Informational Commands
			else if ("commands".startsWith(commandName)){	commandCommands(player);	}
			else if ("score".startsWith(commandName))	{	commandScore(player);		}
			else if ("who".startsWith(commandName))		{	commandWho(player);			}
			else if ("map".startsWith(commandName))		{	commandMap(player);			}
			else if ("credits".startsWith(commandName))	{	commandCredits(player);		}
			else if ("title".startsWith(commandName))	{	commandTitle(player, playerInput);	}

			// MiniGames
			else if ("hangman".startsWith(commandName))	{	commandHangman(player, playerInput);	}

			// Social Commands	
			else if ("laugh".startsWith(commandName))	{	commandLaugh(player, playerInput);	}		
			else if ("dance".startsWith(commandName))	{	commandDance(player, playerInput);	}
			
			// Quit
			else if ("quit".startsWith(commandName))	{	commandQuit(player); quit = true;	}

			/*	---------------------------=Admin / Broken Commands=---------------------------	*/
			else if (player.getName().equalsIgnoreCase(Config.getAdmin())) {

				// Item Commands
				if		("ilist".startsWith(commandName))		{	commandItemsList(player);		}
				else if ("itemlist".startsWith(commandName))	{	commandItemsList(player);		}

				// Superduper Admin Commands	
				else if ("shutdown".startsWith(commandName))	{	commandShutdown(player);		}
				else if ("freeze".startsWith(commandName))		{	commandFreeze(player, playerInput); }
				else if ("unfreeze".startsWith(commandName))	{	commandUnFreeze(player, playerInput); }
				else if ("info".startsWith(commandName))		{	Player.infoAll(playerInput);	}

				else {
					
					player.message("Huh? Type \"commands\" for a list of commands.\n\r");
					foundCommand = false;				
				}
			}
			/* --------------------------=End Admin / Broken Commands=-------------------------	*/

			else {
				
				player.message("Huh? Type \"commands\" for a list of commands.\n\r");
				foundCommand = false;				
			}

			if (!quit) {
				
				player.message(player.buildPrompt());
			}			
		}

		else if (player.getHealth() <= 0) {
			
			player.message("You are dead. :( You can can recall (try again) or quit.\n\r");
			player.message(player.buildPrompt());
			foundCommand = false;
		}
		
		else if (player.getFrozen()) {
			
			player.message("Your input is frozen!");
			foundCommand = false;
		}
		
		else {
			
			player.message("Huh? Type \"commands\" for a list of commands.");
			foundCommand = false;
		}

		return foundCommand;
	}

	// List all Items in the game
    private static void commandItemsList(Player player) {
	
		String output = Item.getItemsList();
		
		player.message("(#yLocation#n)\t[#yItemID#n: #yItemName#n] #yItemDescription#n\n\r" + 
					   output);
	}

	// Send the player back to the HOMELOCATION
    private static void commandRecall(Player player) {
		
		player.setLocation(World.HOMELOCATION, World.HOMELOCATION);
		player.setHealth(player.getMaxHealth());
		player.message("Your eyes close, your body flickers with fluidic energy, and you " +
					   "materialize in your home location.\n\r");
		commandLook(player, "", false);
	}

	// Freeze a single player so they may no longer act
	private static void commandFreeze(Player player, String playerInput) {
		
		String pName = TextManipulator.oneArgument(playerInput).toLowerCase();
		pName = pName.toUpperCase().charAt(0) + pName.substring(1);
			
		if (Player.freezePlayer(playerInput)) {
			
			player.message(pName + " was frozen successfully.\n\r");
			Player.sendMessageToPlayerByName(pName, "Your input has been frozen. You are " +
													"unable to play JavaWorld.");
		}

		else {
			
			player.message("Could not find " + pName + ". Please try again.\n\r");
		}
	}

	// Unfreeze a single payer so that may act again
	private static void commandUnFreeze(Player player, String playerInput) {
		
		String pName = TextManipulator.oneArgument(playerInput).toLowerCase();
		pName = pName.toUpperCase().charAt(0) + pName.substring(1);
			
		if (Player.unFreezePlayer(playerInput)) {
			
			player.message(pName + " was unfrozen successfully.\n\r");
			Player.sendMessageToPlayerByName(pName, "\n\rYour input has been unfrozen. You are " +
													"now able to play JavaWorld again.");
		}

		else {
			
			player.message("Could not find " + pName + ". Please try again.\n\r");
		}

	}

	// A method to shutdown the entire game; save everyone first
    private static void commandShutdown(Player player) {
		
		System.out.println("Cleaning up. Game is exiting.");

		Player.saveAndCloseConnections();

		System.exit(0);
	}

	// A method to quit from the game for a player (save them, then extract them)
    private static void commandQuit(Player player) {
		
		commandSavePlayer(player);
		
		try {

			player.playerConnection.close();
		}
		
		catch (IOException e) {
			
			System.out.println("IOException with comm_quit: " + e.getMessage());
		}

		player.setQuit(true);
	}

	// A method to see who is online
    private static void commandWho(Player player) {

		String status;
		int pCount = 0;
		
		String output = "#r" + Player.getNumberOnline() + " Players #yonline in " +
						"JavaWars:#n\n\n\r";

		output += Player.getWhoStatus();
				
		player.message(output);
	}

	// A method to see vital statistics about a player
    private static void commandScore(Player player) {
		
		String output;

		output = "#yName:#n " + player.getName() + "\n\r";
		output += "#rHealth:#n #C" + player.getHealth() + "#n/#C" + player.getMaxHealth() + 
				  "#n\n\r";
		output += "#gLocation:#n (" + player.getRoom().getX() + ", " + 
				  player.getRoom().getY() + ")\n\r";
		output += "#bIn Room:#n " + player.getRoom().getRoomName() + "\n\r";
			
		player.message(output);
	}

	// A method to move east
    private static void commandEast(Player player) {
		
		if (player.moveEast()) {
			
			player.discoverRoom();
			commandLook(player, "", false);
		}
		
		else {
			
			player.message("*bump* You can't go East!\n\r");
		}
	}

	// Moves the player West
    private static void commandWest(Player player) {
		
		if (player.moveWest()) {
			
			player.discoverRoom();
			commandLook(player, "", false);
		}

		else {
			
			player.message("*bump* You can't go West!\n\r");
		}
	}

	// Move the player North
    private static void commandNorth(Player player) {
		
		if (player.moveNorth()) {
			
			player.discoverRoom();
			commandLook(player, "", false);
		}
		
		else {
			
			player.message("*bump* You can't go North!\n\r");
		}
	}

	// Move the player south
    private static void commandSouth(Player player) {
		
		if (player.moveSouth()) {
			
			player.discoverRoom();
			commandLook(player, "", false);
		}

		else {
			
			player.message("*bump* You can't go South!\n\r");
		}
	}

	// Build a minimap for the given player
    private static String minimap(Player player) {
		
		String map = "";
		int mapSize = 1;
		String oneCell;
		String lConnect;
		String rConnect;
		String inRoom;
		
		for(int i = player.getY() + mapSize; i >= player.getY() - mapSize; i--) {
			
			for(int j = player.getX() - mapSize; j <= player.getX() + mapSize; j++) {
				
				// if they discovered the room OR the room exists
				// color the room information differently IF it exists
				if (player.discoveredRoom(j, i) || World.checkRoomExists(j, i)) {
					
					if (player.discoveredRoom(j - 1, i) && player.discoveredRoom(j, i)) {
						
						lConnect = "-";
					}
					
					else if (World.checkRoomExists(j - 1, i)) {
						
						lConnect = "#r-#n";
					}
					
					else {
						
						lConnect = " ";
					}
					
					if (player.discoveredRoom(j + 1, i) && player.discoveredRoom(j, i)) {
						
						rConnect = "-";
					}
					
					else if (World.checkRoomExists(j + 1, i)) {
						
						rConnect = "#r-#n";
					}
					
					else {
						
						rConnect = " ";
					}
					
					if (player.getX() == j && player.getY() == i) {
						
						inRoom = "#CP#n";
					}
					
					else if (!player.discoveredRoom(j, i)) {

						inRoom = "#rO#n";
					}
						
					else {
							
						inRoom = "#gO#n";
					}
					
					oneCell = lConnect + inRoom + rConnect;
				}
				
				else {
					
					oneCell = "   ";
				}
				
				map += oneCell;
			}
			
			map += "\n\r";
			
			for(int j = player.getX() - mapSize; j <= player.getX() + mapSize; j++) {
				
				if (player.discoveredRoom(j, i) && player.discoveredRoom(j, i - 1)) {
					
					map += " | ";
				}
				
				else if (World.checkRoomExists(j, i) && World.checkRoomExists(j, i - 1)) {
					
					map += " #r|#n ";
				}
				
				else {
					
					map += "   ";
				}
			}
			
			map += "\n\r";
		}
				
		return map;
	}

	// Begin a game of hangman
	private static void commandHangman(Player player, String argument) {
		
		String commandType = TextManipulator.oneArgument(argument).toLowerCase();
		
		if (commandType.startsWith("start")) {
			
			Hangman.newGame(TextManipulator.getArgumentN(argument, 2, false));
		}
		
		else if (commandType.startsWith("status")) {
			
			player.message(Hangman.getStatus());
		}
		
		else if (commandType.startsWith("play")) {
			
			String oneArg = TextManipulator.getArgumentN(argument, 2, false);
			char letter = oneArg.charAt(0);
			
			Hangman.tryLetter(letter);
		}
	}

	// Build an overhead map
    private static void commandMap(Player player) {
		
		String map = "";
		String oneLine;
		int mapSize = 6;
		String oneCell;
		String lConnect;
		String rConnect;
		String inRoom;
		
		for(int i = player.getY() + mapSize; i > player.getY() - mapSize; i--) {
			
			oneLine = "";
			
			for(int j = player.getX() - mapSize; j < player.getX() + mapSize; j++) {
				
				if (player.discoveredRoom(j, i) || (player.isAdmin() &&
					World.checkRoomExists(j, i))) {
					
					if (player.discoveredRoom(j - 1, i) && player.discoveredRoom(j, i)) {
						
						lConnect = "-";
					}
					
					else {
						
						lConnect = "#y*#n";
					}
					
					if (player.discoveredRoom(j + 1, i) && player.discoveredRoom(j, i)) {
						
						rConnect = "-";
					}
					
					else {
						
						rConnect = "#y*#n";
					}
					
					if (player.getX() == j && player.getY() == i) {
						
						inRoom = "#CP#n";
					}
					
					else {
						
						inRoom = " ";
					}
					
					if (World.checkRoomExists(j, i)) {

						oneCell = lConnect + "#r[" + inRoom + "#r]#n" + rConnect;
					}
					
					else {
						
						oneCell = "#y*****#n";
					}
				}
				
				else {
					
					oneCell = "#y*****#n";
				}
				
				oneLine += oneCell;
			}
			
			if (oneLine.contains("[")) {

				map += oneLine + "\n\r";			
			}
			
			oneLine = "";
			
			for(int j = player.getX() - mapSize; j < player.getX() + mapSize; j++) {
				
				if (player.discoveredRoom(j, i) && player.discoveredRoom(j, i - 1)) {
					
					oneLine += "#y* #n| #y*#n";
				}
				
				else if (!player.discoveredRoom(j, i) || !player.discoveredRoom(j, i - 1)) {
					
					oneLine += "#y*****#n";
				}
			}

			map += oneLine + "\n\r";
		}
		
		map = "#y************************************************************#n\n\r" + map;
		map += "#y************************************************************#n\n\r";
		
		player.message(map);
	}

	// List all of the available commands from the command list
    private static void commandCommands(Player player) {
		
		int lineLength = 0;
		String output = "Here is a command list:\n\r";
		
		for(int i = 0; i < commands.length; i++) {

			output += String.format("%-12s", commands[i]);
			
			lineLength += 12;
			
			if (lineLength > 80) {
				
				lineLength = 0;
				output += "\n\r";
			}
		}
		
		if (player.isAdmin()) {
			
			lineLength = 0;
			
			output += "\n\r--------------------------- #g== | #yAdmin Commands#n #g| " +
					  "==#n ---------------------------\n\r";
			
			for(int i = 0; i < adminCommands.length; i++) {

				output += String.format("%12s", adminCommands[i]);
				
				lineLength += 12;

				if (lineLength > 80) {
					
					lineLength = 0;
					output += "\n\r";
				}
			}
		}
		
		player.message(output + "\n\r");
	}

	// Display the player's inventory
	private static void commandInventory(Player player) {
		
		player.message(player.displayInventory());
	}
	
	// Save the player
	public static void commandSavePlayer(Player player) {
		
		player.message("Saving player file.\n\r");
		Player.savePlayer(player);
	}
	
	// Equip a given item
	private static void commandEquip(Player player, String oneItem) {
		
		if (oneItem.length() > 0) {
		
			Item tempItem = player.hasItemInInventory(oneItem);
			
			if (tempItem != null && !tempItem.getEquipped()) {
				
				player.message(player.equipItem(tempItem));
			}
			
			else {
				
				player.message("You don't seem to have " + oneItem + " to equip.\n\r");
			}
		}				
	}

	// Remove a given item	
	private static void commandRemove(Player player, String oneItem) {
		
		if (oneItem.length() > 0) {
			
			Item tempItem = player.hasItemInEquipment(oneItem);
			
			if (tempItem != null && tempItem.getEquipped()) {
				
				player.message(player.unequipItem(tempItem));
			}
			
			else {
				
				player.message("You don't seem to have " + oneItem + " to unequip.\n\r");
			}
		}
	}

	// Get a given item from the room's floor
	private static void commandGet(Player player, String oneItem) {
		
		if (oneItem.length() > 0) {
		
			Item tempItem = player.getRoom().containsItem(oneItem);
			
			if (tempItem != null) {
				
				player.pickupItem(tempItem);
				
				player.message("You picked up " + tempItem.getName() + ".\n\r");
			}
			
			else {
				
				player.message("The room doesn't contain " + oneItem + ".\n\r");
			}
		}
		
		else {
			
			player.message("Get what?\n\r");
		}
	}

	// A method to drop a given item	
	public static void commandDrop(Player player, String oneItem) {
		
		if (oneItem.length() > 0) {
		
			Item tempItem = player.hasItemInInventory(oneItem);
			
			if (tempItem != null && !tempItem.getEquipped()) {
				
				player.dropItem(tempItem);
			}
			
			else {
				
				player.message("You are not carrying " + oneItem + ".\n\r");
			}
		}
		
		else {
			
			player.message("Drop what?\n\r");
		}
	}

	// A method to look at the given room
	public static void commandLook(Player player, String action, boolean startNewLine) {
		
		String output = "";
		String miniMap = "";
		String temp = "";
		boolean foundItem = false;
		Room room = player.getRoom();
		
		if (startNewLine) {
			
			player.message("\n\r");
		}

		// The room location, name, exits, description		
		output += (room.getNewRoomFlag() ? "#r<NewRoom>#n" : "") + "[#y" + room.getX() + 
				  ", " + room.getY() + "#n]: #g" + room.getRoomName() + "#n\n";
		output += room.getExits(room.getX(), room.getY());
		output += room.getRoomDescription() + "\n";

		// Display any players in the room
		temp = Player.getPlayersInRoom(player);
		
		// If there are no players, well, notify of that
		if (temp.equals("")) {
			
			output += "There are no players in this room.\n";
		}
		
		else {
			
			output += temp;
		}
		
		// Display the room's inventory
		output += room.getInventory();
		
		// Build the minimap
		miniMap = minimap(player);

		// Make sure the output is formatted nicely
		output = TextManipulator.splitAString(output, 90);

		// Place the minimap on the left and the room text on the right
		output = TextManipulator.smushLeft(miniMap, output);
		
		// Message the given player the description
		player.message(output);
	}

	// A method to allow a player to laugh
	private static void commandLaugh(Player player, String action) {
		
		Player.messageTheRoom(player.getX(), player.getY(), player.getName() + " begins laughing " +
							 "hysterically!\n\r", player);
		player.message("You begin laughing hysterically.\n\r");	
	} 

	// A method to allow a player to dance
    private static void commandDance(Player player, String action) {

		Player.messageTheRoom(player.getX(), player.getY(), player.getName() + " begins dancing " +
							 "enthusiastically!\n\r", player);
		player.message("You begin dancing enthusiastically.\n\r");
	}

	// A method to chat or socialize with other players
	private static void commandChat(Player player, String playerInput) {
		
		// What the acting player will see, set with a default
		String playerOutput	= "You didn't add a message!\n\r";

		// What the world will see
		String otherPlayerOutput = "";
		
		if (playerInput.length() > 0) {
	
			Player.messageAll(player.getName() + " chats, \"" + playerInput + "\".\n\r", player);
			player.message("You chat, \"" + playerInput + "\".\n\r");
			//Player.rebuildAllPrompts();			
		}
		
		else {
			
			player.message(playerOutput);
		}
	}

	// A method to rename a player
	private static void commandName(Player player, String playerInput) {
		
		String name = TextManipulator.oneArgument(playerInput);
		
		player.setName(name.toUpperCase().charAt(0) + name.substring(1));
		
		player.message("Your name has been set to " + name + ".\n\r");
	}

	// A method to show the equipment of a player	
	public static void commandShowEquipped(Player player) {
		
		player.message(player.getAllEquipment());
	}
	
	// A method to give credit to myself and for ASCII art credit
	public static void commandCredits(Player player) {
		
		String output = "JavaWorld was created by me, Nicholas Warner for my Java2 final project." +
						" All code is originally written by me, Nicholas Warner. Specific pieces " +
						"of code were written for my Java 1 final project. These include:\n\n\r" +
						"\t- The MiniMap and Map methods (with slight tweaks)\n\r" +
						"\t- The structure of the checkCommand method in the Interpreter class\n\r" +
						"\t- Approximately 60 lines from the ToolKit class\n\r" +
						"\t- Approximately 30 lines from the Item class\n\r" +
						"\t- Approximately 60 lines from the Room class\n\r" +
						"\t- Approximately 60 lines from the World class\n\r" +
						"\t- Approximately 120 lines from the Player class\n\n\r" +
						"The remaining ~5000+ lines of code and comments were written over the " +
						"course of the final two weeks of April (~9 days in total).\n\n\r" +
						"The inspiration for this game came from the Multi-User-Dungeons (MUDs)" +
						", specifically those in the Aber, DIKU, Circle, Rom, and GodWars " +
						"families that I used to play as a child in the '90s and early 2000s." +
						"\n\n\r#RCredit goes to b'ger who's ASCII artwork was used for the " +
						"equipment model. The ASCII artwork was found at: \n\r" +
						"\t- http://www.chris.com/ascii/index.php?art=people/vikings\n\r";
		
		player.message(output);
	}

	// A method to change a player's title	
	public static void commandTitle(Player player, String playerInput) {
		
		if (playerInput.length() > 35) {
			
			playerInput = playerInput.substring(0, 35);
		}

		player.setTitle(playerInput);
		
		player.message("Title set.\n\r");
	}
	
	// toString method
	public String toString() {
		
		return ("Class: Interpreter");
	}
	
	// equals method
	public boolean equals(Interpreter oneInterpreter) {
		
		if (oneInterpreter.toString().equals(toString())) {
			
			return true;
		}
		
		return false;
	}
}
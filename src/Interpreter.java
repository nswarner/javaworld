import java.io.IOException;

/**
 *<pre>
 *	Purpose
 *
 *		This class inteprets a player's command input and assigns that input to a given method. All
 *		of the commands which a player can use are listed in this class and some may be called
 *		externally (such as saving) but most are private.
 *
 *	NOTE
 *		All commands accept a Player Object as an argument. This is because every command will send
 *		text to the player who initiates the command.
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
 *</pre>
 * @author Nicholas Warner
 * @version 5.1, May 2015
 */
public final class Interpreter {

	/** A list of all available commands. */
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

	/** A list of all available admin commands. */
	public static final String[] adminCommands = {
		
							"shutdown",
							"ilist",
							"freeze",
							"unfreeze",
							"info"
                            };

	/** Private constructor ensures Interpreter cannot be instantiated. */
	private Interpreter() {

		throw new AssertionError();
	}

	/** 
	 * A method to interpret a player's input and, after validating that it is
	 * valid input, match the input to a command (commandMethod).
	 *
	 * @param player The given player who is running the command.
	 * @param playerInput The String input that the player types in, both the
	 *						command and any secondary arguments.
	 * @return Returns a true or false value whether the command was valid or not.
	 */
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

	/**
	 * A method to list all Items which are currently loaded into the game.
	 *
	 * @param player The given player using the Items command.
	 */
    private static void commandItemsList(Player player) {
	
		String output = Item.getItemsList();
		
		player.message("(#yLocation#n)\t[#yItemID#n: #yItemName#n] #yItemDescription#n\n\r" + 
					   output);
	}

	/**
	 * A method to send the player back to the HOMELOCATION, as defined in the World class.
	 *
	 * @param player The given player using the Recall command.
	 */
    private static void commandRecall(Player player) {
		
		player.setLocation(World.HOMELOCATION, World.HOMELOCATION);
		player.setHealth(player.getMaxHealth());
		player.message("Your eyes close, your body flickers with fluidic energy, and you " +
					   "materialize in your home location.\n\r");
		commandLook(player, "", false);
	}

	/**
	 * A method which freezes a single player so they may no longer act.
	 *
	 * @param player The given player using the Freeze command.
	 * @param playerInput A String which contains the name of the player to be frozen.
	 */
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

	/** 
	 * A method which unfreezes a single payer so that may act again.
	 *
	 * @param player The given player using the UnFreeze command.
	 * @param playerInput A String which contains the name of the player to be unfrozen.
	 */
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

	/**
	 * A method to shutdown the entire game; save everyone first and exits cleanly.
	 *
	 * @param player The given player using the Shutdown command.
	 */
    private static void commandShutdown(Player player) {
		
		System.out.println("Cleaning up. Game is exiting.");

		Player.saveAndCloseConnections();

		System.exit(0);
	}

	/**
	 * A method to quit from the game for a player (save them, then extract them).
	 *
	 * @param player The given player using the Quit command.
	 */
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

	/** 
	 * A method to see who is online at the present time.
	 *
	 * @param player The given player using the Who command.
	 */
    private static void commandWho(Player player) {

		String status;
		int pCount = 0;
		
		String output = "#r" + Player.getNumberOnline() + " Players #yonline in " +
						"JavaWars:#n\n\n\r";

		output += Player.getWhoStatus();
				
		player.message(output);
	}

	/** 
	 * A method to see vital statistics about a player.
	 *
	 * @param player The given player using the Score command.
	 */
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

	/** 
	 * A method to move the Player east by one coordinate value.
	 *
	 * @param player The given player using the East command.
	 */
    private static void commandEast(Player player) {
		
		if (player.moveEast()) {
			
			player.discoverRoom();
			commandLook(player, "", false);
		}
		
		else {
			
			player.message("*bump* You can't go East!\n\r");
		}
	}

	/** 
	 * A method to move the Player west by one coordinate value.
	 *
	 * @param player The given player using the West command.
	 */
    private static void commandWest(Player player) {
		
		if (player.moveWest()) {
			
			player.discoverRoom();
			commandLook(player, "", false);
		}

		else {
			
			player.message("*bump* You can't go West!\n\r");
		}
	}

	/**
	 * A method to move the Player north by one coordinate value.
	 *
	 * @param player The given player using the north command.
	 */
    private static void commandNorth(Player player) {
		
		if (player.moveNorth()) {
			
			player.discoverRoom();
			commandLook(player, "", false);
		}
		
		else {
			
			player.message("*bump* You can't go North!\n\r");
		}
	}

	/** 
	 * A method to move the player south by one coordinate value.
	 *
	 * @param player The given player using the south command.
	 */
    private static void commandSouth(Player player) {
		
		if (player.moveSouth()) {
			
			player.discoverRoom();
			commandLook(player, "", false);
		}

		else {
			
			player.message("*bump* You can't go South!\n\r");
		}
	}

	/**
	 * A method to build a minimap for the given player as a String.
	 *
	 * @param player The given player using the minimap command.
	 * @return A built grid map of discovered rooms for the Player.
	 */
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

	/**
	 * A method to begin a game of Hangman.
	 *
	 * @param player The given player using the Hangman command.
	 * @param argument The given String argument from the player.
	 */
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

	/**
	 * A method to build an overhead map for the Player.
	 *
	 * @param player The given player using the Map command.
	 */
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

	/**
	 * A method meant to list all of the available commands from the command list to a
	 *	given player.
	 *
	 * @param player The given player using the Commands command.
	 */
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

	/** 
	 * A method to display the player's inventory.
	 *
	 * @param player The given player using the Inventory command.
	 */
	private static void commandInventory(Player player) {
		
		player.message(player.displayInventory());
	}
	
	/**
	 * A method to save the player to the flat file database.
	 *
	 * @param player The given player using the Save command.
	 */
	public static void commandSavePlayer(Player player) {
		
		player.message("Saving player file.\n\r");
		Player.savePlayer(player);
	}
	
	/**
	 * A method to equip a given item on the player.
	 *
	 * @param player The given player using the Equip command.
	 * @param oneItem The given item intended to be equipped.
	 */
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

	/**
	 * A method to remove a given item from the player's equipment.
	 *
	 * @param player The given player using the Remove command.
	 * @param oneItem The given Item the player is attempting to Remove.
	 */
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

	/**
	 * A method to get a given item from the room's floor(inventory)
	 *
	 * @param player The given player using the Get command.
	 * @param oneItem The given item meant to be picked up.
	 */
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

	/**
	 * A method to drop a given item from a player's inventory.
	 *
	 * @param player The given player using the Drop command.
	 * @param oneItem The given Item that the Player is trying to drop.
	 */
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

	/**
	 * A method to look at the given room and see a description of it.
	 *
	 * @param player The given player using the Look command.
	 * @param action The String of arguments a player issues along with the look
	 *					command. For example, a player may look at another player,
	 *					at an item, or at the room(default).
	 * @param startNewLine A boolean value which indicates whether the player requires
	 *						a new line sent to them prior to sending additional text.
	 */ 
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

	/**
	 * A method to allow a player to laugh and demonstrate this to those in their same room.
	 *
	 * @param player The given player using the Laugh command.
	 * @param action A String argument to go alongside; possibly if a player wanted to
	 *					laugh at another player.
	 */
	private static void commandLaugh(Player player, String action) {
		
		Player.messageTheRoom(player.getX(), player.getY(), player.getName() + " begins laughing " +
							 "hysterically!\n\r", player);
		player.message("You begin laughing hysterically.\n\r");	
	} 

	/**
	 * A method to allow a player to dance, demonstrate it to the room.
	 *
	 * @param player The given player using the Dance command.
	 * @param action A String argument to go alongside; possibly if a player wanted to
	 *					dance with another player.
	 */
    private static void commandDance(Player player, String action) {

		Player.messageTheRoom(player.getX(), player.getY(), player.getName() + " begins dancing " +
							 "enthusiastically!\n\r", player);
		player.message("You begin dancing enthusiastically.\n\r");
	}

	/**
	 * A method to chat or socialize with other players.
	 *
	 * @param player The given player using the Chat command.
	 * @param playerInput A String containing the message for the player to chat.
	 */
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

	/**
	 * A method to rename a player.
	 *
	 * @param player The given player using the Name command.
	 * @param playerInput A String representing the new Name of the Player.
	 */
	private static void commandName(Player player, String playerInput) {
		
		String name = TextManipulator.oneArgument(playerInput);
		
		player.setName(name.toUpperCase().charAt(0) + name.substring(1));
		
		player.message("Your name has been set to " + name + ".\n\r");
	}

	/**
	 * A method to show the equipment of a player.
	 *
	 * @param player The given player using the Equipment command.
	 */
	public static void commandShowEquipped(Player player) {
		
		player.message(player.getAllEquipment());
	}
	
	/**
	 * A method to give credit to myself and for ASCII art credit to another.
	 *
	 * @param player The given player using the credits command.
	 */
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

	/**
	 * A method to change a player's title.
	 *
	 * @param player The given player using the Title command.
	 * @param playerInput A String containing the new title of the player.
	 */
	public static void commandTitle(Player player, String playerInput) {
		
		if (playerInput.length() > 35) {
			
			playerInput = playerInput.substring(0, 35);
		}

		player.setTitle(playerInput);
		
		player.message("Title set.\n\r");
	}
}

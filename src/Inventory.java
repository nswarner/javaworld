/**
 *	Purpose
 *
 *		The Inventory class contains a list of all the Items that a player has at the moment with
 *		the exception of Items currently equipped. The Inventory class also allows the Player to
 *		manage the Items in their inventory by dropping them or picking up more.
 *
 *	Algorithm
 *
 *		1. Declare the Inventory class
 *		2. Import the necessary packages (IO and Util)
 *		3. Declare a LinkedList for our Items to represent an Inventory
 *		4. Declare a boolean to indiate whether it's a room or not
 *		5. Declare a default constructor
 *		6. Declare a paramterized constructor
 *		7. Declare mutator methods
 *		8. Declare a method to display the inventory (room or player)
 *		9. Declare methods to test whether the inventory contains an Item in
 *			- Inventory
 *			- Equipped
 *			- Or both
 *		10. Declare a method to save the inventory to a flat file
 *		11. Declare a method to load the inventory from the flat file
 *		12. Declare a toString method
 *		13. Declare an equals method
 *
 *	Structure / Process
 *
 *		The Inventory class is instantiated as a part of the Player class. It's modified through
 *		the player class.
 *
 *	Author			- Nicholas Warner
 *	Created 		- 4/24/2015
 *	Last Updated	- 5/1/2015
 */

// Import our packages
import java.util.*;
import java.io.*;

// Declare the class
public class Inventory {

	// Since we're randomly removing and adding items, a LinkedList seems good
	private LinkedList<Item> itemList;
	// Rooms have different display-Inventorys than Players
	private boolean isARoom;

	// Our default constructor
	public Inventory() {
		
		itemList = new LinkedList<Item>();
		isARoom = false;
	}

	// Our paramterized Constructor
    public Inventory(boolean isARoom) {

		itemList = new LinkedList<Item>();
		this.isARoom = isARoom;
    }

	// Mutator Methods    
    public void addToInventory(Item oneItem) {
    	
    	itemList.add(oneItem);
    }
    
    public String removeFromInventory(Item oneItem) {
    	
    	itemList.remove(oneItem);
    	return oneItem.getName() + " was dropped from inventory.\n\r";
    }

	// Inventory Methods    
    public String displayInventory(boolean isARoom) {
    	
    	// To hold the output as we build it
    	String output = "";
    	
    	// Perhaps there are no items in the Inventory
    	boolean noItems = true;
    	
    	// But if there are
    	if (itemList.size() > 0) {

			// Let's go through them
    		for(int i = 0; i < itemList.size(); i++) {
    		
    			// Hey, we found one
	    		Item tempItem = itemList.get(i);

				// And if we're a room, then we display something different
				if (isARoom) {

		    		output += "[#YItem#n: #y" + tempItem.getRealName() + "#n]: A " + 
		    				  tempItem.getName() + "#n is lying in the room.\n";
				}
				
				// Than if we're a Player who doesn't leave their stuff lying around
				else {
					
					if (!tempItem.getEquipped()) {
					
						output += "[#YItem#n: #y" + String.format("%-20s", tempItem.getRealName()) + 
								  "#n]: " + tempItem.getName() + "#n\n\r"; 
						
						// And of course, no items is false because we have an item!
						noItems = false;
					}
				}
    		}
    	}

		// Assuming itemList.size == 0
		else if (!isARoom && noItems){
			
			// Player's Inventory is bare
			output = "(Empty)";
		}
		
		// Send the output out
		return output;
    }
    
    // A method to test whether the Inventory contains an item of a given name
    public Item contains(String itemName) {
    	
    	// Lower case it
    	itemName = itemName.toLowerCase();
    	
    	// Go through every item
    	for(int i = 0; i < itemList.size(); i++) {
    		
    		// If it's lowercase name begins with our itemName
    		if (itemList.get(i).getRealName().toLowerCase().startsWith(itemName)) {
    			
    			// Then we got it!
    			return itemList.get(i);
    		}
    	}
    	
    	// We didn't get it
    	return null;
    }

	// Same as contains, but we ignore when it's in the Inventory and not equipped
    public Item containsInEquipment(String itemName) {
    	
    	itemName = itemName.toLowerCase();
    	
    	for(int i = 0; i < itemList.size(); i++) {
    		
    		if (itemList.get(i).getRealName().toLowerCase().startsWith(itemName)) {
    			
    			if (itemList.get(i).getEquipped()) {

	    			return itemList.get(i);
    			}
    		}
    	}
    	
    	return null;
    }
    
	// Same as contains, but we ignore when it's equipped and not in the inventory
    public Item containsInInventory(String itemName) {
    	
    	itemName = itemName.toLowerCase();
    	
    	for(int i = 0; i < itemList.size(); i++) {
    		
    		if (itemList.get(i).getRealName().toLowerCase().startsWith(itemName)) {
    			
    			if (!itemList.get(i).getEquipped()) {

	    			return itemList.get(i);
    			}
    		}
    	}
    	
    	return null;
    }
    
    // A method used to save the Inventory to a file
    public void saveInventory(String playerName) {
    	
    	// We use PrintWriter for file output
    	PrintWriter fileOut = null;
    	// Each item is temporarily held
    	Item tempItem = null;
    	
    	// We make sure the filename is lowercase
    	playerName = playerName.toLowerCase();
    	
    	// And here we go!
    	try {
    		
    		// Let's open the file for writing
    		fileOut = new PrintWriter(new File("../player/" + playerName + ".inventory"));
    		
    		// Let's go through every item
    		for(int i = 0; i < itemList.size(); i++) {
    			
    			// Hold onto it
    			tempItem = itemList.get(i);
    			// Printout it's toString
    			fileOut.print(tempItem.toString());
    			// Printout it's ItemDescription toString
    			fileOut.print(tempItem.iDescToString().toString());
    		}
    		
    		// -1 signifies EOF
   			fileOut.println("-1");
   			
   			// Always close the file!
   			fileOut.close();
    	}
 
 		// Catch Exceptions   	
    	catch (IOException e) {
    		
    		System.out.println("IOException in saveInventory: " + e.getMessage());
    		System.exit(0);
    	}
    	
    	catch (Exception e) {
    		
    		System.out.println("Exception in saveInventory: " + e.getMessage());
    		System.exit(0);
    	}
    }
    
    // Load up the Inventory from a save file
    public void loadInventory(String playerName) {
    	
    	// Scanner for reading fileIn
    	Scanner fileIn		= null;
    	// Temp item to hold the item temporarily
    	Item tempItem		= null;
    	// Condition to keep going until the end of file
    	boolean keepGoing	= true;
    	// And a temporary ItemDescription
    	ItemDescription tempItemDescription	= null;
    	
    	// The object's fields
		int itemX	= 0;
		int itemY	= 0;
		int oid		= 0;
		
		// The ItemDescription's fields
		String prefix				= "";
		String suffix				= "";
		String itemName				= "";
		String itemSimpleName		= "";
		String itemMetalTypeName	= "";
		int itemMetalTypeValue		= 0;
		double modifierName			= 0.0;
		double modifierValue		= 0.0;
		int iLevel					= 0;
		int locationWorn			= 0;

		// Always work with lowercase names
    	playerName = playerName.toLowerCase();
    	
    	// Let's try
    	try {
    		
    		// Open up our file for reading
    		fileIn = new Scanner(new File("../player/" + playerName + ".inventory"));
    		
    		// And we'll keepGoing until EOF
    		while(keepGoing) {
    			
    			// Let's get our X location for our Item
    			itemX = Integer.parseInt(fileIn.nextLine());
    			
    			// If it's a -1, we're EOF
    			if (itemX != -1) {
    				
    				// Now just pull in the information in the same way we saved it
    				itemY	= Integer.parseInt(fileIn.nextLine());
    				oid		= Integer.parseInt(fileIn.nextLine());
    				
    				prefix				= fileIn.nextLine();
    				suffix				= fileIn.nextLine();
    				itemName			= fileIn.nextLine();
    				itemSimpleName		= fileIn.nextLine();
    				itemMetalTypeName	= fileIn.nextLine();
       				itemMetalTypeValue	= Integer.parseInt(fileIn.nextLine());
    				modifierName		= Double.parseDouble(fileIn.nextLine());
    				modifierValue		= Double.parseDouble(fileIn.nextLine());
    				iLevel				= Integer.parseInt(fileIn.nextLine());
    				locationWorn		= Integer.parseInt(fileIn.nextLine());
    				
    				// Instantiate the ItemDescription
    				tempItemDescription = new ItemDescription(prefix, suffix, itemName,
    														  itemSimpleName, itemMetalTypeName,
    														  itemMetalTypeValue, modifierName,
    														  modifierValue, iLevel, locationWorn);
					// Instantiate the Item
    				tempItem = new Item(itemX, itemY, oid, tempItemDescription);
    				
    				// Add the item to our itemList
    				itemList.add(tempItem);
    			}

				// itemX == -1, we stop
				else {
					
					keepGoing = false;
				}
    		}
    	}
    	
    	// Catch Exceptions
    	catch (IOException e) {
    		
    		System.out.println("IOException in loadInventory: " + e.getMessage());
    		System.exit(0);
    	}
    	
    	catch (Exception e) {
    		
    		System.out.println("Exception in loadInventory: " + e.getMessage());
    		System.exit(0);
    	}
    }

    // Our toString method
    public String toString() {
    	
    	return "Class: Inventory\nInventory Size: " + itemList.size();
    }
    
    // Our equals method
    public boolean equals(Inventory oneInventory) {
    	
    	if (oneInventory.toString().equals(toString())) {
    		
    		return true;
    	}
    	
    	return false;
    }
}

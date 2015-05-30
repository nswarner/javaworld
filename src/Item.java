/**
 *	Purpose
 *
 *		The Item class represents a single item or tangible object within JavaWorld. These items
 *		currently are meant to be worn on the player's body.
 *
 *	Algorithm
 *
 *		1. Declare the Item class
 *		2. Import the necessary packages, classes, interfaces, ...
 *		3. Declare and instantiate a static LinkedList to hold all Items in the game
 *		4. Declare and instantiate a static int representing a unique object ID
 *		5. Declare instance variables for the Item
 *			- int itemX, itemY -> The Item's current roomX and roomY location
 *			- int oid -> The object's unique ID number
 *			- ItemDescription iDesc -> The item's description information
 *			- boolean isEquipped -> is the item equipped?
 * 		6. Declare a default constructor
 * 		7. Declare a parameterized constructor
 *			- itemX, itemY, oid, itemDescription
 *		8. Declare a parameterized constructor
 *			- itemX, itemY, oid, wornOnLocation, and iLevel
 *		9. Declare the necessary Accessor methods
 *		10. Declare the necessary Mutator methods
 *		11. Declare a method to check whether an Item exists in the Item LinkedList
 *		12. Declare a method to dynamically build an item and place it at a given (x, y)
 *		13. Declare a toString method
 *		14. Declare an equals method	
 *
 *	Structure / Process
 *
 *		Items are loaded in shortly after the World and Rooms in the GameServer class. They are
 *		randomly created and distributed across the World on the basis of a percentage chance.
 *
 *	Author			- Nicholas Warner
 *	Created 		- 4/24/2015
 *	Last Updated	- 5/1/2015
 */

// Import the necessary packages, classes, interfaces, ...
import java.util.Scanner;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

// Our Item class
public class Item {
	
	// The static LinkedList of Items
	private static LinkedList<Item> items = new LinkedList<Item>();
	// An int as a running unique ID of new objects
	private static int runningOID = 0;
		
	// Instance variables
	private int itemX;
	private int itemY;
	private int oid;
	private ItemDescription iDesc;
	private boolean isEquipped;
	
	// The default constructor
	public Item() {
		
		itemX = 0;
		itemY = 0;
		oid = 0;
		iDesc = new ItemDescription();
		isEquipped = false;
	}
	
	// The parameterized constructor
	public Item(int itemX, int itemY, int oid, ItemDescription iDesc) {
		
		this.itemX	= itemX;
		this.itemY	= itemY;
		this.oid	= oid;
		this.iDesc	= iDesc;
	}
	
	// The parameterized constructor
	public Item(int oid, int itemX, int itemY, int wornOnLocation, int iLevel) {
		
		this.oid		= oid;
		this.itemX		= itemX;
		this.itemY		= itemY;
        iDesc			= new ItemDescription(wornOnLocation, iLevel);
        isEquipped		= false;
	}

	// Accessor Methods
    public int getX() {
		
		return itemX;
	}
	
    public int getY() {
		
		return itemY;
	}
	
    public String getName() {
		
		return iDesc.getName();
	}

	public boolean getEquipped() {
		
		return isEquipped;
	}
	
    public int getOID() {
	
		return oid;
	}

    public String getRealName() {
    	
    	return iDesc.getSimpleName();
    }
        
    public int getLocationWorn() {
    	
    	return iDesc.getLocationWorn();
    }

	// A method to get a List of all items in the Item LinkedList
	public static String getItemsList() {
		
		String output = "";
		
		for(int i = 0; i < items.size(); i++) {
			
			output += "(" + items.get(i).getX() + ", " + items.get(i).getY() + ")\t" + "[" + 
					  items.get(i).getOID();
			output += ": " + items.get(i).getName() + "]#n\n\r";
		}
		
		return output;
	}

	// Mutator Methods
	public void setEquipped(boolean isEquipped) {
		
		this.isEquipped = isEquipped;
	}
		
    public void setX(int x) {
		
		if (x >= 0 && x <= 200) {
			
			itemX = x;
		}		
	}
	
    public void setY(int y) {
		
		if (y >= 0 && y <= 200) {

			itemY = y;
		}
	}
	
    public void setName(String name) {
		
		iDesc.setName(name);
	}

	// A method to add a new item to the Item LinkedList
	public static void addItem(Item item) {
		
		items.add(item);
		
		if (World.checkRoomExists(item.itemX, item.itemY)) {
		
			World.getRoom(item.itemX, item.itemY).addItem(item);
		}
	}
	
	// A method to remove an item from the Item LinkedList
    public static void deleteItem(Item item) {
        
        items.remove(item);
    }

	// Item Methods
    public static boolean checkItemExists(Item item) {
        
        return items.contains(item);
    }
	
	// A method to dynamically build a single item
    public static void dynamicallyBuildItems(int numberOfItems, int roomX, int roomY, boolean room) {
    	
    	// If they gave us a valid roomX and roomY in the world
    	if (roomX >= 0 && roomX < World.MAXROOMS && roomY >= 0 && roomY < World.MAXROOMS) {
    	
    		// If it's supposed to go in a default room
	    	if (room) {
	
				// Make the number of Items requested
		    	for(int i = 0; i < numberOfItems; i++) {
	    		
	    			// And add them to the Item LinkedList
		    		addItem(new Item(runningOID++, 101, 101, ToolKit.rand(0, 
		    						 ItemDescription.NUMBER_WORN_ON_LOCATIONS), 
		    						 ToolKit.rand(0, 71)));
		    	}
	    	}
	    	
	    	// Otherwise not the default room
	    	else {
	    		
	    		// Make the number of Items requested
		    	for(int i = 0; i < numberOfItems; i++) {
	    		
	    			// And add them to the Item LinkedList
		    		addItem(new Item(runningOID++, roomX, roomY, ToolKit.rand(0, 
		    						 ItemDescription.NUMBER_WORN_ON_LOCATIONS), 
		    						 ToolKit.rand(0, 71)));
		    	}    		
	    	}
    	}
    }

	// toString Methods    
    public String iDescToString() {
    	
    	return iDesc.toString();
    }
    
    public String toString() {
    	
    	return itemX + "\n" +
    		   itemY + "\n" +
    		   oid + "\n";
    }
    
    // equals Method
    public boolean equals(Item oneItem) {
    	
    	if (toString().equals(oneItem.toString()) && iDescToString().equals(
    																  oneItem.iDescToString())) {
    		
    		return true;
    	}
    	
    	return false;
    }

}

import java.util.Scanner;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 *<pre>
 *	Purpose
 *
 *		The Item class represents a single item or tangible object within JavaWorld. These items
 *		currently are meant to be worn on the player's body.
 *
 *	Structure / Process
 *
 *		Items are loaded in shortly after the World and Rooms in the GameServer class. They are
 *		randomly created and distributed across the World on the basis of a percentage chance.
 *</pre>
 * @author Nicholas Warner
 * @version 5.1, May 2015
 * @see ItemDescription
 * @see Inventory
 * @see Equipment
 */
public class Item {
	
	/** The static LinkedList of Items instantiated into the World. */
	private static LinkedList<Item> items = new LinkedList<Item>();
	/** An int as a running unique ID of new objects. */
	private static int runningOID = 0;
	/** The X Coordinate location of the given Item. */
	private int itemX;
	/** The Y Coordinate location of the given Item. */
	private int itemY;
	/** The Unique ID of the Item. */
	private int oid;
	/** An ItemDescription Object describing the Item. */
	private ItemDescription iDesc;
	/** A boolean indicating whether the given Item is equipped or not. */
	private boolean isEquipped;
	
	/** 
	 * Initializes all ints to 0, booleans to false, and instantiates an 
	 * ItemDescription.
	 */
	public Item() {
		
		itemX = 0;
		itemY = 0;
		oid = 0;
		iDesc = new ItemDescription();
		isEquipped = false;
	}
	
	/**
	 * Initializes this Item with an X and Y coordinate, a unique object ID,
	 * and a reference to a given ItemDescription.
	 *
	 * @param itemX The X coordinate location of this Item.
	 * @param itemY The Y coordinate location of this Item.
	 * @param oid The Unique Object ID of this Item.
	 * @param iDesc A reference to an instantiated ItemDescription.
	 */
	public Item(int itemX, int itemY, int oid, ItemDescription iDesc) {
		
		this.itemX	= itemX;
		this.itemY	= itemY;
		this.oid	= oid;
		this.iDesc	= iDesc;
	}
	
	/**
	 * Initializes this Item with a unique object ID, an X and Y coordinate,
	 * a location where the Item is worn, and an iLevel for this Item. The
	 * iLevel and wornOnLocation are used to instantiate a new ItemDescription.
	 *
	 * @param oid The unique Object ID of this Item.
	 * @param itemX The X coordinate location of this Item.
	 * @param itemY The Y coordinate location of this Item.
	 * @param wornOnLocation The location that the Item is worn on by a Player.
	 * @param iLevel The Item Level, an indication of its Strength.
	 */
	public Item(int oid, int itemX, int itemY, int wornOnLocation, int iLevel) {
		
		this.oid		= oid;
		this.itemX		= itemX;
		this.itemY		= itemY;
        iDesc			= new ItemDescription(wornOnLocation, iLevel);
        isEquipped		= false;
	}

	/**
	 * A method to get the Item's X coordinate location.
	 *
	 * @return The Item's itemX coordinate location.
	 */
    public int getX() {
		
		return itemX;
	}

	/**
	 * A method to get the Item's Y coordinate location.
	 *
	 * @return The Item's itemY coordinate location.
	 */
    public int getY() {
		
		return itemY;
	}

	/**
	 * A method to get the Item's name from its ItemDescription.
	 *
	 * @return The Item's ItemDescription's name.
	 */
    public String getName() {
		
		return iDesc.getName();
	}

	/**
	 * A method to get whether the item is equipped or not.
	 *
	 * @return The isEquipped boolean value, indicating whether the
	 *			Item is equipped or not.
	 */
	public boolean getEquipped() {
		
		return isEquipped;
	}

	/**
	 * A method to get this Item's unique ID.
	 *
	 * @return This Item's unique ID.
	 */
    public int getOID() {
	
		return oid;
	}

	/**
	 * A method to get the real name of the Item.
	 *
	 * @return Returns the Item's ItemDescription's name.
	 */
    public String getRealName() {
    	
    	return iDesc.getSimpleName();
    }

	/**
	 * A method to get the location where this Item is worn.
	 *
	 * @return Returns the Item's ItemDescription's wornOnLocation.
	 */ 
    public int getLocationWorn() {
    	
    	return iDesc.getLocationWorn();
    }

	/** 
	 * A method to get a String listing of all items in the Item LinkedList.
	 *
	 * @return Returns a String listing all of the Items in the itemList.
	 */
	public static String getItemsList() {
		
		String output = "";
		
		for(int i = 0; i < items.size(); i++) {
			
			output += "(" + items.get(i).getX() + ", " + items.get(i).getY() + ")\t" + "[" + 
					  items.get(i).getOID();
			output += ": " + items.get(i).getName() + "]#n\n\r";
		}
		
		return output;
	}

	/**
	 * A method to set or change whether this Item is equipped or not.
	 *
	 * @param isEquipped The boolean value indicating whether this Item is equipped
	 *						or not.
	 */
	public void setEquipped(boolean isEquipped) {
		
		this.isEquipped = isEquipped;
	}

	/**
	 * A method to set the X coordinate of this Item.
	 *
	 * @param x The new X coordinate being set for this Item.
	 */	
    public void setX(int x) {
		
		if (x >= 0 && x <= 200) {
			
			itemX = x;
		}		
	}

	/**
	 * A method to set the Y coordinate of this Item.
	 *
	 * @param y The new Y coordinate being set for this Item.
	 */
    public void setY(int y) {
		
		if (y >= 0 && y <= 200) {

			itemY = y;
		}
	}

	/** A method to set or change the name of this Item.
	 *
	 * @param name The new String name of this Item.
	 */
    public void setName(String name) {
		
		iDesc.setName(name);
	}

	/**
	 * A method to add a new item to the Item LinkedList.
	 *
	 * @param item A reference to the Item to add to the itemList.
	 */
	public static void addItem(Item item) {
		
		items.add(item);
		
		if (World.checkRoomExists(item.itemX, item.itemY)) {
		
			World.getRoom(item.itemX, item.itemY).addItem(item);
		}
	}
	
	/** 
	 * A method to remove an item from the Item LinkedList.
	 *
	 * @param item The item to remove from the items list.
	 */
    public static void deleteItem(Item item) {
        
        items.remove(item);
    }

	/**
	 * A method to check whether a given Item is located within the items LinkedList.
	 *
	 * @param item The item to be checked if it's contained within the items LinkedList.
	 * @return A boolean value indicating whether the Item exists within the items LinkedList.
	 */
    public static boolean checkItemExists(Item item) {
        
        return items.contains(item);
    }
	
	/** 
	 * A method to dynamically build items from randomized Item values, placing them within
	 * a given room (via x and y coordinate) OR placed into a default room.
	 *
	 * @param numberOfItems The given number of Items intended to be generated by the method.
	 * @param roomX The X location of the Room where the Item will be placed.
	 * @param roomY The Y location of the Room where the Item will be placed.
	 * @param room A boolean indicating whether the Items are meant to be placed in the given X
	 *				and Y room coordinates or a default location (HOMELOCATION)
	 * @see #items
	 * @see World#HOMELOCATION
	 * @see World#MAXROOMS
	 * @see ToolKit#rand(int, int)
	 * @see ItemDescription#NUMBER_WORN_ON_LOCATIONS
	 */
    public static void dynamicallyBuildItems(int numberOfItems, int roomX, int roomY, boolean room) {
    	
    	// If they gave us a valid roomX and roomY in the world
    	if (roomX >= 0 && roomX < World.MAXROOMS && roomY >= 0 && roomY < World.MAXROOMS) {
    	
    		// If it's supposed to go in a default room
	    	if (room) {
	
				// Make the number of Items requested
		    	for(int i = 0; i < numberOfItems; i++) {
	    		
	    			// And add them to the Item LinkedList
		    		addItem(new Item(runningOID++, World.HOMELOCATION, World.HOMELOCATION, 
									 ToolKit.rand(0, ItemDescription.NUMBER_WORN_ON_LOCATIONS), 
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

	/**
	 * A method to return a String expressing the values of the Item's ItemDescription.
	 *
	 * @return Returns the Item's ItemDescription String expressing the values of the
	 *			ItemDescription.
	 * @see ItemDescription#toString()
	 */
    public String iDescToString() {
    	
    	return iDesc.toString();
    }

	/**
	 * A method to return the Item's location and unique ID.
	 *
	 * @return Returns a String containing the location of this Item's x and y coordinates
	 *			and this Item's unique ID.
	 */
    public String toString() {
    	
    	return itemX + "\n" +
    		   itemY + "\n" +
    		   oid + "\n";
    }
    
    /**
	 * A method which tests whether this Item is equal to a given Item.
	 *
	 * @param oneItem The given Item meant to be tested against.
	 * @return Returns a boolean of true if the Items are equal and false if not.
	 * @see #toString
	 */
    public boolean equals(Item oneItem) {
    	
    	if (toString().equals(oneItem.toString()) && iDescToString().equals(
    																  oneItem.iDescToString())) {
    		
    		return true;
    	}
    	
    	return false;
    }

}

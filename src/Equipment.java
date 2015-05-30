/* Purpose
 *		
 *		The Equipment class holds a list of all the equipment a given character has equipped at
 *		any time. It also has methods to manage equipping, removing, and displaying the given
 *		equipped items (with an ASCII art drawing of a warrior).
 *		
 * Algorithm
 *		
 *		1. Declare the Equipment class
 *		2. Declare an array to hold the equipment in specific slots
 *		3. Declare a default constructor which initializes the array to null
 *		4. Declare a method to unequip and equip items
 *		5. Declare a method to test whether an item is equipped or not
 *		6. Declare a method to unequip an item
 *		7. Declare a method to display a character
 *		
 * Structure / Process
 *		
 *		The Equipment class is instantiated as a part of the Player class. It's modified through
 *		the player class.
 *		
 *	Author			- Nicholas Warner
 *	Created			- 4/24/2015
 *	Last Updated	- 5/1/2015
 */

public class Equipment {

	// Declare an array to hold slots of gear
	private Item[] equippedGear;

	// Declare a default constructor to intiialize our array to null
    public Equipment() {

		// One slot for each wear location
		equippedGear = new Item[ItemDescription.NUMBER_WORN_ON_LOCATIONS];

		for(int i = 0; i < equippedGear.length; i++) {
			
			equippedGear[i] = null;
		}
    }
    
    // A method which equips the given item
    public String equipItem(Item oneItem) {
    	
    	// Make sure we send the player something useful, did they succeed or fail?
    	String output = "";
    	// Figure out where the item's supposed to be worn
    	int itemLoc = oneItem.getLocationWorn();

		// If the player has something equipped already
    	if (equippedGear[itemLoc] != null) {
    		
    		// Toggle the item's equipped field (false means no longer equipped)
    		equippedGear[itemLoc].setEquipped(false);
    		// Notify the player they removed the item in favor of..
    		output += "You remove " + equippedGear[itemLoc].getName() + ".\n\r";
    	}
    	
    	// Toggle the item's equipped field
    	oneItem.setEquipped(true);
    	// Equip the given item
    	equippedGear[oneItem.getLocationWorn()] = oneItem;
    	// Notify the player they succeeded in wearing the new item
    	output += "You equip " + oneItem.getName() + ".\n\r";
    	
    	// Return the output to presumably the player
    	return output;
    }
    
    // A method which tests whether an item is equipped or not
    public boolean isEquipped(Item oneItem) {
    	
    	// Tests if it's equipped in any slot (unnecessary, but intended for future use)
    	for(int i = 0; i < equippedGear.length; i++) {
    		
    		// If it's the item we want
    		if (equippedGear[i] != null && equippedGear[i].equals(oneItem)) {
    			
    			// We found it
    			return true;
    		}
    	}
    	
    	// Otherwise, nope, not equipped
    	return false;
    }
    
    // A method which removes an item from a player's Equipment
    public String unequipItem(Item oneItem) {
    	
    	// Output for the player
    	String output = "";
    	// Figure out where the item is equipped
    	int itemLoc = oneItem.getLocationWorn();
    	
    	// If there's something equipped in that spot
    	if (equippedGear[itemLoc] != null) {

			// Remove it
			oneItem.setEquipped(false);
			// Notify the player they removed it
    		output += "You remove " + oneItem.getName() + ".\n\r";
    		// Make sure the spot is now empty
    		equippedGear[itemLoc] = null;
    	}

		// Otherwise, notify the player they failed    	
    	else {
    		
    		output += "You don't seem to be wearing " + oneItem.getName() + ".";
    	}
    	
    	// Send the output to presumably the player
    	return output;
    }

	// A method which displays all of the currently equipped gear    
    public String displayEquipment() {
    	
    	// Output for the player
    	String output		= "";
    	// A simple field to hold the ASCII color
    	String[] gearColor	= new String[9];
    	// A field to hold the name of the item equipped in the location
    	String[] eqList		= new String[9];
    	
    	// Build our color and equipment name lists
    	for(int i = 0; i < equippedGear.length; i++) {

			// If there's gear    		
    		if (equippedGear[i] != null) {
    			
    			// Then we get values!
    			gearColor[i]	= equippedGear[i].getName().charAt(0) + "";
    			gearColor[i]	+= equippedGear[i].getName().charAt(1) + "";
    			eqList[i]		= equippedGear[i].getName();
    		}
    		
    		// Otherwise, they have nothing. Boring.
    		else {
    			
    			gearColor[i]	= "#n";
    			eqList[i]		= "nothing.";
    		}
    	}

		// ... Credit to b'ger, see "credits" for more information.    	
    	output += gearColor[1] + "         __     __#n\n\r";
    	output += gearColor[1] + "        / < ___ > \\#n\n\r";
    	output += gearColor[1] + "        '-._____.-'#n\n\r";
    	output += gearColor[1] + "         ,#n| ^_^ |" + gearColor[1] + ",\t\t#n[Head:\t" +
    				eqList[1] + "#n]\n\r";
    	output += 				 "          ((())))\n\r";
    	output +=				 "            | |  \n\r";
    	output += gearColor[3] + "       ," + gearColor[2] + "############" + gearColor[3] +
    			  "\\#n\t\t[Chest:\t" + eqList[2] + "#n]\n\r";
    	output += gearColor[3] + "      /" + gearColor[2] + "  #########" + gearColor[3] + 
    			  ",  \\\t\t#n[Arms:\t" + eqList[3] + "#n]\n\r";
    	output += gearColor[3] + "     /_<'" + gearColor[2] + "#########" + gearColor[3] + 
    			  "'./_\\#n\t\t[Hands:\t" + eqList[4] + "#n]\n\r";
    	output += gearColor[3] + "    '_7_" + gearColor[2] + " ######### " + gearColor[3] +
    			  "_o_7#n\n\r";
    	output += gearColor[4] + "     (  \\" + gearColor[5] + "[o-o-o-o]" + gearColor[4] +
    			  "/  )#n\t\t[Belt:\t" + eqList[5] + "#n]\n\r";
    	output += gearColor[4] + "      \\|l" + gearColor[2] + "#########" + gearColor[4] + 
    			  "l|/#n\n\r";
    	output += gearColor[2] + "         ####_#### #n\n\r";
    	output += gearColor[6] + "        /    |    \\ #n\n\r";
    	output += gearColor[6] + "        |    |    |#n\t\t[Legs:\t" + eqList[6] + "#n]\n\r";
    	output += gearColor[6] + "        |" + gearColor[7] + "_  _" + gearColor[6] + "|" + 
    			  gearColor[7] + "_  _" + gearColor[7] + "|#n\n\r";
    	output += gearColor[7] + "        |\\\\//|\\\\//|#n\n\r";
    	output += gearColor[7] + "        \\//\\\\|//\\\\/#n\t\t[Boots:\t" + eqList[7] +
    			  "#n]\n\r";
    	output += gearColor[7] + "      ___\\\\// \\\\//___#n\n\r";
    	output += gearColor[7] + "     (((___X\\ /X___))) #n\n\r";
    	output += "(Use the 'credits' command for ASCII Art author information)\n\r";

		// Send the equipment on to the player
		return output;    	
    }
    
    // toString Method
    public String toString() {
    	
    	int c = 0;
    	
    	for(int i = 0; i < equippedGear.length; i++) {
    		
    		if (equippedGear[i] != null) {
    			
    			c++;
    		}
    	}
    	
    	return "Class: Equipment\nNumber of Worn Items: " + c;
    }
    
    // equals Method
    public boolean equals(Equipment oneEquipment) {
    	
    	if (toString().equals(oneEquipment)) {
    		
    		return true;
    	}
    	
    	return false;
    }
}
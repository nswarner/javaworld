README for JavaWorld

/---------------------------------------------------------------------------\
|																			|
|	Author: Nicholas Stephen Warner											|
|	Email: nswarner2@gmail.com												|
|	Created: May 2015														|
|	Purpose: Created for a final project in my second semester Java class.	|
|				Also submitted to my college's annual programming			|
|				competition. JavaWorld won first prize.						|
|																			|
\---------------------------------------------------------------------------/

Table of Contents

	Section 1	- Welcome
	Section 2	- How do I play JavaWorld? (See NOTE)
	Section 3	- What can I do in JavaWorld?
	Section 4	- Can I also use this? Yes!
	Section 5	- All the PostScript notes.
	Appendix A	- TODO
	Appendix B	- Compiling, Building, and Running JavaWorld

--------------------------------------------------------------------------------

Section 1 - Welcome

Thank you for loading JavaWorld. In order to play JavaWorld, you'll need a telnet
client; if you're unfamiliar with this, I recommend downloading putty. You can 
find putty at http://the.earth.li/~sgtatham/putty/latest/x86/putty.exe which is 
the original author's website.

--------------------------------------------------------------------------------

Section 2 - How do I play JavaWorld?

====================================

NOTE: This section assumes that you are familiar with Java and are able to build
a given project from source files. This project was originally created using
JCreator and therefore has the project layout and workspace / project files
associated with JCreator. After the initial project was created, most of the
future developments were added via a mix of IDEs, therefore, you may build this 
project via command line from the ./src/ folder using the build.php script.

See Appendix B for further information.

====================================

Once you've built and started the game, (you'll need to press 'n' in the console
window when it first starts for the game to run; see P.P.S. for more information)
you can log into the game with your telnet client. If you use putty, the 
following are the default settings:

	Host Name (or IP address): localhost
	Port: 5002
	Connection type: Raw
	
I recommend also typing in a "Saved Sessions" name to avoid having to retype this
information every time. Now that you've setup putty, click the "Open" button on
the bottom right of the putty screen.

You should be greeted with ASCII art that says "JavaWorld" and notes me as the
programmer for the game. Please choose a name that you'd like to play with. When
you type in a name, you'll be prompted for a password. While the passwords are
serialized separately from the player's profile, and therefore not stored in
plain text, I still recommend using a temporary or throw-away password. Encryption
was not part of our course curriculum, therefore, serializing the password String
was the best I was allowed to use.

--------------------------------------------------------------------------------

Section 3 - What can I do in JavaWorld?

After typing your password in twice, you'll be loaded into the Game. You'll start
at the HOMELOCATION, (100, 100). This game is setup on an (x, y) plane, where
the cardinal directions, north, east, south, west, will navigate you through
the world. You can type "commands" at anytime to see a list of available
commands. Each (x, y) point is a "room". Some interesting commands which may 
help you navigate JavaWorld are:

	- map
	- get <item>
	- drop <item>
	- wear <item>
	- equipment
	- inventory
	- who
	- chat

Map gives you an overhead map of your current area and the rooms you've
explored. As you explore more rooms, your map will flesh out. Also, as you
explore, you'll find items in rooms. You may pick them up and wear them. As
you wear the items, you'll notice on the equipment screen that your adventurer
will actually show the items being worn! A gold chest plate will make the
adventurers chest golden, a cyan halo will show cyan on top of the ASCII
Warrior's head as well.

Now for the social aspect; at the moment, this game lacks objectives. Within
the next few weeks, I intend to add in several objectives, such as games of
tag, hide and seek, find items, slay monsters, complete puzzles, and other
fun and interesting social adventures. Having said that, this game at the moment
is a chat room. Don't be disappointed!

Multiple people may login using the same process as you with Putty. They
may adventure around JavaWorld with you finding items, exploring the map, and
chatting with you. Meanwhile, you may customize your adventurer's title, set
of equipment, and name. If you'd like to save your profile, simply type save!

--------------------------------------------------------------------------------

Section 4 - Can I also use this? Yes!

Finally, the underlying structure of the game, in Java source files, is very
well documented and thoroughly explained. If you get the inkling to extend
this game for your own means, go for it! All I ask is that in the old MUD
fashion, you send a quick email over to me: nswarner2@gmail.com

Thank you and have fun inside JavaWorld!

--------------------------------------------------------------------------------

Section 5 - All the PostScript Notes

P.S. I'd love to hear feedback, advice, and even positive criticisms. Feel
free to email me with any of the above! You may also notice that the program
is written with a fairly narrow column-length. This is to ensure that the
code can be printed off without unnecessary line-wrapping. Also, this program
was developed in several IDEs and text-tools, such as NetBeans, JCreator,
vim, Notepad, and Notepad++. In all editing tools, tabs were set to 4 spaces.
Apologies if your IDE has a different tab size (or if the spacing is wonky!)

P.P.S. If you'd like a new map with a different room layout, when the game is
first starting, the console window will ask if you would like to generate a
new seed file. Choose yes; watch the output to ensure you don't receive a message
indicating that the game recommends restarting with a new seed file, and
have fun! (If that happens, stop the game, start it again, and generate another
seed file. I think I once had to do this twice in a row, but otherwise, it's
usually pretty good!)

P.P.P.S. My sincerest apologies for the lackluster room names and descriptions!
I'm not a fantastically creative person, I tend to prefer the logic of code.
Better descriptions and names will come in the next update!

--------------------------------------------------------------------------------

Appendix A - TODO

	1. Finish converting all of the documentation over to javadoc. 
		- Config.java and Coordinate.java have been converted
	2. Explore every branching statement and ensure that every case is
		accounted for. This includes ensuring that every call to an
		external method has been tested.
	3. Create a build script which results in a jar file.

Appendix B - Compiling, Building, and Running JavaWorld

	1. Pull JavaWorld from its git repository
		- Presuming 'git clone git@github.com:nswarner/JavaWorld'
		- The following 3 commands start in the top level directory
	2. Enter into the ./src directory and run the build.php script
	3. Enter into the ./classes directory and run the build_jar script
	4. Enter into the ./bin directory and run JavaWorld.jar

	Alternatively, after 1...
	2. Run the 'imlazy' script. It will do the rest of the work for you.


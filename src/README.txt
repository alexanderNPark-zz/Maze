To run this program is very simple:
The main class is RoomGenerator.
You have two options.
Running RoomGenerator with no arguments prompts you for a dimension number:

java RoomGenerator
Enter square maze dimension:


Entering in the number(n) generates a random maze n x n via GUI.
(recommended n is between 10 to 30, too big of a number and the GUI will be so big you can't see the full maze)
The maze will not only be drawn, but also a path to it will be shown if you enter a key into the console after the maze is drawn.
In the console, details will be printed via DFS and BFS node paths.

The second is running RoomGenerator with one argument which is the name of a file:
java RoomGenerator maze.txt

A test file was given and a maze will be GUI generated and the path will be printed also.
Details will also be released in console. Same rules apply like the no arguments part.

This maze guarantees that there is a path and only one opening in the exit door.
It also promises that all rooms can be accessed in path.
This means that every door is connected to the main path and the graph is a single connected component.

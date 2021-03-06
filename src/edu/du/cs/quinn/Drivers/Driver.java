/**
 * This is the main Driver class for the entire Music Project.
 * It takes in all user input from the Eclipse console and implements
 * all of the utility classes.
 * 
 * @author Quinn Hannah-White
 * @date 5/30/15
 */

package edu.du.cs.quinn.Drivers;

import java.util.*;
import edu.du.cs.quinn.Music.*;
import edu.du.cs.quinn.CounterPoint.*;

public class Driver {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scanIn = new Scanner(System.in);
		String command = null;
		LineInterpreter interpreter;
		Line mySopLine = null;
		Line myBasLine = null;
		Line myAltLine = null;
		SpeciesOne counterOne;
		SpeciesTwo counterTwo;
		SpeciesThree counterThree;
		SpeciesFour counterFour;
		Key myKey;
		HashMap<String,Integer> keyMap = new HashMap<String, Integer>();
		
		/**
		 * Creates the HashMap for creating the Key
		 */
		for(int n=0;n<12;n++) {
			switch(n) {
			case 0:
				keyMap.put("C", n+(12));
				keyMap.put("c", n+(12));
				break;
			case 1:
				keyMap.put("C#", n+(12));
				keyMap.put("c#", n+(12));
				break;
			case 2:
				keyMap.put("D", n+(12));
				keyMap.put("d", n+(12));
				break;
			case 3:
				keyMap.put("D#", n+(12));
				keyMap.put("d#", n+(12));
				break;
			case 4:
				keyMap.put("E", n+(12));
				keyMap.put("e", n+(12));
				break;
			case 5:
				keyMap.put("F", n+(12));
				keyMap.put("f", n+(12));
				break;
			case 6:
				keyMap.put("F#", n+(12));
				keyMap.put("f#", n+(12));
				break;
			case 7:
				keyMap.put("G", n+(12));
				keyMap.put("g", n+(12));
				break;
			case 8:
				keyMap.put("G#", n+(12));
				keyMap.put("g#", n+(12));
				break;
			case 9:
				keyMap.put("A", n+(12));
				keyMap.put("a", n+(12));
				break;
			case 10:
				keyMap.put("A#", n+(12));
				keyMap.put("a#", n+(12));
				break;
			case 11:
				keyMap.put("B", n+(12));
				keyMap.put("b", n+(12));
				break;
			}
		}
		
		System.out.println("What key will this song be in?: ");
		System.out.println("(e.g. A for A major, a for A minor, A# for A sharp major, a# for A sharp minor.)");
		command = scanIn.nextLine();
		String copyOfCommand = command.toUpperCase();
		
		if(command.equals(copyOfCommand)) {
			myKey = Key.getInstance(keyMap.get(command), true);
		}
		else {
			myKey = Key.getInstance(keyMap.get(command), false);
		}
		
		System.out.print("Which counterpoint are you running?(1,2,3,4): ");
		command = scanIn.nextLine();
		
		/**
		 * Calls the necessary functions for creating the counterpoint composition
		 */
		switch(command) {
		case "1":
			counterOne = new SpeciesOne(myKey);
			counterOne.assembleLines();
			mySopLine = counterOne.getSopranoLine();
			myAltLine = counterOne.getAltoLine();
			myBasLine = counterOne.getBassLine();
			interpreter = new LineInterpreter(mySopLine,myAltLine,myBasLine,"counterpoint.midi");
			interpreter.readLinesToMidi();
			interpreter.outputToMidi();
			break;
		case "2":
			counterTwo = new SpeciesTwo(myKey);
			counterTwo.assembleLines();
			mySopLine = counterTwo.getSopranoLine();
			//myAltLine = counterTwo.getAltoLine();
			//myBasLine = counterTwo.getBassLine();
			interpreter = new LineInterpreter(mySopLine,myAltLine,myBasLine,"counterpoint.midi");
			interpreter.readLinesToMidi();
			interpreter.outputToMidi();
			break;
		case "3":
			counterThree = new SpeciesThree(myKey);
			counterThree.assembleLines();
			mySopLine = counterThree.getSopranoLine();
			//myAltLine = counterThree.getAltoLine();
			//myBasLine = counterThree.getBassLine();
			interpreter = new LineInterpreter(mySopLine,myAltLine,myBasLine,"counterpoint.midi");
			interpreter.readLinesToMidi();
			interpreter.outputToMidi();
			break;
		case "4":
			counterFour = new SpeciesFour(myKey);
			counterFour.assembleLines();
			mySopLine = counterFour.getSopranoLine();
			//myAltLine = counterFour.getAltoLine();
			//myBasLine = counterFour.getBassLine();
			interpreter = new LineInterpreter(mySopLine,myAltLine,myBasLine,"counterpoint.midi");
			interpreter.readLinesToMidi();
			interpreter.outputToMidi();
			break;
		}
		
		System.out.println(mySopLine);
		System.out.println(myAltLine);
		System.out.println(myBasLine);
		System.out.println("Your song has been created in the file 'counterpoint.midi'");
	}

}


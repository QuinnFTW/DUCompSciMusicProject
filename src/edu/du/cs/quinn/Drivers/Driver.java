package edu.du.cs.quinn.Drivers;

import java.util.*;
import edu.du.cs.quinn.Music.*;
import edu.du.cs.quinn.CounterPoint.*;

public class Driver {

	public static void main(String[] args) {
		Scanner scanIn = new Scanner(System.in);
		String command = null;
		Line myLine = new Line();
		Note myNote = new Note(64, 0);
		Key myKey = Key.getInstance(60, false);
		SpeciesOne counter = new SpeciesOne(myKey);
		
		counter.assembleLines();
		
		myLine = counter.getSopranoLine();
		
		LineInterpreter interpreter = new LineInterpreter(myLine,null,null,"counterpoint.midi");
		interpreter.readLinesToMidi();
		interpreter.outputToMidi();
		
		/*System.out.print("Which counterpoint are you running?(1,2,3,4): ");
		command = scanIn.nextLine();
		
		switch(command) {
		case "1":
		case "2":
		case "3":
		case "4":
		default:
		}	*/
		
		/*System.out.println("What key will this song be in?: ");
		System.out.println("(e.g. A for A major, a for A minor, A# for A sharp major, a# for A sharp minor.)");
		command = scanIn.nextLine();*/
		
		/*System.out.println("Your song has been created in the file 'counterpoint.midi'");*/
	}

}

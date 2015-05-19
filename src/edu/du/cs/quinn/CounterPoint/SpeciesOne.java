package edu.du.cs.quinn.CounterPoint;

import edu.du.cs.quinn.Music.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.HashSet;

public class SpeciesOne implements CounterPoint {
	private Note myNote;
	private Key myKey;
	private Random rand;
	private Line sopranoLine;
	private Line altoLine;
	private Line bassLine;
	private Stack<ArrayList<Note>> theSopStack;
	private Stack<ArrayList<Note>> theAltStakck;
	private Stack<ArrayList<Note>> theBasStack;
	
	public SpeciesOne(Key myKey) {
		this.myKey = myKey;
		rand = new Random();
		sopranoLine = new Line();
		altoLine = new Line();
		bassLine = new Line();
	}
	
	public void assembleLines() {
		theSopStack = new Stack<ArrayList<Note>>();
		ArrayList<Note> theList = new ArrayList<Note>();
		myNote = new Note(myKey.getScalePitch(0),0);
		theList.add(myNote);
		theSopStack.push(theList);
		int decide = rand.nextInt(5);
		switch(decide) {
		case 0:
		case 3:
			decide = 2;
			break;
		case 1:
		case 4:
			decide = 4;
			break;
		case 2:
			decide = 7;
			break;
		}
		
		for (int i=1; i<=decide; i++) {
			theList = new ArrayList<Note>();
			myNote = new Note(myKey.getScalePitch(i),i);
			theList.add(myNote);
			theSopStack.push(theList);
		}
		
		while(!theSopStack.isEmpty()) {
			decide = rand.nextInt(3);
			if(decide==0) {
				int n = rand.nextInt(3);
				switch(n) {
				case 0:
					myNote = new Note(myKey.getScalePitch(0),0);
					sopranoLine.addNote(myNote);
					break;
				case 1:
					myNote = new Note(myKey.getScalePitch(2),2);
					sopranoLine.addNote(myNote);
					break;
				case 2:
					myNote = new Note(myKey.getScalePitch(4),4);
					sopranoLine.addNote(myNote);
					break;
				}
				for ( Note nextPossibility : theSopStack.peek())
				{
					if (nextPossibility.equals(myNote))
					{
						theSopStack.pop();
						break;
					}
				}
			}
			else {
				theList = theSopStack.pop();
				int n = rand.nextInt(theList.size());
				sopranoLine.addNote(theList.get(n));
			}
		}
	}

	public Line getSopranoLine() {
		return sopranoLine;
	}

	public Line getAltoLine() {
		return altoLine;
	}

	public Line getBassLine() {
		return bassLine;
	}

}

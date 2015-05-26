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
		Note[] notes = { myKey.getScalarNote(2), myKey.getScalarNote(1), myKey.getScalarNote(0) };
		sopranoLine = new Line(50, 80, notes);
		/*altoLine = new Line();
		bassLine = new Line();
		*/
	}
	
	public void assembleLines() {
		for (int i = 0; i < 3; i++)
		{
			sopranoLine.addNote(true);
		}
		
		while(!sopranoLine.isFinished())
		{
			if (sopranoLine.hasNextNote() && sopranoLine.size() < 10)
			{
				sopranoLine.addNote(false);
			}
			else
			{
				sopranoLine.removeEndNote();
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
	
	private boolean isLocallyGood()
	{
		
		return true;
	}

}

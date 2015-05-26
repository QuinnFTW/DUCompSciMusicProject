package edu.du.cs.quinn.CounterPoint;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import edu.du.cs.quinn.Music.Key;
import edu.du.cs.quinn.Music.Line;
import edu.du.cs.quinn.Music.Note;

public class SpeciesFour implements CounterPoint {
	private Note myNote;
	private Key myKey;
	private Random rand;
	private Line sopranoLine;
	private Line altoLine;
	private Line bassLine;
	private Stack<ArrayList<Note>> theSopStack;
	private Stack<ArrayList<Note>> theAltStakck;
	private Stack<ArrayList<Note>> theBasStack;

	public SpeciesFour(Key myKey) {
		this.myKey = myKey;
		rand = new Random();
	}
	
	@Override
	public void assembleLines() {
		
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

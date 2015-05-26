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
	
	public SpeciesOne(Key myKey) {
		this.myKey = myKey;
		rand = new Random();
		int minSoprano = 50;
		int maxSoprano = 80;
		HashSet<Note> spanNotes = myKey.getSpanNotes(minSoprano, maxSoprano);
		ArrayList<Note> listOfNotes = null;
		while(!spanNotes.isEmpty())
		{
			listOfNotes = new ArrayList<Note>();
			Note firstNote = Line.selectRandom(spanNotes);
			listOfNotes.add(firstNote);
			int i;
			for(i = firstNote.getDegree() - 1; i % 7 != 0; i--)
			{
				listOfNotes.add(myKey.getScalarNote(i));
			}
			Note finalNote = myKey.getScalarNote(i);
			if (!(finalNote.getPitch() < minSoprano))
			{
				listOfNotes.add(finalNote);
				break;
			}
			
		}
		
		if (listOfNotes.isEmpty())
		{
			System.err.println("something went wrong initializing the soprano line");
			System.exit(2);
		}
		
		Note[] actualNotes = new Note[listOfNotes.size()];
		
		for(int i = 0; i < listOfNotes.size(); i++)
		{
			actualNotes[i] = listOfNotes.get(i);
		}
		
		sopranoLine = new Line(minSoprano, maxSoprano, actualNotes);
		/*altoLine = new Line();
		bassLine = new Line();
		*/
	}
	
	public void assembleLines() {
		System.out.println("started");
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
		System.out.println("FINISHED");
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

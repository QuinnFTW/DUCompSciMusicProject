package edu.du.cs.quinn.CounterPoint;

import edu.du.cs.quinn.Music.*;

import java.io.IOException;
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
	private int maxLength;
	private final int numberOfLines = 2;
	
	public SpeciesOne(Key myKey) {
		this.myKey = myKey;
		rand = new Random();
		int minSoprano = 50;
		int maxSoprano = 80;
		int minBass = 30;
		int maxBass = 50;
		maxLength = 6;
		
		// creating the soprano line
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

		
		// creating the bass line
		Note firstNote = null;
		int i;
		if (myKey.getScalePitch(0) < minBass)
		{
			i = 7;
			while (firstNote == null)
			{
				if (myKey.getScalePitch(i) >= minBass)
				{
					firstNote = myKey.getScalarNote(i);
				}
				i += 7;
			}
		}
		else if (myKey.getScalePitch(0) > maxBass)
		{
			i = -7;
			while (firstNote == null)
			{
				if (myKey.getScalePitch(i) <= maxBass)
				{
					firstNote = myKey.getScalarNote(i);
				}
				i -= 7;
			}
		}
		else
		{
			firstNote = myKey.getScalarNote(0);
			i = 0;
		}
		
		HashSet<Note> choices = new HashSet<Note>();
		Note noteToAdd = myKey.getScalarNote(i + 4);
		if (noteToAdd.getPitch() >= minBass && noteToAdd.getPitch() <= maxBass)
		{
			choices.add(noteToAdd);
		}
		noteToAdd = myKey.getScalarNote(i - 3);
		if (noteToAdd.getPitch() >= minBass && noteToAdd.getPitch() <= maxBass)
		{
			choices.add(noteToAdd);
		}
		Note middleNote = Line.selectRandom(choices);
		
		choices = new HashSet<Note>();
		choices.add(firstNote);
		noteToAdd = myKey.getScalarNote(i + 7);
		if (noteToAdd.getPitch() >= minBass && noteToAdd.getPitch() <= maxBass)
		{
			choices.add(noteToAdd);
		}
		noteToAdd = myKey.getScalarNote(i - 7);
		if (noteToAdd.getPitch() >= minBass && noteToAdd.getPitch() <= maxBass)
		{
			choices.add(noteToAdd);
		}
		Note endNote = Line.selectRandom(choices);
		
		if (firstNote == null || middleNote == null || endNote == null)
		{
			System.err.println("something has gone wrong creating the bass line");
			System.exit(2);
		}
		Note[] bassNotes = {firstNote, middleNote, endNote};
		bassLine = new Line(minBass, maxBass, bassNotes);
		
		/*altoLine = new Line();
		*/
	}
	
	public void assembleLines() {
		System.out.println("started");
		int index = 0;
		while(!sopranoLine.isFinished() || !bassLine.isFinished())
		{
			switch(index % numberOfLines)
			{
			case 0:
				if (bassLine.hasNextNote() && index < maxLength * numberOfLines)
				{
					bassLine.addNote(true);
					index++;
				}
				else
				{
					sopranoLine.removeEndNote();
					bassLine.resetPossibilities();
					index--;
				}
				break;
			case 1:
				if (sopranoLine.hasNextNote() && index < maxLength * numberOfLines)
				{
					sopranoLine.addNote(true);
					if (isLocallyGood(index / numberOfLines))
					{
						index++;
					}
					else
					{
						sopranoLine.removeEndNote();
					}
				}
				else
				{
					bassLine.removeEndNote();
					sopranoLine.resetPossibilities();
					index--;
				}
				break;
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
	
	private boolean isLocallyGood(int index)
	{
		// first check for bass & soprano
		Note lowerNote = bassLine.getNote(index);
		Note upperNote = sopranoLine.getNote(index);
		int intervalType = lowerNote.intervalType(upperNote);
		int distanceBetween = upperNote.getPitch() - lowerNote.getPitch();
		boolean areConsonant = lowerNote.isIntervalConsonant(upperNote);
		
		// dissonance is bad
		
		if (!areConsonant)
		{
			return false;
		}
		
		// crossing is bad
		if (distanceBetween < 0 )
		{
			return false;
		}
		
		// fourths are bad
		if (intervalType == 4)
		{
			return false;
		}
		
		// if the bass moves by a fourth, the bass needs a dissonant contiguous note in that timespan
		if (index > 0)
		{
			Note previousLower = bassLine.getNote(index - 1);
			Note previousUpper = sopranoLine.getNote(index - 1);
			
			boolean similarMotion = ( previousLower.getDegree() < lowerNote.getDegree()
							&& previousUpper.getDegree() < upperNote.getDegree() )
					|| ( previousLower.getDegree() > lowerNote.getDegree()
							&& previousUpper.getDegree() > upperNote.getDegree() );

			if (intervalType == 5 || intervalType == 8 || intervalType == 1)
			{
				// parallel fifths or octaves is bad
				int earlierDistanceBetween = previousUpper.getPitch() - previousLower.getPitch();
				if (earlierDistanceBetween == distanceBetween)
				{
					return false;
				}

				// unless it is the last note, similar motion to a fifth or octave is bad
				if (similarMotion && !isFinished())
				{
					return false;
				}
			}

			// similar motion *from* unison is also bad
			if (intervalType == 1 && 
					( (previousUpper.getDegree() > upperNote.getDegree()
							&& previousLower.getDegree() > lowerNote.getDegree() ) 
						|| (previousUpper.getDegree() < upperNote.getDegree()
								&& previousLower.getDegree() < lowerNote.getDegree())))
			{
				return false;
			}

			boolean lowerUpperContiguousConsonance = !previousLower.isIntervalConsonant(upperNote);
			boolean upperLowerContiguousConsonance = !lowerNote.isIntervalConsonant(previousUpper);
			int bassJump = lowerNote.getDegree() - previousLower.getDegree();
			if (bassJump == 3 || bassJump == -3)
			{
				boolean contiguousDissonance = lowerUpperContiguousConsonance
						|| upperLowerContiguousConsonance;
				if (!contiguousDissonance)
				{
					return false;
				}
			}
			
			// cross relations (unequal notes of the same degree being contiguous) are bad
			int lowerUpperContiguousInterval = previousLower.intervalType(upperNote);
			int upperLowerContiguousInterval = lowerNote.intervalType(previousUpper);
			if (lowerUpperContiguousInterval == 1 || lowerUpperContiguousInterval == 8)
			{
				if (!lowerUpperContiguousConsonance)
				{
					return false;
				}
			}
			if (upperLowerContiguousInterval == 1 || upperLowerContiguousInterval == 8)
			{
				if (!upperLowerContiguousConsonance)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isFinished()
	{
		return sopranoLine.isFinished()
				//&& altoLine.isFinished()
				&& bassLine.isFinished();
	}

}

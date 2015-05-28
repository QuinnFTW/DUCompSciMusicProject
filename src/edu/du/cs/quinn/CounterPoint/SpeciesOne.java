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
	private int minLength;
	private int maxLength;
	private final int numberOfLines = 3;
	
	public SpeciesOne(Key myKey) {
		this.myKey = myKey;
		rand = new Random();
		int minSoprano = 50;
		int maxSoprano = 80;
		int minAlto = 40;
		int maxAlto = 60;
		int minBass = 30;
		int maxBass = 50;
		minLength = 5;
		maxLength = 7;
		
		// creating the soprano line
		sopranoLine = new Line(minSoprano, maxSoprano);

		// creating the alto line
		altoLine = new Line(minAlto, maxAlto);
		
		// creating the bass line
		bassLine = new Line(minBass, maxBass);
		
	}
	
	public void assembleLines() {
		System.out.println("started");
		int index = 0;
		while(!isFinished())
		{
			switch(index % numberOfLines)
			{
			case 0:
				if (bassLine.hasNextNote() && index < maxLength * numberOfLines - 2)
				{
					//System.out.println("add base");
					bassLine.addNote(false);
					index++;
				}
				else
				{
					//System.out.println("bass invalid; remove soprano");
					sopranoLine.removeEndNote();
					bassLine.resetPossibilities();
					index--;
				}
				break;
			case 1:
				if (altoLine.hasNextNote() && index < maxLength * numberOfLines)
				{
					//System.out.println("add alto");
					altoLine.addNote(false);
					if (isGoodBassAlto(index / numberOfLines))
					{
						index++;
					}
					else
					{
						//System.out.println("oops; remove alto");
						altoLine.removeEndNote();
					}
				}
				else
				{
					//System.out.println("alto invalid; remove bass");
					bassLine.removeEndNote();
					altoLine.resetPossibilities();
					index--;
				}
				break;
			case 2:
				if (sopranoLine.hasNextNote() && index < maxLength * numberOfLines)
				{
					//System.out.println("add soprano");
					sopranoLine.addNote(false);
					if (isGoodBassSoprano(index / numberOfLines)
							&& isGoodAltoSoprano(index / numberOfLines)
							&& bassFourthCheck(index / numberOfLines))
					{
						index++;
					}
					else
					{
						//System.out.println("oops; remove soprano");
						sopranoLine.removeEndNote();
					}
				}
				else
				{
					//System.out.println("soprano invalid; remove alto");
					altoLine.removeEndNote();
					sopranoLine.resetPossibilities();
					index--;
				}
				break;
			}
		}
		System.out.println("FINISHED");
	}
	
	//checks for jumps of a fourth in the bass
	//and if there is one and there isn't a contiguous dissonance with one of the notes as the bass, 
	//return false
	private boolean bassFourthCheck(int index)
	{
		if (index == 0)
		{
			return true;
		}
		Note bassFirst = bassLine.getNote(index - 1);
		Note bassSecond = bassLine.getNote(index);

		int bassJump = bassSecond.getDegree() - bassFirst.getDegree();
		if (bassJump == 3 || bassJump == -3)
		{
			boolean firstContiguousConsonant = 
					bassFirst.isIntervalConsonant(sopranoLine.getNote(index))
					&& bassFirst.isIntervalConsonant(altoLine.getNote(index));
			boolean secondContiguousConsonant =
					bassSecond.isIntervalConsonant(sopranoLine.getNote(index - 1))
					&& bassSecond.isIntervalConsonant(altoLine.getNote(index - 1));
			if (firstContiguousConsonant && secondContiguousConsonant)
			{
				return false;
			}
		}
		return true;
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
	
	private boolean isGoodAltoSoprano(int index)
	{
		Note lowerNote = altoLine.getNote(altoLine.size() - 1);
		Note upperNote = sopranoLine.getNote(sopranoLine.size() - 1);
		int intervalType = lowerNote.intervalType(upperNote);
		int distanceBetween = upperNote.getPitch() - lowerNote.getPitch();
		boolean areConsonant = lowerNote.isIntervalConsonant(upperNote);
		
		// dissonance is bad, unless it is an augmented fourth or diminished fifth in the upper two lines
		
		if (!areConsonant && (distanceBetween % 12) != 6)
		{
			return false;
		}
		
		// crossing is bad
		if (distanceBetween < 0 )
		{
			return false;
		}
		
		// fourths are alright in the upper two lines
		
		
		if (index > 0)
		{
			Note previousLower = altoLine.getNote(index - 1);
			Note previousUpper = sopranoLine.getNote(index - 1);
			
			//similar motion to a fifth or octave is bad
			boolean similarMotion = ( previousLower.getDegree() < lowerNote.getDegree()
							&& previousUpper.getDegree() < upperNote.getDegree() )
					|| ( previousLower.getDegree() > lowerNote.getDegree()
							&& previousUpper.getDegree() > upperNote.getDegree() );

			if ((intervalType == 5 || intervalType == 8 || intervalType == 1) && similarMotion)
			{
				return false;
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
	
	private boolean isGoodBassAlto(int index)
	{
		Note lowerNote = bassLine.getNote(bassLine.size() - 1);
		Note upperNote = altoLine.getNote(altoLine.size() - 1);
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
		
		if (index > 0)
		{
			Note previousLower = bassLine.getNote(index - 1);
			Note previousUpper = altoLine.getNote(index - 1);
			
			//similar motion to a fifth or octave is bad
			boolean similarMotion = ( previousLower.getDegree() < lowerNote.getDegree()
							&& previousUpper.getDegree() < upperNote.getDegree() )
					|| ( previousLower.getDegree() > lowerNote.getDegree()
							&& previousUpper.getDegree() > upperNote.getDegree() );

			if ((intervalType == 5 || intervalType == 8 || intervalType == 1) && similarMotion)
			{
				return false;
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
	
	private boolean isGoodBassSoprano(int index)
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
		
		if (index > 0)
		{
			Note previousLower = bassLine.getNote(index - 1);
			Note previousUpper = sopranoLine.getNote(index - 1);
			
			//similar motion to a fifth or octave is bad
			boolean similarMotion = ( previousLower.getDegree() < lowerNote.getDegree()
							&& previousUpper.getDegree() < upperNote.getDegree() )
					|| ( previousLower.getDegree() > lowerNote.getDegree()
							&& previousUpper.getDegree() > upperNote.getDegree() );

			if ((intervalType == 5 || intervalType == 8 || intervalType == 1) && similarMotion)
			{
				return false;
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
		if (sopranoLine.size() <= minLength)
		{
			return false;
		}
		int interval = sopranoLine.getNote(sopranoLine.size() - 1).intervalType(Key.getInstance().getScalarNote(0));
		boolean upperIsTonic = interval == 1 || interval == 8;
		interval = bassLine.getNote(bassLine.size() - 1).intervalType(Key.getInstance().getScalarNote(0));
		boolean lowerIsTonic = interval == 1 || interval == 8;
		return upperIsTonic && lowerIsTonic
				&& sopranoLine.isFinished()
				&& altoLine.isFinished()
				&& bassLine.isFinished()
				&& altoLine.size() == bassLine.size()
				&& altoLine.size() == sopranoLine.size();
	}

}

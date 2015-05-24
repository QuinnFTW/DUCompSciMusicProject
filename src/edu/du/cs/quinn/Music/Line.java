package edu.du.cs.quinn.Music;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class Line {
	private ArrayList<Note> myScore;
	private Stack<HashSet<Note>> possibleNotes;
	private Stack<Integer> locationOfLastIncomplete;
	private Stack<HashSet<Note>> requiredNext;
	private Stack<Stack<HashSet<Note>>> fulfillments;
	private Stack<Stack<Integer>> fulfillmentLocations;
	private Note finalNote;
	private int spanLength;
	
	public Line(int minPitch, int maxPitch, Note[] required) {
		myScore = new ArrayList<Note>();
		possibleNotes = new Stack<HashSet<Note>>();
		locationOfLastIncomplete = new Stack<Integer>();
		requiredNext = new Stack<HashSet<Note>>();
		fulfillments = new Stack<Stack<HashSet<Note>>>();
		fulfillmentLocations = new Stack<Stack<Integer>>();
		finalNote = required[required.length - 1];
		for(Note note : required)
		{
			HashSet<Note> set = new HashSet<Note>();
			set.add(note);
			requiredNext.add(set);
		}
		spanLength = required.length;
		
		possibleNotes.push(Key.getInstance().getSpanNotes(minPitch, maxPitch));
	}
	
	public Note addNote(boolean shouldExtend) {
		HashSet<Note> possibilities = possibleNotes.peek();
		if(possibilities.isEmpty())
		{
			System.err.println("Error: could not add a note");
			System.exit(1);
		}
		
		if (!shouldExtend)
		{
			// check to see if it is possible to resolve the latest dependent note
			HashSet<Note> possibleCompletions = requiredNext.peek();
			if (!possibleCompletions.isEmpty())
			{
				Note n = selectRandom(possibleCompletions);
				addNote(n);
				return n;
			}
		}
		Note aNote = selectRandom(possibilities);
		addNote(aNote);
		return aNote;
	}
	
	private void addNote(Note aNote)
	{
		Stack<HashSet<Note>> fulfilled = new Stack<HashSet<Note>>();
		Stack<Integer> fulfilledLocations = new Stack<Integer>();
		boolean keepGoing = true;
		while(keepGoing)
		{
			keepGoing = false;
			for(Note n : requiredNext.peek())
			{
				if (aNote.equals(n))
				{
					fulfilled.add(requiredNext.pop());
					fulfilledLocations.add(locationOfLastIncomplete.pop());
					keepGoing = true;
					break;
				}
			}
		}
		fulfillments.add(fulfilled);
		fulfillmentLocations.add(fulfilledLocations);
		
		boolean spanNote = requiredNext.size() < spanLength;
		if (spanNote)
		{
			spanLength = requiredNext.size();
		}
		else
		{
			HashSet<Note> resolution = getResolution(aNote);
			if(!resolution.isEmpty())
			{
				requiredNext.push(resolution);
				locationOfLastIncomplete.push(myScore.size());
			}
		}
		
		HashSet<Note> activeNotes = new HashSet<Note>();
		HashSet<Note> possibilities = new HashSet<Note>();
		for (Note n : myScore.subList(locationOfLastIncomplete.peek(), myScore.size()))
		{
			activeNotes.add(n);
		}
		for (Note n : activeNotes)
		{
			for (Note p : possibleDependents(n))
			{
				int distance = aNote.getPitch() - p.getPitch();
				if ((distance < 3 && distance > -3) || aNote.isIntervalConsonant(p))
				{
					possibilities.add(p);
				}
			}
		}
		possibleNotes.push(possibilities);
		myScore.add(aNote);
	}
	
	public void removeEndNote() {
		possibleNotes.pop();
		possibleNotes.peek().remove(myScore.get(myScore.size() - 1));
		while(!fulfillments.peek().isEmpty())
		{
			requiredNext.add(fulfillments.peek().pop());
		}
		while(!fulfillmentLocations.peek().isEmpty())
		{
			locationOfLastIncomplete.add(fulfillmentLocations.peek().pop());
		}
		myScore.remove(myScore.size() - 1);
	}
	
	public int getSize() {
		return myScore.size();
	}
	
	public Note getNote(int index) {
		return myScore.get(index);
	}
	
	private HashSet<Note> getResolution(Note aNote)
	{
		HashSet<Note> resolution = new HashSet<Note>();
		int degree = aNote.getDegree();
		Key theKey = Key.getInstance();
		int expectedPitch = theKey.getScalePitch(degree);
		int typeOfNote = theKey.getTonic().intervalType(aNote);
		if (aNote.getPitch() == expectedPitch)
		{
			Note lowerNeighbor = theKey.getScalarNote(degree - 1);
			Note upperNeighbor = theKey.getScalarNote(degree + 1);
			switch(typeOfNote)
			{
			case 2:
			case 4:
				resolution.add(lowerNeighbor);
				resolution.add(upperNeighbor);
				break;
			case 6:
				if (theKey.isMajor())
				{
					for(Note n : myScore.subList(locationOfLastIncomplete.peek(), getSize()))
					{
						if (n.equals(lowerNeighbor))
						{
							resolution.add(upperNeighbor);
							break;
						}
					}
				}
				resolution.add(lowerNeighbor);
				break;
			case 7:
				if (theKey.isMajor())
				{
					for (Note n : myScore.subList(locationOfLastIncomplete.peek(), getSize()))
					{
						if (n.equals(upperNeighbor))
						{
							resolution.add(lowerNeighbor);
							break;
						}
					}
					resolution.add(upperNeighbor);
				}
				else
				{
					boolean hasUpper = false;
					boolean hasLower = false;
					for (Note n : myScore.subList(locationOfLastIncomplete.peek(), getSize()))
					{
						if ((!hasUpper) && n.equals(upperNeighbor))
						{
							resolution.add(lowerNeighbor);
							hasUpper = true;
						}
						if ((!hasLower) && n.equals(lowerNeighbor))
						{
							resolution.add(upperNeighbor);
							hasLower = true;
						}
						if (hasUpper && hasLower)
						{
							break;
						}
					}
				}
				break;
			}
		}
		else
		{
			if (typeOfNote == 7)
			{
				resolution.add(theKey.getScalarNote(degree + 1));
			}
			else
			{
				Note upperNeighbor = new Note(theKey.getScalePitch(degree + 1) + 1, degree + 1);
				Note lowerNeighbor = theKey.getScalarNote(degree - 1);
				boolean hasUpper = false;
				boolean hasLower = false;
				for (Note n : myScore.subList(locationOfLastIncomplete.peek(), getSize()))
				{
					if ((!hasUpper) && n.equals(upperNeighbor))
					{
						resolution.add(lowerNeighbor);
						hasUpper = true;
					}
					if ((!hasLower) && n.equals(lowerNeighbor))
					{
						resolution.add(upperNeighbor);
						hasLower = true;
					}
					if (hasUpper && hasLower)
					{
						break;
					}
				}
			}
		}
		return resolution;
	}
	
	public boolean hasNextNote()
	{
		return !possibleNotes.peek().isEmpty();
	}
	
	public boolean isFinished()
	{
		return myScore.get(myScore.size() - 1).equals(finalNote) && requiredNext.isEmpty();
	}
	
	private static HashSet<Note> possibleDependents(Note aNote)
	{
		HashSet<Note> possibilities = new HashSet<Note>();
		Key theKey = Key.getInstance();
		int typeOfNote = theKey.getTonic().intervalType(aNote);
		int degree = aNote.getDegree();
		Note lowerNeighbor = theKey.getScalarNote(degree - 1);
		Note upperNeighbor = theKey.getScalarNote(degree + 1);
		switch(typeOfNote)
		{
		case 8:
		case 1:
			possibilities.add(new Note(aNote.getPitch() - 1, degree - 1));
		case 3:
			possibilities.add(lowerNeighbor);
			possibilities.add(upperNeighbor);
			break;
		case 5:
			possibilities.add(upperNeighbor);
			possibilities.add(lowerNeighbor);
		case 6:
			possibilities.add(new Note(aNote.getPitch() + 2, degree + 1));
			break;
		case 7:
			possibilities.add(new Note(aNote.getPitch() - 2, degree - 1));
			break;
		}
		
		return possibilities;
	}
	
	public void removeNextNote(Note aNote)
	{
		possibleNotes.peek().remove(aNote);
	}
	
	public static Note selectRandom(HashSet<Note> set)
	{
		int choose = (int)(Math.random() * set.size());
		int i = 0;
		for(Note n : set)
		{
			if (i == choose)
			{
				return n;
			}
			i++;
		}
		return null;
	}
	
	public int size()
	{
		return myScore.size();
	}

}

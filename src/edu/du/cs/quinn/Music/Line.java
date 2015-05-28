package edu.du.cs.quinn.Music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class Line {
	private ArrayList<Note> myScore;
	private ArrayList<HashMap<Note, Integer>> possibleNotes;
	private Stack<Integer> locationOfLastIncomplete;
	private Stack<HashSet<Note>> requiredNext;
	private ArrayList<Stack<HashSet<Note>>> fulfillments;
	private ArrayList<Stack<Integer>> fulfillmentLocations;
	private int minPitch;
	private int maxPitch;
	private static final Random rand = new Random();
	
	public Line(int minPitch, int maxPitch) {
		this.minPitch = minPitch;
		this.maxPitch = maxPitch;
		myScore = new ArrayList<Note>();
		possibleNotes = new ArrayList<HashMap<Note, Integer>>();
		locationOfLastIncomplete = new Stack<Integer>();
		fulfillments = new ArrayList<Stack<HashSet<Note>>>();
		fulfillmentLocations = new ArrayList<Stack<Integer>>();
		requiredNext = new Stack<HashSet<Note>>();
		
		HashMap<Note,Integer> allowableNotes = new HashMap<Note,Integer>();
		for(Note n : Key.getInstance().getSpanNotes(minPitch, maxPitch))
		{
			allowableNotes.put(n, 0);
		}
		possibleNotes.add(allowableNotes);
	}
	
	public Note addNote(boolean tryToFinish) {
		
		Set<Note> possibilities = possibleNotes.get(possibleNotes.size() - 1).keySet();
		if(possibilities.isEmpty())
		{
			System.err.println("Error: could not add a note");
			System.exit(1);
		}
		
		if (tryToFinish)
		{
			// check to see if it is possible to resolve the latest dependent note
			if (!requiredNext.isEmpty())
			{
				HashSet<Note> possibleCompletions = new HashSet<Note>();
				for(Note n : requiredNext.peek())
				{
					if(possibilities.contains(n))
					{
						possibleCompletions.add(n);
					}
				}
				if (!possibleCompletions.isEmpty())
				{
					// if so, adds a randomly selected resolution
					Note n = selectRandom(possibleCompletions);
					addNote(n);
					return n;
				}
			}
		}
		// randomly selects a note to add
		Note aNote = selectRandom(possibilities);
		addNote(aNote);
		return aNote;
	}
	
	// does the meat of the work adding notes
	private void addNote(Note aNote)
	{
		// if this note resolves anything, tie the things it resolves to its location
		Stack<HashSet<Note>> fulfilled = new Stack<HashSet<Note>>();
		Stack<Integer> fulfilledLocations = new Stack<Integer>();
		fulfillments.add(fulfilled);
		fulfillmentLocations.add(fulfilledLocations);
		
		boolean keepGoing = !requiredNext.isEmpty();
		while(keepGoing)
		{
			keepGoing = false;
			for(Note n : requiredNext.peek())
			{
				if (aNote.equals(n))
				{
					fulfilledLocations.push(locationOfLastIncomplete.pop());
					fulfilled.push(requiredNext.pop());
					keepGoing = !requiredNext.isEmpty();
				}
			}
		}
		
		// actually add the note to the line
		myScore.add(aNote);
		
		// if the note is dependent on others, push its possible resolutions to the "required" stack
		// 		and push its location to the location stack
		// 		and if it is dependent on a note that precedes other resolutions,
		// 		add those resolutions to the stack as well.
		HashSet<Note> resolution = getResolution(aNote);
		if (!resolution.isEmpty())
		{
			int dependLocation = possibleNotes.get(possibleNotes.size() - 1).get(aNote);
			for (int i = size() - 2; i >= dependLocation; i--)
			{
				Stack<Integer> currentLocations = fulfillmentLocations.get(i);
				Stack<HashSet<Note>> currentRequirements = fulfillments.get(i);
				for (int j = 0; j < currentLocations.size(); j++)
				{
					if (currentLocations.get(j) <= dependLocation)
					{
						locationOfLastIncomplete.push(currentLocations.remove(j));
						requiredNext.push(currentRequirements.remove(j));
						j--;
					}
				}
			}
			requiredNext.push(resolution);
			locationOfLastIncomplete.push(size() - 1);
		}
			
		setPossibilities();
	}
	
	public int depth()
	{
		return requiredNext.size();
	}
	
	public Note getNote(int index) {
		return myScore.get(index);
	}
	
	private HashSet<Note> getResolution(Note aNote)
	{
		HashSet<Note> resolution = new HashSet<Note>();
		int degree = aNote.getDegree();
		Key theKey = Key.getInstance();
		int pitch = aNote.getPitch();
		int expectedPitch = theKey.getScalePitch(degree);
		int typeOfNote = theKey.getTonic().intervalType(aNote);
		if (pitch == expectedPitch)
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
					for(Note n : myScore.subList(locationOfLastIncomplete.peek(), size()))
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
					for (Note n : myScore.subList(locationOfLastIncomplete.peek(), size()))
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
					for (Note n : myScore.subList(lastBlocker(aNote), size()))
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
			else if (typeOfNote == 6)
			{
				Note upperNeighbor = new Note(pitch + 2, degree + 1);
				Note lowerNeighbor = theKey.getScalarNote(degree - 1);
				boolean hasUpper = false;
				boolean hasLower = false;
				for (Note n : myScore.subList(lastBlocker(aNote), size() - 1))
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
		return !possibleNotes.get(possibleNotes.size() - 1).isEmpty();
	}
	
	public boolean isFinished()
	{
		return !myScore.isEmpty() && requiredNext.isEmpty();
	}
	
	private int lastBlocker(Note aNote)
	{
		int dependent = 0;
		if (aNote != null)
		{
			try{
				dependent = possibleNotes.get(size()).get(aNote);
			} catch(Exception e)
			{}
		}
		if (locationOfLastIncomplete.isEmpty())
		{
			return 0;
		}
		return Math.max(locationOfLastIncomplete.peek(), dependent);
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
	
	public void removeEndNote() {
		if (size() == 0)
		{
			System.err.println("Could not remove another note. It probably became impossible to finish");
			System.exit(3);
		}
		
		Note aNote = myScore.get(size() - 1);
		
		possibleNotes.remove(possibleNotes.size() - 1);
		possibleNotes.get(possibleNotes.size() - 1).remove(aNote);
		if(!locationOfLastIncomplete.isEmpty() && locationOfLastIncomplete.peek() >= size() - 1)
		{
			locationOfLastIncomplete.pop();
			requiredNext.pop();
		}
		Stack<HashSet<Note>> f = fulfillments.get(fulfillments.size() - 1);
		while(!f.isEmpty())
		{
			requiredNext.push(f.pop());
		}
		Stack<Integer> l = fulfillmentLocations.get(fulfillmentLocations.size() - 1);
		while(!l.isEmpty())
		{
			locationOfLastIncomplete.push(l.pop());
		}
		myScore.remove(size() - 1);
	}
	
	public void removeNextNote(Note aNote)
	{
		possibleNotes.get(possibleNotes.size() - 1).remove(aNote);
	}
	
	public void resetPossibilities()
	{
		possibleNotes.remove(possibleNotes.size() - 1);
		if (size() == 0)
		{
			HashMap<Note,Integer> allowableNotes = new HashMap<Note,Integer>();
			possibleNotes.add(allowableNotes);
			for(Note n : Key.getInstance().getSpanNotes(minPitch, maxPitch))
			{
				allowableNotes.put(n, 0);
			}
		}
		else
		{
			setPossibilities();
		}
	}
	
	public static Note selectRandom(Set<Note> set)
	{
		int choose = rand.nextInt(set.size());
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

	// push the set of all possible next notes to possibilities
	private void setPossibilities()
	{
		Note aNote = myScore.get(size() - 1);
		HashMap<Note,Integer> notes = new HashMap<Note,Integer>();
		possibleNotes.add(notes);
		
		for(Note n : Key.getInstance().getSpanNotes(aNote.getPitch() - 12, aNote.getPitch() + 12))
		{
			int degreeDistance = aNote.getDegree() - n.getDegree();
			if (n.getPitch() >= minPitch && n.getPitch() <= maxPitch
					&& (aNote.isIntervalConsonant(n) || (degreeDistance <= 1 && degreeDistance >= -1))
					&& n.getPitch() >= aNote.getPitch() - 12 && n.getPitch() <= aNote.getPitch() + 12)
			{
				notes.put(n, 0);
			}
		}
		
		for (int i = size() - 1; i > lastBlocker(aNote); i--)
		{
			Note currentNote = myScore.get(i);
			HashSet<Note> possibilities = possibleDependents(currentNote);
			for(Note n : possibilities)
			{
				int distance = aNote.getPitch() - n.getPitch();
				int degreeDistance = aNote.getDegree() - n.getDegree();
				if (!notes.containsKey(n) && distance >= -12 && distance <= 12
						&& n.getPitch() >= minPitch && n.getPitch() <= maxPitch
						&& (aNote.isIntervalConsonant(n) || (degreeDistance <= 1 && degreeDistance >= -1)))
				{
					notes.put(n, i);
				}
			}
		}
		
		// there can't be a note repeated three times
		if (size() >= 2)
		{
			if(aNote.equals(myScore.get(size() - 2)))
			{
				notes.remove(aNote);
			}
		}
		
	}
	
	public int size()
	{
		return myScore.size();
	}
	
	public String toString()
	{
		String string = "";
		for (Note n : myScore)
		{
			string += n + "\n";
			
		}
		string += "incompletes:\n";
		for (Integer i : locationOfLastIncomplete)
		{
			string += i + "\n";
		}
		string += "numberRequired:\n" + requiredNext.size() + "\n";
		return string;
	}

}

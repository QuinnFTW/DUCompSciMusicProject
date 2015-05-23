package edu.du.cs.quinn.Music;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class Line {
	private ArrayList<Note> myScore;
	private Stack<HashSet<Note>> possibleNotes;
	private Stack<Integer> locationOfLastIncomplete;
	private HashSet<Note>[] spanNotes;
	private int latestSpanLocation;
	private int nextSpanIndex;
	
	public Line(int minPitch, int maxPitch, HashSet<Note>[] required) {
		myScore = new ArrayList<Note>();
		possibleNotes = new Stack<HashSet<Note>>();
		locationOfLastIncomplete = new Stack<Integer>();
		spanNotes = required;
		latestSpanLocation = -1;
		nextSpanIndex = 0;
		
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
			if (locationOfLastIncomplete.peek() == null)
			{
				for(Note n : spanNotes[nextSpanIndex])
				{
					if (possibilities.contains(n))
					{
						addNote(n);
						return n;
					}
				}
			}
			else
			{
				// check to see if it is possible to resolve the latest dependent note
				for (Note n : Key.getInstance().getResolution(myScore.get(locationOfLastIncomplete.peek())))
				{
					if (possibilities.contains(n))
					{
						addNote(n);
						return n;
					}
				}
			}
		}
		Note aNote = selectRandom(possibilities);
		addNote(aNote);
		return aNote;
	}
	
	private void addNote(Note aNote)
	{
		//TODO
	}
	
	public void removeEndNote() {
		possibleNotes.pop();
		possibleNotes.peek().remove(myScore.get(myScore.size() - 1));
		myScore.remove(myScore.size() - 1);
	}
	
	public int getSize() {
		return myScore.size();
	}
	
	public Note getNote(int index) {
		return myScore.get(index);
	}
	
	public boolean hasNextNote()
	{
		return !possibleNotes.peek().isEmpty();
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
	
	public void removeNextNote(Note aNote)
	{
		possibleNotes.peek().remove(aNote);
	}

}

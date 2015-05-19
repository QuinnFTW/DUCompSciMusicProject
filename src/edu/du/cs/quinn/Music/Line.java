package edu.du.cs.quinn.Music;

import java.util.ArrayList;

public class Line {
	private ArrayList<Note> myScore;
	
	public Line() {
		myScore = new ArrayList<Note>();
	}
	
	public void addNote(Note myNote) {
		myScore.add(myNote);
	}
	
	public void removeNote(int index) {
		myScore.remove(index);
	}
	
	public void removeEndNote() {
		myScore.remove(myScore.size());
	}
	
	public int getSize() {
		return myScore.size();
	}
	
	public Note getNote(int index) {
		return myScore.get(index);
	}
	
	public boolean hasNextNote()
	{
		//TODO
		return false;
	}
	
	public Note getNextNote()
	{
		//TODO
		return null;
	}
	
	public void removeNextNote()
	{
		//TODO
	}

}

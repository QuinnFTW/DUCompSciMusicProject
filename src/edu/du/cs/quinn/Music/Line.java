package edu.du.cs.quinn.Music;

import java.util.ArrayList;

public class Line {
	private ArrayList<Note> myScore;
	
	public Line() {
		myScore = new ArrayList<Note>();
	}
	
	public void addNote(Note myNote) {
		
	}
	
	public void removeNote(int index) {
		
	}
	
	public void removeEndNote() {
		
	}
	
	public int getSize() {
		return myScore.size();
	}
	
	public Note getNote(int index) {
		return myScore.get(index);
	}

}

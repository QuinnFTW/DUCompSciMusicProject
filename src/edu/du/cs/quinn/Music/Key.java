package edu.du.cs.quinn.Music;

import java.util.HashSet;


public class Key {
	private static Key instance = null;
	private int thePitch;
	private Note centerTonic;
	private int[] scalePitches;
	private boolean isMajor;
	private Key(int p, boolean isMajor) {
		thePitch = p;
		this.isMajor = isMajor;
		scalePitches = new int[7];
		setScalePitches();
		centerTonic = new Note(getScalePitch(0), 0);
	}
	
	public static Key getInstance()
	{
		return instance;
	}
	
	public static Key getInstance(int p, boolean isMajor) {
	   if(instance == null) {
	      instance = new Key(p, isMajor);
	   }
	   return instance;
	}

	public Note getScalarNote(int degree)
	{
		return new Note(getScalePitch(degree), degree);
	}
	
	public int getScalePitch(int degree)
	{
		int remainder = degree % 7;
		if (remainder < 0)
		{
			remainder += 7;
		}
		int pitchClass = scalePitches[remainder];
		int octave = degree / 7;
		return pitchClass + (octave * 12);
	}
	
	public HashSet<Note> getSpanNotes(int minPitch, int maxPitch)
	{
		HashSet<Note> span = new HashSet<Note>();
		int lowDegree = ((minPitch - scalePitches[0]) / 12) * 7;
		while(true)
		{
			int pitch = getScalePitch(lowDegree);
			if (pitch > maxPitch)
			{
				break;
			}
			span.add(new Note(pitch, lowDegree));
			pitch = getScalePitch(lowDegree + 2);
			if (pitch > maxPitch)
			{
				break;
			}
			span.add(new Note(pitch, lowDegree + 2));
			pitch = getScalePitch(lowDegree + 4);
			if (pitch > maxPitch)
			{
				break;
			}
			span.add(new Note(pitch, lowDegree + 4));
			lowDegree += 7;
		}
		return span;
	}
	
	public Note getTonic()
	{
		return centerTonic;
	}
	
	private void setScalePitches()
	{
		scalePitches[0] = thePitch;
		scalePitches[1] = thePitch + 2;
		scalePitches[3] = thePitch + 5;
		scalePitches[4] = thePitch + 7;
		if (isMajor)
		{
			scalePitches[2] = thePitch + 4;
			scalePitches[5] = thePitch + 9;
			scalePitches[6] = thePitch + 11;
		}
		else
		{
			scalePitches[2] = thePitch + 3;
			scalePitches[5] = thePitch + 8;
			scalePitches[6] = thePitch + 10;
		}
	}
	
	public boolean isMajor()
	{
		return isMajor;
	}
	
}

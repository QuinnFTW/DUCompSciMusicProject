/**
 * This is a singleton class that determines the key
 * for which the the counterpoint composition will be
 * created.
 * 
 * @author Griffin Good
 * @date 5/30/15
 */

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
		centerTonic = new Note(thePitch, 0);
	}
	
	/**
	 * Returns the one and only instance of the
	 * Key class.
	 * 
	 * @return instance The only instance of the Key class
	 */
	public static Key getInstance()
	{
		return instance;
	}
	
	/**
	 * Checks if there is already an instance of the
	 * Key class. If no, it creates one, if yes, it does
	 * not.
	 * 
	 * @param p The pitch for the key
	 * @param isMajor If the key is major or minor
	 * @return instance The only instance of the Key class
	 */
	public static Key getInstance(int p, boolean isMajor) {
	   if(instance == null) {
	      instance = new Key(p, isMajor);
	   }
	   return instance;
	}

	/**
	 * Returns the scaler notes based off of the key and
	 * the specified degree.
	 * 
	 * @param degree The degree of change
	 * @return Note The scalar notes of the specified degree
	 */
	public Note getScalarNote(int degree)
	{
		return new Note(getScalePitch(degree), degree);
	}
	
	/**
	 * Helper class for getScalarNotes
	 * 
	 * @param degree The same degree for getScalarNotes
	 * @return int Difference from starting note to Scaler note
	 */
	public int getScalePitch(int degree)
	{
		int remainder = degree % 7;
		int octave = degree / 7;
		if (remainder < 0)
		{
			octave--;
			remainder += 7;
		}
		int pitchClass = scalePitches[remainder];
		return pitchClass + (octave * 12);
	}
	
	/**
	 * Returns all the helper notes for the given pitch
	 * 
	 * @param minPitch Minimum pitch
	 * @param maxPitch Maximum pitch
	 * @return All of the helper notes
	 */
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
	
	/**
	 * Returns the centerTonic for the key
	 * 
	 * @return centerTonic
	 */
	public Note getTonic()
	{
		return centerTonic;
	}
	
	/**
	 * Initialization of all the scale pitches for the key
	 */
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
	
	/**
	 * Tells if the key is major or minor
	 * 
	 * @return isMajor
	 */
	public boolean isMajor()
	{
		return isMajor;
	}
	
}

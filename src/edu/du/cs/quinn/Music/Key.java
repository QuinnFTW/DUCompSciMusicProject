package edu.du.cs.quinn.Music;


public class Key {
	private static Key instance = null;
	private int thePitch;
	private int[] scalePitches;
	private boolean isMajor;
	private Key(int p, boolean isMajor) {
		thePitch = p;
		this.isMajor = isMajor;
		scalePitches = new int[7];
		setScalePitches();
	}
	
	public static Key getInstance(int p, boolean isMajor) {
	   if(instance == null) {
	      instance = new Key(p, isMajor);
	   }
	   return instance;
	}
	
	public int getScalePitch(int degree)
	{
		int pitchClass = scalePitches[degree % 7];
		int octave = degree / 7;
		return pitchClass + octave * 12;
		
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

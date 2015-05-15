package edu.du.cs.quinn.Music;


public class Key {
	private static Key instance = null;
	@SuppressWarnings("unused")
	private int thePitch;
	private int minorRaiseValue;
	private Key(int p, boolean isMajor) {
		thePitch = p;
		if(isMajor)
		{
			minorRaiseValue = 0;
		}
		else
		{
			minorRaiseValue = -1;
		}
	}
	
	public static Key getInstance(int p, boolean isMajor) {
	   if(instance == null) {
	      instance = new Key(p, isMajor);
	   }
	   return instance;
	}
	
	public boolean isMajor()
	{
		return minorRaiseValue == 0;
	}
	
}

package edu.du.cs.quinn.Music;


public class Key {
	private static Key instance = null;
	@SuppressWarnings("unused")
	private static Pitch thePitch;
	protected Key() {      
		// Exists only to defeat instantiation.
	}
	
	public static Key getInstance(Pitch p) {
	   if(instance == null) {
	      instance = new Key();
	      thePitch = p;
	   }
	   return instance;
	}
}

package edu.du.cs.quinn.Music;

public class Note {
	
	private int pitch;
	private int degree;
	
	public Note(int relativePitchClass, int scaleDegree)
	{
		pitch = relativePitchClass;
		degree = scaleDegree;
	}
	
	public int getPitch() {
		return pitch;
	}
	
	public int intervalType(Note upperNote)
	{
		int upperDegree = upperNote.degree;
		int lowerDegree = this.degree;
		if (upperDegree == lowerDegree)
		{
			return 1;
		}
		return (upperDegree - lowerDegree - 1) % 7 + 2;
	}
	
	public boolean isIntervalConsonant(Note upperNote)
	{
		boolean perfect;
		int perfectOrMajorDistance;
		int actualDistance = (upperNote.pitch - this.pitch) % 12;
		switch(intervalType(upperNote))
		{
		case 2:
		case 7:
			return false;
		case 1:
		case 8:
			perfectOrMajorDistance = 0;
			perfect = true;
			break;
		case 3:
			perfectOrMajorDistance = 4;
			perfect = false;
			break;
		case 4:
			perfectOrMajorDistance = 5;
			perfect = true;
			break;
		case 5:
			perfectOrMajorDistance = 7;
			perfect = true;
			break;
		case 6:
			perfectOrMajorDistance = 9;
			perfect = false;
			break;
		default:
			return false;
		}
		return perfectOrMajorDistance == actualDistance || (!perfect && actualDistance == perfectOrMajorDistance - 1);
	}
	
	public String toString()
	{
		return "Note with a pitch of " + pitch + " and degree of " + degree;
	}

}

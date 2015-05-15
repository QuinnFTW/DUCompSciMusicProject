package edu.du.cs.quinn.Music;

public class KeyTest {

	public static void main(String[] args) {
		Note[] scale = new Note[14];
		
		scale[0] = new Note(0, 0);
		scale[1] = new Note(2, 1);
		scale[2] = new Note(4, 2);
		scale[3] = new Note(5, 3);
		scale[4] = new Note(7, 4);
		scale[5] = new Note(9, 5);
		scale[6] = new Note(11, 6);
		scale[7] = new Note(12, 7);
		scale[8] = new Note(14, 8);
		scale[9] = new Note(16, 9);
		scale[10] = new Note(17, 10);
		scale[11] = new Note(19, 11);
		scale[12] = new Note(21, 12);
		scale[13] = new Note(23, 13);
		
		for (int lowerNoteIndex = 0; lowerNoteIndex < 14; lowerNoteIndex++)
		{
			Note lowerNote = scale[lowerNoteIndex];
			for (int upperNoteIndex = lowerNoteIndex; upperNoteIndex < 14; upperNoteIndex++)
			{
				Note upperNote = scale[upperNoteIndex];
				System.out.println("Note " + lowerNoteIndex + " and note " + upperNoteIndex);
				System.out.println("are " + (lowerNote.intervalType(upperNote) - 1) + " steps apart");
				if (lowerNote.isIntervalConsonant(upperNote))
				{
					System.out.println("and form a consonant interval");
				}
				else
				{
					System.out.println("and form a dissonant interval");
				}
				System.out.println();
			}
		}
	}

}

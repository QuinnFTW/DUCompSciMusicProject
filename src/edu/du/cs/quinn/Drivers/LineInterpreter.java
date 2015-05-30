/**
 * This class takes up to three lines and creates a MIDI file in the
 * main directory of the project.
 * 
 * @author Quinn Hannah-White
 * @date 5/30/15
 */

package edu.du.cs.quinn.Drivers;

import java.io.File;
import java.io.IOException;
import edu.du.cs.quinn.Music.*;
import javax.sound.midi.*;

public class LineInterpreter {
	private Line sopranoLine;
	private Line altoLine;
	private Line bassLine;
	private File outputFile;
	private Sequence sequence;
	private Track track;
	private static final int VELOCITY = 64;
	
	/**
	 * Basic constructor
	 * 
	 * @param lineOne Soprano Line
	 * @param lineTwo Alto Line
	 * @param lineThree Bass Line
	 * @param fileName Constant counterpoint.midi
	 */
	public LineInterpreter(Line lineOne, Line lineTwo, Line lineThree, String fileName) {
		sopranoLine = lineOne;
		altoLine = lineTwo;
		bassLine = lineThree;
		outputFile = new File(fileName);
		try {
			sequence = new Sequence(Sequence.PPQ, 1);
		}
		catch (InvalidMidiDataException e) {
			e.printStackTrace();
			System.exit(1);
		}
		track = sequence.createTrack();
	}

	/**
	 * Rather simple, uses the createNoteOn and Off functions
	 * to add the individual notes to the MIDI file.
	 */
	public void readLinesToMidi() {
		if(sopranoLine!=null){
			for(int i=0;i<sopranoLine.size();i++) {
				track.add(createNoteOnEvent(sopranoLine.getNote(i).getPitch(), i));
				track.add(createNoteOffEvent(sopranoLine.getNote(i).getPitch(), i+1));
			}
		}
		if(altoLine!=null){
			for(int i=0;i<altoLine.size();i++) {
				track.add(createNoteOnEvent(altoLine.getNote(i).getPitch(), i));
				track.add(createNoteOffEvent(altoLine.getNote(i).getPitch(), i+1));
			}
		}
		if(bassLine!=null){
			for(int i=0;i<bassLine.size();i++) {
				track.add(createNoteOnEvent(bassLine.getNote(i).getPitch(), i));
				track.add(createNoteOffEvent(bassLine.getNote(i).getPitch(), i+1));
			}
		}
	}
	
	/**
	 * Creates the MIDI file, with the Java sound API
	 */
	public void outputToMidi() {
		try {
			MidiSystem.write(sequence, 0, outputFile);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Turns a note "ON" for the MIDI sequence
	 * 
	 * @param nKey The pitch of the note being added
	 * @param lTick The duration of the note
	 * @return Final Note event
	 */
	private static MidiEvent createNoteOnEvent(int nKey, long lTick) {
		return createNoteEvent(ShortMessage.NOTE_ON, nKey, VELOCITY, lTick);
	}
	
	/**
	 * Turns a note "OFF" for the MIDI sequence
	 * 
	 * @param nKey The pitch of the note being added
	 * @param lTick The duration of the note
	 * @return Final Note event
	 */
	private static MidiEvent createNoteOffEvent(int nKey, long lTick) {
		return createNoteEvent(ShortMessage.NOTE_OFF, nKey, 0, lTick);
	}
	
	/**
	 * Adds the note event to the MIDI sequence
	 * 
	 * @param nCommand ON or OFF
	 * @param nKey The pitch of the note being added or stopped
	 * @param nVelocity 
	 * @param lTick The duration of the note
	 * @return The event added to the MIDI sequence
	 */
	private static MidiEvent createNoteEvent(int nCommand, int nKey, int nVelocity, long lTick) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(nCommand, 0, nKey, nVelocity);
		}
		catch (InvalidMidiDataException e) {
			e.printStackTrace();
			System.exit(1);
		}
		MidiEvent event = new MidiEvent(message, lTick);
		return event;
	}
}	
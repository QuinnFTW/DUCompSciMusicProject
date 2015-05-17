package edu.du.cs.quinn.Drivers;

import java.io.File;
import java.io.IOException;

import edu.du.cs.quinn.Music.Line;

import javax.sound.midi.*;

public class LineInterpreter {
	private Line sopranoLine;
	private Line altoLine;
	private Line bassLine;
	private File outputFile;
	private Sequence sequence;
	private Track track;
	private static final int VELOCITY = 64;
	
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

	public void readLinesToMidi() {
		if(sopranoLine!=null){
			for(int i=0;i<sopranoLine.getSize();i++) {
				track.add(createNoteOnEvent(sopranoLine.getNote(i).getPitch(), i));
				track.add(createNoteOffEvent(sopranoLine.getNote(i).getPitch(), i+1));
			}
		}
		if(altoLine!=null){
			for(int i=0;i<altoLine.getSize();i++) {
				track.add(createNoteOnEvent(altoLine.getNote(i).getPitch(), i));
				track.add(createNoteOffEvent(altoLine.getNote(i).getPitch(), i+1));
			}
		}
		if(bassLine!=null){
			for(int i=0;i<bassLine.getSize();i++) {
				track.add(createNoteOnEvent(bassLine.getNote(i).getPitch(), i));
				track.add(createNoteOffEvent(bassLine.getNote(i).getPitch(), i+1));
			}
		}
	}
	
	public void outputToMidi() {
		try {
			MidiSystem.write(sequence, 0, outputFile);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static MidiEvent createNoteOnEvent(int nKey, long lTick) {
		return createNoteEvent(ShortMessage.NOTE_ON, nKey, VELOCITY, lTick);
	}
	
	private static MidiEvent createNoteOffEvent(int nKey, long lTick) {
		return createNoteEvent(ShortMessage.NOTE_OFF, nKey, 0, lTick);
	}
	
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
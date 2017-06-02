import javax.sound.midi.*;
import java.util.concurrent.TimeUnit;
import java.util.*;
import java.io.*;

public class Test {
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException{
		OpenFrame frame1 = new OpenFrame(); //creates opening frame
		Sequencer seq = MidiSystem.getSequencer(false); //gets default sequencer
		Transmitter trans = seq.getTransmitter();
		while(frame1.fileNotFound()){System.out.print("");} //waits until a file is selected
		File midiFile = frame1.getFile();
		frame1.close();
		SynthSetup frame2 = new SynthSetup(); //creates setup frame
		while(!frame2.playing()){System.out.print("");} //waits until user presses play
		SoundProcessor rec = new SoundProcessor(frame2.getWave(), frame2.getFrequency(),
				frame2.getFilter(), frame2.getAmplitude());
		trans.setReceiver(rec);
		Sequence sequence = MidiSystem.getSequence(midiFile);
		seq.setSequence(sequence);
		TimeUnit.SECONDS.sleep(1); //waits to make sure synth settings are set
		seq.open();
		seq.start();
		while(!frame2.stop()){ //loops until stop
			if(!frame2.playing()){ 
				seq.stop();
				rec.pause();
				while(!frame2.playing()){System.out.print("");}//loops until user presses play
				seq.start();
			}
			System.out.print(""); //these are in a lot of my waiting loops and it was the only way I could get them to work
		}
		seq.stop();
		seq.close();
		rec.close();
		System.exit(0);
		
		
		
		
	}

}

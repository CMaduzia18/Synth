import javax.sound.midi.*;
import com.jsyn.*;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.*;
public class SoundProcessor implements Receiver{

	private Synthesizer synth;
	private LineOut line;
	private TunableFilter filter;
	private UnitOscillator[] oscillators;
	private int filterFreq;
	private int polycount;
	private int polycounter;
	private double amplitude;
	
	public SoundProcessor(int osciChoice, int freq, int filterChoice, double amp){
		polycount = 12;
		synth = JSyn.createSynthesizer();
		line = new LineOut();
		if(filterChoice == 0){ //decides which filter type is used
			filter = new FilterHighPass();
		}else{
			filter = new FilterLowPass();
		}
		filterFreq = freq;
		filter.frequency.set(filterFreq);
		synth.add(line);
		synth.add(filter);
		oscillators = new UnitOscillator[polycount];
		filter.output.connect(0, line.input, 0);//links the filter output to stero outputs
		filter.output.connect(0, line.input, 1);
		if(osciChoice == 0){//decides which oscillator is used
			for(int i = 0; i < oscillators.length; i++){
				oscillators[i] = new SineOscillator();
			}
		}
		else if(osciChoice == 1){
			for(int i = 0; i < oscillators.length; i++){
				oscillators[i] = new TriangleOscillator();
			}
		}else if(osciChoice == 2){
			for(int i = 0; i < oscillators.length; i++){
				oscillators[i] = new SquareOscillator();
			}
		}else{
			for(int i = 0; i < oscillators.length; i++){
				oscillators[i] = new SawtoothOscillator();
			}
		}
		for(int i = 0; i < oscillators.length; i++){//links oscillators to filter input
			oscillators[i].amplitude.set(0);
			synth.add(oscillators[i]);
			oscillators[i].output.connect(filter.input);
		}
		amplitude = amp;
		polycounter = 0;
		synth.start();
		line.start();
		
	}
	@Override
	public void send(MidiMessage message, long timeStamp) {
		if(((ShortMessage) message).getCommand() == 144){//reads a NoteOn command from Channel 1
			int note = ((ShortMessage) message).getData1();
			double hertz = Math.pow(2, ((note-69.0)/12.0))*440.0;
			oscillators[polycounter].frequency.set(hertz);
			oscillators[polycounter].amplitude.set(amplitude);
			polycounter++;
			if(polycounter == polycount){
				polycounter = 0;
			}
			System.out.println(note + " "+  hertz);
		}else if(((ShortMessage) message).getCommand() == 128){//reads a NoteOff command from Channel 1
			int note = ((ShortMessage) message).getData1();
			double hertz = Math.pow(2, ((note-69.0)/12.0))*440.0;
			for(int i = 0; i<polycount; i++){
				if(oscillators[i].frequency.get() == hertz){
					oscillators[i].amplitude.set(0);
				}
			}
		}
		
	}

	@Override
	public void close() {//stops the synth
		line.stop();
		synth.stop();
		
	}
	
	public void pause(){//pauses by setting amplitudes to 0
		for(int i = 0; i< oscillators.length; i++){
			oscillators[i].amplitude.set(0);
		}
	}

}

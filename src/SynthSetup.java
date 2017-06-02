import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SynthSetup extends JFrame implements ActionListener, ChangeListener{
	private int osciSelect; //0-sine 1-tri 2-square 3-saw
	private int filterSelect; //0-highpass 1-lowpass
	private int frequency;
	private int amplitude;
	private boolean playBool;
	private boolean stopBool;
	private JSpinner frequencySpinner;
	private JSpinner amplitudeSpinner;
	private JButton play;
	private JButton pause;
	private JButton stop;
	private JRadioButton sine;
	private JRadioButton triangle;
	private JRadioButton square;
	private JRadioButton sawtooth;
	private JRadioButton lowPass;
	private JRadioButton highPass;
	
	
	public SynthSetup(){
		super();
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new GridBagLayout());
		
		
		//initial setup of wave radio buttons
		osciSelect = 0;
		sine = new JRadioButton("Sine");
		sine.setActionCommand("sine");
		sine.setSelected(true);
		triangle = new JRadioButton("Triangle");
		triangle.setActionCommand("triangle");
		square = new JRadioButton("Square");
		square.setActionCommand("square");
		sawtooth = new JRadioButton("Sawtooth");
		sawtooth.setActionCommand("sawtooth");
		
		//adding the radio buttons to a panel
		JPanel osciPanel = new JPanel(new GridLayout(0,1));
		osciPanel.add(sine);
		osciPanel.add(triangle);
		osciPanel.add(square);
		osciPanel.add(sawtooth);
		osciPanel.setBorder(BorderFactory.createTitledBorder("Waveform"));
		
		//setting up formatting
		GridBagConstraints osciConst = new GridBagConstraints();
		layoutSetup(osciConst, 0, 0, 4, 1);
		
		//adding wave buttons to group
		ButtonGroup waveGroup = new ButtonGroup();
		waveGroup.add(sine);
		waveGroup.add(triangle);
		waveGroup.add(square);
		waveGroup.add(sawtooth);
		
		contentPane.add(osciPanel, osciConst);
		
		
		//initial setup of filter radio buttons
		filterSelect = 1;
		frequency = 4000;
		lowPass = new JRadioButton("Low Pass");
		lowPass.setActionCommand("lowPass");
		lowPass.setSelected(true);
		highPass = new JRadioButton("High Pass");
		highPass.setActionCommand("highPass");
		
		//adding them to a group
		ButtonGroup filterGroup = new ButtonGroup();
		filterGroup.add(lowPass);
		filterGroup.add(highPass);
		
		SpinnerModel frequencyModel = new SpinnerNumberModel(4000, 0, 10000, 50);
		
		//adding buttons and spinner to panel
		JPanel filterPanel = new JPanel(new GridLayout(0,1));
		filterPanel.add(highPass);
		filterPanel.add(lowPass);
		filterPanel.setBorder(BorderFactory.createTitledBorder("Filter"));
		frequencySpinner = addLabeledSpinner(filterPanel, "Frequency", frequencyModel);
		
		//setting up layout
		GridBagConstraints filterConst = new GridBagConstraints();
		layoutSetup(filterConst, 1, 0, 4, 1);
		
		contentPane.add(filterPanel, filterConst);
		
		
		//amplitude setup
		SpinnerModel amplitudeModel = new SpinnerNumberModel(20, 1, 100, 5);
		
		//adding to panel
		amplitude = 20;
		JPanel amplitudePanel = new JPanel();
		amplitudeSpinner = addLabeledSpinner(amplitudePanel, "% Amplitude", amplitudeModel);
		amplitudePanel.setBorder(BorderFactory.createTitledBorder("Amplitude"));
		
		//setting up layout
		GridBagConstraints amplitudeConst = new GridBagConstraints();
		layoutSetup(amplitudeConst, 0, 4, 1, 1);
		
		contentPane.add(amplitudePanel, amplitudeConst);
		
		
		//setting up control buttons
		play = new JButton("Play");
		play.setActionCommand("play");
		pause = new JButton("Pause");
		pause.setActionCommand("pause");
		pause.setEnabled(false);
		stop = new JButton("Stop");
		stop.setActionCommand("stop");
		stop.setEnabled(false);
		playBool = false;
		stopBool = false;
		
		//putting control buttons in a panel
		JPanel controlPanel = new JPanel(new GridLayout(0,1));
		controlPanel.add(play);
		controlPanel.add(pause);
		controlPanel.add(stop);
		
		//setting up layout
		GridBagConstraints controlConst = new GridBagConstraints();
		layoutSetup(controlConst, 2, 0, 1, 3);
		
		contentPane.add(controlPanel, controlConst);
		
		
		//adding all the listeners
		amplitudeSpinner.addChangeListener(this);
		frequencySpinner.addChangeListener(this);
		play.addActionListener(this);
		pause.addActionListener(this);
		stop.addActionListener(this);
		sine.addActionListener(this);
		triangle.addActionListener(this);
		square.addActionListener(this);
		sawtooth.addActionListener(this);
		lowPass.addActionListener(this);
		highPass.addActionListener(this);
		
		
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {//responding to button presses
		if(e.getActionCommand().equals("sine")){
			osciSelect = 0;
		}else if(e.getActionCommand().equals("triangle")){
			osciSelect = 1;
		}else if(e.getActionCommand().equals("square")){
			osciSelect = 2;
		}else if(e.getActionCommand().equals("sawtooth")){
			osciSelect = 3;
		}else if(e.getActionCommand().equals("highPass")){
			filterSelect = 0;
		}else if(e.getActionCommand().equals("lowPass")){
			filterSelect = 1;
		}else if(e.getActionCommand().equals("play")){//disables all modification components
			playBool = true;
			play.setEnabled(false);
			pause.setEnabled(true);
			stop.setEnabled(true);
			sine.setEnabled(false);
			triangle.setEnabled(false);
			square.setEnabled(false);
			sawtooth.setEnabled(false);
			frequencySpinner.setEnabled(false);
			amplitudeSpinner.setEnabled(false);
			lowPass.setEnabled(false);
			highPass.setEnabled(false);
		}else if(e.getActionCommand().equals("pause")){
			playBool = false;
			play.setEnabled(true);
			pause.setEnabled(false);
			stop.setEnabled(true);
		}else if(e.getActionCommand().equals("stop")){
			playBool = true;
			stopBool = true;
		}
	}
	
	/*full honesty, took this method from the oracle docs because it does exactly what I need to do
	 * http://docs.oracle.com/javase/tutorial/uiswing/examples/components/SpinnerDemoProject/src/components/SpinnerDemo.java
	 * creates a spinner with an associated label and adds it to a container
	 */
	private JSpinner addLabeledSpinner(Container c, String label, SpinnerModel model) {
		JLabel l = new JLabel(label);
		c.add(l);

		JSpinner spinner = new JSpinner(model);
		l.setLabelFor(spinner);
		c.add(spinner);

		return spinner;
	}

	@Override
	public void stateChanged(ChangeEvent e) { //handles spinner input
		if(e.getSource().equals(frequencySpinner)){
			frequency = (int) frequencySpinner.getValue();
		}else if(e.getSource().equals(amplitudeSpinner)){
			amplitude = (int) amplitudeSpinner.getValue();
		}
		
	}
	
	//sets all the constraints to the given parameters with some defaults
	private void layoutSetup(GridBagConstraints c, int x, int y, int h, int w){
		c.gridx = x;
		c.gridy = y;
		c.gridheight = h;
		c.gridwidth = w;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.anchor = c.PAGE_START;
	}
	
	public int getWave(){
		return osciSelect;
	}
	public int getFilter(){
		return filterSelect;
	}
	public int getFrequency(){
		return frequency;
	}
	public double getAmplitude(){//returns amplitude on a scale from 0-1
		return ((double)amplitude)/100.0;
	}
	public boolean playing(){
		return playBool;
	}
	public boolean stop(){
		return stopBool;
	}
	

}

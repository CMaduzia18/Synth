import javax.sound.midi.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenFrame extends JFrame implements ActionListener{
	private JFileChooser chooser;
	private JFrame frame;
	private File file;
	
	public OpenFrame() {
		super();
		JButton open = new JButton("Open a midi file");
		open.setActionCommand("opendialog");
		open.addActionListener(this);
		this.add(open);
		this.setVisible(true);
		chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("MIDI File", "mid"));
		this.pack();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if("opendialog".equals(e.getActionCommand())){
			int returnVal = chooser.showOpenDialog(this);//opens file chooser dialog when button pressed
			
			if(returnVal == JFileChooser.APPROVE_OPTION){
				file = chooser.getSelectedFile(); 
			}
		}
	}
	
	public File getFile(){
		return file;
	}
	public boolean fileNotFound(){ //returns true if the file has not been picked yet
		return file == null;
	}
	
	public void close(){//closes window
		this.setVisible(false);
	}
}

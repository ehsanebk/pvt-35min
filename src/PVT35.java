/**
 * @author Ehsan Khosroshahi
 * Experiment : 35 min pvt test
 * Test Version 1
 * 340 trials 
 * 
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import org.apache.commons.lang3.time.StopWatch;


//5230 *key Pressed* =J		1
//5960 *key Pressed* =K		2
//6400 *key Pressed* =L		3
//6710 *key Pressed* =;		4
//7490 *key Pressed* =?		foil or 1-back

public class PVT35 extends JFrame implements KeyListener{

	JLabel num;
	JButton startButton;
	JTextField tf;
	JPanel buttonPanel;
	Random random;
	KeyListener respond_to_probe;
	
	boolean key;
	int trial_number;
	Timer stimulusTimer;
	long startTime;
	
	// constant variables
	String FILE_NAME = "./reactionTimes.txt";  // writing to a file for each participant
	static final int TIME_BETWEEN_STUDY_AND_PROBE  = 6000;  
	static final int NUMBER_OF_N_BACK_STIMULI = 8;
	
	boolean append_to_file  = false;
	FileWriter write; 
	PrintWriter print_line;
	
	private StopWatch stopWatch;
	
	public PVT35() {

		if (!isDisplayable()) {
			// Can only do this when the frame is not visible
			setUndecorated(true);
		}

		GraphicsDevice gd =
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (gd.isFullScreenSupported()) {
			//setUndecorated(true);
			gd.setFullScreenWindow(this);

		} else {
			System.err.println("Full screen not supported");
			setExtendedState(Frame.MAXIMIZED_BOTH);
			setUndecorated(true);
		}
		
		stopWatch = new StopWatch();

		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		setLayout(new GridLayout(3, 3, 5, 5));

		buttonPanel = new JPanel();

		startButton = new JButton("Start");
		startButton.setPreferredSize(new Dimension(200, 50));
		startButton.setBackground(Color.red);
		// button.setOpaque(true);
		buttonPanel.add(startButton, BorderLayout.NORTH);
		add(buttonPanel);

		add(new Convas());
		add(new Convas());
		add(new Convas());

		num = new JLabel();
		num.setFont(new Font("Serif", Font.PLAIN, 60));
		num.setForeground(Color.white);
		num.setHorizontalAlignment(SwingConstants.CENTER);
		num.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		add(num);

		add(new Convas());
		add(new Convas());
		add(new Convas());
		add(new Convas());

		setSize(900, 600);

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		event e = new event();
		startButton.addActionListener(e);

		random = new Random();

		try {
			write = new FileWriter(FILE_NAME, append_to_file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		print_line = new PrintWriter(write);

		/* A key handler which handle the key responses of the subject 
		            after the probe and go forward with the experiment 
		 */
		respond_to_probe = new KeyListener() {
			public void keyPressed(KeyEvent e) { }

			public void keyReleased(KeyEvent e) { 
				stimulusAtTime(0,"","");
				stimulusAtTime_feedback(100);
				key =true;
			}

			public void keyTyped(KeyEvent e) { }
		};

	}

	public class event implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			startButton.setVisible(false);
			startTime = System.currentTimeMillis();

			// starting the trials Trial 
			trial_number = 0;
			// starting the experiment after 1 second of clicking the strat bottom
			stopWatchAtTime(1000); 
		}
	}


	// set the text to s on the screen at time t after calling in the experiment and write it to text file
	public void stimulusAtTime(int t,final String s, final String information){

		stimulusTimer = new Timer(t, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				num.setText(s);
				if (information.length() >0){
					long endTime   = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					print_line.println(totalTime + "\t" + information);
				}
			}
		});
		stimulusTimer.setRepeats(false); // Only execute once
		stimulusTimer.start();

	}

	public void stopWatchAtTime(int t){

		print_line.flush();
		if (trial_number >=340){
			num.setText("END");	
		}
		else{
			stimulusTimer = new Timer(t, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					stopWatch.start();
					
					num.setText("<html>"+"test"+"</html>");
					// writing to file:
					long endTime   = System.currentTimeMillis();
					long reactionTime = endTime - startTime;
					print_line.println(reactionTime + "\t" + "Fixation "+ "test");

					stimulusAtTime_study(2000);
				}
			});
			stimulusTimer.setRepeats(false); // Only execute once
			stimulusTimer.start();
		}
	}

	public void stimulusAtTime_study(int t){

		stimulusTimer = new Timer(t, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				num.setText("<html>"+"test"+"<br/><center> - </center>"+
						"<br/> <center>"+"test" +"</center></html>");
				// writing to file:
				long endTime   = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				print_line.println(totalTime + "\t" + "Study --> " +"test"+"-" +"test");


				stimulusAtTime_warning(6000);
			}
		});
		stimulusTimer.setRepeats(false); // Only execute once
		stimulusTimer.start();

	}

	public void stimulusAtTime_warning(int t){

		stimulusTimer = new Timer(t, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				num.setText("<html>"+ "+" +"</html>");

				long endTime   = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				print_line.println(totalTime + "\t" + "Warning --> +");

				stimulusAtTime_probe(TIME_BETWEEN_STUDY_AND_PROBE);
			}
		});
		stimulusTimer.setRepeats(false); // Only execute once
		stimulusTimer.start();

	}

	public void stimulusAtTime_probe(int t){

		key =false;
		stimulusTimer = new Timer(t, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				num.setText("<html>"+"test"+"</html>");


				long endTime   = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				print_line.println(totalTime + "\t" + "PROBE = "+"test");



				addKeyListener(respond_to_probe);

				Timer checkAfter6sec = new Timer(6000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (key == false){
							stimulusAtTime(0,"","");
							stimulusAtTime_feedback(100);
						}

					}
				});
				checkAfter6sec.setRepeats(false); // Only execute once
				checkAfter6sec.start();
			}
		});
		stimulusTimer.setRepeats(false); // Only execute once
		stimulusTimer.start();

	}

	public void stimulusAtTime_feedback(int t){

		removeKeyListener(respond_to_probe);
		stimulusTimer = new Timer(t, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				num.setText("<html>"+"test"+"</html>");

				long endTime   = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				print_line.println(totalTime + "\t" + "Feed Back = "+"test");

				distractorAtTime(2000);
			}
		});
		stimulusTimer.setRepeats(false); // Only execute once
		stimulusTimer.start();

	}

	// the distractor : 1-Back task for 10 sec and it will update the timing (time)
	public void distractorAtTime(int t) {

		String stimulus=""; 
		for (int i = 0; i < NUMBER_OF_N_BACK_STIMULI; i++) {
			stimulus = "test";
			stimulusAtTime(t,stimulus, "N-Back :" + stimulus);
			stimulusAtTime(t+1000,"","");
			t+= 1200;
		} 

		trial_number++; // end of a trial:  adding 1 to the number of trials and going back to the fixation 
		stopWatchAtTime(t);

	}


	// next 3 function are for capturing the key input and write them on a text file
	@Override
	public void keyPressed(KeyEvent e) {
		String r = KeyEvent.getKeyText(e.getKeyCode());
		if (r.equals("J")){
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			print_line.println(totalTime + "\t"+ "*key Pressed* ="+ "1");}
		else if (r.equals("K")){
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			print_line.println(totalTime + "\t"+ "*key Pressed* ="+ "2");}
		else if (r.equals("L")){
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			print_line.println(totalTime + "\t"+ "*key Pressed* ="+ "3");}
		else if (r.equals(";")){
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			print_line.println(totalTime + "\t"+ "*key Pressed* ="+ "4");}
		else if (r.equals('?')){
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			print_line.println(totalTime + "\t"+ "*key Pressed* ="+ "foil/1-back");}
		else{
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			print_line.println(totalTime + "\t"+ "*key Pressed* ="+ KeyEvent.getKeyText(e.getKeyCode()));}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
			print_line.flush();
			System.exit(0);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	private static int randomInteger(int Start, int End, Random aRandom) {
		if (Start > End) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		// get the range, casting to long to avoid overflow problems
		long range = (long) End - (long) Start + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * aRandom.nextDouble());
		int randomNumber = (int) (fraction + Start);
		return randomNumber;
	}

	public static void main(String args[]) {
		PVT35 environment = new PVT35();
		environment.setExtendedState(Frame.MAXIMIZED_BOTH);
		environment.setBackground(Color.BLACK);

	}


}
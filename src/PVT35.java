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
import java.util.Random;
import java.util.Calendar;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import org.apache.commons.lang3.time.StopWatch;

public class PVT35 extends JFrame implements KeyListener{

	JLabel num;
	JLabel nameLabel;
	JTextField nameInput;
	JButton startButton;
	JPanel buttonPanel;
	JPanel namePanel;
	Random random;
	
	int trial_number;
	Timer stimulusTimer;
	Timer stopWatchCount;
	long startTime;
	long trailAtTime;
	
	// constant variables
	Calendar cal = Calendar.getInstance();
	String FILE_NAME;  // writing to a file for each participant
	public static final int ONE_SEC = 1000;   //time step in milliseconds
	public static final int TENTH_SEC = 100;
	public static final int HUNDREDTH_SEC = 10;
	public static final int MILLISEC = 1;
	public static final int TOTAL_NUMBER_OF_TRIALS = 340;
	
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
		startButton.setFont(new Font("Arial Bold", Font.PLAIN, 40));
		startButton.setBackground(Color.red);
		// button.setOpaque(true);
		buttonPanel.add(startButton, BorderLayout.NORTH);
		add(buttonPanel);

		namePanel = new JPanel();
		
		nameLabel = new JLabel("Please write your name:");
		nameLabel.setFont(new Font("Arial Bold", Font.PLAIN, 30));
		namePanel.add(nameLabel, BorderLayout.NORTH);
		nameInput = new JTextField(15);
		nameInput.setFont(new Font("Arial Bold", Font.PLAIN, 30));
		namePanel.add(nameInput, BorderLayout.NORTH);
		add(namePanel);
		
		add(new Convas());
		add(new Convas());
		
		num = new JLabel();
		num.setFont(new Font("Arial Bold", Font.PLAIN, 80));
		num.setForeground(Color.black);
		num.setHorizontalAlignment(SwingConstants.CENTER);
		num.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		add(num);
		
		add(new Convas());
		add(new Convas());
		add(new Convas());
		
		setSize(900, 600);

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		startEvent e = new startEvent();
		startButton.addActionListener(e);

		random = new Random();

		

	}

	// event handler for the start button
	public class startEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			startTime = System.currentTimeMillis();
			startButton.setVisible(false);
			nameInput.setVisible(false);
			nameLabel.setVisible(false);

			 FILE_NAME = nameInput.getText() + " " + cal.getTime();
			// output file
			try {
				write = new FileWriter(FILE_NAME, append_to_file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			print_line = new PrintWriter(write);
			print_line.println(String.format("%-4s %-4s %s","trial","\t"+"RT","\t"+"Trial Start Time" ));
			
			trial_number = 0;// starting the trials Trial
			stopWatchAtTime(1000);// starting the experiment after 1 second of clicking the start bottom
			stopWatchCount = new Timer(HUNDREDTH_SEC, new stopWatchCounter());
			stopWatchCount.setRepeats(true);
			System.out.println(nameInput.getText());
		}
	}
	
	public class stopWatchCounter implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			num.setText(""+stopWatch.getTime());
		}
	}

	// start the stop watch on the screen at time t after the function is being called
	public void stopWatchAtTime(int t){
		print_line.flush();
		stimulusTimer = new Timer(t, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stopWatch.start();
				stopWatchCount.start();
				trial_number++;
				trailAtTime = System.currentTimeMillis() - startTime;
			}
		});
		stimulusTimer.setRepeats(false); // Only execute once
		stimulusTimer.start();

	}

	// next 3 function are for capturing the key input and write them on a text file
	@Override
	public void keyPressed(KeyEvent e) {
		if (stopWatch.isStarted()){
			stopWatch.stop();
			stopWatchCount.stop();
			//String r = KeyEvent.getKeyText(e.getKeyCode());
			print_line.println(String.format("%-4s %-4s %s" ,trial_number,"\t"+stopWatch.getTime(),"\t"+trailAtTime));
			
			// clearing the screen after 500 ml and run a trial after random 2-10 sec
			Timer clearStopWatch = new Timer(500, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					stopWatch.reset();
					num.setText("");
					if (trial_number>=TOTAL_NUMBER_OF_TRIALS){
						num.setText("END");
					}else
						stopWatchAtTime(randomInteger(2, 10, random)*1000);
				}
			});
			clearStopWatch.setRepeats(false); // Only execute once
			clearStopWatch.start();
		}
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
		environment.setBackground(Color.gray);
	}
}
/* 
 * Homework6.java
 * 
 * Created on July 8, 2018
 * 
 */
package com.rbevans.bookingrate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

/**
*
* @author athom126
*/
public class Homework6 {
	
	// Initialize window and controls for GUI
	static JFrame frame = new JFrame("Beartooth Hiking Company Tours");
	static JRadioButton gardiner = new JRadioButton("Gardiner Lake");
	static JRadioButton hellroaring = new JRadioButton("Hellroaring Plateau");
	static JRadioButton beaten = new JRadioButton("The Beaten Path");
	static JRadioButton buttonArray[] = {gardiner, hellroaring, beaten};
	static JComboBox<Integer> duration = new JComboBox<Integer>();
	static JRadioButton hikeArray[] = {gardiner, hellroaring, beaten};
	static JTabbedPane jtp = new JTabbedPane();
	
	/**
	 * Creates a JFrame using Swing components and is the driver program
	 * 
     * @param args the command line arguments
     */
	public static void main(String[] args) throws ParseException {
		
		// add jFrame
		frame.setLocationByPlatform(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		// create panel and set appearance
		JPanel panel = new JPanel();
		jtp.addTab("Get Quote", panel); // add panel to JTabbedPane
		Border border = BorderFactory.createLineBorder(Color.black, 2);
		TitledBorder title = BorderFactory.createTitledBorder(border, 
							"Pricing & Scheduling");
		title.setTitleFont(new Font("Georgia", Font.PLAIN, 30));
		title.setTitleJustification(TitledBorder.CENTER);
		panel.setBorder(title);
		panel.setBackground(Color.white);
		panel.setPreferredSize(new Dimension(500, 200)); // keep window at fixed size
		panel.setLayout(new GridBagLayout()); // set the type of layout
		GridBagConstraints gc = new GridBagConstraints(); 
		gc.gridx = 0;
		gc.gridy = GridBagConstraints.RELATIVE;
		gc.anchor = GridBagConstraints.WEST;
		frame.add(jtp);

		/* labels */
		
		// add labels and submit button to panel
		panel.add(new JLabel("Select Hike:"), gc);
		panel.add(new JLabel("Start Date:"), gc);
		panel.add(new JLabel("Duration:"), gc);
		JButton submit = new JButton("Get Quote");
		submit.setEnabled(false);
		
		/*components for respective labels */
		
		// create and add radio buttons for selecting hike
		gc.insets = new Insets(5, 5, 5, 5);
		ButtonGroup options = new ButtonGroup();
		gardiner.setBackground(Color.white); // style the buttons
		hellroaring.setBackground(Color.white);
		beaten.setBackground(Color.white);
		options.add(gardiner); // add hikes to button group
		options.add(hellroaring);
		options.add(beaten);
		gc.gridx = 1; // position button into a single row
		gc.gridy = 0;
		panel.add(gardiner, gc);
		gc.gridx = 2;
		gc.gridy = 0;
		panel.add(hellroaring, gc);
		gc.gridx = 3;
		gc.gridy = 0;
		panel.add(beaten, gc);
		
		// add action listeners for each radio button and
		// dynamically change list items depending on hike selected
		gardiner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				duration.removeAllItems(); // clear drop down list
	            duration.addItem(3); // populate drop down list
	            duration.addItem(5);
	            submit.setEnabled(true); // enable the submit button
			}
	     });
	     hellroaring.addActionListener(new ActionListener() {
	    	 public void actionPerformed(ActionEvent e) {
	    		 duration.removeAllItems();
	    		 duration.addItem(2);
	             duration.addItem(3);
	             duration.addItem(4);
	             submit.setEnabled(true);
	         }
	     });
	    beaten.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		duration.removeAllItems();
	    		duration.addItem(5);
	            duration.addItem(7);
	            submit.setEnabled(true);
	    	}
	    });
		
		// create and add text field for starting date of hike (MM/DD/YYYY)
		gc.gridx = 1; // position text field for date
		gc.gridy = 1;
		gc.ipadx = 5;
		MaskFormatter dateFormat;
		JFormattedTextField startDate;
		dateFormat = new MaskFormatter("##/##/####"); // specify a date format
		startDate = new JFormattedTextField(dateFormat); // enforce date format onto text field
		startDate.setPreferredSize(new Dimension(70, 20)); // restrict size of text field
		panel.add(startDate, gc); // addtext field to window

		// add dynamic drop down menu for selecting duration
		// of hike depending on the hike selected
		gc.gridx = 1;
		gc.gridy = 2;
		duration.setPreferredSize(new Dimension(40, 20));
		panel.add(new JScrollPane(duration), gc);
		duration.setBackground(Color.white);
		
		// add submit button and create its action listener
		gc.anchor = GridBagConstraints.SOUTH;
		gc.gridx = 3;
		gc.gridy = 4;
		panel.add(submit, gc);
		submit.setBackground(Color.white);
		
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// store month day and year as integers
					String dateArr[] = startDate.getText().split("/");
					int month = Integer.parseInt(dateArr[0]);
					int day = Integer.parseInt(dateArr[1]);
					int year = Integer.parseInt(dateArr[2]);
					
					// store selected button as a string
					String hike;
					for(int i = 0; i < 3; i++) {
						if(hikeArray[i].isSelected()) {
							hike = hikeArray[i].getText();
							
							// get and store hike duration as integer
							int numDays = (int) duration.getSelectedItem();
							
							// pass values to validate
							validateInput(hike, month, day, year, numDays);
							break;
						}
					}
				} catch(NumberFormatException ne) {
					JOptionPane.showMessageDialog(frame, 
								"Please enter a date in the format MM/DD/YYYY.");
				}
			}
		});
		
		// display the window
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Sends user input to server for validation and quote information
	 * 
	 * @param hike
	 * @param month
	 * @param day
	 * @param year
	 * @param numDays
	 * 
	 */
	public static void validateInput(String hike, int month, int day, int year,
									int numDays) {
		
		// set hike id
		int hike_id = -1;
		if(hike == "Gardiner Lake") {
			hike_id = 0;
		}
		else if(hike == "Hellroaring Plateau") {
			hike_id = 1;
		}
		else {
			hike_id = 2;
		}
		
		String quote = null; // stores quote information from server
		
		try {
			// create socket for connection
			Socket socket = new Socket("web7.jhuep.com", 20025);
			
			// create streams for reading from and writing to server
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);                        
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	            									socket.getInputStream()));
			
	        // send message to server
	        out.println(hike_id+":"+year+":"+month+":"+day+":"+numDays);
				
	        // get response from server
			quote = in.readLine();
				
			// close streams and socket
			out.close();
			in.close();
			socket.close();
				
			// if bad input, let user know
			if(!quote.contains("Quoted Rate")) {
				displayErrorMessage(quote);
			}
			// if good input, display results
			else {
				// call createResutsTab to make a new tab for displaying results 
				createResultsTab(hike, month, day, year, numDays, quote);
			}
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(frame, "Unable to connect to server.");
		} catch(IOException e) {
			JOptionPane.showMessageDialog(frame, "IO Exception");
		}
	}
	
	/**
	 * Displays error message to user if bad input
	 * 
	 * @param quote
	 * 
	 */
	public static void displayErrorMessage(String quote) {
		
		String message;
		
		// depending on the type of error, construct a message
		if(quote.equals("-0.01:begin or end date was out of season")) {
			message = "Month out of season. Please select a month "
					+ "from June to October.";
		}
		else if(quote.equals("-0.01:One of the dates was not a valid day")) {
			message = "Invalid day or year selected."
					+ " Please enter an appropriate day "
					+ "and/or year from 2007 to 2020.";
		}
		else {
			message = "Please select a date that is within season of a valid year "
						+ "and day for a given month\n"
						+ "-Season runs from June 1st to October 1st\n"
						+ "-Year may range from 2007 to 2020\n"
						+ "-Day must be within range for "
						+ "the given month, typically 1-31";
		}
		
		// display error message
		JOptionPane.showMessageDialog(frame, message);
	}
	
	/**
	 * Creates new tab for displaying results
	 * 
	 * @param hike
	 * @param month
	 * @param day
	 * @param year
	 * @param numDays
	 * @param cost
	 * 
	 */
	public static void createResultsTab(String hike, int month, int day, 
									int year, int numDays, String quote) {
		// create panel and set appearance
		JPanel results = new JPanel();
		jtp.addTab("Results", results); // add panel to JTabbedPane
		Border border = BorderFactory.createLineBorder(Color.black, 2);
		TitledBorder title = BorderFactory.createTitledBorder(border, 
									"Results");
		title.setTitleFont(new Font("Georgia", Font.PLAIN, 30));
		title.setTitleJustification(TitledBorder.CENTER);
		results.setBorder(title);
		results.setBackground(Color.white);
		results.setPreferredSize(new Dimension(500, 200));
		results.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.WEST;
		
		// parse quote for cost and store it
		double cost = Double.parseDouble(quote.split(":")[0]);
		
		// add labels to panel to show results
		results.add(new JLabel("Select Hike: "+hike.toUpperCase()), gbc);
		results.add(new JLabel("Start Date: "+month+"/"+day+"/"+year), gbc);
		results.add(new JLabel("Duration: "+numDays+" DAYS"), gbc);
		results.add(new JLabel(("Cost: $"+cost)), gbc); // gets cost	
	}
}

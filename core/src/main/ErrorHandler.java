package main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ErrorHandler {
	
	private static final int width = 800, height = 620;

	public static void makeWindow(String error){
		JFrame frame = new JFrame("Error!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().setSize(width, height);
		frame.getContentPane().add(new JLabel(error), BorderLayout.WEST);
		frame.pack();
		frame.setVisible(true);
	}
	
}

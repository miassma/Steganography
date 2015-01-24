
package stegan;

import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class AboutView extends JDialog {
	
	private JLabel courseLabel = new JLabel("APO Project: Steganography (2014-2015)");
	private JLabel authorsLabel = new JLabel("Autor: Marta Grzęda");
	private JLabel leadLabel = new JLabel("Prowadzący: Marek Doros");
	
	public AboutView(Frame parentView) {
		super(parentView);
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		Border courseLabelBorder = BorderFactory.createEmptyBorder(0, 0, 15, 0);
		courseLabel.setFont(new Font("Arial Bold", Font.BOLD, 16));
		courseLabel.setBorder(courseLabelBorder);
		
		add(courseLabel);
		add(authorsLabel);
		add(leadLabel);
		
		setSize(500,120);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("About program");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}
}
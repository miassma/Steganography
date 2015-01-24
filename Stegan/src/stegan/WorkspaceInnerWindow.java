package stegan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.List;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class WorkspaceInnerWindow extends JInternalFrame implements Observer {

	private JLabel imageLabel = new JLabel();
	private ImageModel imageModel;
	private JButton okBtn = new JButton("OK");
	private JLabel communicate = new JLabel("Wybierz dowolny piksel");
	private JLabel choice = new JLabel("wybrany kolor          ");
	private JPanel buttonPane = new JPanel();
	private int xImagePos = 0;
	private int yImagePos = 0;
	private Point coordinates;
	private JLabel hint = new JLabel("Tolerancja");
	private JComboBox value = new JComboBox();
	
	public WorkspaceInnerWindow(ImageModel imageModel) {
		this.imageModel = imageModel;
		
		setLayout(new BorderLayout());
		setClosable(true);
		setAutoscrolls(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		
		getContentPane().add(new JScrollPane(imageLabel), BorderLayout.CENTER);
		
		setTitle(imageModel.getName());
		imageLabel.setHorizontalAlignment(JLabel.CENTER);
		imageLabel.setIcon(new ImageIcon(imageModel.getImage()));
		setSize(new Dimension(
			imageModel.getWidth() + 60,
			imageModel.getHeight() + 60
		));			
	}
	
	public WorkspaceInnerWindow(ImageModel imageModel, boolean Clickable) {
		this.imageModel = imageModel;
		
		setLayout(new BorderLayout());
		setClosable(true);
		setAutoscrolls(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		
		getContentPane().add(new JScrollPane(imageLabel), BorderLayout.CENTER);
		
		
		communicate.setHorizontalAlignment(JLabel.CENTER);
		this.add(communicate, BorderLayout.NORTH);
		buttonPane.add(choice);
		buttonPane.add(hint);
				
		for (int i = 1; i <= 100; ++i) {
		    value.addItem(i);
		}
		buttonPane.add(value);
		buttonPane.add(okBtn);
		
		this.add(buttonPane, BorderLayout.SOUTH);
		
		
		setTitle(imageModel.getName());
		imageLabel.setHorizontalAlignment(JLabel.CENTER);
		imageLabel.setIcon(new ImageIcon(imageModel.getImage()));
		
		setSize(new Dimension(
			imageModel.getWidth() + 100,
			imageModel.getHeight() + 100
		));			
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				// odleglosc obrazka od lewego, gornego rogu obszaru okna
				int xOffset = (imageLabel.getWidth() - imageLabel.getIcon().getIconWidth())/2;
				int yOffset = (imageLabel.getHeight() - imageLabel.getIcon().getIconHeight())/2;
				// wspolrzedne klikniecia wzgledem obrazka
				xImagePos = e.getX()-xOffset;
				yImagePos = e.getY()-yOffset;
				// bierzemy tylko klikniecia w obrazek
				if (
					xImagePos >= 0 
					&& yImagePos >= 0 
					&& xImagePos < imageLabel.getIcon().getIconWidth()
					&& yImagePos < imageLabel.getIcon().getIconHeight()
					
				) {
				// pobieramy kolor z obrazka
					int gray = getImageModel().getImage().getRaster().getPixel(xImagePos, yImagePos, new int[1])[0];
					choice.setText("wybrany kolor: " + gray+"      ");
					coordinates =  new Point(xImagePos, yImagePos);
					//System.out.println("X: " + xImagePos + ", Y: " + yImagePos + ", KOLOR: " + gray);
				}
			}
		});
	}
	
	public ImageModel getImageModel() {
		return imageModel;
	}

	@Override
	public void update(Observable o, Object arg) {
		imageLabel.setIcon(new ImageIcon(imageModel.getImage()));
		setTitle(imageModel.getName());
		
		System.out.println("refreshing the view");
	}
	
	public void setOkButtonActionListener(ActionListener listener) {
		okBtn.addActionListener(listener);
	}
	
	public Point getCoordinates(){
		return coordinates;
	}
	
	public int getTolerance(){
		return (int)value.getSelectedItem();
	}
}


package stegan;

import java.awt.BorderLayout;
import java.awt.Component;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Math.pow;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import static stegan.SteganographyOperationsUtil.posterize;


public class HidingOperationView extends JFrame  implements ActionListener {

	
        private JPanel leftPanel;
	private JPanel rightPanel;
        private JPanel operations;
        private PreviewableDialog hidingImage;
        private PreviewableDialog imageToHide;
	private JLabel info;	
        private JLabel chooseHidingImageInfo = new JLabel("<html><br><br>Choose the hiding image<br></html>");
        private JLabel chooseImageToHideInfo = new JLabel("<html><br><br>Choose the image to hide<br></html>");
        /* to do...
        private JLabel currentPixelInImageToHide = new JLabel("<html><br><br>Piksel obrazu ukrywanego:<br></html>");
        public JLabel currentPixelInHidingImage = new JLabel("<html><br><br>Piksel obrazu ukrywającego:<br></html>");
        public JLabel pixelAfterHiding = new JLabel("<html><br><br>Piksel obrazu po ukryciu:<br></html>");*/
        private JLabel SizeOfToHide = new JLabel("");
        private JLabel SizeOfHiding = new JLabel("");
        public JTable pixelOfToHideTable = new JTable(1,8);
        public JTable pixelOfHidingTable = new JTable(1,8);
        public JTable pixelAfterHidingTable = new JTable(1,8);
        
	private JComboBox<ImageModel> firstImageComboBox = new JComboBox<ImageModel>();
	private JComboBox<ImageModel> secondImageComboBox = new JComboBox<ImageModel>();
	private JButton okBtn = new JButton("OK");
	private JButton hideBtn = new JButton("Start hiding");
        /* to do
        private JButton nextStep = new JButton("Następny krok");
        private JButton doAll = new JButton("Przejdź do końca procesu");
        */
	private WorkspaceController workspaceController;
        public ImageModel copyOfToHide;
        int posterisation;
        
	public HidingOperationView(Frame parent, WorkspaceController workspaceController) {
                
		this.workspaceController = workspaceController;
                this.setLayout(new BorderLayout());
                
                leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		                
                DefaultComboBoxModel<ImageModel> firstImageComboBoxModel = new DefaultComboBoxModel<ImageModel>();
		DefaultComboBoxModel<ImageModel> secondImageComboBoxModel = new DefaultComboBoxModel<ImageModel>();
		for(ImageModel imageModel : workspaceController.getAllOpenImageModels()) {
			firstImageComboBoxModel.addElement(imageModel);
			secondImageComboBoxModel.addElement(imageModel);
		}
                
		firstImageComboBox.setModel(firstImageComboBoxModel);
		secondImageComboBox.setModel(secondImageComboBoxModel);
                
                setMaxSize(firstImageComboBox); 
                setMaxSize(secondImageComboBox); 
                
                leftPanel.add(chooseHidingImageInfo, LEFT_ALIGNMENT);
                leftPanel.add(firstImageComboBox, CENTER_ALIGNMENT);
                leftPanel.add(chooseImageToHideInfo, LEFT_ALIGNMENT);
		leftPanel.add(secondImageComboBox, CENTER_ALIGNMENT);
               
		info = new JLabel("<html><br><br><br><br><br><br></html>");
                leftPanel.add(info);
		leftPanel.add(okBtn);
                leftPanel.add(SizeOfHiding);
                leftPanel.add(SizeOfToHide);
                hideBtn.setVisible(false);
                leftPanel.add(hideBtn);
                
		operations = new JPanel(new GridBagLayout());
                
		GridBagConstraints leftPanelC = new GridBagConstraints();
		leftPanelC.gridx = 0;
		leftPanelC.gridy = 0;
		leftPanelC.weighty = 0.1;
		leftPanelC.weighty = 1;
		leftPanelC.fill = GridBagConstraints.HORIZONTAL;
		leftPanelC.anchor = GridBagConstraints.NORTHWEST;	
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		
		hidingImage = new PreviewableDialog(this, "hiding image", getSelectedFirstImageModel());
                hidingImage.getBeforeImagePanel().setPreferredSize(new Dimension(300, 300));
                rightPanel.add(hidingImage.getBeforeImagePanel());
                
                imageToHide = new PreviewableDialog(this, "image to hide", getSelectedSecondImageModel());
                imageToHide.getBeforeImagePanel().setPreferredSize(new Dimension(300, 300));
                rightPanel.add(imageToHide.getBeforeImagePanel());
                
		firstImageComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				hidingImage.setBeforeImageModel(getSelectedFirstImageModel());
                                
			}

		});
                
                secondImageComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				imageToHide.setBeforeImageModel(getSelectedSecondImageModel());
                                
			}

		});
                
                okBtn.addActionListener(new ActionListener(){
                        @Override
			public void actionPerformed(ActionEvent arg0) {
                            copyOfToHide = new ImageModel(getSelectedSecondImageModel());
                            posterisation = SteganographyOperationsUtil.checkImages(getSelectedFirstImageModel(),
                                        getSelectedSecondImageModel(), copyOfToHide);
                            if(posterisation < 0){ 
                                info.setText("<html><br><br>Image to hide is too big<br><br><br></html>");
                            }else if(posterisation == 256){
                                 info.setText("<html><br><br>The image to hide will keep 255 shades of gray<br><br><br></html>");   
                                 int size = getSelectedFirstImageModel().getWidth() * getSelectedFirstImageModel().getHeight();
                                 SizeOfHiding.setText("<html><br>Size of the hiding image: "+ size+"<br><br></html>");
                            
                                    size = getSelectedSecondImageModel().getWidth() * getSelectedSecondImageModel().getHeight();
                                    SizeOfToHide.setText("<html>Size of image to hide: "+ size+"<br><br></html>");
                            
                                    hideBtn.setVisible(true);
                                    
                            }else{
                                        info.setText("<html><br><br>Image to hide was reduced to "+ posterisation + " shades of gray<br><br><br></html>");
                                        imageToHide.setBeforeImageModel(copyOfToHide);
                            
                            int size = getSelectedFirstImageModel().getWidth() * getSelectedFirstImageModel().getHeight();
                            SizeOfHiding.setText("<html><br>Size of the hiding image:"+ size+"<br><br></html>");
                            
                            size = getSelectedSecondImageModel().getWidth() * getSelectedSecondImageModel().getHeight();
                            SizeOfToHide.setText("<html>Size of image to hide "+ size+"<br><br></html>");
                            
                            hideBtn.setVisible(true);
                            }
                        }               
                });
                
                hideBtn.addActionListener(new ActionListener(){
                   @Override
                   public void actionPerformed(ActionEvent arg0){
                       /* to do
                       leftPanel.add(currentPixelInImageToHide, LEFT_ALIGNMENT);
                       leftPanel.add(pixelOfToHideTable);
                       leftPanel.add(currentPixelInHidingImage, LEFT_ALIGNMENT);
                       leftPanel.add(pixelOfHidingTable);
                       leftPanel.add(pixelAfterHiding, LEFT_ALIGNMENT);
                       leftPanel.add(pixelAfterHidingTable);
                               */
                       SteganographyOperationsUtil.hidingOperation(
						getSelectedFirstImageModel(), copyOfToHide, posterisation);
                       /*leftPanel.add(nextStep);
                       leftPanel.add(doAll);*/
                       (HidingOperationView.this).dispose();
                        
                   }                    
                });                
         		
		leftPanel.setSize(400,600);
		setSize(800, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                
                setVisible(true);    
		
	}
       
        public ImageModel getSelectedFirstImageModel() {
		return (ImageModel) firstImageComboBox.getSelectedItem();
	}
	
	public ImageModel getSelectedSecondImageModel() {
		return (ImageModel) secondImageComboBox.getSelectedItem();
	}

        private void setMaxSize(JComponent jc){
            Dimension max = jc.getMaximumSize();
            Dimension pref = jc.getPreferredSize();
            max.height = pref.height;
            jc.setMaximumSize(max);
        } 
        
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   

}


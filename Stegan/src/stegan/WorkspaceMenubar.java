package stegan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class WorkspaceMenubar extends JMenuBar{
    
        private JMenu fileMenu = new JMenu("File");
	private JMenuItem fileMenuOpen= new JMenuItem("Open...");
        private JMenuItem fileMenuSave = new JMenuItem("Save");
	private JMenuItem fileMenuClose = new JMenuItem("Close");
        
        private JMenu steganographyMenu = new JMenu("Steganography");
        private JMenuItem steganographyMenuStartHidding = new JMenuItem("Start hiding");
        private JMenuItem steganographyMenuStartEncoding = new JMenuItem("Encode image");
        
        private JMenu helpMenu = new JMenu("Help");
	private JMenuItem aboutProgramMenu = new JMenuItem("About program");
        
        private IWorkspaceMenubarListener workspaceMenubarListener;
        
        public WorkspaceMenubar() {
		arrangeItems();
		attachListeners();
	}
        
        private void attachListeners() {
		fileMenuOpen.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
			if (workspaceMenubarListener != null) {
                            workspaceMenubarListener.fileMenuOpenClicked();
			}
                    }                
		});
                fileMenuSave.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
			if (workspaceMenubarListener != null) {
                            workspaceMenubarListener.fileMenuSaveClicked();
			}
                    }                
		});
		fileMenuClose.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
			if (workspaceMenubarListener != null) {
			    workspaceMenubarListener.fileMenuCloseClicked();
		        }
		    }
		});
                
                steganographyMenuStartHidding.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
			if (workspaceMenubarListener != null) {
			    workspaceMenubarListener.steganographyMenuStartHiddingClicked();
		        }
		    }
		});
                
                steganographyMenuStartEncoding.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
			if (workspaceMenubarListener != null) {
			    workspaceMenubarListener.steganographyMenuStartEncodingClicked();
		        }
		    }
		});
                
                aboutProgramMenu.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
			if (workspaceMenubarListener != null) {
			    workspaceMenubarListener.aboutProgramMenuClicked();
		        }
		    }
		});
        }
        
        private void arrangeItems() {
		fileMenu.add(fileMenuOpen);
                fileMenu.add(fileMenuSave);
                fileMenuSave.setEnabled(false);
                fileMenu.add(fileMenuClose);
		add(fileMenu);
	
		steganographyMenu.add(steganographyMenuStartHidding);
		steganographyMenu.add(steganographyMenuStartEncoding);
                steganographyMenuStartHidding.setEnabled(false);
                steganographyMenuStartEncoding.setEnabled(false);
                add(steganographyMenu);
                
		helpMenu.add(aboutProgramMenu);
		add(helpMenu);
	}
        public void setworkspaceMenubarListener(
                IWorkspaceMenubarListener workspaceMenubarListener) {
            this.workspaceMenubarListener = workspaceMenubarListener;
        }

	public void setSpecificMenuItemsEnabled(boolean enabled) {
                steganographyMenuStartHidding.setEnabled(enabled);
                steganographyMenuStartEncoding.setEnabled(enabled);
		fileMenuSave.setEnabled(enabled);
	}       

   
     
 
}

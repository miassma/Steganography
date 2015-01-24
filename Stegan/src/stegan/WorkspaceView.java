package stegan;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

public class WorkspaceView extends JFrame{   
    
    private WorkspaceMenubar workspaceMenubar = new WorkspaceMenubar();
    private JDesktopPane desktopPane = new JDesktopPane();              
  
    public WorkspaceView() {
	setLayout(new BorderLayout());
	add(desktopPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	setTitle("Steganography");
	setJMenuBar(workspaceMenubar);
	setSize(Toolkit.getDefaultToolkit().getScreenSize());
     }
	public void setWorkspaceMenubarListener(IWorkspaceMenubarListener listener) {
		workspaceMenubar.setworkspaceMenubarListener(listener);
	}
        
	public void setSpecificMenuItemsEnabled(boolean enabled) {
		workspaceMenubar.setSpecificMenuItemsEnabled(enabled);
	}
	
	public WorkspaceInnerWindow getSelectedWindow() {
		return (WorkspaceInnerWindow) desktopPane.getSelectedFrame();
	}
	
	public List<WorkspaceInnerWindow> getAllWindows() {
		List<WorkspaceInnerWindow> allWindows = new ArrayList<>();
		for(JInternalFrame internalFrame : desktopPane.getAllFrames()) {
			allWindows.add((WorkspaceInnerWindow) internalFrame);
		}
		
		return allWindows;
	}
	
	public void addWindow(WorkspaceInnerWindow innerWindow) {
		WorkspaceInnerWindow selectedWindow;
		if((selectedWindow = getSelectedWindow()) != null) {
			Point loc = selectedWindow.getLocation();
			
			innerWindow.setLocation(loc.x+15, loc.y+15);
		}
		
		desktopPane.add(innerWindow);
	}

	public void showError(String errorMsg) {
		JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
	}   
        
        
      
    
}

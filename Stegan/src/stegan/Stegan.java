package stegan;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Stegan {

    public void startApp() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Unable to load native look and feel");
		}
		
		WorkspaceView workspaceView = new WorkspaceView();
		WorkspaceController workspaceController = new WorkspaceController(workspaceView);
		workspaceView.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Stegan().startApp();
			}
		});
	}
    
}

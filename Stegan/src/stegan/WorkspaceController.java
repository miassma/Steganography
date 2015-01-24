
package stegan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.filechooser.FileNameExtensionFilter;


public class WorkspaceController {

	private WorkspaceView workspaceView;
	
	public WorkspaceController(WorkspaceView workspaceView) {
		this.workspaceView = workspaceView;
		
		workspaceView.setWorkspaceMenubarListener(new WorkspaceMenubarListener());
	}

	public void showWorkspace() {
		workspaceView.setVisible(true);
	}
	

	public void addNewWindow(ImageModel imageModel) {
		WorkspaceInnerWindow innerWindow = new WorkspaceInnerWindow(imageModel);
		innerWindow.setVisible(true);
		innerWindow.addInternalFrameListener(new InternalFrameListenerImpl(workspaceView));
		
		imageModel.addObserver(innerWindow);
		
		workspaceView.addWindow(innerWindow);
		
		try {
			innerWindow.setSelected(true);
		} catch (PropertyVetoException e) {
			System.err.println("Unable to change focus to new window");
		}
	}
	
	public void addNewWindowClickable(ImageModel imageModel, boolean Clickable) {
		WorkspaceInnerWindow innerWindow = new WorkspaceInnerWindow(imageModel, Clickable);
		innerWindow.setVisible(true);
		innerWindow.addInternalFrameListener(new InternalFrameListenerImpl(workspaceView));
		
		imageModel.addObserver(innerWindow);
		
		workspaceView.addWindow(innerWindow);
		
		try {
			innerWindow.setSelected(true);
		} catch (PropertyVetoException e) {
			System.err.println("Unable to change focus to new window");
		}
	}
	
	public ImageModel getSelectedWindowImageModel() {
		WorkspaceInnerWindow selectedWindow = workspaceView.getSelectedWindow();
		
		if (selectedWindow != null) {
			return selectedWindow.getImageModel();
		}
		
		return null;
	}
	
	public List<ImageModel> getAllOpenImageModels() {
		List<ImageModel> allImageModels = new ArrayList<ImageModel>();
		List<WorkspaceInnerWindow> allWindows = workspaceView.getAllWindows();
		
		for(WorkspaceInnerWindow window : allWindows) {
			allImageModels.add(window.getImageModel());
		}
		
		return allImageModels;
	}
	
	public class InternalFrameListenerImpl implements InternalFrameListener {

		WorkspaceView view;
		
		public InternalFrameListenerImpl(WorkspaceView view) {
			this.view = view;
		}

		@Override
		public void internalFrameOpened(InternalFrameEvent e) {}

		@Override
		public void internalFrameClosing(InternalFrameEvent e) {}

		@Override
		public void internalFrameClosed(InternalFrameEvent e) {}

		@Override
		public void internalFrameIconified(InternalFrameEvent e) {}

		@Override
		public void internalFrameDeiconified(InternalFrameEvent e) {}

		@Override
		public void internalFrameActivated(InternalFrameEvent e) {
			view.setSpecificMenuItemsEnabled(true);
		}

		@Override
		public void internalFrameDeactivated(InternalFrameEvent e) {
			view.setSpecificMenuItemsEnabled(false);
		}

	}
	
	public class WorkspaceMenubarListener implements IWorkspaceMenubarListener {

        

            @Override
            public void fileMenuOpenClicked() {
                Preferences pref = Preferences.userRoot();
                String path = pref.get("DEFAULT_PATH", "");
                JFileChooser fileChooser = new JFileChooser();
                
                // Set the path that was saved in preferences
                fileChooser.setCurrentDirectory(new File(path));

                fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "jpeg", "bmp"));
                fileChooser.setMultiSelectionEnabled(false);

                
                int result = fileChooser.showOpenDialog(workspaceView);
                if (result == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        fileChooser.setCurrentDirectory(file);

                        // Save the selected path
                        pref.put("DEFAULT_PATH", file.getAbsolutePath());
                        if (file.exists()) {
                                try {
                                        ImageModel imageModel = ImageModel.fromFile(file);
                                        addNewWindow(imageModel);
                                } catch (IOException e1) {
                                        workspaceView.showError("Can't load the image");
                                }
                        } else {
                                workspaceView.showError("Image does not exist");
                        }
                }
            }

            @Override
            public void fileMenuSaveClicked() {
                    //todo
            }

            @Override
            public void fileMenuCloseClicked() {
                    System.exit(0);
            }

            @Override
            public void steganographyMenuStartHiddingClicked() {
                   final HidingOperationView HidingView = new HidingOperationView(workspaceView, WorkspaceController.this);
			/*HidingView.setHideButtonActionListener(new ActionListener() {
				
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                SteganographyOperationsUtil.hidingOperation(
						HidingView.getSelectedFirstImageModel(), 
                                                HidingView.copyOfToHide, HidingView.posterisation);
                                HidingView.dispose();
                            }               
                        });*/
			
            }

            @Override
            public void steganographyMenuStartEncodingClicked() {                
                addNewWindow(SteganographyOperationsUtil.encodingOperation(getSelectedWindowImageModel()));                    
            }


            @Override
            public void aboutProgramMenuClicked() {
                    AboutView aboutView = new AboutView(workspaceView);
                    aboutView.setVisible(true);
            }

	}	
	

}

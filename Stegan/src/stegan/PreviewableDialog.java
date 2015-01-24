package stegan;
import java.awt.Frame;
import javax.swing.JDialog;

public class PreviewableDialog extends JDialog {
	
	private ScaledImagePanel beforeImagePanel;
	private ScaledImagePanel afterImagePanel;
	protected ImageModel beforeImageModel;
	protected ImageModel afterImageModel;
	
	public PreviewableDialog(Frame parent, String title, ImageModel imageModel) {
		super(parent, title);
		
		this.beforeImageModel = imageModel;
		this.afterImageModel = imageModel;
		beforeImagePanel = new ScaledImagePanel(beforeImageModel.getImage());
		afterImagePanel = new ScaledImagePanel(afterImageModel.getImage());

	}
	
	public ImageModel getAfterImageModel() {
		return afterImageModel;
	}

        public void setBeforeImageModel(ImageModel beforeImageModel){
            this.beforeImageModel = beforeImageModel;
            beforeImagePanel.setImage(beforeImageModel.getImage());
        }
        
	public void setAfterImageModel(ImageModel afterImageModel) {
		this.afterImageModel = afterImageModel;
		
		afterImagePanel.setImage(afterImageModel.getImage());
	}

	public ImageModel getBeforeImageModel() {
		return beforeImageModel;
	}

	public ScaledImagePanel getBeforeImagePanel() {
		return beforeImagePanel;
	}

	public ScaledImagePanel getAfterImagePanel() {
		return afterImagePanel;
	}

}

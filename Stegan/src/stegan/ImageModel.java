
package stegan;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;

import javax.imageio.ImageIO;

public class ImageModel extends Observable {
	
	private BufferedImage image;
	private String name;
	
	public ImageModel() {
	}
	/**
	 * konstruktor do klonowania obiektu
	 * 
	 * @param ImageModel obiekt do sklonowania
	 */
	public ImageModel(ImageModel original) {
		ColorModel colorModel = original.image.getColorModel();

		this.name = original.name + " copy";
		this.image = new BufferedImage(
			colorModel, 
			original.image.copyData(null), 
			colorModel.isAlphaPremultiplied(), 
			null
		);
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		
		imageChanged();
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}
	
	public int getPixelsCount() {
		return getWidth() * getHeight();
	}
	
	public int getPixelValue(int x, int y) {
		return getImage().getRaster().getPixel(x,y, new int[1])[0];
	}
	
	public void setPixelValue(int x, int y, int value) {
		getImage().getRaster().setPixel(x, y, new int[] {value});
	}

	public String getName() {
		if (name == null) {
			return "untitled";
		}

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static ImageModel fromFile(File file) throws IOException {
		BufferedImage bufImage = ImageIO.read(file);
		BufferedImage bufImageGray = new BufferedImage(bufImage.getWidth(), bufImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		Graphics bufImageGrayGraphics = bufImageGray.getGraphics();  
		bufImageGrayGraphics.drawImage(bufImage, 0, 0, null);  
		bufImageGrayGraphics.dispose();  
		
		ImageModel imageModel = new ImageModel();
		imageModel.setImage(bufImageGray);
		imageModel.setName(file.getName());
		
		return imageModel;
	}

	public static ImageModel fromHidden(int width, int height){
		
		BufferedImage bufImageGray = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		
		Graphics bufImageGrayGraphics = bufImageGray.getGraphics();  
                for(int x = 0; x < width; x++){
                    for(int y = 0; y < height; y++){
                        bufImageGray.setRGB(x,y, 0);
                    }
                }
                //bufImageGrayGraphics.drawImage(imgModel.getImage(), 0, 0, null);  
		bufImageGrayGraphics.dispose();  
		
		ImageModel imageModel = new ImageModel();
		imageModel.setImage(bufImageGray);
		imageModel.setName("decrypted");
		
		return imageModel;
	}
	/**
	 * @see http://stackoverflow.com/questions/10088465/need-faster-way-to-get-rgb-value-for-each-pixel-of-a-buffered-image
	 */
	public double[] getHistogram() {
		double[] histogram = new double[256];
		int[] pixel = new int[1];
		int pixelsCount = getWidth() * getHeight();
		
		Arrays.fill(histogram, 0);
		
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
		        pixel = image.getRaster().getPixel(x, y, new int[1]);
		        histogram[pixel[0]]++;
		    }
		}
		
		for (int i=0;i<256;i++) {
			histogram[i] = histogram[i] / pixelsCount;
		}
		
		return histogram;
	}
	
	public double[] getHistogramWithPixelsCount() {
		double[] histogram = new double[256];
		int[] pixel = new int[1];
		int pixelsCount = getWidth() * getHeight();
		
		Arrays.fill(histogram, 0);
		
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
		        pixel = image.getRaster().getPixel(x, y, new int[1]);
		        histogram[pixel[0]]++;
		    }
		}
		
		for (int i=0;i<256;i++) {
			histogram[i] = histogram[i];
		}
		
		return histogram;
	}
	
	public void imageChanged() {
		setChanged();
		notifyObservers();
	}
	
	public String toString() {
		return getName();
	}
}


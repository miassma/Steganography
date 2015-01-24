package stegan;

import java.awt.image.BufferedImage;
import static java.lang.Math.pow;
import java.math.*;

public class SteganographyOperationsUtil {   
    
// checking if the image to hide can fit the hiding image
// it returns the range of shades of gray that can be kept in the hiding image
    public static int checkImages(ImageModel hiding, ImageModel toHide, ImageModel copyOfToHide){
        int hidingSize = hiding.getWidth() * hiding.getHeight();
        int toHideSize = toHide.getWidth() * toHide.getHeight();
        int header = 40;    
            
            
            int value = 8;
            while((toHideSize * value + header) > hidingSize){  //if doesnt fit, reducing one range, checking again
                value--;
                if(value==0){
                    System.out.println("za duży obraz do ukrycia");
                    break;
                    
                }
            }
            if(value == 0) return -1;
            if(value<8) posterize(copyOfToHide, (int)pow(2, value)); //run the posterisation if needed
            return (int)pow(2, value);
    }

/*  preparing the hiding image
    we need zero on each LSB
    */
    public static void prepareHidingImage(ImageModel imgModel){
        for (int x = 0; x < imgModel.getWidth(); ++x) {
            for (int y = 0; y < imgModel.getHeight(); ++y) {
            	int color = imgModel.getImage().getRaster().getPixel(x, y, new int[1])[0];
                int temp = 254;
                int newColor = color & temp;
                int[] newColorPixel = {newColor};
                imgModel.getImage().getRaster().setPixel(x, y, newColorPixel);
            }
        }        
        imgModel.imageChanged();
    }
    
   
    //fullfill by leading zeros with the lenght of the option
    public static String fillString(String toFill, int option){
        String zero = "0";
        if (toFill.length() < option){            
            int temp = option - toFill.length();
            do{                
                toFill = zero.concat(toFill);
            }while(--temp >0);
        }        
        return toFill;
    }
    
    /* fullfill the string to get the color matching the posterisation range
    
       zum Beispiel:  1 will be 11111111 (2 ranges of gray)
            101 will become 10110110 (3 ranges of gray)
        1011 will become 1011 (4 ranges of gray)
    */
    public static String complete(String toComplete){
        while (toComplete.length() < 9){
            toComplete = toComplete+toComplete;
        }
        toComplete = toComplete.substring(0, 8); 
       // System.out.println("color after complete: "+ toComplete);
        return toComplete;
    }
   
    //hiding Image
    public static void hidingOperation(ImageModel hiding, ImageModel toHide, int value){
           
       prepareHidingImage(hiding);
       
       System.out.println("posterisation range: " + value);

       String posterisation = Integer.toString(value-1, 2);
       String hiddenWidth = Integer.toString(toHide.getWidth(), 2);
       String hiddenHeight = Integer.toString(toHide.getHeight(), 2);
      
       System.out.println("width: " + hiddenWidth);
       System.out.println("hide: " + hiddenHeight);
       System.out.println("posterisation: "+ posterisation);
       
       hiddenWidth = fillString(hiddenWidth, 16);
       hiddenHeight = fillString(hiddenHeight, 16);
       posterisation = fillString(posterisation, 8);

       String header = hiddenWidth;
       header = header.concat(hiddenHeight);
       header = header.concat(posterisation);
       System.out.println("header: "+header);
       System.out.println("header length: "+ header.length());
       
       int newColor;
       int temp = 0;
       int temp2 = 0;
       int bitsToCheck = (int)logb(value, 2); //how many bits of each pixel we have to hide for given postarisation
       System.out.println("Bits to check in hiding operation: "+bitsToCheck);
       int zero = 0;
       int one = 1;
       int i = 0;
       int j = 0;
       String colorOfToHideBinary = "";
      
       outerLoop:
        for (int x = 0; x < hiding.getWidth(); ++x) {
            for (int y = 0; y < hiding.getHeight(); ++y) {
            	int color = hiding.getImage().getRaster().getPixel(x, y, new int[1])[0];
                //filling header
                if(temp < header.length()){
                    if(header.charAt(temp)== '0'){
                        newColor = color | zero;                        
                    }else{ newColor = color | one;                        
                    }temp++;
                //hiding image
                }else{
                    /* 
                    getting the value of the next pixel of the image to hide, only if temp ==0, 
                    what means that it is the first pixel or each needed bits by the posterisation range
                    has been already checked
                    */
                    if(temp2 == 0){
                        
                        int colorOfToHide = toHide.getImage().getRaster().getPixel(i, j, new int[1]) [0];
                        colorOfToHideBinary = Integer.toString(colorOfToHide, 2);
                        colorOfToHideBinary = fillString(colorOfToHideBinary, 8);
                        //System.out.println("Hiding x:"+x+"; y:"+y+"; i:"+i+"; j:"+j+"colorToHide "+colorOfToHideBinary);
                    }
                    //System.out.println("temp2: " +temp2);
                    
                    //i check each value of the color in binary, but only as much as needed by the posterisation range                 
                    if (colorOfToHideBinary.charAt(temp2) == '0'){
                        newColor = color | zero;
                    }else { newColor = color | one;}
                    temp2++;
                    if (temp2 == bitsToCheck){
                        temp2 = 0;
                        j++;
                        if(j == toHide.getHeight()){
                            j = 0;
                            i++;                           
                        }
                    }                
                }
                int[] newColorPixel = {newColor};
                //System.out.println("new color pixel: "+newColor);
                hiding.getImage().getRaster().setPixel(x, y, newColorPixel);
                 if(i == toHide.getWidth()){
                                System.out.println("Stop at i="+i+" j="+j);
                                break outerLoop;
                }
            }  
        }
        hiding.imageChanged();     
        
    }
    
    //decrypting image
    
    public static ImageModel encodingOperation(ImageModel imgModel){
        int i = 0;
        int j = 0;
        String widthB = "";
        String heightB = "";
        String posterisationB = "";
        int temp = 0;
        int one = 1;
        
        /* loop for taking values from the header, seems to work pretty fine */
        outerLoop:
         for (int x = 0; x < imgModel.getWidth(); ++x) {
            for (int y = 0; y < imgModel.getHeight(); ++y) {
            	int color = imgModel.getImage().getRaster().getPixel(x, y, new int[1])[0];
                
                if(temp<16){
                        if ((color & one) == one) widthB = widthB.concat("1");
                        else widthB = widthB.concat("0");
                }else if(temp < 32){
                        if ((color & one) == one) heightB = heightB.concat("1");
                        else heightB = heightB.concat("0");
                }else if(temp <40){
                        if ((color & one) == one) posterisationB = posterisationB.concat("1");
                        else posterisationB = posterisationB.concat("0");
                }else{
                        //System.out.println("x: "+x+"; y: "+y+"; i: "+i+"; j: "+j);
                        break outerLoop;
                }temp++; j++;               
            }i++;
        }  
        
        int width = Integer.parseInt(widthB, 2);
        int height = Integer.parseInt(heightB, 2);
        int posterisation = Integer.parseInt(posterisationB, 2);
        int bitsToCheck = (int)logb(posterisation+1, 2);
        
        System.out.println("*****************************");
        System.out.println("ENCRYPTING");
        System.out.println("Bits to check:" + bitsToCheck);
        
        int temp2 = 0;
        String colorInBinary = "";
        
        System.out.println("Width from encoding: "+ width);
        System.out.println("Height from encoding: "+ height);
        System.out.println("Posterisation from encoding: "+ (posterisation+1));
        
        //preparing the canvas for the encoded image, width and height from the header
        ImageModel encryptedImage = ImageModel.fromHidden(width, height);
        
        int a = 0;
        int b = 0;
        
        /*  encoding the image
            starting after the point after the header, saved in the variables i,j            
        */
        
        outerLoop:
        for (int x = i; x < imgModel.getWidth(); ++x) {
            for (int y = j; y < imgModel.getHeight(); ++y) {
            	int color = imgModel.getImage().getRaster().getPixel(x, y, new int[1])[0];
                //System.out.println("x: "+x+"; y: "+y+" a:"+a+" b:"+b);
                
                /* pixel by pixel reading color of the hidden image
                    temp2 checks, where to stop - how many LSB of the hiding image keeps information bout one pixel of the hidden img
                */
                
                if ((color & one) == one) colorInBinary = colorInBinary.concat("1");
                        else colorInBinary= colorInBinary.concat("0");
                temp2++;
                if (temp2 == bitsToCheck){
                    temp2 = 0;
                    /*
                    fullfilling the color to the right by the given posterisation range
                        not to get by 2 levels of gray: 0 and 1
                        but 00000000 and 11111111
                    and so on....
                    */
                    colorInBinary = complete(colorInBinary); 
                    int newColor = Integer.parseInt(colorInBinary, 2);
                    colorInBinary = "";
                    int[] newColorPixel = {newColor};
                    //System.out.println("a = "+a+"; b= "+b+"; new color: "+newColor);
                    encryptedImage.getImage().getRaster().setPixel(a, b, newColorPixel);
                    
                    b++;
                    //System.out.println("x: "+x+"; y: "+y+" a:"+a+" b:"+b);
                    if(b == height){
                        a++;
                        b=0;
                    }if (a == width){
                        System.out.println("At the end hx ="+a+" ,hy="+b);
                        break outerLoop;
                    }           
                }               
            }
        }
      return encryptedImage;  
    }
    
    public static double logb( double a, double b ){
        return Math.log(a) / Math.log(b);
    }
    
    /*redukcja poziomow szarości */
    public static void posterize(ImageModel imgModel, int value) {
        int[] lut = new int[256];
        float param1 = 255.0f / (value - 1);
        float param2 = 256.0f / (value);
        for (int i = 0; i < 256; ++i) {
            lut[i] = (int)((int)(i / param2) * param1);
        }
        useLUT(imgModel, lut);
    }

    
    public static void useLUT(ImageModel imgModel, int[] lut) {
        for (int x = 0; x < imgModel.getWidth(); ++x) {
            for (int y = 0; y < imgModel.getHeight(); ++y) {
            	int color = imgModel.getImage().getRaster().getPixel(x, y, new int[1])[0];
                
                int[] newColorPixel = {lut[color]};
                imgModel.getImage().getRaster().setPixel(x, y, newColorPixel);
            }
        }        
        imgModel.imageChanged();
    }
    
    public static void reduce(ImageModel imgModel, int value){
        int[] lut = new int[256];
        int param1 = 255 / (value-1);
        int temp = param1;
        int brightness = 0;
        for (int i = 0; i < 256; ++i) {            
            if(i==temp){
                temp=param1+temp;
                brightness++;
            }
            lut[i] = brightness;
        }
        useLUT(imgModel, lut);
    }
    
    public static void expand(ImageModel imgModel, int value){
        int[] lut = new int[256];
        int param1 = 255 / (value-1);
        int brightness = 0;
             
               
        for (int i = 0; i < value; ++i) {          
            lut[i] = brightness;
            brightness = param1+brightness;
        }
        useLUT(imgModel, lut);
    }

	public static void brightness(ImageModel imgModel, int value) {
        int[] lut = new int[256];
        for(int i = 0; i < 256; ++i){
            lut[i] = Math.max(0, Math.min(255, i + value));
        }
        useLUT(imgModel, lut);	
	}

	public static void contrast(ImageModel imgModel, int value) {
		double mult = 1.0 + ((double) value)/100.0;
        int[] lut = new int[256];
        for(int i = 0; i < 256; ++i){
            double temp = ((double)i / 255.0) - 0.5;
            temp = temp * mult + 0.5;

            lut[i] = Math.max(0, Math.min(255, (int)(temp*255.0)));
        }
        useLUT(imgModel, lut);
	}

	public static void threshold(ImageModel imgModel, double low, double high, boolean binarize, boolean negative) {
		int[] lut = new int[256];
		int inRangeVal;
     	int offRangeVal;
        
        for (int i = 0; i < 256; ++i) {
        	 if (binarize) {
             	inRangeVal =  negative ? 255 : 0;
             	offRangeVal = negative ? 0 : 255;
             } else {
             	inRangeVal =  negative ? 255 - i  : i;
             	offRangeVal = 0;
             }
        	 
        	lut[i] = (i>=low && i<=high) ? inRangeVal : offRangeVal;
        }
        
        useLUT(imgModel, lut);
	}
	
   
    
    public static boolean isPixelInRange(int x, int y, int width, int height) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }
}

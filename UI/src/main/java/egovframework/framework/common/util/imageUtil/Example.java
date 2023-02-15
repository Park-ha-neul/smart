package egovframework.framework.common.util.imageUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import egovframework.framework.common.cal.util.CalUtil;

/**
 *
 * @author James
 */
public class Example {

	private static Log log = LogFactory.getLog(Example.class);

	static String targetPath = "C:/Users/Venus/Pictures/";

    public static void main(String[] args) {

    	String imgLocation = "C:/Users/Venus/Pictures/article.jpg";
    	/*
        String imgLocation = (args.length == 1 ? args[0] : null);
        if (imgLocation == null)
            throw new IllegalArgumentException("One argument required: path-to-image");
        */

        try {
            Image img = null;
            if (imgLocation.startsWith("http")) {
                //read the image from a URL
                img = ImageLoader.fromUrl(new URL(imgLocation));
            }
            else {
                File f = new File(imgLocation);
                if (!f.exists() || !f.isFile())
                    throw new IllegalArgumentException("Invalid path to image");
                else {
                    //read the image from a file
                    img = ImageLoader.fromFile(f);
                }
            }

            //output source type
//            System.out.println("Image source type: "+ img.getSourceType());
            log.error("Image source type: "+ img.getSourceType());
            //output dimensions
//            System.out.println("Image dimensions: "+ img.getWidth() +"x"+ img.getHeight());
            log.error("Image dimensions: "+ img.getWidth() +"x"+ img.getHeight());

            //crop it
            Image cropped = img.crop(200, 200, 500, 350);
            //cropped.writeToJPG(new File("cropped.jpg"), 0.95f);
            cropped.writeToFile(new File(targetPath + "cropped.png"));
            cropped.dispose();


            //resize
            Image resized = img.getResizedToWidth(400);
            //save it with varying softness and quality
            softenAndSave(resized, 0.95f, 0f);
            softenAndSave(resized, 0.95f, 0.1f);
            softenAndSave(resized, 0.95f, 0.2f);
            softenAndSave(resized, 0.95f, 0.3f);
            softenAndSave(resized, 0.8f, 0.08f);
            softenAndSave(resized, 0.6f, 0.08f);
            softenAndSave(resized, 0.4f, 0.08f);
            resized.dispose();

            /*
            //write a 0.95 quality JPG without using Sun's JPG codec
            resized.writeToFile(new File(targetPath + "resized--q0.95--s0.0--nocodec.jpg"));

            //resize it to a square with different settings for edge cropping
            squareIt(img, 400, 0.0, 0.95f, 0.08f);
            squareIt(img, 400, 0.1, 0.95f, 0.08f);
            squareIt(img, 400, 0.2, 0.95f, 0.08f);

            //small thumbs
            squareIt(img, 50, 0.0, 0.95f, 0.08f);
            squareIt(img, 50, 0.1, 0.95f, 0.08f);
            squareIt(img, 50, 0.1, 0.5f, 0.08f);
            */
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private static void softenAndSave(Image img, float quality, float soften) throws IOException {
    	if(img.getSourceType().equals(ImageType.JPG)){
    		//img.soften(soften).writeToJPG(new File(targetPath + "resized--q"+ quality +"--s"+ soften +".jpg"), quality);
    		img.soften(soften).writeToFile(new File(targetPath + "resized--q"+ quality +"--s"+ soften +".jpg"));
    	}
    	else{
    		img.soften(soften).writeToFile(new File(targetPath + "resized--q"+ quality +"--s"+ soften +".png"));
    	}
    }

    /*
    private static void squareIt(Image img, int width, double cropEdges, float quality, float soften) throws IOException {
        Image square = img.getResizedToSquare(width, cropEdges).soften(soften);
        square.writeToJPG(new File(targetPath + "square--w"+ width +"--e"+ cropEdges +"--q"+ quality +".jpg"), quality);
        square.dispose();
    }
    */

}

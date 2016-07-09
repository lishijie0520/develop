package com.develop.sign;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;



public class ImageFormatConversion {
	public ImageFormatConversion() {
	}
    public static void imageFormat(String fileName,OutputStream out,String fileType,String formatFileType) throws Exception{
    	File file2 = new File(fileName);
		FileInputStream fis = new FileInputStream(file2);
		Iterator readers = ImageIO.getImageReadersByFormatName(fileType);
		ImageInputStream iis = ImageIO.createImageInputStream(fis);
		while (readers.hasNext()){
			ImageReader reader = (ImageReader)readers.next();
			reader.setInput(iis,true);
			BufferedImage buffer = reader.read(0);
			ImageIO.write(buffer,formatFileType,out); 
		}
    }
    public static void main(String[] args) throws Exception {
    	
    	String formatFile="/Users/hsm/git/develop/src/test/resources/image/id.pbm";
    	
    	String  fileName= "/Users/hsm/git/develop/src/test/resources/image/id.jpg";
    	
    	
    	FileOutputStream out = new FileOutputStream(new File(formatFile));
    	String fileType="pnm";
    	String formatFileType="jpg";
    	ImageFormatConversion.imageFormat(fileName,out,formatFileType,fileType);
    	
    	
    	
    	
    	
	}
}

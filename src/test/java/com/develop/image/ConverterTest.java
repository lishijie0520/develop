package com.develop.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.sun.media.jai.codec.PNMEncodeParam;

public class ConverterTest {

	@Test
	public void testname() throws Exception {
		System.out.println(Arrays.asList(ImageIO.getReaderMIMETypes()));
		System.out.println(Arrays.asList(ImageIO.getWriterFormatNames()));
		System.out.println(Arrays.asList(ImageIO.getReaderFormatNames()));

		URL pngFile = getClass().getResource("/test.png");
		BufferedImage img = ImageIO.read(pngFile);

		for (String type : ImageIO.getWriterFormatNames()) {
			if (type.equalsIgnoreCase("jpg") || type.equalsIgnoreCase("jpeg")) {
				// Avoid issue #6 on OpenJDK8/Debian 
				continue;
			}
			
			File f = File.createTempFile("imageio-test", "." + type);
			ImageIO.write(img, type, f);
			System.out.println(f);
			ImageIO.read(f);
		}
	}
	
	
	@Test
	public void testname1() throws Exception {
		System.out.println(Arrays.asList(ImageIO.getReaderMIMETypes()));
		System.out.println(Arrays.asList(ImageIO.getWriterFormatNames()));
		System.out.println(Arrays.asList(ImageIO.getReaderFormatNames()));

		
		//aa();
		
		
		URL resource = getClass().getResource("/82511_20160616092323921.jpg");
		//URL resource = getClass().getResource("/test.png");
		//URL resource = new URL("/Users/hsm/git/develop/src/test/resources/test.png");
		//aa();
		//File file = new File("/Users/hsm/git/develop/src/test/resources/82511_20160616092323921.png");
		//URL resource = file.toURI().toURL();
 		BufferedImage img = ImageIO.read(resource);
		String type = "pnm";
		File f =  new  File("/Users/hsm/Desktop/82511_20160616092323921."+ type);
		ImageIO.write(img, type, f);
		System.out.println(f);
		ImageIO.read(f);
	}
	@Test
	public void test1(){
		
		//convert("/Users/hsm/git/develop/src/test/resources/82359_20160617103203896.png", formatName, result);
	}
	
	//82359_20160617103203896
	 public static void convert(String source, String formatName, String result) {  
	        try {  
	            File f = new File(source);  
	            f.canRead();  
	            BufferedImage src = ImageIO.read(f);  
	            ImageIO.write(src, formatName, new File(result));  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	
	public void aa() throws Exception{
		File file = new File("/Users/hsm/git/develop/src/test/resources/82511_20160616092323921.png");
		URL resource = file.toURI().toURL();
 		BufferedImage img = ImageIO.read(resource);
		String type = "jpg";
		File f =  new  File("/Users/hsm/git/develop/src/test/resources/82511_20160616092323921."+ type);
		ImageIO.write(img, type, f);
		System.out.println(f);
		ImageIO.read(f);
		
	}
	
	public void ss(){
		   // RenderedImage image = "// the image to be stored //";
		    RenderedImage image = null;
		    String filename = "// path and name of the file to be written //";
		    String format = "// the format of the file //";

		    PNMEncodeParam param = new PNMEncodeParam();
		    param.setRaw(false);

		    //RenderedOp op = JAI.create("filestore", image,filename, format, param);
		   
	}
	
	
	
	@Test
	public void testname2() throws Exception {
		System.out.println(Arrays.asList(ImageIO.getReaderMIMETypes()));
		System.out.println(Arrays.asList(ImageIO.getWriterFormatNames()));
		System.out.println(Arrays.asList(ImageIO.getReaderFormatNames()));

		
		//aa();
		
		
		URL resource = getClass().getResource("/82511_20160616092323921.jpg");
		//URL resource = getClass().getResource("/test.png");
		//URL resource = new URL("/Users/hsm/git/develop/src/test/resources/test.png");
		//aa();
		//File file = new File("/Users/hsm/git/develop/src/test/resources/82511_20160616092323921.png");
		//URL resource = file.toURI().toURL();
 		BufferedImage img = ImageIO.read(resource);
		String type = "pnm";
		File f =  new  File("/Users/hsm/Desktop/82511_20160616092323921."+ type);
		ImageIO.write(img, type, f);
		System.out.println(f);
		ImageIO.read(f);
	}
	
	
	@Test
	public void test(){
		try {
			
			//相对路径
			String  path = this.getClass().getResource("/").getFile().toString();
	        path = path.replace("test-classes", "classes");
			
			String pngImagePath = "/Users/hsm/git/develop/src/test/resources/82511_20160616092323921.png";
			String jpgImagePath = "/Users/hsm/git/develop/src/test/resources/82511_20160616092323921.jpg";
			String pnmImagePath = "/Users/hsm/git/develop/src/test/resources/82511_20160616092323921.pnm";
			
			
			String jpgPath = pngToJpg(pngImagePath, jpgImagePath);//png --> jpg
			
			String pnmPath = converter(jpgPath, pnmImagePath, "pnm");//jpg --> pnm
			
			deleteFile(jpgPath);
			
			deleteFile(pnmPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 *  图片格式转换
	 * @param sourceImagePath 源图片路径
	 * @param targetImagePath 转化图片路径
	 * @param type 转成图片的类型
	 * @return 新格式图片路径
	 * @throws Exception
	 */
	public String  converter(String sourceImagePath,String targetImagePath,String type) throws Exception {
		
		//URL resource = getClass().getResource("/test.jpg");
		File file = new File(sourceImagePath);
		
		URL resource = file.toURI().toURL();
		
 		BufferedImage img = ImageIO.read(resource);
		File f =  new  File(targetImagePath);
		
		ImageIO.write(img, type, f);
		
		ImageIO.read(f);
		
		return targetImagePath;
		
	}
	
	

	/**
	 * 透明的png转换成背景为白色的jpg图片 
	 * @param pngImagePath
	 * @param jpgImagePath
	 * @return 新格式图片路径
	 */
	public String  pngToJpg(String pngImagePath,String jpgImagePath) {
		BufferedImage bufferedImage;

		try {

			// read image file
			bufferedImage = ImageIO.read(new File(pngImagePath));

			// create a blank, RGB, same width and height, and a white
			// background
			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg",new File(jpgImagePath));

			
		} catch (IOException e) {

			e.printStackTrace();

		}
		return jpgImagePath;
	}
	
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
       boolean flag = false;
       File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
	
	
}

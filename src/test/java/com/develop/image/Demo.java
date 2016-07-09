package com.develop.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
 
import javax.imageio.*;
public class Demo {
  public static final String JPG = "jpg";
  public static final String GIF = "gif";
  public static final String PNG = "png";
  public static final String BMP = "bmp";
  
  public static final String PBM = "pbm";//不支持
  public static void main(String[] args) {
    String src = "/Users/hsm/git/develop/src/test/resources/image/32637_20160615104032381.";
   // String src = "/Users/hsm/git/develop/src/test/resources/image/id.";
//    new Demo().Conversion(JPG,PNG,src);//JPG转成PNG
//    new Demo().Conversion(JPG,GIF,src);//JPG转成GIF
   // new Demo().Conversion(JPG,BMP,src);//JPG转成BMP

    
    new Demo().Conversion(PNG,PBM,src);//JPG转成BMP
    //其余格式转化只要调用Conversion函数即可
  }
   
  //inputFormat表示原格式，outputFormat表示转化后的格式
  public void Conversion(String inputFormat,String outputFormat,String src){
     
    try {
      File input = new File(src+inputFormat);
      BufferedImage bim = ImageIO.read(input);
      File output = new File(src+outputFormat);
      ImageIO.write(bim, outputFormat, output);
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
 
}
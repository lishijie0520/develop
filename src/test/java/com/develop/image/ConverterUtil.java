package com.develop.image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ConverterUtil {

  // JGP格式
  public static final String JPG = "jpeg";
  // GIF格式
  public static final String GIF = "gif";
  // PNG格式
  public static final String PNG = "png";
  // BMP格式
  public static final String BMP = "bmp";
 //PBM PGM PPM 统称PNM
  
  public static void converter(File imgFile,String format,File formatFile)
      throws IOException{
    BufferedImage bIMG =ImageIO.read(imgFile);
    ImageIO.write(bIMG, format, formatFile);
  }
 
 
  
  public static void main(String[] args) {
    try {
      // 转换为JGP
      ConverterUtil.converter(new File("c:\\psb.jpg"),JPG, new File("c:\\psb2.jpg"));
      // 转换为GIF
      ConverterUtil.converter(new File("c:\\psb.jpg"),GIF, new File("c:\\psb2.gif"));
      // 转换为PNG
      ConverterUtil.converter(new File("c:\\psb.jpg"),PNG, new File("c:\\psb2.png"));
      // 转换为BMP
      ConverterUtil.converter(new File("c:\\psb.jpg"),BMP, new File("c:\\psb2.bmp"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
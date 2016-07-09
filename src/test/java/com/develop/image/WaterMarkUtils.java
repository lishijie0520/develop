package com.develop.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sun.image.codec.jpeg.*;


public class WaterMarkUtils{
    public static void main(String[]args) throws Exception{
        
        //1.jpg是你的 主图片的路径
        InputStream is = new FileInputStream("/Users/hsm/git/develop/src/test/resources/test222-1.png");
        
        File picture = new File("/Users/hsm/git/develop/src/test/resources/test222-1.png");  
       
        BufferedImage sourceImg =ImageIO.read(new FileInputStream(picture));   
        System.out.println(String.format("%.1f",picture.length()/1024.0));  
        System.out.println(sourceImg.getWidth());  
        System.out.println(sourceImg.getHeight());  
        
        int width = sourceImg.getWidth();
        int height = sourceImg.getHeight();
        
        
        //通过JPEG图象流创建JPEG数据流解码器
        JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
        //解码当前JPEG数据流，返回BufferedImage对象
        BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
        //得到画笔对象
        Graphics g = buffImg.getGraphics();
        
        //创建你要附加的图象。
        //2.jpg是你的小图片的路径
       // ImageIcon imgIcon = new ImageIcon("2.jpg"); 
        
        //得到Image对象。
        //Image img = imgIcon.getImage();
        
        //将小图片绘到大图片上。
        //5,300 .表示你的小图片在大图片上的位置。
        //g.drawImage(img,5,330,null);
        
        
        
        //设置颜色。
        g.setColor(Color.BLACK);
        
        
        //最后一个参数用来设置字体的大小
        Font f = new Font("Times New Roman",Font.BOLD,14);
        
        g.setFont(f);
        
        
        
        //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
        g.drawString("0F418317",width/2,height/2);
        
        g.dispose();
        
        
        
        OutputStream os = new FileOutputStream("union.jpg");
        
        //创键编码器，用于编码内存中的图象数据。
        
        JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
        en.encode(buffImg);
        
        
        is.close();
        os.close();
        
        System.out.println ("合成结束。。。。。。。。");
        
        
    }   
    
    
    
}
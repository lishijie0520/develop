package com.develop.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

public class ConvertJPEG
{

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException
    {
        File __pngFile = new File("/Users/hsm/git/develop/src/test/resources/82359_20160617103203896.png");
        File __jpgFile = new File("/Users/hsm/git/develop/src/test/resources/82359_20160617103203896.jpg");
        writeJPEG(__pngFile, __jpgFile, 80);
    }

    public static void writeJPEG(File $source, File $dest, int $quality) throws IOException
    {
        String __formatName = "jpeg";
        BufferedImage __image = ImageIO.read($source);
        //如果图像是透明的，就丢弃Alpha通道
        if(__image.getTransparency() == Transparency.TRANSLUCENT)
            __image = get24BitImage(__image);
            //__image = get24BitImage(__image, Color.BLACK);
        ImageWriter __writer = ImageIO.getImageWritersByFormatName(__formatName).next();
        ImageWriteParam __writeParam = __writer.getDefaultWriteParam();
        FileOutputStream __out = new FileOutputStream($dest);
        __writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        __writeParam.setCompressionQuality((float)$quality/100f);
        __writer.setOutput(ImageIO.createImageOutputStream(__out));
        __writer.write(null, new IIOImage(__image, null, null), __writeParam);
        __out.flush();
        __out.close();
        __writer.dispose();
    }

    /**
     * 使用删除alpha值的方式去掉图像的alpha通道
     * @param $image
     * @return
     */
    protected static BufferedImage get24BitImage(BufferedImage $image)
    {
        int __w = $image.getWidth();
        int __h = $image.getHeight();
        int[] __imgARGB = getRGBs($image.getRGB(0, 0, __w, __h, null, 0, __w));
        BufferedImage __newImg = new BufferedImage(__w, __h, BufferedImage.TYPE_INT_RGB);
        __newImg.setRGB(0, 0, __w, __h, __imgARGB, 0, __w);
        return __newImg;
    }

    /**
     * 使用绘制的方式去掉图像的alpha值
     * @param $image
     * @param $bgColor
     * @return
     */
    protected static BufferedImage get24BitImage(BufferedImage $image, Color $bgColor)
    {
        int $w = $image.getWidth();
        int $h = $image.getHeight();
        BufferedImage __image = new BufferedImage($w, $h, BufferedImage.TYPE_INT_RGB);
        Graphics2D __graphic = __image.createGraphics();
        __graphic.setColor($bgColor);
        __graphic.fillRect(0,0,$w,$h);
        __graphic.drawRenderedImage($image, null);
        __graphic.dispose();
        return __image; 
    }

    /**
     * 将32位色彩转换成24位色彩（丢弃Alpha通道）
     * @param $argb
     * @return
     */
    public static int[] getRGBs(int[] $argb)
    {
        int[] __rgbs = new int[$argb.length];
        for(int i=0;i<$argb.length;i++)
        {
            __rgbs[i] = $argb[i] & 0xFFFFFF;
        }
        return __rgbs;
    }
}

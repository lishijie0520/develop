package com.develop.sign;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * JBIG图片生成工具类
 */
public class JbigUtils {

    private static Logger log = Logger.getLogger(JbigUtils.class);

    static{
        try {
            //System.loadLibrary("WIN7_64_JBIG");
            System.load("/Users/hsm/git/develop/src/test/java/com/develop/sign/libWIN7_64_JBIG.so");
        }catch (UnsatisfiedLinkError e){
        	e.printStackTrace();
           // log.debug( "Cannot load WIN7_64_JBIG library:\n " + e.toString() );
        }catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 调用.so内部的图片处理方法
     * @param jfnin 待转换的文件路径
     * @param jfout 换后的文件路径
     * @return
     */
    public static native boolean ConvertPBMtoJBGbySO(String jfnin,String jfout);

    /**
     * 将.pbm格式的图片转换为.jbig格式的图片
     * @param jfnin 待转换的文件路径
     * @return 生成的jbig文件路径
     */
    public static String ConvertPBMtoJBG(String jfnin){
        String jfoutPbm = Png2Pbm(jfnin);
        String jfoutJBG = jfoutPbm.replace(".pbm",".jbig");
        boolean flag = ConvertPBMtoJBGbySO(jfnin,jfoutJBG);
        if(flag){
            return jfoutJBG;
        }else {
            return null;
        }
    }

    /**
     * 将手刷签名的.png图片转换为.pbm图片
     * @param jfnin 待转换的文件路径
     * @throws IOException
     */
    public static String Png2Pbm(String jfnin){
        String jfoutPbm = jfnin.replace(".png",".pbm");
        try {
            File out= null;
            File in = new File(jfnin);
            BufferedImage input = ImageIO.read(in);
            out = new File(jfoutPbm);
            ImageIO.write(input, "PNG", out);
            input.flush();
        }catch (IOException e){
            log.debug(jfnin + "文件不存在!");
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return jfoutPbm;
    }

    public static void main(String[] args){
        System.out.println("JavaLibrary :"+System.getProperty("java.library.path"));
        String jfnin = "/Users/hsm/git/develop/src/test/resources/image/32637_20160615104032381.png";
        JbigUtils.ConvertPBMtoJBG(jfnin);
    }
}

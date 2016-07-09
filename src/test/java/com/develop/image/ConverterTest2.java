package com.develop.image;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.converter.pack.JBigInflateConverter;


//
public class ConverterTest2 {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
			logger.info("图片格式转换成功[{}]", new Object[] { jpgPath });
			String pnmPath = converter(jpgPath, pnmImagePath, "pnm");//jpg --> pnm
			logger.info("图片格式转换成功[{}]", new Object[] { pnmPath });
			
			deleteFile(jpgPath);
			
			deleteFile(pnmPath);
			
		} catch (Exception e) {
			logger.error("图片格式转换失败 [{}]", new Object[] { e.getMessage()});
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
		
		
		aa(ImageIO.read(f));
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
    
    
    //转成单色位图
    public void aa(BufferedImage bufferedImage) throws IOException{
    	//BufferedImage sourceImg = ImageIO.read(new File(bufferedImage));  
    	BufferedImage sourceImg = bufferedImage;  
    	  int h = sourceImg.getHeight();
    	  int w = sourceImg.getWidth();
    	  int[] pixels = new int[w * h]; // 定义一数组，用来存储图片的象素
    	  int gray;
    	 
    	  PixelGrabber pg = new PixelGrabber(sourceImg, 0, 0, w, h, pixels,    0, w);
    	  try {
    	   pg.grabPixels(); // 读取像素值
    	  } catch (InterruptedException e) {
    	   System.err.println("处理被异常中断！请重试！");
    	  }
    	 
    	  for (int j = 0; j < h; j++) // 扫描列 
    	  {
    	   for (int i = 0; i < w; i++) // 扫描行
    	   { // 由红，绿，蓝值得到灰度值
    	    gray = (int) (((pixels[w * j + i] >> 16) & 0xff) * 0.8);
    	    gray += (int) (((pixels[w * j + i] >> 8) & 0xff) * 0.1);
    	    gray += (int) (((pixels[w * j + i]) & 0xff) * 0.1);
    	    pixels[w * j + i] = (255 << 24) | (gray << 16) | (gray << 8)
    	      | gray;
    	   }
    	  }
    	  MemoryImageSource s = new   MemoryImageSource(w,h,pixels,0,w);
    	  Image img =Toolkit.getDefaultToolkit().createImage(s);
    	  BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
    	  buf.createGraphics().drawImage(img, 0, 0, null);
    	  
    	  ImageIO.write(buf, "BMP", new File("/Users/hsm/Desktop/images/111.bmp"));
    }
    
    @Test
    public  void jbig() {
		//String data = "0000010000000260000000CC000000070800031CF4D880FF02FF02FF02FF02FF029BCF1ECE48E7EC7980FF0207C2BEA1F4B46035BE0BBA4FB96393920B9458EC87EAA398DFA5B8C0FF024C7F9F8127AA01BE289F6DCBE2FEC2D1A0FF028C42D508F50D8D34763029104AF31FD176E033758752F6FF023C8AC22948362C4FDEE9C0D75772248CEB0D940D4980FF02AFBB134174E053689D985EC6AC029483C4726F80FF026B8BDC4373AC99895512230CF99F991E85A79318D46E1F644F30DC27E9925D8BE92E62CB5391F4FF02A19DC958119F99DAB5DC145BAE35E360C5DF7BE7B26A7EEC3D11CDA880706271BE695ED3383E9DF2A3D53E90FF0292EC5B58F2F4C68C32EF175CE859B99C3FD8198EA031111CC975FEDB7B1D355DC7FEFCAE76B6CADA8ABCFF02E0D627E8971C1806049C710B625FC6D984A860468A41E07F044AB66131D3B78626920291CA518AAC6A092D4F9D54B3FF02F46F363CDB8097EACAC44A1AB1461972449C5A9BB48569392D3F797179604643C2B22F8B400D4F134D05D8564365A6DB2306F47540FF02F92DB8399C79FC49B380C96FCC9A9D7693F7D65F2F75281212792E1C68000B6212FF005B41CC9A37CFD7434630FF02F4FE3BB97EE948473BDC15883F598D1880267F21CC5B462C0B2A35051860D13589BA0580FF02F2F727E57869B6A80ABE45C84EB2D8ED960D4E72E227367E42F92D830B5CD6AA85E16AE0C548D1D533AC0A320F63C0FF02ED9C860233F7554A28FD3167B6B0DF98E7875784F759040B37C16D78152512CF7FE402DE43A5D080FF02FB7E79A27A4254D910E299EE0985AABE670051E9D656027BFB425CAE9C24FF02BABCEA6647689C32C114B4EEE466B2FA5F17BAC0FF0251B928B49D2B0F6F8BACFF0212A2A17C2832B20035C8FF020DBC58FF02FF02FF02FF02FF02FF02";
		//String data="0000010000000140000000AA000000070800031CFF02FF02C2347F2D42C9D48BEBB224FF02045BB52AB96167389D1409B0FF0251E74C68F12FFF0287ABD82E4A17BF13EDCA5A2AFF027E23FDF3C8C5C78977E7B4F14ACB10C26FA0FF025E7C8C39C6D7B15AB0B880FF022A728537E57493F8E0FF0224F3A175B4CE593DE980662682273A7DF8A0FF0253023A58716E4DA80527D5CBD0505562F8C2EA42D0FF029473BC2009ECFED56FD14937859FDFFF024186841113B48C20CD6F76EE6DE780FF02ABC97AEA8FCCFF02D02150FF02FF02FF02FF02FF02FF02FF02FF02FF02FF02FF02";//签名域
		//String data="028200000100000000F000000061000000020800031CFF02FF02FF02FF02FF02FF02FF02FF02FF02FF02FF02FF02FF02FF02FF02FF02FF02EA36203013FF023201350AAC78FF0246F197BEFF0211F8FF0211E2305AF35C7560565DC8B8E0FF023256F9F421E128F2EA4A2CB3738B621840FF0216EA147751361DBFEE35B89C50FF0243E8B8401FBC45D02AC0FF023571898F3852D637B724FF027563207D3329E354854E17EDACFAE4FF02727010328DF8980C48C0FF028AA159615BBA6EFF02030968F43680FF02A3FE9A5EB8B6A1F7D594A9B7FF02973FFCBFFE68662BFC0294AF5388EF40FF029999B2AA194084FF020760FF02A560FF02FF02FF02FF028CC0FF02C3FF02FF00A4FF02F0FF02FF02FF02FF02FF02FF02FF02FF02";
		String data="000001000000010000000050000000070800031CF4F8FF02FF02CDD5ECE4884E05EA30A0165436901711C136CFB8FF023EC2C1E035A81EA5F0B174FB88C470FF020BF261FBEE60F8073423B3FDDBCFA30417FCB4F8056238A107E63AAF6BCDCEE0FF027CEDF547805A0AB0F5F0CCA74A64AAF1EFCF360D788E8FE9F5182AFF00DEADF43A38246480FF02700ADF3F3F765AFA8E9C325025A4A24A3A44569A4FC4BE28F6287D537999F400D927E198CD6179C65C18FF0266557A853E64FA2C452F9A90F9638501E5FD53D1B11295347B601F54A70731E0FAA620C79B5038FF02490D5EB1309F728C462366D3C69F8CFF028EAF60FF02FF02FF02";
		createImg(data,"/Users/hsm/Desktop/images/gld");
	}
	
	public void createImg(String data, String fileName) {
		
		try {
		File file = new File(fileName + ".jbig");
		FileOutputStream fis = new FileOutputStream(file);
		fis.write(HexString2Bytes(data));//16进制转为byte数组,网上方法很多
		fis.flush();
		fis.close();

		JBigInflateConverter jic = new JBigInflateConverter();//调用jar包里面的方法
		 jic.DoConvert(fileName+".jbig", fileName+".bmp");
		} catch (Exception e) {
		e.printStackTrace();
		
		}
	}
	private static int parse(char c) {  
	    if (c >= 'a')  
	        return (c - 'a' + 10) & 0x0f;  
	    if (c >= 'A')  
	        return (c - 'A' + 10) & 0x0f;  
	    return (c - '0') & 0x0f;  
	}  
	
	public static byte[] HexString2Bytes(String hexstr) {  
	    byte[] b = new byte[hexstr.length() / 2];  
	    int j = 0;  
	    for (int i = 0; i < b.length; i++) {  
	        char c0 = hexstr.charAt(j++);  
	        char c1 = hexstr.charAt(j++);  
	        b[i] = (byte) ((parse(c0) << 4) | parse(c1));  
	    }  
	    return b;  
	}  
	
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
}

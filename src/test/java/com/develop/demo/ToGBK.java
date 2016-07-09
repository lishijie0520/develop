package com.develop.demo;

import org.junit.Test;

public class ToGBK {
	@Test
	public void test(){
		//E4B8AAE4BD93E688B7E7BF81E7BF8AE68890
		//4E2A4F5362378D756D77519B
        System.out.println(HexString2Bytes("E4B8AAE4BD93E688B7E7BF81E7BF8AE68890"));
        System.out.println(HexString2Bytes("4E2A4F5362378D756D77519B"));
        
    	System.out.println(enUnicode("个体户赵海军"));
		System.out.println(toChineseHex("个体户赵海军"));
		System.out.println(byte2hex("个体户赵海军".getBytes()));
		
	}
	//16进制转为byte数组
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
		private static int parse(char c) {  
		    if (c >= 'a')  
		        return (c - 'a' + 10) & 0x0f;  
		    if (c >= 'A')  
		        return (c - 'A' + 10) & 0x0f;  
		    return (c - '0') & 0x0f;  
		}  
		//转成16进度
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
		public static String enUnicode(String content){//将汉字转换为16进制数
			String enUnicode=null;
			for(int i=0;i<content.length();i++){
				if(i==0){
					enUnicode=getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
				}else{
					enUnicode=enUnicode+getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
				}
			}
			return enUnicode;
		}
		private static String getHexString(String hexString){
			String hexStr="";
			for(int i=hexString.length();i<4;i++){
				if(i==hexString.length())
					hexStr="0";
				else
					hexStr=hexStr+"0";
			}
			return hexStr+hexString;
		}
		  public static String toChineseHex(String s)
		    {
		        String ss = s;
		        byte[] bt = ss.getBytes();
		        String s1 = "";
		        for (int i = 0; i < bt.length; i++)
		        {
		            String tempStr = Integer.toHexString(bt[i]);
		            if (tempStr.length() > 2)
		                tempStr = tempStr.substring(tempStr.length() - 2);
		            s1 = s1 + tempStr;
		        }
		        return s1.toUpperCase();
		    }
		
}

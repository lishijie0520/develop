package com.develop.hex;
import java.text.ParseException;


public class Aa {
	private static String hexStr = "0123456789ABCDEF"; 
    public static void main(String args[]) throws ParseException{
    	//070816070  8499510
    	
    	//07081607 08499510  
 	   String code1 = encode("9CE00K00781065");
     	   String code2 = encode("TOX$123FTOX$321");
 	   System.out.println(code1);
 	   System.out.println(code2);
 	   System.out.println("7F0C1D14017903767F6814010203");
 	   
 	    String result ="";
 	   for(int i=0; i<code1.length();i++){
 		   char c1 = code1.charAt(i);
 		   char c2 = code2.charAt(i);
 		   String a = convertHexToBinary(c1+"");
 		   String b = convertHexToBinary(c2+"");
 		   byte [] aa = a.getBytes();
 		   byte [] bb = b.getBytes();
 		   String cc ="";
 		   for( int j = 0;j<aa.length;j++ ){
 			  cc +=   aa[j]^bb[j];   
 		   }
 		   String tmp =Integer.toHexString(Integer.valueOf(cc,2).intValue()).toString();
 		  
 		  result +=tmp;
 	   }
    }
    
    public static String convertHexToBinary(String hexString){
        long l = Long.parseLong(hexString, 16);
        String binaryString = Long.toBinaryString(l);
        int shouldBinaryLen = hexString.length()*4;
        StringBuffer addZero = new StringBuffer();
        int addZeroNum = shouldBinaryLen-binaryString.length();
        for(int i=1;i<=addZeroNum;i++){
            addZero.append("0");
        }
        return addZero.toString()+binaryString;        
    }
    
    public static String encode(String str) {
    	   // 根据默认编码获取字节数组
    	   byte[] bytes = str.getBytes();
    	   String strs = "";
    	   // 将字节数组中每个字节拆解成2位16进制整数
    	   for (int i = 0; i < bytes.length; i++) {
                  //取得高四位
    	    strs += hexStr.charAt((bytes[i] & 0xf0) >> 4);
    	   // System.out.println(i+"--"+bytes[i]+"----"+(bytes[i] & 0xf0)+"----"+hexString.charAt((bytes[i] & 0xf0) >> 4)+"---"+hexString.charAt((bytes[i] & 0x0f) >> 0));
    	    //取得低四位
    	    strs += hexStr.charAt((bytes[i] & 0x0f) >> 0);
    	   }
    	   return strs;
    	}
}
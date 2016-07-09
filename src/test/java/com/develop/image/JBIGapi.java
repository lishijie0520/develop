package com.develop.image;


public class JBIGapi {
	static{
		try {
			System.out.println("JavaLibrary :"+System.getProperty("java.library.path")); 
		    System.loadLibrary("WIN7_64_JBIG"); 
		}catch (Throwable t) {
		   t.printStackTrace();
		}
	}
	public static native byte ConvertJBGtoPBM(byte[] jfnin,byte[] jfout);
	
	public static boolean ConvertJBGtoPBM(String jfnin,String jfout){
		boolean flag=false;
		byte[] bytes=jfnin.getBytes();
		byte[] out=jfout.getBytes();
		byte b=ConvertJBGtoPBM(bytes,out);
		if(b==1){
			flag=true;
		}
		return flag;
	}
}


package com.develop.image;

import org.apache.commons.lang.StringUtils;


public class Test {

	static {
		System.load("/root/demo2/libWIN7_64_JBIG.so");
	}

	// 使用jni的关键字native
	// 这个关键字决定我们那些方法能够在C文件使用
	// 只须声明，不必实现

	
	public static native byte ConvertJBGtoPBM(byte[] jfnin,byte[] jfout);
	
	public boolean ConvertJBGtoPBM(String jfnin,String jfout){
		boolean flag=false;
		byte[] bytes=jfnin.getBytes();
		byte[] out=jfout.getBytes();
		byte b=ConvertJBGtoPBM(bytes,out);
		if(b==1){
			flag=true;
		}
		return flag;
	}
	
	
	public static native byte Convert_JBG_to_PBM(byte[] jfnin,byte[] jfout);
	// 
	public native boolean Convert_PBM_to_JBG(String jfnin, String jfout);

	public static void main(String[] args) {
//		Test myJni = new Test();
//		System.out.print(myJni.Convert_PBM_to_JBG(args[0], args[1]));
//		System.out.print("\n");
		
		test(90570);
		
		
		
	}
	public static void test(long userId){
		String userIds = "90570,90572";
        if(StringUtils.isNotBlank(userIds)){
            String[] userIdArr = userIds.split(",");
            if(StringUtils.isNotBlank(userIdArr[0]) && Long.valueOf(userIdArr[0]) == userId){
            	System.out.println("11111111111111");
            }else if(userIdArr.length > 1 && StringUtils.isNotBlank(userIdArr[1]) && Long.valueOf(userIdArr[1]) == userId){
            	System.out.println("55555");
            }
        }
	}
}
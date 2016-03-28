package com.lsj.uinpay.mobilepay;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

import com.lsj.test.utils.HttpClientUtil;
import com.lsj.test.utils.JSONUtil;

public class Demo {
	public String url = "http://180.168.27.30:30400/credit_card/pay";
	public String companyId ="16000251";
	public String key3des = "JR9xoQr7RblhHrUIgDA8Oci/01NsENJggEeTuc5DFPPk/AbjPRDjH5vukQm0+DwpjXCvbKoLZx7iVBqxUoX/FsGniW4L8qgeXuk3BI0eHchcUsGWQj3xVuSIS856+1WthM7sqVopZ3Hrul3onjCv+q3R6jYML2KI34dVofJfLgzifCrd3rlKBT6qDL7yE3SokPxDpkQHMPvux9NPkSvkI2z49NPbsaYRU2agB49gaDwEsCtnninhtI2KM5OHps+3sEHIj1BxvNvZzqn9LwmGAMW6+9Z7QFb/If3BGDYd8PQUJdTQoddyxy7eYwnQNS49pXQhj0MOdHqiXu2aFfaRtQ==";
	
	@Test
	public void pay(){
		Map<String,String> parameterMap = new LinkedHashMap<String,String>();
		parameterMap.put("amount", "2");
		parameterMap.put("idcardtype", "01");
		parameterMap.put("idcard", "447769804451095");
		parameterMap.put("owner", "张三");
		parameterMap.put("currency", "156");
		parameterMap.put("usercode", "10008922");
		parameterMap.put("identitytype", "0");
		parameterMap.put("companyid", "16000251");
		parameterMap.put("orderid", "12345611380869017521606");
		parameterMap.put("phone", "13401003060");
		parameterMap.put("productcode", "1");
		parameterMap.put("productdesc", "成品 3 级天琊一个（信用卡支付）");
		parameterMap.put("productname", "诛仙-3 阶成品天琊");
		parameterMap.put("transtime", "1369893519");
		parameterMap.put("userip", "172.18.66.218");
		parameterMap.put("validthru", "0215");
		parameterMap.put("sign", "ExdYcut6LgrKGsHuAyoxFTMDuDYVmyFFu7GRHPRwB/DBwm6cyBe9Sr2rti1/SjWPcdXL"
				+ "oWIHWEJ9IFKPK+3ieKU/MkNqeh1opH/4MEM59W314jQL3/sPS+X8qsEInj7OsfXCfOKXJ"
				+ "XTw+WeVBOBHep4SBIAkgLjvRYSg1/Bv7ck=");
		
		
		String json = JSONUtil.parseToString(parameterMap);
		System.out.println(json);
		String msg = HttpClientUtil.httpPostWithJSON(url, json);
		System.out.println(msg);
		
	}
	@Test
	public void  query(){
		
		
	}
	@Test
	public void test3des(){
		String data = "ABCDEFG";
		String key = "11223f588810303828257951cbdd556677297398303036e2";
		System.out.println(encryptThreeDES(data.getBytes(), key));
		
	}
	/**
	 * 3DES加密
	 * @param data 待加密数
	 * @param keys 密钥 24字节长度
	 * @return byte[] 加密数据
	 */
	public static String encryptThreeDES(byte[] data, String keys){
		String result="";
		try {
			//System.out.println("data.length: " + data.length);
			for (int i=0; i<data.length; i++) {
				//System.out.print(data[i] + ",");
			}
			//System.out.println();
			byte[] key=hexStringToByte(keys);
			System.out.println(new String(key));
			//System.out.println();
			SecretKey secretKey = new SecretKeySpec(key, "DESede");
			Cipher c1 = Cipher.getInstance("DESede");
			c1.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] res = c1.doFinal(data);
			result = byte2hex(res);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("" + e);
		}
		return result;
	}
	public static byte[] hexStringToByte(String hex) {
		hex=hex.toUpperCase();
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}
	//转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs="";
        String stmp="";

        for (int n=0;n<b.length;n++) {
            stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) 
            	hs=hs+"0"+stmp;
            else 
            	hs=hs+stmp;
            //if (n<b.length-1)  hs=hs+":";
        }
        return hs.toUpperCase();
    }
    private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
}

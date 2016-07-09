package com.lsj.uinpay.mobilepay;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.poi.ddf.EscherColorRef.SysIndexSource;
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
		
		try {

			for (int i = 0; i < 5; i++) {
				Thread.sleep(5000);
				System.out.println("1111");
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	@Test
	public void test3des(){
		//String data = "ABCDEFG";
		String key = "11223f588810303828257951cbdd556677297398303036e2";
		String data = "14522f0f4a3650519b1f6181d84355ae28a8dfc341bdeae0c856a628ad7bae2d4fe26bd4a4600018bba6b7f85ae7319a5cbeaf10c63392be48095e30755b727ed685ea509d6529664738ac5f399201e85db0b6fe99fd00a18edd6272c3e728b7caf8f6f94c4413a6d997bcf7aab37797f5bbffb3c6723c75ab14f9bb624aa09a7f981ede81bc5a1537de2dd2b3c071e74bb6870b6ff1bf669a276cc95ec4d86bf328df4e38360b709d4662611c248cf8d90731c1900340d5afdf6f03abd13dc6b2cf5df0c4e0bfa598cf07365b507392248ecad416cb1c3bc2a88413c70cd303baf113737cffd28d9e078a2bad713650342b2d32df9936bee1143696be8672f0a88d680654e9c9986df28175b75f96487101e98c7ab320d02abba5ea3bb4d36d49dd667f20ec2740d7685c4bf8b045900740832b35aa9b97e99b31ed7045776e5a92fb0458d7ba6ae75025ae95b2e8672b6aded9c4c17d5f2b6df3cdf9901ab12f6b3d77f3acaee7b4d3d966e027af45c2c7179862398361e97eb06af94beb94cb7b68613ec41d286d6645ea2ffe051be05bc27c221b2b7582444a6dea7efaec0162bf74c5d6a64dca5e4cec3a63764bee9210743c37e2cd26d8e4a5469f5d902726846e82e909b40e116d325ab795ad422d6b4eb926117d4cbda902780901fa526917c23a34b4f213834de1d5d18358d1759fb9eaa6b6f7b5bfe899cd0bc785febcba76f2b56b9e9ea879715eddbb5f5cbeaf10c63392be6c970e6a089f2b77";
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
    @Test
    public void test1(){
    	long amount = 100;
    	BigDecimal bigDecimal = new BigDecimal(amount);
    	BigDecimal divide = bigDecimal.divide(new BigDecimal(100));
    	
    	 DecimalFormat df=new DecimalFormat("0.00");
    	System.out.println(df.format(divide).toString());
    }
}

package com.lsj.cmbc.scoket;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author lishijie
 * 模拟 民生实时联机代付  服务端
 */

public class  CmbcServerDemo{
	public String messagekey = "12345678901234567890123456789012";
	/**
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 请求消息映射集合
	 */
	private final Map<String, Element> reqMsgMapping = new ConcurrentHashMap<String, Element>();

	/**
	 * 响应消映射集合
	 */
	private final Map<String, Element> resMsgMapping = new ConcurrentHashMap<String, Element>();
	/**
	 * 报文配置服务
	 */
	private MessageConfigService messageConfigService;
	@Test
	public void test() {
		try {
			
			ServerSocket serverSocket = new ServerSocket(9108);
			
			Socket socket = serverSocket.accept();
			while (true) {// 保持长连接
				try {
					Thread.sleep(3000);// 等待时间
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (socket != null) {
					InputStream is = null;
					OutputStream os = null;
					try {

						String ip1 = socket.getInetAddress().getHostAddress();
						socket.setKeepAlive(true);
						is = socket.getInputStream();
						byte[] buf = new byte[1024];
						int len = -1;
						while ((len = is.read(buf)) != -1) {
							String text = new String(buf, 0, len);

							System.out.println("服务器接收到："+text);
							if (text.equals("00000000"))
								continue;
							System.out.println("1111111111111111111111-----------");
							String tranId = "";
							if (text.indexOf("TRAN_ID") > -1) {
								tranId = StringUtil.splitString(text, "<TRAN_ID>", "</TRAN_ID>");
							}
							byte[] puck = puck(tranId);
							  String bankData ="0004171002           <?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
				                        +"<TRAN_RESP>" 
				        		        +"<RESP_TYPE>F</RESP_TYPE>"
				                        +"<RESP_CODE>00</RESP_CODE>"
										+"<RESP_MSG>交易成功</RESP_MSG>"
										+"<COMPANY_ID>CS</COMPANY_ID>"
										+"<MCHNT_CD></MCHNT_CD>"
										+"<TRAN_DATE>20151206</TRAN_DATE>"                       //新增系统追踪号 长度6位  取当前时间(唯一)
										+"<TRAN_TIME>010928</TRAN_TIME>"
										+"<TRAN_ID>"+tranId+"</TRAN_ID>"
										+"<BANK_TRAN_ID>2015120601090700000008</BANK_TRAN_ID>"
										+"<BANK_TRAN_DATE>20151206</BANK_TRAN_DATE >"
										+"<RESV>备注</RESV>"
										+"</TRAN_RESP>A6AA1748F5EA3053EA23A28284E7130E";
							//  System.out.println("bankData:"+bankData);
							  
							  String oldTranId = "";
							  String oldBankTranId = "";
							  
							  if (text.indexOf("ORI_TRAN_ID") > -1) {
								  oldTranId = StringUtil.splitString(text, "<ORI_TRAN_ID>", "</ORI_TRAN_ID>");
								}
							  if (text.indexOf("ORI_BANK_TRAN_ID") > -1) {
								  oldBankTranId = StringUtil.splitString(text, "<ORI_BANK_TRAN_ID>", "</ORI_BANK_TRAN_ID>");
							  }
							  byte[] querypuck = querypuck(tranId, oldTranId, oldBankTranId);
							  String bankQueryData ="0006093002           <?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
									  +"<TRAN_RESP>" 
									  +"<RESP_TYPE>S</RESP_TYPE>"
									  +"<RESP_CODE>00</RESP_CODE>"
									  +"<RESP_MSG>交易成功</RESP_MSG>"
									  +"<COMPANY_ID>CS</COMPANY_ID>"
									  +"<MCHNT_CD></MCHNT_CD>"
									  +"<TRAN_DATE>20151206</TRAN_DATE>"                       //新增系统追踪号 长度6位  取当前时间(唯一)
									  +"<TRAN_TIME>010928</TRAN_TIME>"
									  +"<TRAN_ID>CS2015120601090700000008</TRAN_ID>"
									  +"<ORI_TRAN_DATE>20151206</ORI_TRAN_DATE>"
									  +"<ORI_TRAN_ID>"+oldTranId+"</ORI_TRAN_ID >"
									  +"<ORI_BANK_TRAN_ID>"+System.currentTimeMillis()+"</ORI_BANK_TRAN_ID>"
									  +"<ORI_BANK_TRAN_DATE>20151206</ORI_BANK_TRAN_DATE>"
									  +"<ORI_RESP_TYPE>R</ORI_RESP_TYPE>"
									  +"<ORI_RESP_CODE>交易成功</ORI_RESP_CODE>"
									  +"<ORI_RESP_MSG></ORI_RESP_MSG>"
									  +"<RESV>备注</RESV>"
									  +"</TRAN_RESP>A6AA1748F5EA3053EA23A28284E7130E";
							
							// <TRAN_REQ><COMPANY_ID>CS</COMPANY_ID><MCHNT_CD></MCHNT_CD><TRAN_DATE>20151206</TRAN_DATE><TRAN_TIME>010928</TRAN_TIME><TRAN_ID>CS2015120601090700000008</TRAN_ID><CURRENCY>RMB</CURRENCY><ACC_NO>6212260200064820083</ACC_NO><ACC_NAME>卞嫣然</ACC_NAME><BANK_TYPE>320</BANK_TYPE><BANK_NAME>null</BANK_NAME><TRANS_AMT>56679</TRANS_AMT><REMARK>单笔测试</REMARK><RESV></RESV>
							//  System.out.println("bankQueryData:"+bankQueryData);
							
							
							os = socket.getOutputStream();
							if(text.contains("3002")){
								System.out.println("发送给客户端 查询结果：" + new String(querypuck));
							}else{
								System.out.println("发送给客户端 交易结果：" + new String(puck));
							}
							os.write(text.contains("3002")?querypuck:puck);
							//os.write(text.getBytes());
							os.flush();
						}
		                
		            }catch(Exception e){  
		                System.out.println("出现了错误,关闭连接");
		                if (is!= null)
		                	is.close();
		                if (os!= null)
		                	os.close();
		                e.printStackTrace();
		                
		            }  
		        }  
		    }  
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
	}
	@Test
	public void test2(){

		try {
			
			ServerSocket serverSocket = new ServerSocket(9108);
			
			Socket socket = serverSocket.accept();
			while (true) {// 保持长连接
				try {
					Thread.sleep(3000);// 等待时间
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (socket != null) {
					InputStream is = null;
					OutputStream os = null;
					try {

						String ip1 = socket.getInetAddress().getHostAddress();
						socket.setKeepAlive(true);
						is = socket.getInputStream();
						byte[] buf = new byte[1024];
						int len = -1;
						while ((len = is.read(buf)) != -1) {
							String text = new String(buf, 0, len);

							System.out.println("服务器接收到："+text);
							if (text.equals("00000000"))
								continue;
							
							
							os.write(text.getBytes());
							os.flush();
						}
		                
		            }catch(Exception e){  
		                System.out.println("出现了错误,关闭连接");
		                if (is!= null)
		                	is.close();
		                if (os!= null)
		                	os.close();
		                e.printStackTrace();
		                
		            }  
		        }  
		    }  
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
	
	}
	
	/**
	 * 解包v2
	 * 
	 * @param bytes
	 * @return
	 */
	public Map<String, Object> unpack2(byte[] bytes) {
		Map<String, Object> dataContainer = new HashMap<String, Object>();
		try {
			String charset = "UTF-8";// 字符集
			int companyCodeLength = messageConfigService.getInt("COMPANY_CODE_LENGTH", 15);// 合作方编码长度
			int messageCodeLength = messageConfigService.getInt("MESSAGE_CODE_LENGTH", 8);// 报文码长度
			int signCodeLength = messageConfigService.getInt("SIGN_CODE_LENGTH", 4);// 签名编码长度
			PublicKey publicKey = (PublicKey) messageConfigService.getObject("PUBLIC_KEY");// 银行公钥
			PrivateKey privateKey = (PrivateKey) messageConfigService.getObject("PRIVATE_KEY");// 合作方私钥

			int headLength = companyCodeLength + messageCodeLength + signCodeLength;
			// 提取交易服务码
			String messageCode = new String(ArrayUtils.subarray(bytes, companyCodeLength, companyCodeLength + messageCodeLength)).trim();
			logger.debug("messageCode:" + messageCode);
			dataContainer.put("MESSAGE_CODE", messageCode);
			// 提取签名长度
			int signlength = NumberUtils.toInt(new String(ArrayUtils.subarray(bytes, companyCodeLength + messageCodeLength, headLength)).trim());
			logger.debug("signlength:" + signlength);
			// 提取签名域
			byte[] signBytes = ArrayUtils.subarray(bytes, headLength, headLength + signlength);
			// 提取xml密文
			byte[] encryptedBytes = ArrayUtils.subarray(bytes, headLength + signlength, bytes.length);

			byte[] xmlBytes = CryptoUtil.decrypt(encryptedBytes, privateKey, 2048, 11, "RSA/ECB/PKCS1Padding");// 解密
			String xml = new String(xmlBytes, charset);
			logger.info("--->>>{}:{}", new Object[] { messageCode, xml });

			Document doc = DocumentHelper.parseText(xml);
			Element rootMessageElement = doc.getRootElement();
			Element configRootElement = (Element) resMsgMapping.get(messageCode).elements().get(0);
			for (Element configElement : (List<Element>) configRootElement.elements()) {
				if (!XMLMessageUtil.unpackField(dataContainer, rootMessageElement, configElement, "", charset)) {
					return dataContainer;
				}
			}

			boolean isValid = CryptoUtil.verifyDigitalSign(xmlBytes, signBytes, publicKey, "SHA1WithRSA");// 验签
			if (!isValid) {
				logger.error("报文验签不通过");
				dataContainer.put("YHYDLX", "FAIL");
				dataContainer.put("YHYDM", "97");
				dataContainer.put("YHYDMS", "验签失败");
				return dataContainer;
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return null;
	}
	
	
	
	
	public  byte[] querypuck(String tranId,String oldTranId,String oldBankTranId){
		
		String charset = "UTF-8";// 字符集
		int headLength = NumberUtils.toInt("6", 6);// 报文头长度
		int messageCodeLength = NumberUtils.toInt("15", 15);// 报文码长度
		byte[] bytes = null;
		
		try {
			StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRAN_RESP>");
			buffer.append("<RESP_TYPE>").append("S").append("</RESP_TYPE>");
			buffer.append("<RESP_CODE>").append("00").append("</RESP_CODE>");
			buffer.append("<RESP_MSG>").append("交易成功").append("</RESP_MSG>");
			buffer.append("<COMPANY_ID>").append("CS").append("</COMPANY_ID>");
			buffer.append("<MCHNT_CD></MCHNT_CD>");
			buffer.append("<TRAN_DATE>").append("20151206").append("</TRAN_DATE>");
			buffer.append("<TRAN_TIME>").append("010928").append("</TRAN_TIME>");
			buffer.append("<TRAN_ID>").append(tranId).append("</TRAN_ID>");
			buffer.append("<ORI_TRAN_DATE>").append("20151206").append("</ORI_TRAN_DATE>");
			buffer.append("<ORI_TRAN_ID>").append(oldTranId).append("</ORI_TRAN_ID>");
			buffer.append("<ORI_BANK_TRAN_ID>").append(oldBankTranId).append("</ORI_BANK_TRAN_ID>");
			buffer.append("<ORI_BANK_TRAN_DATE>").append("20151206").append("</ORI_BANK_TRAN_DATE>");
			buffer.append("<ORI_RESP_TYPE>").append("S").append("</ORI_RESP_TYPE>");
			buffer.append("<ORI_RESP_CODE>").append("00").append("</ORI_RESP_CODE>");
			buffer.append("<ORI_RESP_MSG>").append("交易成功").append("</ORI_RESP_MSG>");
			buffer.append("<RESV>").append("").append("</RESV>");
			buffer.append("</TRAN_RESP>");
			byte[] bodyBytes = buffer.toString().getBytes(charset);
			String mac = md5(buffer.append(messagekey).toString().getBytes(charset));
			bytes = ArrayUtils.addAll(bytes,
					StringUtils.leftPad(String.valueOf(bodyBytes.length), headLength, "0").getBytes(charset));
			bytes = ArrayUtils.addAll(bytes, StringUtils.rightPad("3002", messageCodeLength, " ").getBytes(charset));
			bytes = ArrayUtils.addAll(bytes, bodyBytes);
			bytes = ArrayUtils.addAll(bytes, mac.getBytes(charset));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	
	
	public  byte[]  puck(String tranId){
		String charset = "UTF-8";// 字符集
		int headLength = NumberUtils.toInt("6", 6);// 报文头长度
		int messageCodeLength = NumberUtils.toInt("15", 15);// 报文码长度
		byte[] bytes = null;
		
		try {
			StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRAN_RESP>");
			buffer.append("<RESP_TYPE>").append("S").append("</RESP_TYPE>");
			buffer.append("<RESP_CODE>").append("00").append("</RESP_CODE>");
			buffer.append("<RESP_MSG>").append("交易成功").append("</RESP_MSG>");
			buffer.append("<COMPANY_ID>").append("CS").append("</COMPANY_ID>");
			buffer.append("<MCHNT_CD></MCHNT_CD>");
			buffer.append("<TRAN_DATE>").append("20151206").append("</TRAN_DATE>");
			buffer.append("<TRAN_TIME>").append("010928").append("</TRAN_TIME>");
			buffer.append("<TRAN_ID>").append(tranId).append("</TRAN_ID>");
			buffer.append("<BANK_TRAN_ID>").append("2015120601090700000008").append("</BANK_TRAN_ID>");
			buffer.append("<BANK_TRAN_DATE>").append("20151206").append("</BANK_TRAN_DATE>");
			buffer.append("<RESV>").append("备注").append("</RESV>");
			buffer.append("</TRAN_RESP>");
			byte[] bodyBytes = buffer.toString().getBytes(charset);
			String mac = md5(buffer.append(messagekey).toString().getBytes(charset));
			bytes = ArrayUtils.addAll(bytes,
					StringUtils.leftPad(String.valueOf(bodyBytes.length), headLength, "0").getBytes(charset));
			bytes = ArrayUtils.addAll(bytes, StringUtils.rightPad("1002", messageCodeLength, " ").getBytes(charset));
			bytes = ArrayUtils.addAll(bytes, bodyBytes);
			bytes = ArrayUtils.addAll(bytes, mac.getBytes(charset));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	private String md5(byte[] bytes) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(bytes);
		String result = new BigInteger(1, md.digest()).toString(16);
		result = StringUtils.leftPad(result, 32, "0").toUpperCase();
		return result;
	}
	
}
class StringUtil{
	   public static String splitString(String str,String beginTag,String endTag){
	    	String result = "";
	    	if(null==str||"".equals(str)){
	    		return result;
	    	}else{
	    	String[] r1 = str.split(beginTag);
	    	String[] r2 = r1[1].split(endTag);
	    	result = r2[0];
	    	return result;
	    	}
	    }
}

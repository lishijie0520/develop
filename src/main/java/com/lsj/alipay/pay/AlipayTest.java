//package com.lsj.alipay.pay;
//
//import java.io.StringReader;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.dom4j.Document;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//import org.junit.Before;
//import org.junit.Test;
//import org.xml.sax.InputSource;
//
//import com.hsm.ipay.utils.HttpClientUtil;
//import com.hsm.ipay.utils.jd.MD5;
//
//public class AlipayTest {
//	/**
//	 * 合作者身份ID
//	 */
//	private String partner ="2088701922972391";
//	
//	private String key = "ik103zwu7ruitl1sk8upxwk1yybpoacu";
//	
//	private String charset = "utf-8";
//	
//	private String signType = "MD5";
//	
//	private String url = "https://mapi.alipay.com/gateway.do";
//	
//	@Before
//	public void init(){
//		System.out.println(Calendar.getInstance().getTimeInMillis()); 
//		System.out.println(System.currentTimeMillis()); 
//	}
//	
//	/**
//	 * 支付宝  支付接口  统一预下单接口
//	 * @param service 接口名称  
//	 * @param partner 合作者身份ID
//	 * @param _input_charset 编码  get 请求返回的数据对应的编码，post请求返回编码都是GBK(编码不一致存在乱码)
//	 * @param sign_type 签名方式 DSA、RSA、MD5 三个值可 选,必须大写。
//	 * @param sign 密文
//	 * @param out_trade_no 商户网站唯一订单号
//	 * @param subject 订单标题 (用户会在支付成功页面里看到这个信息)
//	 * @param product_code 订单业务类型  (QR_CODE_OFFLINE: 二维码支付)
//	 * @param total_fee 订单总金额，单位为“元”，精确 到小数点后 2 位。 0.01
//	 * @param dynamic_id_type 订动态ID类型   bar_code:条码
//	 * @param dynamic_id 条形码   这个是扫码终端设备从用户手机上扫取到的支付授权号
//	 */
//	@Test
//	public void pay() throws Exception{
//		String service = "alipay.acquire.createandpay";
//		
//		String dynamicId = "284841233141328383";
//		String orderId = String.valueOf(System.currentTimeMillis());
//		String [] parameters = {
//				"service="+service,
//				"partner="+partner,
//				"_input_charset="+charset,
//				"out_trade_no="+orderId,
//				"subject=iphone6s手机",
//				"product_code=BARCODE_PAY_OFFLINE",
//				"total_fee=0.01",
//				"dynamic_id_type=bar_code",
//				"dynamic_id="+dynamicId,
//		};
//		//排序  60e1937a0cb155663476ce8dd5486cf8
//		Arrays.sort(parameters);
//		
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < parameters.length; i++) {
//			sb.append(parameters[i]);
//			sb.append("&");
//		}
//		
//		String substring = sb.substring(0,sb.length()-1);
//		
//		System.err.println(substring);
//		
//		String md5 = MD5.md5(substring, key);
//		
//		System.err.println(md5);
//		
//		Map<String,String> paramsMap = new HashMap<String,String>();
//		paramsMap.put("service", service);
//		paramsMap.put("partner", partner);
//		paramsMap.put("_input_charset", charset);
//		paramsMap.put("sign_type", signType);
//		paramsMap.put("sign", md5);
//		//业务参数
//		paramsMap.put("out_trade_no", orderId);
//		paramsMap.put("subject", "iphone6s手机");
//		paramsMap.put("product_code", "BARCODE_PAY_OFFLINE");
//		paramsMap.put("total_fee", "0.01");
//		paramsMap.put("dynamic_id_type", "bar_code");
//		paramsMap.put("dynamic_id", dynamicId);
//		
//		//String postSendData = HttpClientUtil.postSendData(url, charset, paramsMap);
//		String postSendData = HttpClientUtil.getSendData(url, charset, paramsMap);
//		System.out.println(postSendData);
//	
//		if(postSendData.indexOf("sign")>-1) {
//			String sign = StringUtil.splitString(postSendData, "<sign>", "</sign>");
//			//if(!sign.equals(md5))
//				//return;
//		}
//		if(postSendData.indexOf("result_code")>-1)
//		 {
//			String resultCode = StringUtil.splitString(postSendData, "<result_code>", "</result_code>");
//		}
//		if(postSendData.indexOf("trade_no")>-1)
//		{
//			String tradeNo = StringUtil.splitString(postSendData, "<trade_no>", "</trade_no>");
//		}
//		
//		
//	}
//	
//	@Test
//	public void query() throws Exception{
//		String orderId = "14478124910002313785";
//		String [] parameters = {"service=alipay.acquire.query",
//				"partner="+partner,
//				"_input_charset="+charset,
//				"out_trade_no="+orderId,
//				
//		};
//		//排序
//		Arrays.sort(parameters);
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < parameters.length; i++) {
//			sb.append(parameters[i]);
//			sb.append("&");
//		}
//		String substring = sb.toString().substring(0,sb.length()-1);
//		System.err.println(substring);
//		String md5 = MD5.md5(substring, key);
//		
//		Map<String,String> paramsMap = new HashMap<String,String>();
//		paramsMap.put("service", "alipay.acquire.query");
//		paramsMap.put("partner", partner);
//		paramsMap.put("_input_charset", charset);
//		paramsMap.put("sign_type",signType);
//		paramsMap.put("sign", md5);
//		paramsMap.put("out_trade_no", orderId);
//		//String postSendData = HttpClientUtil.postSendData(url, charset, paramsMap);
//		
//		
//		//get
//		String postSendData = HttpClientUtil.getSendData(url, charset, paramsMap);
//		System.out.println(postSendData);
//	}
//	/**
//	 * 撤消接口
//	 * @throws Exception 
//	 */
//	@Test
//	public void cancel() throws Exception{
//		String orderId = "14478124910002313785";
//		String tradeNo ="2015112421001004610253823781";
//		//2015112421001004610262897142
//		//2015112421001004610253823781
//		String [] parameters = {"service=alipay.acquire.cancel",
//				"partner="+partner,
//				"_input_charset="+charset,
////				"out_trade_no="+orderId,
//				"trade_no="+tradeNo,
//				
//		};
//		//排序
//		Arrays.sort(parameters);
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < parameters.length; i++) {
//			sb.append(parameters[i]);
//			sb.append("&");
//		}
//		String substring = sb.toString().substring(0,sb.length()-1);
//		System.err.println(substring);
//		String md5 = MD5.md5(substring, key);
//		
//		Map<String,String> paramsMap = new HashMap<String,String>();
//		paramsMap.put("service", "alipay.acquire.cancel");
//		paramsMap.put("partner", partner);
//		paramsMap.put("_input_charset", charset);
//		paramsMap.put("sign_type",signType);
//		paramsMap.put("sign", md5);
//		//paramsMap.put("out_trade_no", orderId);
//		paramsMap.put("trade_no", tradeNo);
//		//String postSendData = HttpClientUtil.postSendData(url, charset, paramsMap);
//		
//		
//		//get
//		String postSendData = HttpClientUtil.getSendData(url, charset, paramsMap);
//		System.out.println(postSendData);
//	}
//	
//	/**
//	 * 退款接口
//	 * @throws Exception 
//	 * 
//	 */
//	@Test
//	public void refund() throws Exception{
//		//String orderId = "14478124910002313785";
//		String orderId = "1447905310773";
//		
//		String [] parameters = {"service=alipay.acquire.refund",
//				"partner="+partner,
//				"_input_charset="+charset,
//				"out_trade_no="+orderId,
//				"refund_amount=0.01"
//				
//		};
//		//排序
//		Arrays.sort(parameters);
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < parameters.length; i++) {
//			sb.append(parameters[i]);
//			sb.append("&");
//		}
//		String substring = sb.toString().substring(0,sb.length()-1);
//		System.out.println(substring);
//		String md5 = MD5.md5(substring, key);
//		System.out.println(md5);
//		Map<String,String> paramsMap = new HashMap<String,String>();
//		paramsMap.put("service", "alipay.acquire.refund");
//		paramsMap.put("partner", partner);
//		paramsMap.put("_input_charset", charset);
//		paramsMap.put("sign_type",signType);
//		paramsMap.put("sign", md5);
//		paramsMap.put("out_trade_no", orderId);
//		paramsMap.put("refund_amount", "0.01");
//		
//		//String postSendData = HttpClientUtil.postSendData(url, charset, paramsMap);
//		//get
//		String postSendData = HttpClientUtil.getSendData(url, charset, paramsMap);
//		System.out.println(postSendData);
//	}
//	
//	@Test
//	public void  sign() throws Exception{
//	String orderId = "1447905310773";
//		
//		String [] parameters = {"buyer_logon_id=lis***@qq.com",
//				"buyer_user_id=2088002918839682",
//				"amount=0.01",
//				"fund_channel=10",
//				"out_trade_no=14478124910002313785",
//				"partner=2088701922972391",
//				"result_code=SUCCESS",
//				"send_pay_date=2015-11-18 10:08:27",
//				"total_fee=0.01",
//				"trade_no=2015111821001004680050493042",
//				"trade_status=TRADE_SUCCESS"
//		};
//		//排序
//		//Arrays.sort(parameters);
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < parameters.length; i++) {
//			sb.append(parameters[i]);
//			sb.append("&");
//		}
//		String substring = sb.toString().substring(0,sb.length()-1);
//		System.err.println(substring);
//		String md5 = MD5.md5(substring, key);
//		System.out.println(md5);
//		System.out.println("86d9eac6bac43082eaacea318bca3455");
//	}
//	
//    /**
//     * xml 转成 map 
//     * @param data
//     * @return
//     * @throws Exception
//     */
//	public static Map<String, String> parseXml(String data) throws Exception {
//		// 解析结果存储在HashMap
//		Map<String, String> map = new HashMap<String, String>();
//		
//		StringReader xml = new StringReader(data);
//		
//		InputSource source = new InputSource(xml);
//		
//		// 读取输入流
//		SAXReader reader = new SAXReader();
//		
//		Document document = reader.read(source);
//		// 得到xml根元素
//		Element root = document.getRootElement();
//		// 得到根元素的所有子节点
//		List<Element> elementList = root.elements();
//
//		// 遍历所有子节点
//		for (Element e : elementList)
//			map.put(e.getName(), e.getText());
//		
//		return map;
//	}
//	
//}
//

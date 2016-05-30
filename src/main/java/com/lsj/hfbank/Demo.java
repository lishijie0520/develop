package com.lsj.hfbank;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.lsj.test.utils.HttpClientUtil;

/**
 * 恒丰银行接口
 * @author lishijie
 *
 */
public class Demo {
	//私钥
	String  privateKey = "30820278020100300d06092a864886f70d0101010500048202623082025e02010002818100c1464d5781252ad51c284ec076003647911e5b393443ada1e9d97a336641580dfc15f0cad5259c370d429850e7f4fe9636474b721717e99ee4f4c873495fe7d0f9d796dd37ed062b85c72ae4f4c71f661e1254672a60bba38a07ed2e4cc841829dd370098fd2d7106614bf119d453a5ccf1a15ad0da4586ad30ab6accae4ec8f020301000102818100b97c6edd7be3dfa786ce6845ecf9671e67f76a350c23a78fac8e3ae8f949dce80ef63f9169de1e7df70be282431aacfa1c3d6ffa9e8f247689e5223b024038887b3ffe80806c5dc130bb7a384923dc15699be0ce2b14c92179ff74fec4ffc7b33c7d8a5ce6af94c02a3b6e25713481ac9c402c80eefc6c04fafc3874109b7701024100e18c635c76fac15a61f5619604cf2b004da42d39a41fef7aeeedd34f0c59c05bb009f0593abea52ee34fd84d71cd98029a34def2ee88188acf995173f155a707024100db5e70217b899f7a88302bb1c7eef246a97b3350a48601aa1f9fcdaf586728b3c60f05cceacfd578cbebae4cff249714dbd1d62501e176a35ae6151e80cc6439024100c73b1d21b62f75d002aba011c619c16039350685de061fa2359f7a825ee480d7fcbe2ac286f39431c0b4ecda2be6719a0ae11cf6f5e0dddcefd7fc6d30067acd024100838e9c42883adda8775a242a271416b9e7d419cf280bb19ddbff5333f01955a0928bd9d7ad8e5ca067f2fc81f2957801ab5ea945625e5b6ed6099beac225518902400ffe82ad43480334cd39b11994ba26538228a63574447ecfdc7a6043e21f3f536c97b67423aa40bd561edf31444dc9a0bac832567c381ca70a1fd6aa975ef212";
	//公钥 发给银行
	String  publicKey = "30819f300d06092a864886f70d010101050003818d0030818902818100c1464d5781252ad51c284ec076003647911e5b393443ada1e9d97a336641580dfc15f0cad5259c370d429850e7f4fe9636474b721717e99ee4f4c873495fe7d0f9d796dd37ed062b85c72ae4f4c71f661e1254672a60bba38a07ed2e4cc841829dd370098fd2d7106614bf119d453a5ccf1a15ad0da4586ad30ab6accae4ec8f0203010001";
	//银行 公钥
	String bankPublicKey = "30819f300d06092a864886f70d010101050003818d0030818902818100b6dd7c9363087e3f1f6493727ed77692f38ebfc0a1c2d80ea1eeab3471078598d5071b034bdbf2be843f99beb578bb229938bcb9f415fada0241537279df21758341d57a24d10c0755648025b8686cabfee33566cc07d208ed279bffa10b04bc37dd2b261820b563e1f203ac28a14dd760e0c78dd5031ca50230b9213ff6de430203010001";
	//报盘通知url
	String urlNotice = "http://11.136.254.27:6088/hftrans2/ws/notice/offer";
	//编码
	String charset = "UTF-8";
	
	/**
	 * 报盘通知接口 0001
	 */
	@Test
	public void notice(){
	  //tranNo 交易流水号  
      //platCode 平台代码
	  //tranTime 交易时间 
	  //tranCode 交易代码
	  //privateData 私有域
	  // reqData  fileName 文件名
	  //chkValue 签名数据
		
	// String data = "tranNo=00&platCode=100001&tranTime=201510121212112&tranCode=12345678901234567890123456789000&privateData=20151010&reqData={"oriTranNo":"1"}&chkValue="asdqwerqf12312312afeqerw"";
	String platCode ="1111";
	String merchantCode = "hftx";
	String seqCode = "000001";
	String fileName = "20160529"+platCode+seqCode+"_"+merchantCode;
	 String [] parameters = {
				"tranNo="+System.currentTimeMillis(),
				"platCode="+platCode,
				"tranTime="+"20160529111422",
				"tranCode=0001",
				"privateData=",
				"reqData={\"fileName\":"+fileName+"}",
				"chkValue="+"kwerweirwoi",
		};
	 
		Map<String,String> paramsMap = new HashMap<String,String>();
		paramsMap.put("tranNo", String.valueOf(System.currentTimeMillis()));
		paramsMap.put("platCode", platCode);
		paramsMap.put("tranTime", "20160529111422");
		paramsMap.put("tranCode", "0001");
		//业务参数
		paramsMap.put("reqData", "{\"fileName\":"+fileName+"}");
		paramsMap.put("chkValue", "kwerweirwoi");
	
		
	 
	  String postSendData = HttpClientUtil.getSendData(urlNotice, charset, paramsMap);
	  System.out.println(postSendData);
	  
	  //批次号_企业编号.TXT (此批次号为交易批次号，与文件内容首行批次号一致)  如：批次号（YYMMDD+4位平台代码+6位序列号）_企业编号
	  //上送日期|上送时间|申请人所属企业名称|申请人所属企业编号|批次号|交易笔数|交易金额|对公对私标志|汇总行签名域
	  //交易流水号|收款方行号|收款方行号|收款帐号|收款帐户名|金额|付款备注
	  
	  //20160529|145011|北京慧付天下信息技术有限公司|hftx|201605290001000001|2|3|1|????
	  //2016052900010000010001|308111|中国招商银行|620000000000000|李诗杰|1|测试数据
	  //2016052900010000010002|308111|中国招商银行|620000000000000|李诗杰|2|测试数据
	  String sign = Service.sign("", privateKey);
		
	}
	/**
	 * 回盘通知接口 0002
	 */
	@Test
	public void receive(){
		
	}
	/**
	 * 结果查询接口 0003
	 */
	@Test
	public void resultQuery(){
		
	}
	/**
	 * 同步企业密钥接口
	 */
    @Test
    public void sysnKey(){
    	
    }
    
    /**
     * 组装txt请示报文
     */
    
    @Test
    public void reqData (){
    	
    }
    
    /**
     * 解析回盘数据
     */
    @Test
    public void parseData(){
    	
    }
    /**
     * 签名
     */
    @Test
    public void sign(){
    	String data = "lishijie";
    	Service.sign(data, privateKey);
    }
}

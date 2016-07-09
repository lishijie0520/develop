package com.develop.dom4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jni.File;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

public class Demo {
	/**
	 * 创建xml文档
	 * @throws IOException 
	 */
	// <?xml version="1.0" encoding="GBK"?> <stream> <status>AAAAAAA</status> <statusText>交易成功</statusText> <list name="userDataList"> <row> <status>AAAAAAC</status> <statusText>预约成功</statusText> <stt>2</stt> </row> </list> </stream>
	@Test
	public void test() throws IOException{
		String fileName="text.xml";
		
		Document document = DocumentHelper.createDocument();
		document.addComment("创建xml test");
		Element stream = document.addElement("stream");
		Element status = stream.addElement("status");
		status.setText("AAAAAAA");
		
		Element statusText = stream.addElement("statusText");
		statusText.setText("交易成功");
		Element list = stream.addElement("list");
		list.addAttribute("name", "userDataList");
		Element row = list.addElement("row");
		Element row_status = row.addElement("status");
		row_status.addText("AAAAAAC");
		Element row_statusText = row.addElement("statusText");
		row_statusText.addText("预约成功");
		
		
		System.out.println(document.asXML());
		FileWriter fileWriter = new FileWriter(fileName);
		XMLWriter writer = new XMLWriter(fileWriter);
		writer.write(document);
		writer.close();
	}
	@Test 
	public void test2() throws DocumentException{
		String data = "<?xml version=\"1.0\" encoding=\"GBK\"?> <stream> <accountName>北京起华伟业科技发展有限公司</accountName> <accountNo>7117010182600007188</accountNo> <openBankName>中信银行北京珠市口支行</openBankName> <returnRecords>20</returnRecords> <status>AAAAAAA</status> <statusText>交易成功</statusText> <totalRecords>24</totalRecords> <list name=\"userDataList\"> <row> <abstract>测试款</abstract> <creditDebitFlag>C</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>翁翊成</oppAccountName> <oppAccountNo>6226220119258225</oppAccountNo> <sumTranNo>1000000002306</sumTranNo> <tranAmount>7.00</tranAmount> <tranDate>20160505</tranDate> <tranNo>J0000002545107</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002307</sumTranNo> <tranAmount>1.01</tranAmount> <tranDate>20160505</tranDate> <tranNo>J0000003457145</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>银行收费</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName/> <oppAccountNo/> <sumTranNo>1000000002308</sumTranNo> <tranAmount>5.99</tranAmount> <tranDate>20160506</tranDate> <tranNo>BAT00029195236</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>往来</abstract> <creditDebitFlag>C</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>北京花生米数字科技发展有限公司</oppAccountName> <oppAccountNo>7117010182600007927</oppAccountNo> <sumTranNo>1000000002309</sumTranNo> <tranAmount>1000.00</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000004712370</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002310</sumTranNo> <tranAmount>0.01</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000005052745</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002311</sumTranNo> <tranAmount>0.02</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000005819315</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002312</sumTranNo> <tranAmount>0.01</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000006397953</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002313</sumTranNo> <tranAmount>0.02</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000006458711</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002314</sumTranNo> <tranAmount>0.01</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000006928869</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002315</sumTranNo> <tranAmount>0.04</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000006928889</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002316</sumTranNo> <tranAmount>0.03</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000006929021</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002317</sumTranNo> <tranAmount>0.05</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000006929102</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002318</sumTranNo> <tranAmount>0.02</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000006929103</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002319</sumTranNo> <tranAmount>0.05</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000007496315</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002320</sumTranNo> <tranAmount>0.03</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000007497801</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002321</sumTranNo> <tranAmount>0.02</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000007497802</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002322</sumTranNo> <tranAmount>0.04</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000007497811</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>摘要</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName>李诗杰</oppAccountName> <oppAccountNo>6225880161588869</oppAccountNo> <sumTranNo>1000000002323</sumTranNo> <tranAmount>0.01</tranAmount> <tranDate>20160510</tranDate> <tranNo>J0000007497817</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>银行收费</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName/> <oppAccountNo/> <sumTranNo>1000000002324</sumTranNo> <tranAmount>786.22</tranAmount> <tranDate>20160511</tranDate> <tranNo>BAT00028795236</tranNo> <voucherName/> <voucherType/> </row> <row> <abstract>银行收费</abstract> <creditDebitFlag>D</creditDebitFlag> <e3rtDate/> <e3rtFlag>1</e3rtFlag> <oppAccountName/> <oppAccountNo/> <sumTranNo>1000000002325</sumTranNo> <tranAmount>213.42</tranAmount> <tranDate>20160511</tranDate> <tranNo>BAT00028795308</tranNo> <voucherName/> <voucherType/> </row> </list> </stream>";
		Document parseText = DocumentHelper.parseText(data);
		Element stream = parseText.getRootElement();
		Element accountName = stream.element("accountName");
		//System.out.println(accountName.getText());
		
		Element list = stream.element("list");
		
		
		
		List<Element> rowlist = list.elements("row");
		
		System.out.println(list.asXML());
		System.out.println(rowlist);
		for (Element element : rowlist) {
			Map<String, String> parsMap = new HashMap<String,String>();
			String abstrac = element.element("abstract").getText();
			System.out.println(abstrac);
			parsMap.put("", "");
			//System.out.println("element:"+element.asXML());
			
			//List<Element> content = element.elements();
		
		    getNodes(element);
			
		}
		
	}
	
	public void getNodes(Element node){  
	    System.out.println("--------------------");  
	      
	    //当前节点的名称、文本内容和属性  
	    System.out.println("当前节点名称："+node.getName());//当前节点名称  
	    System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称  
	    List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list  
	    for(Attribute attr:listAttr){//遍历当前节点的所有属性  
	        String name=attr.getName();//属性名称  
	        String value=attr.getValue();//属性的值  
	        System.out.println("属性名称："+name+"属性值："+value);  
	    }  
	      
	    //递归遍历当前节点所有的子节点  
	    List<Element> listElement=node.elements();//所有一级子节点的list  
	    for(Element e:listElement){//遍历所有一级子节点  
	        this.getNodes(e);//递归  
	    }  
	}  

}

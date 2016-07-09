package com.lsj.zx.bank;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
public class TestClient 
{
    public static String URL_STR="http://202.99.22.26:8002/servlets/com/icitic/ebank/MainControl";
    public static String FILE_STR="220415.xml";
    public static String IP_STR="127.0.0.1";
    public static String PORT_STR ="6789";   
    
    public static void main(String[] args) throws Exception 
    {
      String userid="";
      String trancode=""; 
      String flag="";
      String filetmp="";
      if(args.length>=3)
      {
        	     IP_STR = args[0];
        	     PORT_STR = args[1];
        	     FILE_STR=args[2];        
  
      }else
      {
         System.out.println("参数不足，请将参数补足");	
         System.exit(0);		
      }
     
      System.getProperties().put( "java.proxySet", "true" );
      System.setProperty("http.proxyHost",IP_STR);  //客户端proxy运行地址
      System.setProperty("http.proxyPort",PORT_STR);       //客户端proxy打开代理服务端口
      filetmp=readContent(FILE_STR);
      URL url=new URL(URL_STR);
      HttpURLConnection connect=(HttpURLConnection) url.openConnection();			
        //下次连接不用Cache
      connect.setDefaultUseCaches(false);
        //这次连接也不用Cache
      connect.setUseCaches(false);
        //打开淂流用于读数据
      connect.setDoInput(true);
        //不能用于写数据
      connect.setDoOutput(true);		        
      connect.setRequestProperty("Content-Type","text/plain");
 

      PrintWriter writer2 =new PrintWriter(connect.getOutputStream());
      /*FileInputStream file =new FileInputStream(file_name);        
      int len=file.available();
      byte[] content = new byte[len];
      file.read(content);*/
      
      //writer2.print(new String(content));
      writer2.print(filetmp);
      writer2.close();
      System.out.println("----->发送给服务端数据为："+filetmp);
      
      BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream()));
      String line="";
      String tmprev="";      
      do 
      {
          line = br.readLine();                	            
          if(line==null ||line.length() == 0) break;
          tmprev=tmprev+line;  
          //System.out.println(line);
      } 
      while ( line!=null );
      connect.disconnect();
      System.out.println("<----接收到服务端的数据为："+tmprev);

      
    }
    public static String readContent(String fileName){                                             
           try{                                          
                FileReader fr=new FileReader(fileName);       
                BufferedReader br=new BufferedReader(fr);     
                String tmpStr="";                             
                String fileContent="";                        
                while((tmpStr=br.readLine())!=null)           
                {                                             
                      fileContent+=tmpStr+"\n";                     
                }                                             
                return fileContent;                           
           }catch(Exception ex){
           	    ex.printStackTrace();                        
                return "";                                    
           }                                             
     }  
 
}



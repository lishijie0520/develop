package com.lsj.test.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;


/**
 * 使用httpclient 各种请求工具类
 * @author lishijie
 *
 */
public class HttpClientUtil {
	
	private static final String APPLICATION_JSON = "application/json";
   /**
    * 
    * @param url post请求地址
    * @param charset 编码  UTF-8
    * @param params  参数
    * @return
    */
	public static String postSendData(String url, String charset, Map<String, String> params) {
		String resp = null;
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		InputStream in = null;
		try {

			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost(url);

			HttpParams httpParams = httpClient.getParams();
			// 请求超时
			httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000 * 60);
			// 读取超时
			httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 1000000 * 60);
			List<NameValuePair> reqPair = new ArrayList<NameValuePair>();
			
			if (params != null) {
				for (Entry<String, String> e : params.entrySet()) {
					reqPair.add(new BasicNameValuePair(e.getKey(),e.getValue()));
				}
			}
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(reqPair, charset);
			httpPost.setEntity(urlEncodedFormEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				InputStream is = responseEntity.getContent();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int ch = 0;
				while ((ch = is.read(buffer)) != -1) {
					baos.write(buffer, 0, ch);
				}
				byte bytes[] = baos.toByteArray();
				resp = new String(bytes, charset);
			}

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new Exception(response.getStatusLine().toString() + "|" + resp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				httpPost.releaseConnection();
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return resp;
	}
	
	
	/**
	 * 
	 * @param url
	 *            get请求地址
	 * @param charset
	 *            编码 UTF-8
	 * @param params
	 *            参数
	 * @return
	 */
	public static String getSendData(String url, String charset, Map<String, String> params) {
		String resp = null;
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		InputStream in = null;
		try {

			// 构建请求参数
			StringBuffer sb = new StringBuffer();
			sb.append("?");
			if (params != null) {
				for (Entry<String, String> e : params.entrySet()) {
					sb.append(e.getKey());
					sb.append("=");
					sb.append(e.getValue());
					sb.append("&");
				}
				sb.substring(0, sb.length() - 1);
			}
			
			httpClient = new DefaultHttpClient();
			
			httpGet = new HttpGet(url+sb.toString());
			
			HttpParams httpParams = httpClient.getParams();
			// 请求超时
			httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000 * 60);
			// 读取超时
			httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 1000000 * 60);
		
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				InputStream is = responseEntity.getContent();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int ch = 0;
				while ((ch = is.read(buffer)) != -1) {
					baos.write(buffer, 0, ch);
				}
				byte bytes[] = baos.toByteArray();
				resp = new String(bytes, charset);
			}

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new Exception(response.getStatusLine().toString() + "|" + resp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				httpGet.releaseConnection();
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return resp;
	}
	/**
	 * 读取josn 流
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static String receivePost(HttpServletRequest request) throws IOException, UnsupportedEncodingException {
	        
	        // 读取请求内容
	        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        String line = null;
	        StringBuilder sb = new StringBuilder();
	        while((line = br.readLine())!=null){
	            sb.append(line);
	        }
	
	        // 将资料解码
	        String reqBody = sb.toString();
	        return URLDecoder.decode(reqBody, HTTP.UTF_8);
	        
	        
	    }
	 /**
     * HttpClient 方式
     * 以流的形式传递
     * @param url
     * @param json
     */
	public static String httpPostWithJSON(String url, String json)  {
	
		String resp = null;
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		InputStream in = null;
		try {

			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost(url);

			// 将JSON进行UTF-8编码,以便传输中文
			// String encoderJson = URLEncoder.encode(json, "UTF-8");
			// String decode = URLDecoder.decode(encoderJson, "UTF-8");

			HttpParams httpParams = httpClient.getParams();
			// 请求超时
			httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000 * 60);
			// 读取超时
			httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 1000 * 60);
			// List<NameValuePair> reqPair = new ArrayList<NameValuePair>();
			// reqPair.add(new BasicNameValuePair("charset", "UTF-8"));
			// reqPair.add(new BasicNameValuePair("req", json));
			// UrlEncodedFormEntity urlEncodedFormEntity = new
			// UrlEncodedFormEntity(reqPair,"UTF-8");
			// httpPost.setEntity(urlEncodedFormEntity);
		
        
			// httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			//设置编码
			StringEntity se = new StringEntity(json, "UTF-8");
			//设置josn 流方式
			se.setContentType(APPLICATION_JSON);
			// se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,APPLICATION_JSON));
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				InputStream is = responseEntity.getContent();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int ch = 0;
				while ((ch = is.read(buffer)) != -1) {
					baos.write(buffer, 0, ch);
				}
				byte bytes[] = baos.toByteArray();
				resp = new String(bytes, "UTF-8");
			}

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new Exception(response.getStatusLine().toString() + "|" + resp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				httpPost.releaseConnection();
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return resp;
    }

}

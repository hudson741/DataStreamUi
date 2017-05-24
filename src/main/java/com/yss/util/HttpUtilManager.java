package com.yss.util;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 封装HTTP get post请求，简化发送http请求
 * @author zhangchi
 *
 */
public class HttpUtilManager {

	private static HttpUtilManager instance = new HttpUtilManager();
	private static HttpClient client;
	private static long startTime = System.currentTimeMillis();
	public  static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();  
	private static ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {

	     public long getKeepAliveDuration(  
	            HttpResponse response,
	            HttpContext context) {
	        long keepAlive = super.getKeepAliveDuration(response, context);  
	        
	        if (keepAlive == -1) {  
	            keepAlive = 5000;  
	        }  
	        return keepAlive;  
	    }  
	  
	};
	private HttpUtilManager() {
		client = HttpClients.custom().setConnectionManager(cm).setKeepAliveStrategy(keepAliveStrat).build(); 
	}

    public static void IdleConnectionMonitor(){
		
		if(System.currentTimeMillis()-startTime>30000){
			 startTime = System.currentTimeMillis();
			 cm.closeExpiredConnections();  
             cm.closeIdleConnections(30, TimeUnit.SECONDS);
		}
	}
	 
	private static RequestConfig requestConfig = RequestConfig.custom()
	        .setSocketTimeout(20000)
	        .setConnectTimeout(20000)
	        .setConnectionRequestTimeout(20000)
	        .build();
	
	
	public static HttpUtilManager getInstance() {
		return instance;
	}

	public HttpClient getHttpClient() {
		return client;
	}

	private HttpPost httpPostMethod(String url) {
		return new HttpPost(url);
	}

	private HttpRequestBase httpGetMethod(String url) {
		return new HttpGet(url);
	}

	public static void main(String[] args) throws Exception{
		String url_prex = "";
		String url="http://localhost:8080/api/weixin/login";


		String encryptedDataS = "WnnpDde4ial9L/pMiAFIPOm1w0EqPrtnQIe/P+Rc+YYwzs0E3b99AtJWFRjD6IkNh5msiOVngLb54+IOG0vL+R8VHZayBexKUJIKnDRuircZ9C2HfyWUdM15szQef/pU2CQ2yetXCrCS0w58Kf9RqrmWf+iaXNLS+8JmEVWIXFDAOwZZeMSAeQei8eYWPzqkSssRSzaCyU34rnGiZrJrWMwq4PKJOhpzx3bTI4u4D6N8RPjr5iqQuuexXJPfw9KuRf47alJj3tpKfh6ETscPNxHjQ0u8iyYFkUsLI+4wQmlNKouhhb2rMEMtos5UJNTmFnW3KrphMtSoYuk+kk1930QMaPPRB+CAAyHG7bKK7uGT0E+5xzneEUUr+pyqwPZ+c5S7zRzmUmVwTNStdgztdtbCWuxooS+e2thyjBvxlBqWkCqyqZJta/9BGw06D7WAipHd+xK+IEcscszezzu0QQR+6TV+Zh7F1yAgqxpi0CY=";

		String ivs = "OsXR3S0DtqD1Y/wQ4jnT1A==";

		String code = "003aATFc1L3NCs05yXEc159DFc1aATF3";

		String param = "code="+code+"&ed="+encryptedDataS+"&iv="+ivs;

		Map<String,String> map = new HashMap<>();
		map.put("code",code);
		map.put("ed",encryptedDataS);
		map.put("iv",ivs);
		String sid = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJob3N0IjoiMTI3LjAuMC4xIiwicmFuZG9tIjoiZGt3bzFhMDRmYiJ9.UFY-rjEuv6su4ThxAMOE9EVI3uSHEHKA-_pz1X_nt3U";
		Map<String,String> headers = new HashMap<>();
		headers.put("_T",sid);
		System.out.println(HttpUtilManager.getInstance().requestHttpPost(url_prex,url,map,headers));
//		System.out.println(HttpUtilManager.getInstance().requestHttpGet(url_prex,"http://localhost:8080/api/order/user/cart",sid));
	}

	public String requestHttpGet(String url_prex,String url,String param,Map<String,String> headers) throws HttpException, IOException{
		
		IdleConnectionMonitor();
		url=url_prex+url;
		if(param!=null && !param.equals("")){
		        if(url.endsWith("?")){
			    url = url+param;
			}else{
			    url = url+"?"+param;
			}
		}
		HttpRequestBase method = this.httpGetMethod(url);
		if(headers!=null){
			Set<String> keys = headers.keySet();
			for(Iterator<String> i = keys.iterator();i.hasNext();){
				String key = (String) i.next();
				method.addHeader(key,headers.get(key));
			}
		}
		method.setConfig(requestConfig);
		HttpResponse response = client.execute(method);
		HttpEntity entity =  response.getEntity();
		if(entity == null){
			return "";
		}
		InputStream is = null;
		String responseData = "";
		try{
		    is = entity.getContent();
		    responseData = IOUtils.toString(is, "UTF-8");
		}finally{
			if(is!=null){
			    is.close();
			}
		}
		return responseData;
	}



	public String requestHttpPost(String url_prex,String url,Map<String,String> params,Map<String,String> headers) throws HttpException, IOException{
		
		IdleConnectionMonitor();
		url=url_prex+url;
		HttpPost method = this.httpPostMethod(url);
		List<NameValuePair> valuePairs = this.convertMap2PostParams(params);
		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
		method.setEntity(urlEncodedFormEntity);
		method.setConfig(requestConfig);
		method.setHeader("Content-Type", "application/x-www-form-urlencoded");

		if(headers!=null){
			Set<String> keys = headers.keySet();
			for(Iterator<String> i = keys.iterator();i.hasNext();){
				String key =  i.next();
				method.addHeader(key,headers.get(key));
			}
		}

		HttpResponse response = client.execute(method);
		HttpEntity entity =  response.getEntity();
		if(entity == null){
			return "";
		}
		InputStream is = null;
		String responseData = "";
		try{
		    is = entity.getContent();
		    responseData = IOUtils.toString(is, "UTF-8");
		}finally{
			if(is!=null){
			    is.close();
			}
		}
		return responseData;
		
	}
	
	private List<NameValuePair> convertMap2PostParams(Map<String,String> params){
		if(params == null || params.isEmpty()){
			return Lists.newArrayList();
		}
		List<String> keys = new ArrayList<String>(params.keySet());
		if(keys.isEmpty()){
			return null;
		}
		int keySize = keys.size();
		List<NameValuePair>  data = new LinkedList<NameValuePair>() ;
		for(int i=0;i<keySize;i++){
			String key = keys.get(i);
			String value = params.get(key);
			data.add(new BasicNameValuePair(key,value));
		}
		return data;
	}

}


package com.example.demo.common;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class AuthUtils {
	 
	public static final String APPID = "wxf4b8cd93784bf564";
	public static final String APPSECRET = "749c55c0eaa8e9abd3b2539156d50146";

	/**
	 * HTTP请求工具方法
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static JSONObject doGetJson(String url) throws IOException {
		JSONObject jsonObject = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet http = new HttpGet(url);
		HttpResponse httpResponse = client.execute(http);
		HttpEntity entity = httpResponse.getEntity();
		if(entity != null) {
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonObject = JSONObject.parseObject(result);
		}
		http.releaseConnection();
		return jsonObject;
	}

}

package com.whoeverlovely.chatapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.junit.Test;

public class HttpClientUtil {
	
	private static final Logger logger = Logger.getLogger("HttpClientUtil");

	public static String postJson(String url, JSONObject parameter) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json");

		StringEntity entity = null;
		StringBuffer respBody = new StringBuffer();
		try {
			entity = new StringEntity(parameter.toString());
			logger.info("Url: " + url);
			logger.info("Request: " + parameter.toString());
			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String result = "";
			while ((result = rd.readLine()) != null) {
				respBody.append(result);
			}
			logger.info("Response status code: " + response.getStatusLine().getStatusCode());
			logger.info("Response: " + respBody);
			client.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return respBody.toString();
	}
	
	public void testSignUp() {
		String url = "http://localhost:8080/mychatapp/SignUp";
		JSONObject data = new JSONObject();
		data.put("pushy_token", "9a6a92bbac5957cf4f0e72");
		data.put("password", "password123");
		postJson(url,data);
	}
	
	@Test
	//Request: data/receiverUserId/chat_token/userId
	public void testForward() {
		String url = "http://47.100.58.218:8080/mychatapp/Forward";
		JSONObject dataJson = new JSONObject();
		dataJson.put("messge", "hello world");
		String data = dataJson.toString();
		String receiverUserId = "2";
		String chat_token = "ce006427-d56c-4436-bf19-92f208d6b73c";
		String userId = "1";
		JSONObject parameter = new JSONObject();
		parameter.put("data", data);
		parameter.put("receiverUserId", receiverUserId);
		parameter.put("chat_token", chat_token);
		parameter.put("userId", userId);
		postJson(url,parameter);
	}
	
}

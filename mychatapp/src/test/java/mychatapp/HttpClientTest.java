package mychatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Test;

public class HttpClientTest {
	
	public static String httpClient(String servletPath, JSONObject parameter) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/mychatapp/" + servletPath);
		httpPost.setHeader("Content-Type", "application/json; UTF-8");

		StringEntity entity = null;
		int code = 0;
		StringBuffer respBody = new StringBuffer();
		try {
			entity = new StringEntity(parameter.toString());
			System.out.println("Request: " + parameter.toString());
			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);
			code = response.getStatusLine().getStatusCode();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String result = "";
			while ((result = rd.readLine()) != null) {
			    respBody.append(result);
			}

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
		return "status code is " + code + "; response body is " + respBody;
	}
	
	@Test
	public void userInitTest() {
		String servletPath = "SignUp";
		JSONObject parameter = new JSONObject();
		parameter.put("user_name", "kk");
		parameter.put("pushy_token", "123qwe");
		parameter.put("public_key", "public_key");
		parameter.put("password", "password");
		String resp = httpClient(servletPath,parameter);
		System.out.println(resp);
	}
}

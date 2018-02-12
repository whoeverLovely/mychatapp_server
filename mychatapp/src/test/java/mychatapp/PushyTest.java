package mychatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class PushyTest {
	
	public static void main(String[] args) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("https://api.pushy.me/push?api_key=894f2b68fb0f6de0d5db134ed1dc545479ac4298c95900779e09d82662cf15fd");
		httpPost.setHeader("Content-Type", "application/json");

		StringBuffer respBody = new StringBuffer();
		try {
			JSONObject notification = new JSONObject();
			JSONObject data = new JSONObject();
			data.put("hello", "world");
			notification.put("to", "9a6a92bbac5957cf4f0e72");
			notification.put("data", data);
			
			/*ByteArrayEntity bae = new ByteArrayEntity(notification.toString().getBytes());*/
			StringEntity bae = new StringEntity(notification.toString());
			httpPost.setEntity(bae);

			CloseableHttpResponse response = client.execute(httpPost);
			
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
		System.out.println(respBody.toString());
	}
}

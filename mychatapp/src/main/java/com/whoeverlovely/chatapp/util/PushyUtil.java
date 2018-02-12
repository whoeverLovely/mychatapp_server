package com.whoeverlovely.chatapp.util;

import java.util.logging.Logger;

import org.json.JSONObject;
import org.junit.Test;

public class PushyUtil {

	final private static String url = ConfigReader.getProp("pushy.push");
	final private static Logger logger = Logger.getLogger("PushyUtil");

	/**
	 * 
	 * @param pushyToken
	 * @param data
	 * @return true - notification sent successfully false - notification sent
	 *         failed
	 * 
	 */
	public static boolean pushData(String pushyToken, JSONObject data) {
		JSONObject notification = new JSONObject();
		notification.put("data", data);
		notification.put("to", pushyToken);

		String result = HttpClientUtil.postJson(url, notification);
		JSONObject resp = new JSONObject(result);
		if (resp != null && resp.has("success"))
			return true;
		else
			logger.info("Pushy error: " + resp.getString("error"));
		return false;

	}

	@Test
	public void test() {

		JSONObject data = new JSONObject();
		data.put("hello", "world");
		System.out.println(pushData("9a6a92bbac5957cf4f0e72", data));

	}

}

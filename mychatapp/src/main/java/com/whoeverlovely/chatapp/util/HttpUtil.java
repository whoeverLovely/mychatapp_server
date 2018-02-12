package com.whoeverlovely.chatapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class HttpUtil {

	private JSONObject jsonRequest;

	public HttpUtil(HttpServletRequest request) throws IOException {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader reader = request.getReader();

		while ((line = reader.readLine()) != null)
			sb.append(line);

		String req = sb.toString();
		jsonRequest = new JSONObject(req);
	}

	public String getJsonRequestParameter(String parameterName) throws IllegalArgumentException{
		if (jsonRequest.has(parameterName))
			return jsonRequest.getString(parameterName);
		else
			throw new IllegalArgumentException();
	}

	public static void responseString(HttpServletResponse resp, String respStr) throws IOException {
		PrintWriter out = resp.getWriter();
		out.print(respStr);
		out.flush();
		out.close();
	}
}

package com.whoeverlovely.chatapp.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.whoeverlovely.chatapp.util.DBUtil;
import com.whoeverlovely.chatapp.util.HttpUtil;
import com.whoeverlovely.chatapp.util.PushyUtil;

/**
 * Servlet implementation class Forward
 */
public class Forward extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger("Forward");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Forward() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response) 
	 *      Request data/receiverUserId/chat_token/userId
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpUtil httpUtil = new HttpUtil(request);
		String data = null;
		String receiverUserId = null;
		String chat_token = null;
		String userId = null;
		String error = null;

		try {
			data = httpUtil.getJsonRequestParameter("data");
			receiverUserId = httpUtil.getJsonRequestParameter("receiverUserId");
			chat_token = httpUtil.getJsonRequestParameter("chat_token");
			userId = httpUtil.getJsonRequestParameter("userId");
			logger.info("Forward request from " + userId + " to " + receiverUserId + ". Data: " + data);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		if (senderVerification(userId, chat_token)) {
			String sql = "select pushy_token from user where id = ?";
			Map<Integer, Object> map = new HashMap<Integer, Object>();
			map.put(1, receiverUserId);
			List<Map<String, Object>> resultList = null;
			try {
				resultList = DBUtil.executeQuery(sql, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (resultList.size() == 1) {
				String pushy_token = (String) resultList.get(0).get("pushy_token");
				JSONObject dataJSON = new JSONObject(data);
				PushyUtil.pushData(pushy_token, dataJSON);
			} else
				error = "Receiver doesn't exist.";
		} else
			error = "Sender identity can't be verified";
		
		if(error != null)
			HttpUtil.responseString(response, new JSONObject().put("error", error).toString());
	}

	private boolean senderVerification(String userId, String chat_token) {
		String sql = "select chat_token, chat_token_generate_time from user where id = ?";
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		map.put(1, userId);
		List<Map<String, Object>> resultList = null;;
		try {
			resultList = DBUtil.executeQuery(sql, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resultList.size() == 1) {
			String chatTokenSaved = (String) resultList.get(0).get("chat_token");
			Long chat_token_generate_time = (Long) resultList.get(0).get("chat_token_generate_time");
			Long expiryDate = chat_token_generate_time + 1000 * 60 * 60 * 24 * 30;
			if (chatTokenSaved.equals(chat_token) && System.currentTimeMillis() < expiryDate) {
				return true;
			}
			// TODO renew chat_token
			return true;
		}
		return false;
	}
}

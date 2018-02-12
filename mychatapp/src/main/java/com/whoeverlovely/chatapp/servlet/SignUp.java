package com.whoeverlovely.chatapp.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.whoeverlovely.chatapp.util.DBUtil;
import com.whoeverlovely.chatapp.util.HttpUtil;

/**
 * Servlet implementation class UserSignUp
 */
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger("SignUp");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignUp() {
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
	 *      response) Request: pushy_token/password Response: userId/chat_token
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpUtil httpUtil = new HttpUtil(request);
		String pushy_token;
		String password;
		String chat_token = null;
		String error = null;

		int userId = 0;
		try {
			pushy_token = httpUtil.getJsonRequestParameter("pushy_token");
			password = httpUtil.getJsonRequestParameter("password");
			System.out.println("Received a new user with pushy_token " + pushy_token);
			logger.info("Received a new user with pushy_token " + pushy_token);
			String sqlVerifiy = "select id from user where pushy_token = ?";
			Map<Integer, Object> mapVerify = new HashMap<Integer, Object>();
			mapVerify.put(1, pushy_token);
			List<Map<String, Object>> resultList = DBUtil.executeQuery(sqlVerifiy, mapVerify);

			if (resultList.size() == 0) {
				// verified it's a new user
				chat_token = UUID.randomUUID().toString();
				String sqlInsert = "insert into user(password, chat_token, chat_token_generate_time, pushy_token, status) values(?,?,?,?,0)";
				Map<Integer, Object> mapInsert = new HashMap<Integer, Object>();
				mapInsert.put(1, password);
				mapInsert.put(2, chat_token);
				mapInsert.put(3, DBUtil.getCurrentTimestamp());
				mapInsert.put(4, pushy_token);
				userId = DBUtil.executeUpdate(sqlInsert, mapInsert);
			} else {
				error = "The user already exists.";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = "Server internal error.";
		} finally {
			if (userId != 0) {
				JSONObject resp = new JSONObject();
				resp.put("userId", String.valueOf(userId));
				resp.put("chat_token", chat_token);
				System.out.println("new user created. userId: " + userId);
				logger.info("new user created. userId: " + userId);
				HttpUtil.responseString(response, resp.toString());
			} else {
				HttpUtil.responseString(response, new JSONObject().put("error", error).toString());
				logger.info("SignUp error: " + error);
			}
		}
	}

}

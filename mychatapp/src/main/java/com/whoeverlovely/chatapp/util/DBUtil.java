package com.whoeverlovely.chatapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class DBUtil {
	private static Connection connectSqlite() throws Exception {
		Connection conn = null;
		String host = ConfigReader.getProp("database.host");
		String dbname = ConfigReader.getProp("database.dbname");
		try {
			Class.forName("org.sqlite.JDBC");
			// db parameters
			String url = host + dbname;
			// create a connection to the database
			conn = DriverManager.getConnection(url);

			System.out.println("Connection to SQLite has been established.");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new Exception("Sqlite connection error.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Sqlite connection error.");
		}
		return conn;
	}

	private static Connection connectMysql() throws Exception {
		Connection conn = null;

		String host = ConfigReader.getProp("database.host");
		String port = ConfigReader.getProp("database.port");
		String dbname = ConfigReader.getProp("database.dbname");
		String user = ConfigReader.getProp("database.user");
		String password = ConfigReader.getProp("database.password");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(host + ":" + port + "/" + dbname, user, password);
			System.out.println("Connection to Mysql has been established.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Mysql connection error.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Mysql connection error.");

		}
		return conn;

	}

	/**
	 * 
	 * @param sql
	 * @param msgParam
	 * @return last_insert_id
	 * @throws Exception
	 */
	public static int executeUpdate(String sql, Map<Integer, Object> msgParam) throws Exception {
		String database = ConfigReader.getProp("database");
		if (database == null) {
			throw new IllegalArgumentException("Haven't set database in config file.");
		}

		Connection conn = null;
		if (database.equals("mysql"))
			conn = connectMysql();
		if (database.equals("sqlite3"))
			conn = connectSqlite();

		int last_insert_id = 0;

		try {
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			for (Map.Entry<Integer, Object> entry : msgParam.entrySet()) {
				int key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof String)
					statement.setString(key, (String) value);
				if (value instanceof Integer)
					statement.setInt(key, (Integer) value);
				if (value instanceof Timestamp)
					statement.setTimestamp(key, (Timestamp) value);
			}

			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			if (result.next()) {
				last_insert_id = result.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return last_insert_id;
	}

	public static List<Map<String, Object>> executeQuery(String sql, Map<Integer, Object> msgParam) throws Exception{

		String database = ConfigReader.getProp("database");
		if (database == null) {
			throw new IllegalArgumentException("Haven't set database in config file.");
		}

		Connection conn = null;
		if (database.equals("mysql"))
			conn = connectMysql();
		if (database.equals("sqlite3"))
			conn = connectSqlite();

		List<Map<String, Object>> resultList = new LinkedList<Map<String, Object>>();

		try {

			PreparedStatement statement = conn.prepareStatement(sql);

			for (Map.Entry<Integer, Object> entry : msgParam.entrySet()) {
				int key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof String)
					statement.setString(key, (String) value);
				if (value instanceof Integer)
					statement.setInt(key, (Integer) value);
			}

			ResultSet result = statement.executeQuery();
			ResultSetMetaData md = result.getMetaData();
			int colCount = md.getColumnCount();
			while (result.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= colCount; i++) {
					map.put(md.getColumnName(i), result.getObject(i));
				}
				resultList.add(map);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	public static Timestamp getCurrentTimestamp() {
		Calendar calendar = Calendar.getInstance();
		Timestamp sqlTimestamp = new Timestamp(calendar.getTime().getTime());
		return sqlTimestamp;
	}
	
	@Test
	public void test() {
		
	}
}

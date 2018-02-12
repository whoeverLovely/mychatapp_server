package com.whoeverlovely.chatapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class ConfigReader {
	
	public static String getProp(String propName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("config.properties");
		
		Properties properties = new Properties();
		String value = null;
		try {
			properties.load(input);
			value = properties.getProperty(propName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return value;
	}
	
	@Test
	public void test() {
		System.out.println(getProp("database.host"));
	}
}

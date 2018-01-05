package com.nettactic.hotelbooking.net;
/**
 * 配置URL
 * @author heshicaihao
 *
 */
public class ConfigURL {
	
	public static final String URL = getUrl();
	public static final String TEST_URL = "http://www.idingzhi.cc/";
	public static final String ONLINE_URL = "http://www.idingzhi.cc/";
	
	private static String getUrl() {
		if (ReadPropertyFile.isTestEnvironment()) {
			return TEST_URL;
		}
		return ONLINE_URL;
	}

}

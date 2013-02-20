package com.watchshow.common.util;

import javax.servlet.http.HttpServletRequest;

public class RemoteClientInfoUtil {
	public static String getRealRemoteIPAddress(HttpServletRequest request) {
		String IPAddress = request.getHeader("x-forwarded-for");
		if (IPAddress != null && IPAddress.trim().length()>0) {
			//可能有代理
			if (IPAddress.indexOf(".") == -1) {
				//没有分割点，必然是非1PV4格式
				IPAddress = null;
			} else {
				if (IPAddress.indexOf(",") != 1) {
					//有','，则有可能有多个代理，取第一个非内网IP
					IPAddress = IPAddress.trim().replace("'", "");
					String[] temparyIP = IPAddress.split(",");
					for (int i=0; i<temparyIP.length; i++) {
						if (isIPAddress(temparyIP[i]) && temparyIP[i].substring(0,3)!="10."
								&&temparyIP[i].substring(0,7)!="192.168" && temparyIP[i].substring(0,7)!="172.16.") {
							IPAddress = temparyIP[i];
							return IPAddress;
						}
					}
				} else if (isIPAddress(IPAddress)) {
					//代理即是IP格式
					return IPAddress;
				} else {
					IPAddress = null;
				}
			}
		}
		if (IPAddress == null || IPAddress.trim().length() == 0) {
			IPAddress = request.getHeader("Proxy-Client-IP");
		}
		if (IPAddress == null || IPAddress.trim().length() == 0) {
			IPAddress = request.getRemoteAddr();
		}
		return IPAddress;
	}

	private static boolean isIPAddress(String string) {
		if (string == null || string.trim().length()<7 || string.trim().length()>15) {
			return false;
		}
		return true;
	}
	
}

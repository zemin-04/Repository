package com.shunhai.skipcloud.core.util;

import javax.servlet.http.HttpServletRequest;

/**
 *	IP工具类
 */
public class IPUtils {

	/**
	 * 获取客户端的ip地址
	 * @param request
	 * @return ip地址
	 */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (!checkIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!checkIP(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 检查ip是否存在且满足ip的格式要求
     * @param ip
     * @return true代表存在且满足
     */
    private static boolean checkIP(String ip) {
        if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)
                || ip.split(".").length != 4) {
            return false;
        }
        return true;
    }

    /**
     * 将127.0.0.1 形式的IP地址转换成10进制整数，这里没有进行任何错误处理
     * @param strIP
     * @return 10进制整数ip
     */
 	public static long ipToLong(String strIP) {
 		long[] ip = new long[4];
 		int position1 = strIP.indexOf(".");
 		int position2 = strIP.indexOf(".", position1 + 1);
 		int position3 = strIP.indexOf(".", position2 + 1);
 		ip[0] = Long.parseLong(strIP.substring(0, position1));
 		ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
 		ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
 		ip[3] = Long.parseLong(strIP.substring(position3 + 1));
 		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
 	}
}

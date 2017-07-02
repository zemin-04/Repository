package com.shunhai.skipcloud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

	public static void main(String[] args) {

		//addColumn();
		changeColumn();
		//long IP = ipToLong("192.168.255.255");
		//System.out.println(IP);
		System.out.println("select * from iplocation where inet_aton('223.245.9.255') between StartIP_L and EndIP_L;");
	}

	private static void changeColumn() {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {

			br = new BufferedReader(new FileReader("F:\\qqwry.txt"));
			bw = new BufferedWriter(new FileWriter("F:\\IP_X.txt"));
			String str = null;
			while ((str = br.readLine()) != null) {
				String[] column = str.split(",");
				if (column[2].contains("中国") || column[2].contains("省")
						|| column[2].contains("市") || column[2].contains("香港")
						|| column[2].contains("澳门") || column[2].contains("台湾")
						|| column[2].contains("新疆") || column[2].contains("西藏")
						|| column[2].contains("内蒙古") || column[2].equals("局域网")
						|| column[2].contains("宁夏")) {
					if (column[3].length() > 25) {
						column[3] = "具体地址未知";
					}
					if(column[2].equals("局域网")){
						System.out.println(str);
					}
					long IP_str = ipToLong(column[0]);
					long IP_end = ipToLong(column[1]);
					StringBuffer buffer = new StringBuffer();

					for (int i = 0; i < 6; i++) {
						if(i<4){
							buffer.append("'").append(column[i]).append("',");
						}else if(i==4){
							buffer.append(IP_str).append(",");
						}else{
							buffer.append(IP_end);
						}
					}

					bw.write(buffer.toString());
					bw.newLine();
				}
			}
			bw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 将127.0.0.1 形式的IP地址转换成10进制整数，这里没有进行任何错误处理
	private static long ipToLong(String strIP) {
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

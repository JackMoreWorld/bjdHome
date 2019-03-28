package com.bgd.support.utils;

import java.util.Random;

/**
 * 
 * @ClassName: RandomUtil
 * @Description: 随机数获取工具
 * @author: JackMore
 * @date: 2019年2月25日 下午2:11:21
 */
public class RandomUtil {

	public static String getRandomString(int length) {
		String str = "abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; ++i) {
			int number = random.nextInt(60);// [0,62)

			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	public static String getRandomString2(int length) {
		Random random = new Random();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; ++i) {
			int number = random.nextInt(3);
			long result = 0;

			switch (number) {
			case 0:
				result = Math.round(Math.random() * 25 + 65);
				sb.append(String.valueOf((char) result));
				break;
			case 1:
				result = Math.round(Math.random() * 25 + 97);
				sb.append(String.valueOf((char) result));
				break;
			case 2:
				sb.append(String.valueOf(new Random().nextInt(10)));
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 获取16进制随机数
	 */
	public static String getCode(int length){
		String str = "123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; ++i) {
			int number = random.nextInt(9);// [0,62)

			sb.append(str.charAt(number));
		}
		return sb.toString();

	}

}

package com.jingewenku.abrahamcaijin.commonutil;
/**
* 主要功能： 字符判断工具类
 * @author: hao
 *  method:
			isEmpty                  : 判断字符串是否为空
			isNotEmpty               : 判断str null,"","null" 均视为空
			checkNameChese           : 检测String是否全是中文
			isChinese                : 判定输入汉字
			toLowerCaseFirstOne      : 将字符串的第一位转为小写
			toUpperCaseFirstOne      : 将字符串的第一位转为大写
			underScoreCase2CamelCase : 下划线命名转为驼峰命名
			camelCase2UnderScoreCase : 驼峰命名法转为下划线命名
			throwable2String         : 将异常栈信息转为字符串
			concat                   : 字符串连接，将参数列表拼接为一个字符串
			concatSpiltWith          : 字符串连接，将参数列表拼接为一个字符串
			toASCII                  : 将字符串转移为ASCII码
			toUnicode                : 将字符串转移为Unicode码
			toUnicodeString          : 将字符串转移为Unicode码
			containsChineseChar      : 是否包含中文字符
			isNumber                 : 参数是否是有效数字 （整数或者小数）
			matcherFirst             : 匹配到第一个字符串
			isInt                    : 参数是否是有效整数
			isDouble                 : 字符串参数是否是double
			isBoolean                : 判断一个对象是否为boolean类型,包括字符串中的true和false
			isTrue                   : 对象是否为true
			contains                 : 判断一个数组里是否包含指定对象
			toInt                    : 将对象转为int值,如果对象无法进行转换,则使用默认值
			toLong                   : 将对象转为long类型,如果对象无法转换,将返回默认值
			toDouble                 : 将对象转为Double,如果对象无法转换,将使用默认值
			splitFirst               : 分隔字符串,根据正则表达式分隔字符串,只分隔首个,剩下的的不进行分隔,如: 1,2,3,4 将分隔为 ['1','2,3,4']
			toString                 : 将对象转为字符串,如果对象为null,则返回null,而不是"null"
			toStringAndSplit         : 将对象转为String后进行分割，如果为对象为空或者空字符,则返回null
*/
public class StringUtils {
	/**
	 * 判断字符串是否为空
	 * @param str 字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
	/**
	 * 判断str null,"","null" 均视为空.
	 * @param str      字符
	 * @return 结果 boolean
	 */
	public static boolean isNotEmpty(String str) {
		boolean bool = true;
		if (str == null || "null".equals(str) || "".equals(str)) {
			bool = false;
		} else {
			bool = true;
		}
		return bool;
	}
	/**
	 * 
	 * 检测String是否全是中文
	 * @param name
	 * @return
	 */

	public static boolean checkNameChese(String name) {
		boolean res = true;
		char[] cTemp = name.toCharArray();
		for (int i = 0; i < name.length(); i++) {
			if (!isChinese(cTemp[i])) {
				res = false;
				break;
			}
		}
		return res;
	}
	/**
	 * 
	 * 判定输入汉字
	 * @param c
	 * @return
	 */

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;
		}
		return false;

	}
}

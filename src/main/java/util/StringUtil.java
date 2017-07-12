package util;

public class StringUtil {
	
	/**
	 * String의 null을 체크하여 true/false리턴한다.
	 * @param string
	 * @return
	 */
	public static boolean isNull(String string) {
		return "".equals(checkNull(string));
	}
	
	/**
	 * string이 null인경우  공백으로 리턴한다.
	 * @param string
	 * @return
	 */
	public static String checkNull(String string) {
		return checkNull(string, "");
	}
	
	/**
	 * string이 null인경우 def로 리턴한다.
	 * @param string
	 * @param def
	 * @return
	 */
	public static String checkNull(String string, String def) {

		if (string == null)
			return def;
		if (string.trim().length() == 0)
			return def;
		return string.trim();
	}
}

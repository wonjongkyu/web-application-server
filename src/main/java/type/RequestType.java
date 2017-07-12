package type;

import util.StringUtil;

public enum RequestType {
    Home("/index.html"),
    UserLogin("/user/login.html"),
    UserCreate("/user/create"),
    UserForm("/user/form.html")
    ;
    
	private String text;
	
	RequestType(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	/**
	 * 문자열(String)을 통해 Enum에 정의된 값을 반환
	 * 
	 * @Method fromString
	 * @param text
	 * @return
	 */
	public static RequestType fromString(String text) {
		if (!StringUtil.isNull(text)) {
			return findAPIRequestTypeValue(text);
		}
		return null;
	}
	
	public static RequestType findAPIRequestTypeValue(String text){
		for (RequestType type : RequestType.values()) {
			if (text.equalsIgnoreCase(type.text)) {
				return type;
			}
		}
		return null;
	}
}

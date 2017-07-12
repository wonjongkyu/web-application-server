package util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import model.User;

public class ObjectUtilTest {
	
	@Test
	public void convertMapToObject(){
		ObjectUtil objUtil = new ObjectUtil();
		User uVO = new User();
		Map<String,String> map = new HashMap<String, String>();
		map.put("userId", "wonjongkyu");
		map.put("password", "1234");
		map.put("name", "원종규");
		map.put("email", "wonjongkyu@naver.com");
		
		objUtil.convertMapToObject(map, uVO);
		
		System.out.println(uVO.getEmail());
		 
	}
	
}
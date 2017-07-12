package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/*
	1. Map의 key만큼 Loop를 돌며 + key값의 첫번째 글자를 대문자로 바꾼다.\
	(보통 name이라는 필드가 있다면 set메소드는 setName 이니까)
	2. 넘긴 POJO의 선언된 Method를 가져와서 우리가 필요한(set할) 메소드를 찾는당
	3. 메소드 실행하며 값을 넘겨준다~ (invoke)
	4. 끝~
	출처: http://erictus.tistory.com/entry/Map-to-Object-와-Object-to-Map [재미있게 살자]
*/
public class ObjectUtil {
	public static Object convertMapToObject(Map<String, String> map, Object objClass){
		String keyAttribute = null;
		StringBuffer methodString = new StringBuffer();
		String setMethodString = "set";
		
		Iterator itr = map.keySet().iterator(); 
		while(itr.hasNext()){
			while(itr.hasNext()){
				methodString.setLength(0);
				keyAttribute = (String) itr.next();
				methodString.append(setMethodString).append(keyAttribute.substring(0,1).toUpperCase()).append(keyAttribute.substring(1));
				// System.out.println("keyAttribute:" + methodString.toString());
				try{
					Method[] methods = objClass.getClass().getDeclaredMethods();
					for(int i=0;i<=methods.length-1;i++){ 
						if(methodString.toString().equals(methods[i].getName())){ 
							// System.out.println("invoke : "+methodString.toString()); 
							methods[i].invoke(objClass, map.get(keyAttribute));
						}
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
	    
		return objClass;
	}

}

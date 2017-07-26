package HttpTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.StringUtil;
import webserver.RequestHandler;

import com.google.common.collect.Maps;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private String method;
	private String path;
	
	private Map<String, String> header = Maps.newHashMap();		// Header 저장
	private Map<String, String> parameter = Maps.newHashMap();	// parameter 저장
	
	/**
	 * 생성자
	 * InputStream 을 받아서 문자열로
	 * @return the header
	 */
	public HttpRequest(InputStream in) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
			String line;
			String temp_header;
			line = br.readLine();
			if(line == null){
				return;
			}
			
			if (line != null) {
				log.debug("original line:{}", line);
				/*
				 *  /user/create?userId=wonjongkyu&password=1234&name=Jongkyu
				 *  ?로 나누고, 뒤에 파마미터 key=value 구분자는 &
				 */
				String[] token = line.split(" ");
				if(token.length > 1){
					method = token[0];
					if(method.equals("GET")){
						commonParser(token[1]);
					}else{	// POST 인 경우
						path = token[1];
					}
				}
				
				method = line.split(" ")[0];
				log.debug("setMethod:{}", method);
				while (line != null) {
					line = br.readLine();
					log.debug("line..{}", line);
					if(line != null){
						if(line.indexOf(": ") > -1){		// header인 경우
							header.put(line.split(": ")[0], line.split(": ")[1]);
						}
						// POST 이면서 비어있을 경우 parameter가 나올 것이다.
						if(line.equals("")){
							if(method.equals("POST")){
								log.debug("POSTTTTT");
								log.debug("POSTTTTT22222222");
								commonParser(line);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void commonParser(String token){
		// GET or POST 인지에 대한 분기문 필요함
		int idx = token.indexOf("?");
		if(idx == -1){
			log.debug("POST:::{}", token);
			parameter = HttpRequestUtils.parseQueryString(token);
		}else if(idx > -1){
			log.debug("Test Path:::{}", token.substring(0,idx));
			path = token.substring(0,idx);
			parameter = HttpRequestUtils.parseQueryString(token.substring(idx+1));
			log.debug("====== success parameter setting ======");
		}
	}

	/**
	 * @return the header
	 */
	public String getHeader(String field_name) {
		String result = null;
		if(!StringUtil.isNull(field_name)){
			result =  header.get(field_name);
		}
		return result;
	}
	
	/**
	 * @return the header
	 */
	public String getParameter(String param) {
		String result = null;
		if(!StringUtil.isNull(param)){
			result =  parameter.get(param);
		}
		return result;
	}
	
	/**
	 * @return the header
	 */
	public String getMethod() {
		return method;
	}
		
	/**
	 * @return the header
	 */
	public String getPath() {
		return path;
	}
 
}

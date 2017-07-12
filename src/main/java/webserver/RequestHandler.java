package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import type.RequestType;
import util.Constants;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;
import util.ObjectUtil;

public class RequestHandler extends Thread {
	
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        	/*
        	 * 요구사항 1
        	 * - 한 줄 단위로 읽기 위해 변경
        	 */
        	BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        	// Get HttpURL
        	String line = br.readLine();					
        	String url = HttpRequestUtils.getHttpURL(line);
        	RequestType requestType = RequestType.fromString(url);
        	if(requestType != null){
        		log.debug("요구사항 1 get URL : {}", requestType.getText());
        	}
        	
        	// Get Content-Length
        	int ContentLength = 0;
        	
        	while(!"".equals(line)){
        		line = br.readLine();
        		// log.debug(line);
        		Pair pair = HttpRequestUtils.parseHeader(line);
        		if(pair != null){
        			if(pair.getKey().toString().equals("Content-Length")){
        				ContentLength = Integer.parseInt(pair.getValue());
        			}
        		}
        		if(line == null){
        			return;
        		}
        	}
        	
        	/* 요구사항 3 */
        	if(ContentLength > 0){
        		log.debug("ContentLength: {}", IOUtils.readData(br,  ContentLength));
        	}
        	
        	/*
        	 * 요구사항 2
        	 */
        	User userVO = new User();
        	url = HttpRequestUtils.splitQuery(url);								// 요청 URL과 파라미터 분리
        	Map<String, String> map = HttpRequestUtils.parseQueryString(url);	// 파라미터 name, value로 분리
        	ObjectUtil.convertMapToObject(map, userVO);							// map to object
        	
        	
        	byte[] body = Files.readAllBytes(new File("./webapp" + HttpRequestUtils.checkRedirectURL(url)).toPath() );
            DataOutputStream dos = new DataOutputStream(out);
            
            if(requestType != null){
	            if(url.equals("/user/create")){
	            	response302Header(dos, body.length);
	            }else if(url.equals("/user/login.html")){
	            	log.debug("login");
	            	response200Header(dos, body.length, "logined=true");
	            }else {
	            	response200Header(dos, body.length, "");
	            }
            }else {
            	response200Header(dos, body.length, "");
            }
            
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String addBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            if(addBodyContent.length() > 0){
            	dos.writeBytes("Set-Cookie: " + addBodyContent + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void response302Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + Constants.DEFAULT_PAGE);		// Location 변경
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

package HttpTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.RequestHandler;

public class HttpRequestTest {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private String testDirectory = "./src/test/resources/";
	
	@Test
	public void request_GET() throws Exception {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_Get.txt"));
		HttpRequest request = new HttpRequest(in);
		
		log.debug("getHeader():{}",request.getHeader("Connection"));
		Assert.assertEquals("GET", request.getMethod());
		Assert.assertEquals("/user/create", request.getPath());
		Assert.assertEquals("keep-alive", request.getHeader("Connection"));
		Assert.assertEquals("wonjongkyu", request.getParameter("userId"));
	}
	
	@Test
	public void request_POST() throws Exception {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_Post.txt"));
		HttpRequest request = new HttpRequest(in);
		
		log.debug("getHeader():{}",request.getHeader("Connection"));
		Assert.assertEquals("POST", request.getMethod());
		Assert.assertEquals("/user/create", request.getPath());
		Assert.assertEquals("keep-alive", request.getHeader("Connection"));
		Assert.assertEquals("wonjongkyu", request.getParameter("userId"));
	}

}

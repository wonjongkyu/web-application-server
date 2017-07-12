package type;

import org.junit.Test;

public class RequestTypeTest {
	
	@Test
	public void enumTest(){
		System.out.println( RequestType.fromString("/user/create") );
		
	}
}

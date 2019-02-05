package api.main.src;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestClient {
	
	public static void main(String[] args) throws UnknownHostException, Exception {
		
		Client client = new Client(InetAddress.getByName("25.3.34.71"), 42353, "APE");
		client.start();
		
	}
	
}

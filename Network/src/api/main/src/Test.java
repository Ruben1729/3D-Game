package api.main.src;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server()
				.withPortRange(PortRange.create().from(42353).to(42360))
				.withThreads(3)
				.init();
		
		long lastTick = System.nanoTime();
		long lastTime = System.nanoTime();
		while(true) {
				
			long currentTime = System.nanoTime();
			if(currentTime - lastTime >= 1000000000) {
				
				System.out.println(server.currentClients());
				
				lastTime = currentTime;
			}
			
			long currentTick = System.nanoTime();
			if(currentTick - lastTick >= 1000000000 / 60) {
				
				server.tickClients();
				
				lastTick = currentTick;
			}
			
		}

	}
	
}

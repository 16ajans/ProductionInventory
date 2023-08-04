package productioninventory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import productioninventory.ServiceWorker.Type;

public class Dispatch {

	private List<ServiceWorker> workers;
	private List<Thread> threads;
	private String hapRoot;
	private LocalDateTime timestamp;

	public void resolve() throws InterruptedException, IOException {
		for (Thread thread : threads)
			thread.start();
		timestamp = LocalDateTime.now();
		for (Thread thread : threads)
			thread.join();
		
		Writer.write(hapRoot, timestamp, workers);
	}

	Dispatch(String hapRoot) {
		this.hapRoot = hapRoot;
		
		workers = new ArrayList<ServiceWorker>();
		threads = new ArrayList<Thread>();

		ServiceWorker CGMSearch = new ServiceWorker(Type.CGM, hapRoot);
		ServiceWorker TIFSearch = new ServiceWorker(Type.TIF, hapRoot);
		workers.add(CGMSearch);
		workers.add(TIFSearch);
		threads.add(new Thread(CGMSearch));
		threads.add(new Thread(TIFSearch));
	}

}

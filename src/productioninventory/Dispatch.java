package productioninventory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import productioninventory.ServiceWorker.Type;

public class Dispatch {

	private List<ServiceWorker> workers;
	private List<Thread> threads;

	private LocalDateTime timestamp;

	public void resolve() throws InterruptedException {
		for (Thread thread : threads)
			thread.start();
		for (Thread thread : threads)
			thread.join();
		timestamp = LocalDateTime.now();
	}

	Dispatch(String hapRoot) {
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

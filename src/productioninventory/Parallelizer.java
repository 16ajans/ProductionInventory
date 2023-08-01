package productioninventory;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parallelizer {

	private List<FileSearch> searches;
	private List<Thread> threads;
	private LocalDateTime timestamp;

	public void start() {
		for (Thread thread : threads) {
			thread.start();
		}
	}

	public void join() throws InterruptedException {
		for (Thread thread : threads) {
			thread.join();
		}
		timestamp = LocalDateTime.now();
	}

	public List<Path> getFoundFiles() {
		return searches.parallelStream().map(FileSearch::getFoundFiles).flatMap(List::stream).collect(Collectors.toList());
	}

	Parallelizer(String hapShareLetter, List<String> haps) {
		searches = new ArrayList<FileSearch>();
		threads = new ArrayList<Thread>();

		for (String hap : haps) {
			FileSearch CGMSearch = new FileSearch(FileSearch.Type.CGM, hapShareLetter, hap);
			FileSearch TIFSearch = new FileSearch(FileSearch.Type.TIF, hapShareLetter, hap);
			searches.add(CGMSearch);
			searches.add(TIFSearch);
			threads.add(new Thread(CGMSearch));
			threads.add(new Thread(TIFSearch));
		}
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
}

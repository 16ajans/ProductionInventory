package productioninventory;

import java.io.IOException;
import java.util.List;

public class Operator {

	public static void main(String... args) throws IOException {

		String hapShareLetter = "T:/";
		List<String> haps = List.of("Auburn", "Everett", "St_Louis");
	
		Parallelizer parallelizer = new Parallelizer(hapShareLetter, haps);
		parallelizer.start();
		try {
			parallelizer.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(parallelizer.getFoundFiles());
	}
}

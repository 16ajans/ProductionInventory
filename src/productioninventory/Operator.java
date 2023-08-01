package productioninventory;

import java.io.IOException;
import java.util.List;

public class Operator {

	public static void main(String... args) throws IOException, InterruptedException {

		String hapShareLetter = "T:/";
		List<String> haps = List.of("Auburn", "Everett", "St_Louis");

		for (String hap : haps) {
			Dispatch dispatch = new Dispatch(hapShareLetter + hap);
			dispatch.resolve();
		}
	}
}

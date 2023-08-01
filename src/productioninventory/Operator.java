package productioninventory;

import java.io.IOException;
import java.util.List;

public class Operator {

	public static void main(String... args) throws IOException, InterruptedException {

		String hapShare = "//Mw/wch-mil/PEDS_HAP_SHARE/";
		List<String> haps = List.of("Auburn", "Everett", "St_Louis");

		for (String hap : haps) {
			Dispatch dispatch = new Dispatch(hapShare + hap);
			dispatch.resolve();
		}
	}
}

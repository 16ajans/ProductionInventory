package productioninventory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import cgminterpreter.CGM;
import cgminterpreter.DoubleDimension;

public class Operator {

	public static void main(String... args) throws IOException {

		String hapShareLetter = "T:/";
		List<String> haps = List.of("Auburn", "Everett", "St_Louis");

		for (String hap : haps) {
			hap = hapShareLetter + hap;
			System.out.println(hap);
			Files.find(Paths.get(hap + "/CGM"), Integer.MAX_VALUE,
					(path, attr) -> path.toString().toLowerCase().endsWith("cgm") && attr.isRegularFile()).parallel()
					.map(Path::toFile).map(file -> {
						try {
							return new CGM(file);
						} catch (IOException e) {
							e.printStackTrace();
							return null;
						}
					}).filter(Objects::nonNull).forEach(cgm -> {
						DoubleDimension ddim = cgm.getSize();
						System.out.print(ddim.toString());
						if (Math.round(ddim.getWidth()) > 36 || ddim.getHeight() > 145)
							System.out.print(" OVERSIZE ");
						else
							System.out.print("          ");
						System.out.println(cgm.getPath().getFileName());
					});
			Files.find(Paths.get(hap + "/TIFF"), Integer.MAX_VALUE,
					(path, attr) -> path.toString().toLowerCase().endsWith("tif") && attr.isRegularFile()).parallel()
					.map(Path::toFile).forEach(tif -> {
						DoubleDimension ddim = TIFWhisperer.getDimensions(tif);
						System.out.print(ddim.toString());
						if (ddim.getWidth() > 36 && ddim.getHeight() > 36)
							System.out.print(" OVERSIZE ");
						else
							System.out.print("          ");
						System.out.println(tif.getName());
					});
		}
	}
}

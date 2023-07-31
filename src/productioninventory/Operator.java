package productioninventory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cgminterpreter.CGM;

public class Operator {

	public static void main(String... args) throws IOException {

		String hapShareLetter = "T:/";

		List<String> tifDirs = List.of("Auburn/TIFF", "Everett/TIFF", "St_Louis/TIFF").parallelStream()
				.map(dir -> hapShareLetter + dir).collect(Collectors.toList());
		List<String> cgmDirs = List.of("Auburn/CGM", "Everett/CGM", "St_Louis/CGM").parallelStream()
				.map(dir -> hapShareLetter + dir).collect(Collectors.toList());

		tifDirs.parallelStream().map(Paths::get).flatMap(t -> {
			try {
				return Files.walk(t);
			} catch (IOException e) {
				e.printStackTrace();
				return Stream.empty();
			}
		}).filter(Files::isRegularFile).filter(tif -> tif.toString().toLowerCase().endsWith("tif")).map(Path::toFile)
				.map(tif -> TIFWhisperer.getDimensions(tif) + tif.getPath().toString()).forEach(System.out::println);

		cgmDirs.parallelStream().map(Paths::get).flatMap(t -> {
			try {
				return Files.walk(t);
			} catch (IOException e) {
				e.printStackTrace();
				return Stream.empty();
			}
		}).filter(Files::isRegularFile).filter(cgm -> cgm.toString().toLowerCase().endsWith("cgm")).map(Path::toFile)
				.map(file -> {
					try {
						return new CGM(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}).filter(Objects::nonNull).map(cgm -> cgm.getSize() + cgm.getPath().toString())
				.forEach(System.out::println);
	}

}

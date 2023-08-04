package productioninventory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import cgminterpreter.CGM;
import cgminterpreter.DoubleDimension;

public class ServiceWorker implements Runnable {
	enum Type {
		CGM, TIF
	}

	ServiceWorker.Type type;
	private String hapRoot;

	private Map<Path, DoubleDimension> entries;

	@Override
	public void run() {
		switch (type) {
		case CGM:
			try {
				System.out.println(String.format("Started parsing CGMs in %s", hapRoot.toString()));
				entries = Files
						.find(Paths.get(hapRoot + "/CGM"), Integer.MAX_VALUE,
								(path, attr) -> path.toString().toLowerCase().endsWith("cgm") && attr.isRegularFile())
						.parallel().map(CGM::parse).collect(Collectors.toMap(CGM::getPath, CGM::getSize));
				System.out.println(String.format("Finished parsing CGMs in %s", hapRoot.toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case TIF:
			try {
				System.out.println(String.format("Started parsing TIFs in %s", hapRoot.toString()));
				entries = Files
						.find(Paths.get(hapRoot + "/TIFF"), Integer.MAX_VALUE,
								(path, attr) -> path.toString().toLowerCase().endsWith("tif") && attr.isRegularFile())
						.parallel().map(TIF::parse).collect(Collectors.toMap(TIF::getPath, TIF::getSize));
				System.out.println(String.format("Finished parsing TIFs in %s", hapRoot.toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	ServiceWorker(Type type, String hapRoot) {
		this.type = type;
		this.hapRoot = hapRoot;
	}

	public Map<Path, DoubleDimension> getEntries() {
		return entries;
	}

	public String getHAPRoot() {
		return hapRoot;
	}
}

package productioninventory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileSearch implements Runnable {
	enum Type {
		CGM, TIF
	}
	
	private FileSearch.Type type;
	private Path root;
	
	private List<Path> foundFiles;

	@Override
	public void run() {
		switch (type) {
		case CGM:
			try {
				System.out.println(String.format("Started CGM search at %s", root.toString()));
				foundFiles = Files.find(Paths.get(root + "/CGM"), Integer.MAX_VALUE,
						(path, attr) -> path.toString().toLowerCase().endsWith("cgm") && attr.isRegularFile()).parallel()
						.collect(Collectors.toList());
				System.out.println(String.format("Finished CGM search in %s", root.toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		case TIF:
			try {
				System.out.println(String.format("Started TIF search at %s", root.toString()));
				foundFiles = Files.find(Paths.get(root + "/TIFF"), Integer.MAX_VALUE,
						(path, attr) -> path.toString().toLowerCase().endsWith("tif") && attr.isRegularFile()).parallel()
						.collect(Collectors.toList());
				System.out.println(String.format("Finished TIF search in %s", root.toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	FileSearch(FileSearch.Type type, String hapShareLetter, String hap) {
		this.type = type;
		this.root = Paths.get(hapShareLetter + hap);
	}

	public List<Path> getFoundFiles() {
		return foundFiles;
	}

}

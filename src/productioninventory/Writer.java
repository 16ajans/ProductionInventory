package productioninventory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;

import cgminterpreter.DoubleDimension;

public class Writer {

	static void write(String hapRoot, LocalDateTime timestamp, Map<Path, DoubleDimension> data) throws IOException {
		
		double length = 0.0;
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("")));
		
		
		out.close();
	}
	
}

package productioninventory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import cgminterpreter.DoubleDimension;

public class Writer {

	static DecimalFormat df = new DecimalFormat("0.00");
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

	static void write(String hapRoot, LocalDateTime timestamp, List<ServiceWorker> data) throws IOException {

		double length = 0.0;
//		int oversize = 0;
		int count = 0;
		PrintWriter out = new PrintWriter(
				new BufferedWriter(new FileWriter("C:/temp/inventory" + hapRoot.split("/")[5] + ".txt")));

		out.println(hapRoot);
		for (ServiceWorker worker : data) {
			Map<Path, DoubleDimension> entries = worker.getEntries();

			for (Path path : entries.keySet()) {
				DoubleDimension ddim = entries.get(path);
				count++;

				length += ddim.getHeight();
				String line = "";

				line += ddim.toString();

				if ((Math.round(ddim.getWidth()) > 36 || Math.round(ddim.getHeight()) > 120)
						&& path.toString().toLowerCase().endsWith("cgm")) {
					line += " OVERSIZE ";
//					oversize++;
				} else if ((ddim.getWidth() > 36 && ddim.getHeight() > 36)
						&& path.toString().toLowerCase().endsWith("tif")) {
					line += " OVERSIZE ";
//					oversize++;
				} else
					line += "          ";

				line += path.getFileName();

				out.println(line);
			}
		}
		out.println("\nTOTAL INCHES:          TOTAL DRAWINGS:");
		out.println(String.format("%" + 13 + "s", df.format(length))
				+ String.format("%" + 25 + "s", Integer.toString(count)));
		out.println("\nRETRIEVED: " + timestamp.format(formatter));

		out.close();
	}

}

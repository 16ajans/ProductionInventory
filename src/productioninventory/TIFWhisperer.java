package productioninventory;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class TIFWhisperer {
	static Dimension getDimensions(File tif) {
		try (ImageInputStream in = ImageIO.createImageInputStream(tif)) {
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				try {
					reader.setInput(in);
					return new Dimension(reader.getWidth(0), reader.getHeight(0));
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					reader.dispose();
				}
			}
		} catch (IOException e1) {
 			e1.printStackTrace();
		}
		return new Dimension();
	}
}

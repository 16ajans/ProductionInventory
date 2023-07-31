package productioninventory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;

import cgminterpreter.DoubleDimension;

public class TIFWhisperer {
	static DoubleDimension getDimensions(File tif) {
		DoubleDimension pixels = null;
		
		try (ImageInputStream in = ImageIO.createImageInputStream(tif)) {
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				try {
					reader.setInput(in);
					pixels = new DoubleDimension(reader.getWidth(0), reader.getHeight(0));
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					reader.dispose();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int widthDPI = 1;
		int heightDPI = 1;

		try {
			final ImageInfo imageInfo = Imaging.getImageInfo(tif);

			widthDPI = imageInfo.getPhysicalWidthDpi();
			heightDPI = imageInfo.getPhysicalHeightDpi();
		} catch (ImageReadException | IOException e) {
			e.printStackTrace();
		}

		return new DoubleDimension(pixels.getWidth() / widthDPI,
				pixels.getHeight() / heightDPI);
	}
}

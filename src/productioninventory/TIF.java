package productioninventory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.Node;

import cgminterpreter.DoubleDimension;

public class TIF {
	Path path;
	
	static TIF parse(Path path) {
		return new TIF(path);
	}
	
	private TIF(Path path) {
		this.path = path;
	}
	
	Path getPath() {
		return path;
	}
	
	DoubleDimension getSize() {
		DoubleDimension pixels = null;
		Double widthMMpPixel = 1.0;
		Double heightMMpPixel = 1.0;
				
		try (ImageInputStream in = ImageIO.createImageInputStream(path.toFile())) {
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				try {
					reader.setInput(in);
					
					IIOMetadata metadata = reader.getImageMetadata(0);
					IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree("javax_imageio_1.0");
					
			        Node dpcWidth = (IIOMetadataNode) root.getElementsByTagName("HorizontalPixelSize").item(0).getAttributes().item(0);			        
			        Node dpcHeight = (IIOMetadataNode) root.getElementsByTagName("VerticalPixelSize").item(0).getAttributes().item(0);
			        
			        widthMMpPixel = Double.valueOf(dpcWidth.getNodeValue());
			        heightMMpPixel = Double.valueOf(dpcHeight.getNodeValue());
					
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

		return new DoubleDimension(pixels.getWidth() * (widthMMpPixel / 25.4),
				pixels.getHeight() * (heightMMpPixel / 25.4));
	}
	
	
}

package cgminterpreter;

import java.awt.geom.Point2D;
import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import cgminterpreter.ScalingMode.Mode;

public class CGM {
	Path path;

	List<Command> commands;

	private int integerPrecision = 16;
	private RealPrecision.Precision realPrecision = RealPrecision.Precision.FIXED_32;
	private boolean realPrecisionProcessed = false;

	private VDCType.Type vdcType = VDCType.Type.INTEGER;
	private int vdcIntegerPrecision = 16;
	private VDCRealPrecision.Type vdcRealPrecision = VDCRealPrecision.Type.FIXED_POINT_32BIT;
	private boolean vdcRealPrecisionProcessed = false;
	
	public static CGM parse(File cgmFile) {
		try {
			return new CGM(cgmFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public CGM(File cgmFile) throws IOException {
		commands = new ArrayList<Command>(500);
		path = cgmFile.toPath();
		InputStream inputStream = new FileInputStream(cgmFile);
//		String cgmFilename = cgmFile.getName();
		DataInputStream in = new DataInputStream(new BufferedInputStream(inputStream));
		read(in);
		in.close();
	}

	public void read(DataInput in) throws IOException {
		boolean extent = false;
		boolean scaling = false;

		while (true) {
			Command c = Command.read(in, this);

			if (c == null) {
				break;
			}

			commands.add(c);

			if (c instanceof VDCExtent)
				extent = true;

			if (c instanceof ScalingMode)
				scaling = true;

			if (scaling && extent)
				break;
		}
	}

	public DoubleDimension getSize() {

		Point2D.Double[] extent = extent();
		if (extent == null)
			return null;

		double factor = 1;

		ScalingMode scalingMode = getScalingMode();
		if (scalingMode != null) {
			Mode mode = scalingMode.getMode();
			if (ScalingMode.Mode.METRIC.equals(mode)) {
				double metricScalingFactor = scalingMode.getMetricScalingFactor();
				if (metricScalingFactor != 0) {
					// 1 inch = 25,4 millimeter
					factor = (metricScalingFactor) / 25.4;
				}
			}
		}

//		int width = (int) Math.ceil((Math.abs(extent[1].x - extent[0].x) * factor));
//		int height = (int) Math.ceil((Math.abs(extent[1].y - extent[0].y) * factor));

		double x = Math.abs(extent[1].x - extent[0].x) * factor;
		double y = Math.abs(extent[1].y - extent[0].y) * factor;

		return new DoubleDimension(x, y);
	}

	public Point2D.Double[] extent() {
		for (Command c : this.commands) {
			if (c instanceof VDCExtent) {
				Point2D.Double[] extent = ((VDCExtent) c).extent();
				return extent;
			}
		}
		return null;
	}

	private ScalingMode getScalingMode() {
		for (Command c : this.commands) {
			if (c instanceof ScalingMode) {
				return (ScalingMode) c;
			}
		}
		return null;
	}

	public Path getPath() {
		return path;
	}

	int getIntegerPrecision() {
		return this.integerPrecision;
	}

	void setIntegerPrecision(int integerPrecision) {
		this.integerPrecision = integerPrecision;
	}

	VDCType.Type getVdcType() {
		return this.vdcType;
	}

	void setVdcType(VDCType.Type vdcType) {
		this.vdcType = vdcType;
	}

	RealPrecision.Precision getRealPrecision() {
		return this.realPrecision;
	}

	void setRealPrecision(RealPrecision.Precision realPrecision) {
		this.realPrecision = realPrecision;
	}

	boolean hasRealPrecisionBeenProcessed() {
		return this.realPrecisionProcessed;
	}

	void setRealPrecisionProcessed(boolean realPrecisionProcessed) {
		this.realPrecisionProcessed = realPrecisionProcessed;
	}

	public VDCRealPrecision.Type getVdcRealPrecision() {
		return this.vdcRealPrecision;
	}

	public void setVdcRealPrecision(VDCRealPrecision.Type vdcRealPrecision) {
		this.vdcRealPrecision = vdcRealPrecision;
	}

	boolean hasVDCRealPrecisionBeenProcessed() {
		return this.vdcRealPrecisionProcessed;
	}

	void setVDCRealPrecisionProcessed(boolean vdcRealPrecisionProcessed) {
		this.vdcRealPrecisionProcessed = vdcRealPrecisionProcessed;
	}

	int getVdcIntegerPrecision() {
		return this.vdcIntegerPrecision;
	}

	void setVdcIntegerPrecision(int vdcIntegerPrecision) {
		this.vdcIntegerPrecision = vdcIntegerPrecision;
	}
}
